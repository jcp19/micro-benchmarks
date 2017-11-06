package microbenchmarks.m5358;

import microbenchmarks.common.INode;
import microbenchmarks.common.Node;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

/**
 * Created by nunomachado on 10/04/17.
 */
public class NodeBNM2 extends Node implements INode{
    private int MAX = 100;
    public InetSocketAddress am;
    public String token;

    public NodeBNM2(){};

    public NodeBNM2(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        super(id,ip);
        this.am = targets.get(0);
        this.token = "";
    }

    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        this.myid = id;
        this.myip = ip;
        this.am = targets.get(0);
        this.token = "";
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
        runM5358();

    }

    private void runM5358(){
        WorkerM5358 t = new WorkerM5358();
        int toRcv = 2;

        try {
            while(toRcv > 0){
                DatagramPacket packet = new DatagramPacket(new byte[256], 256);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                String[]parts = msg.split(":");
                token = parts[0];
                msg = parts[1];
                if (msg.equals("ASSIGN")) {
                    System.out.println("[" + this.getName() + "] received ASSIGN");
                    t.start();
                }
                else if(msg.equals("KILL")) {
                    System.out.println("[" + myid + "] received KILL");
                    byte[] ack = (token+":KILL_ACK").getBytes();
                    packet = new DatagramPacket(ack, ack.length, am);
                    t.kill();
                    System.out.println("[" + this.getName() + "] send KILL_ACK");
                    socket.send(packet);
                }
                toRcv--;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public class WorkerM5358 extends Thread {
        public boolean isKilled;

        public WorkerM5358(){
            isKilled = false;
            this.setName(myid+"-worker");
        }

        @Override
        public void run(){
            int i;
            try {
                for(i = 0; i < MAX && !isKilled; i++){
                    System.out.println("[" + this.getName() + "] do task " + i);
                }
                if(i == MAX && !isKilled){
                    //Thread.sleep(100);
                    byte[] ack = (token+":JOB_TASK_ATTEMPT_COMPLETED").getBytes();
                    DatagramPacket packet = new DatagramPacket(ack, ack.length, am);
                    System.out.println("[" + this.getName() + "] send JOB_TASK_ATTEMPT_COMPLETED");
                    socket.send(packet);
                }
                else if(isKilled){
                    System.out.println("["+this.getName()+"] killed");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        public void kill() {
            isKilled = true;
        }
    }
}
