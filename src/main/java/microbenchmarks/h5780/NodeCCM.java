package microbenchmarks.h5780;

import microbenchmarks.common.INode;
import microbenchmarks.common.Node;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

/**
 * Created by nunomachado on 08/04/17.
 */
public class NodeCCM extends Node implements INode{

    public NodeCCM(){}

    public NodeCCM(String id, InetSocketAddress ip){
        super(id,ip);
    }


    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        initNode(id, ip);
    }

    public void initNode(String id, InetSocketAddress ip)
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
        runH5780();

    }

    private void runH5780() {
        try {
            DatagramPacket packet = new DatagramPacket(new byte[256], 256);
            socket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength());
            System.out.println("["+myid+"] received "+msg);
            if(msg.indexOf(":") == (msg.length()-1)){
                System.out.println("["+myid+"] BUG - join rejected!");
            }
            else{
                System.out.println("["+myid+"] join accepted!");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
