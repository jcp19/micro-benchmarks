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
public class NodeCAM extends Node implements INode {

    public boolean commitAttempt;

    public NodeCAM(){};

    public NodeCAM(String id, InetSocketAddress ip) {
        super(id,ip);
        this.commitAttempt = false;
    }

    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        this.myid = id;
        this.myip = ip;
        this.commitAttempt = false;
        try {
            this.socket = new DatagramSocket(myip);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.setName(id);
        System.out.println("[" + myid + "] started");
    }

    @Override
    public void run(){
        runM5009();

    }

    private void runM5009(){
        int toRcv = 2;
        try{
            while(toRcv > 0){
                DatagramPacket packet = new DatagramPacket(new byte[256],256);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                if(msg.equals("COMMIT")){
                    System.out.println("[" + myid + "] received COMMIT");
                    if(!commitAttempt) {
                        commitAttempt = true;
                    }
                    else{
                        System.out.println("[" + myid + "] BUG - Double commit exception! Rejecting COMMIT because commitAttempt is already set");
                    }
                }
                else if(msg.equals("COMMIT_COMPLETED")) {
                    commitAttempt = false;
                    System.out.println("[" + myid + "] received COMMIT_COMPLETED");
                }
                toRcv--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
