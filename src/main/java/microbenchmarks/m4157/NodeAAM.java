package microbenchmarks.m4157;

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
public class NodeAAM extends Node implements INode {

    InetSocketAddress rm;
    boolean isCleaning;

    public NodeAAM(){}

    public NodeAAM(String id, InetSocketAddress ip, List<InetSocketAddress> targets){
        super(id,ip);
        this.rm = targets.get(0);
        this.isCleaning = false;
    }

    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        this.myid = id;
        this.myip = ip;
        this.rm = targets.get(0);
        this.isCleaning = false;
        try {
            this.socket = new DatagramSocket(myip);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.setName(id);
        System.out.println("["+id+"] started");
    }

    @Override
    public void run(){
        runM4157();
    }

    public void runM4157(){
        try {
            byte[] toSend = "UNREGISTER".getBytes();
            DatagramPacket packet = new DatagramPacket(toSend, toSend.length, rm);
            socket.send(packet);
            System.out.println("[" + myid + "] send UNREGISTER");

            packet = new DatagramPacket(new byte[256],256);
            socket.receive(packet);
            CleanerM4157 cleaner = new CleanerM4157();
            cleaner.start();
            String msg = new String(packet.getData(), 0, packet.getLength());
            if(msg.equals("KILL")){
                System.out.println("["+myid+"] received KILL");
                if(isCleaning){
                    System.out.println("["+myid+"] BUG - Cleaning is still occurring!");
                    cleaner.isKilled = true;
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public class CleanerM4157 extends Thread {
        public boolean isKilled;
        public int NUMFILES;

        public CleanerM4157() {
            NUMFILES = 100;
            isKilled = false;
            this.setName(myid + "-cleaner");
        }

        @Override
        public void run() {
            System.out.println("["+this.getName()+"] start cleaning");
            isCleaning = true;
            int i;
            for(i = 0; i < NUMFILES && !isKilled; i++){
                System.out.print("");
            }
            if(!isKilled) {
                isCleaning = false;
                System.out.println("[" + this.getName() + "] finished cleaning");
            }
            else{
                System.out.println("[" + this.getName() + "] killed");
            }
        }
    }
}
