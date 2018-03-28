package microbenchmarks.common;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by nunomachado on 07/04/17.
 */
public abstract class Node extends Thread {

    public DatagramSocket socket;
    public InetSocketAddress myip;
    public String myid;

    public Node(){}

    public Node(String id, InetSocketAddress ip){
        this.setName(id);
        myid = id;
        myip = ip;
        try {
            this.socket = new DatagramSocket(myip);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("["+id+"] started @ "+myip);
    }

    @Override
    public void run(){}
}
