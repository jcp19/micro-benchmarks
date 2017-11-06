package microbenchmarks.m5009;

import microbenchmarks.common.INode;
import microbenchmarks.common.Node;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

/**
 * Created by nunomachado on 11/04/17.
 */
public class NodeBNM extends Node implements INode {
    InetSocketAddress am;
    boolean committed;
    boolean isKilled;
    int DATAMAX = 100;

    public NodeBNM(){};

    public NodeBNM(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        super(id,ip);
        this.am = targets.get(0);
        this.committed = false;
        this.isKilled = false;
    }

    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        this.myid = id;
        this.myip = ip;
        this.am = targets.get(0);
        this.committed = false;

        try {
            this.socket = new DatagramSocket(myip);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.setName(id);
        System.out.println("[" + id + "] started");
    }

    @Override
    public void run(){
        runM5009();
    }

    private void runM5009(){
        try {
            ListenerM5009 l = new ListenerM5009();
            l.start();

            while(!committed)
            {
                this.isKilled = false;
                byte[] toSend = "COMMIT".getBytes();
                DatagramPacket packet = new DatagramPacket(toSend, toSend.length, am);
                System.out.println("[" + myid + "] send COMMIT");
                socket.send(packet);

                System.out.println("[" + myid + "] start COMMIT");
                int i;
                for(i = 0; i < DATAMAX && !isKilled; i++){
                   System.out.println("[" + myid + "] commit progress: "+i+"%");
                }
                if(i == DATAMAX){
                    committed = true;
                    toSend = "COMMIT_COMPLETED".getBytes();
                    packet = new DatagramPacket(toSend, toSend.length, am);
                    System.out.println("[" + myid + "] send COMMIT_COMPLETED");
                    socket.send(packet);

                }
                else{
                    System.out.println("[" + this.getName() + "] killed, abort commit");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public class ListenerM5009 extends Thread {

        public ListenerM5009() {
            this.setName(myid + "-listener");
        }

        @Override
        public void run() {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[256], 256);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                if (msg.equals("KILL")) {
                    System.out.println("[" + this.getName() + "] received KILL");
                    isKilled = true;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
