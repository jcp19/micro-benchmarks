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
public class NodeBRM extends Node implements INode {
    InetSocketAddress nm;

    public NodeBRM(){}

    public NodeBRM(String id, InetSocketAddress ip, List<InetSocketAddress> targets){
        super(id,ip);
        this.nm = targets.get(0);
    }

    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets){
        this.myid = id;
        this.myip = ip;
        this.nm = targets.get(0);
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

    private void runM3274(){
        try {
            //Thread.sleep(1000);
            byte[] toSend = "CONTAINER".getBytes();
            DatagramPacket packet = new DatagramPacket(toSend, toSend.length, nm);
            socket.send(packet);
            System.out.println("["+myid+"] send CONTAINER");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
