package microbenchmarks.m5009;


import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *  Atomicity Violation
 *  Scenario:
    1. BNM has finished a reduce task
    2. BNM is committing the reduce task results to HDFS (CAM)
    3. CAM notices BNM's process through heartbeat (TRIG)
    4. CAM sets a flag 'commitAttempt' (TRIG)
    5. ARM preemptively kills the reduce task to BNM (while BNM is still committing) (TRIG)
    6. Later, BNM re-works the reduce task (PROP)
    7. Then BNM commits the task to CAM again (PROP)
    8. CAM rejects the BNM commit because the 'commitAttempt' is not null (PROP)
    9. Job hangs in CAM Termination point: CAM keeps killing reduce tasks
 *
 *  Created by nunomachado on 07/04/17.
 */
public class MainM5009 {

    public static void main(String args[]){
        InetSocketAddress ipA = new InetSocketAddress("127.0.0.1",2181);
        InetSocketAddress ipB = new InetSocketAddress("127.0.0.1",2182);
        InetSocketAddress ipC = new InetSocketAddress("127.0.0.1",2183);

        List<InetSocketAddress> targetsA = new ArrayList<InetSocketAddress>(1);
        targetsA.add(ipB);

        List<InetSocketAddress> targetsB = new ArrayList<InetSocketAddress>(1);
        targetsB.add(ipC);


        NodeARM nodeA = new NodeARM("A", ipA, targetsA);
        NodeBNM nodeB = new NodeBNM("B", ipB, targetsB);
        NodeCAM nodeC = new NodeCAM("C", ipC);

        nodeC.start();
        nodeA.start();
        nodeB.start();

    }
}
