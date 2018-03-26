package rexbenchmarks.distributed;

import microbenchmarks.common.Node;
import rexbenchmarks.Counter;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantLock;

public class Receiver extends Node {
    int senders;

    public Receiver(String id, InetSocketAddress ip, int numSenders){
        super(id,ip);
        senders = numSenders;
    }

    @Override
    public void run(){
        try {

            Counter c = new Counter();
            ReentrantLock lock = new ReentrantLock();
            WorkerThread[] workers = new WorkerThread[senders];

            for(int i = 0; i < senders; i++){
                DatagramPacket packet = new DatagramPacket(new byte[256], 256);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                int value = Integer.valueOf(msg);
                if(value == 0){
                    c.x = 0;
                }
                else{
                    workers[i] = new WorkerThread(i, value, c, lock);
                    workers[i].start();
                }
            }

            for(int i = 0; i < senders; i++){
                if(workers[i]!=null)
                    workers[i].join();
            }
            System.out.println("[Receiver] counter = "+c.x);
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
        int senders = Integer.valueOf(args[3]);

        Receiver r = new Receiver(id, ip, senders);
        r.start();
    }


    public class WorkerThread extends Thread{
        int id;
        int value;
        Counter c;
        ReentrantLock l;

        public WorkerThread(int id, int value, Counter counter, ReentrantLock lock){
            this.id = id;
            this.value = value;
            this.c = counter;
            this.l = lock;
        }

        @Override
        public void run(){
            l.lock();
            c.x += value;
            l.unlock();
            System.out.println("[Worker-"+id+"] counter = "+c.x);
        }
    }
}
