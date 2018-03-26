package rexbenchmarks.distributed;

import microbenchmarks.common.Node;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sender extends Node {
    InetSocketAddress target;
    int value;

    public Sender(String id, InetSocketAddress ip, List<InetSocketAddress> targets, int val){
        super(id, ip);
        value = val;
        this.target = targets.get(0);
    }


    @Override
    public void run() {
        try{
            //random sleep to create non-determinism
            Random r = new Random();
            Thread.sleep(r.nextInt(10)*100);

            byte[] valueByte = String.valueOf(value).getBytes();
            DatagramPacket packet = new DatagramPacket(valueByte, valueByte.length, target);
            System.out.println("[" + this.getName() + "] send "+value);
            this.socket.send(packet);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        String id = args[0];
        String hostname = args[1];
        int port = Integer.valueOf(args[2]);
        InetSocketAddress ip = new InetSocketAddress(hostname, port);
        int value = Integer.valueOf(args[3]);

        List<InetSocketAddress> targets = new ArrayList<InetSocketAddress>();
        for(int i = 4; i < args.length; i+=2){
            String targetIp = args[i];
            int targetPort = Integer.valueOf(args[i+1]);
            targets.add(new InetSocketAddress(targetIp, targetPort));
        }

        Sender s = new Sender(id, ip, targets, value);
        s.start();
    }
}
