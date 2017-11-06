package microbenchmarks.m3274;

import microbenchmarks.common.INode;
import microbenchmarks.common.Node;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

/**
 * Created by nunomachado on 06/04/17.
 */
public class NodeCNM extends Node implements INode {
    public boolean hasContainer;
    public InetSocketAddress am;
    private int MAX = 100;

    public NodeCNM(){}

    public NodeCNM(String id, InetSocketAddress ip, List<InetSocketAddress> targets){
        super(id,ip);
        this.hasContainer = false;
        this.am = targets.get(0);
    }

    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets)
    {
        this.myid = id;
        this.myip = ip;
        this.hasContainer = false;
        this.am = targets.get(0);
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
        runM3274();
    }

    public void runM3274(){
        WorkerM3274 t = new WorkerM3274();
        int toRcv = 2;

        try{
            while(toRcv > 0){
                DatagramPacket packet = new DatagramPacket(new byte[256],256);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                if(msg.equals("CONTAINER")){
                    System.out.println("["+myid+"] received CONTAINER");
                    hasContainer = true;
                    t.start();
                }
                else if(msg.equals("KILL")){
                    System.out.println("["+myid+"] received KILL");
                    if(hasContainer){
                        byte[] ack = "KILL_ACK".getBytes();
                        packet = new DatagramPacket(ack, ack.length, am);
                        t.isKilled = true;
                        System.out.println("[" + this.getName() + "] send KILL_ACK");
                        socket.send(packet);
                    }
                    else {
                        System.out.println("["+myid+"] No container, ignore KILL");
                    }
                }
                toRcv--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class WorkerM3274 extends Thread {
        public boolean isKilled;

        public WorkerM3274(){
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
                if(!isKilled){
                    //Thread.sleep(100);
                    byte[] ack = "JOB_TASK_ATTEMPT_COMPLETED".getBytes();
                    DatagramPacket packet = new DatagramPacket(ack, ack.length, am);
                    System.out.println("[" + this.getName() + "] send JOB_TASK_ATTEMPT_COMPLETED");
                    socket.send(packet);
                }
                else {
                    System.out.println("["+this.getName()+"] killed");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
