package microbenchmarks.m3274;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Message-message race at one node
 * Scenario:
     1. BRM schedules the container for CNM (TRIG)
    2. AAM sends KILL msg to CNM in order to preempt the reduce task (TRIG)
    3. CNM ignores KILL msg because CNM has not yet activated the TaskAttemptListener (it will be activated when CNM receives a container) (PROP)
    4. BRM sends the container to CNM
    5. CNM works on the task
    6. CNM returns the result to AM
    7. AAM still waits for the KILL return value, but it never comes back (PROP)
    8. Job hangs
 *
 * Created by nunomachado on 07/04/17.
 */
public class MainM3274 {

    public static void main(String args[]){
        InetSocketAddress ipA = new InetSocketAddress("127.0.0.1",2181);
        InetSocketAddress ipB = new InetSocketAddress("127.0.0.1",2182);
        InetSocketAddress ipC = new InetSocketAddress("127.0.0.1",2183);

        List<InetSocketAddress> targetsA = new ArrayList<InetSocketAddress>(1);
        targetsA.add(ipC);
        NodeAAM nodeA = new NodeAAM("A", ipA, targetsA);

        List<InetSocketAddress> targetsB = new ArrayList<InetSocketAddress>(1);
        targetsB.add(ipC);
        NodeBRM nodeB = new NodeBRM("B", ipB, targetsB);

        List<InetSocketAddress> targetsC = new ArrayList<InetSocketAddress>(1);
        targetsC.add(ipA);
        NodeCNM nodeC = new NodeCNM("C", ipC, targetsC);

        nodeC.start();
        nodeB.start();
        nodeA.start();
    }
}
