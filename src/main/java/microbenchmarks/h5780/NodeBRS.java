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
public class NodeBRS extends Node implements INode {
    InetSocketAddress master;
    InetSocketAddress zk;
    String securityCode;

    public NodeBRS() {}

    public NodeBRS(String id, InetSocketAddress ip, List<InetSocketAddress> targets) {
        super(id, ip);
        this.master = targets.get(0);
        this.zk = targets.get(1);
        this.securityCode = "";
    }

    public void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets){
        this.myid = id;
        this.myip = ip;
        this.master = targets.get(0);
        this.zk = targets.get(1);
        this.securityCode = "";
        try {
            this.socket = new DatagramSocket(myip);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        this.setName(id);
        System.out.println("[" + id + "] started");
    }

    @Override
    public void run() {
        runH5780();

    }

    private void runH5780() {
        try {
            ZKConnector zkc = new ZKConnector();
            zkc.start();

            //Thread.sleep(100);
            String joinstr = "JOIN-" + myid + ":" + securityCode;
            byte[] joinreq = joinstr.getBytes();
            DatagramPacket packet = new DatagramPacket(joinreq, joinreq.length, master);
            System.out.println("[" + this.getName() + "] send "+joinstr);
            socket.send(packet);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public class ZKConnector extends Thread {
        public ZKConnector() {
            this.setName(myid + "-zkconnector");
        }

        @Override
        public void run() {
            try {
                byte[] toSend = "REQ".getBytes();
                DatagramPacket packet = new DatagramPacket(toSend, toSend.length, zk);
                System.out.println("[" + this.getName() + "] send REQ (request security code)");
                socket.send(packet);

                packet = new DatagramPacket(new byte[256],256);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                securityCode = msg;
                System.out.println("[" + this.getName() + "] received security code "+securityCode);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
