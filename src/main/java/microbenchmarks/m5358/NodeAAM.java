package microbenchmarks.m5358;

import microbenchmarks.common.INode;
import microbenchmarks.common.Node;

import java.net.*;
import java.util.List;

/**
 * Created by nunomachado on 10/04/17.
 */
public class NodeAAM extends Node implements INode{

    public enum TaskStatus {PENDING, SUCCEEDED};
    public TaskStatus taskState;
    public InetSocketAddress nm1;
    public InetSocketAddress nm2;
    public boolean done;
    public String nm1Token = "C";
    public String nm2Token = "B";

    public NodeAAM(){}

    public NodeAAM(String id, InetSocketAddress ip, List<InetSocketAddress> targets)
    {
        super(id,ip);
        this.taskState = TaskStatus.PENDING;
        this.nm1 = targets.get(0);
        this.nm2 = targets.get(1);
    }

    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        this.myid = id;
        this.myip = ip;
        this.taskState = TaskStatus.PENDING;
        this.nm1 = targets.get(0);
        this.nm2 = targets.get(1);
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
        try {
            byte[] toSend = (nm1Token+":ASSIGN").getBytes();
            DatagramPacket packet = new DatagramPacket(toSend, toSend.length, nm1);
            System.out.println("[" + this.getName() + "] send ASSIGN to "+nm1Token);
            socket.send(packet);

            toSend = (nm2Token+":ASSIGN").getBytes();
            packet = new DatagramPacket(toSend, toSend.length, nm2);
            System.out.println("[" + this.getName() + "] send ASSIGN to B"+nm2Token);
            socket.send(packet);

            while(!done) {
                packet = new DatagramPacket(new byte[256], 256);
                socket.receive(packet);
                processPacket(packet);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void processPacket(DatagramPacket packet){

        try {
            String msg = new String(packet.getData(), 0, packet.getLength());
            String[]parts = msg.split(":");
            String token = parts[0];
            msg = parts[1];
            boolean isSpeculativeTask = isSpeculativeTask(token);
            System.out.println("[" + this.getName() + "] received " + msg + " from " + token);

            if (msg.equals("JOB_TASK_ATTEMPT_COMPLETED")) {

                if (taskState == TaskStatus.PENDING && !isSpeculativeTask) {
                    taskState = TaskStatus.SUCCEEDED;
                    byte[] killMsg = (nm2Token+":KILL").getBytes();
                    packet = new DatagramPacket(killMsg, killMsg.length, nm2);
                    System.out.println("[" + this.getName() + "] send KILL to speculative "+nm2Token);
                    socket.send(packet);
                } else if (taskState == TaskStatus.SUCCEEDED) {
                    System.out.println("[" + this.getName() + "] BUG - wrong state transition!");
                    done = true;
                }
            }
            else if(msg.equals("KILL_ACK")){
                done = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isSpeculativeTask(String token){
        return token.equals(nm2Token);
    }
}
