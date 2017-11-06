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
public class NodeAAM extends Node implements INode {
    InetSocketAddress nm;

    public NodeAAM(){}

    public NodeAAM(String id, InetSocketAddress ip, List<InetSocketAddress> targets){
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

    public void runM3274(){
        try {
            byte[] toSend = "KILL".getBytes();
            DatagramPacket packet = new DatagramPacket(toSend, toSend.length, nm);
            System.out.println("["+myid+"] send KILL");
            socket.send(packet);

            packet = new DatagramPacket(new byte[256], 256);
            socket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength());
            if(!msg.equals("KILL_ACK")){
                System.out.println("["+myid+"] BUG - double-completion exception! Expected KILL_ACK but received "+msg+" instead");
            }
            else{
                System.out.println("["+myid+"] received "+msg);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
