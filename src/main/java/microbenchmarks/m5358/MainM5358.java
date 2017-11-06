package microbenchmarks.m5358;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Message-message race across two nodes
 * Scenario:
    1. AAM assigns task to CNM1
    2. AAM assigns speculative task to BNM2
    3. CNM1 attempts succeeds
    4. CNM1 reports to AM (TRIG)
    5. AAM changes the task state to SUCCEEDED (TRIG)
    6. AAM sends KILL msg to BNM2 (TRIG)
    7. BNM2 sends JOB_TASK_ATTEMPT_COMPLETED or JOB_MAP_TASK_RESCHEDULED to AM (before the AM's KILL msg arrives) (TRIG)
    8. AAM treats BNM2's msg as invalid event - wrong state transition (PROP)
 *
 * Created by nunomachado on 10/04/17.
 */
public class MainM5358 {

    public static void main(String args[]){
        InetSocketAddress ipA = new InetSocketAddress("127.0.0.1",2181);
        InetSocketAddress ipB = new InetSocketAddress("127.0.0.1",2182);
        InetSocketAddress ipC = new InetSocketAddress("127.0.0.1",2183);

        List<InetSocketAddress> targetsA = new ArrayList<InetSocketAddress>(2);
        targetsA.add(ipC); //nm1
        targetsA.add(ipB); //nm2

        List<InetSocketAddress> targetsB = new ArrayList<InetSocketAddress>(1);
        targetsB.add(ipA);

        List<InetSocketAddress> targetsC = new ArrayList<InetSocketAddress>(1);
        targetsC.add(ipA);

        NodeCNM1 nodeC = new NodeCNM1("C", ipC, targetsC);
        nodeC.start();

        NodeBNM2 nodeB = new NodeBNM2("B",ipB, targetsB);
        nodeB.start();

        NodeAAM nodeA = new NodeAAM("A", ipA, targetsA);
        nodeA.start();
    }
}
