package rexbenchmarks.distributed;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ExampleDistributed {

    public static void main(String args[]){
        int SENDERS = args.length > 0 ? Integer.valueOf(args[0]) : 5;
        System.out.println("SENDERS = "+SENDERS);

        InetSocketAddress ipR = new InetSocketAddress("127.0.0.1",2180);
        Receiver r = new Receiver("NR", ipR, SENDERS);
        r.start();

        List<InetSocketAddress> targets = new ArrayList();
        targets.add(ipR);

        for(int i = 0; i < SENDERS; i++){
            String id = "N"+i;
            InetSocketAddress ip = new InetSocketAddress("127.0.0.1",2181+i);

            Sender s = new Sender(id, ip, targets, i);
            s.start();
        }

    }
}
