package microbenchmarks.m4157;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Message-Compute race
 * Scenario:
    1. AAM finishes the job
    2. AAM unregisters itself to BRM (TRIG)
    3. AAM starts to clean up the job history in itself (concurrently to step 2) (TRIG)
    4. BRM KILL all active containers in NMs, including the AAM's active container (TRIG)
    5. Then AAM will stop the cleanup process and leaves the deprecated files on disk
 *
 * Created by nunomachado on 11/04/17.
 */
public class MainM4157 {

    public static void main(String args[]){
        InetSocketAddress ipA = new InetSocketAddress("127.0.0.1",2181);
        InetSocketAddress ipB = new InetSocketAddress("127.0.0.1",2182);

        List<InetSocketAddress> targetsA = new ArrayList<InetSocketAddress>(1);
        targetsA.add(ipB);
        NodeAAM nodeA = new NodeAAM("A", ipA, targetsA);
        NodeBRM nodeB = new NodeBRM("B", ipB);

        nodeB.start();
        nodeA.start();
    }
}
