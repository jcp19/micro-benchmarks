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
public class NodeARM extends Node implements INode {
    InetSocketAddress nm;

    public NodeARM(){};

    public NodeARM(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        super(id,ip);
        this.nm = targets.get(0);
    }

    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        this.myid = id;
        this.myip = ip;
        this.nm = targets.get(0);
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
            byte[] toSend = "KILL".getBytes();
            DatagramPacket packet = new DatagramPacket(toSend, toSend.length, nm);
            socket.send(packet);
            System.out.println("["+myid+"] send KILL");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
