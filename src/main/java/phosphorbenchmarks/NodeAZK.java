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
public class NodeAZK extends Node implements INode{

    public NodeAZK(){}

    public NodeAZK(String id, InetSocketAddress ip){
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
            if(msg.equals("REQ")){
                byte[] code = "12345".getBytes();
                packet = new DatagramPacket(code, code.length, packet.getSocketAddress());
                socket.send(packet);
                System.out.println("["+myid+"] send security code 12345");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
