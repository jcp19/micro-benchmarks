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
public class NodeBRM extends Node implements INode {

    public NodeBRM(){}

    public NodeBRM(String id, InetSocketAddress ip){
        super(id,ip);
    }

    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets)
    {
        this.myid = id;
        this.myip = ip;
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
            DatagramPacket packet = new DatagramPacket(new byte[256], 256);
            socket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength());
            if (msg.equals("UNREGISTER")) {
               // Thread.sleep(100); //uncomment to observe bug more often
                System.out.println("[" + myid + "] received UNREGISTER");
                byte[] toSend = "KILL".getBytes();
                packet = new DatagramPacket(toSend, toSend.length, packet.getSocketAddress());
                System.out.println("["+myid+"] send KILL");
                socket.send(packet);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
