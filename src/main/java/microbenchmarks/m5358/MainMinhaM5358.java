package microbenchmarks.m5358;

import microbenchmarks.common.INode;
import pt.minha.api.Entry;
import pt.minha.api.Host;
import pt.minha.api.Process;
import pt.minha.api.World;
import pt.minha.api.sim.Simulation;

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
public class MainMinhaM5358 {

    public static void main(String args[]) {
        try {
            World world = new Simulation();

            Host hostC = world.createHost();
            Process procC = hostC.createProcess();
            InetSocketAddress ipC = new InetSocketAddress(hostC.getAddress(),2181);
            Entry<INode> nodeC = procC.createEntry(INode.class, NodeCNM1.class.getName());

            Host hostB = world.createHost();
            Process procB = hostB.createProcess();
            InetSocketAddress ipB = new InetSocketAddress(hostB.getAddress(),2182);
            Entry<INode> nodeB = procB.createEntry(INode.class, NodeBNM2.class.getName());

            Host hostA = world.createHost();
            Process procA = hostA.createProcess();
            InetSocketAddress ipA = new InetSocketAddress(hostA.getAddress(),2183);
            Entry<INode> nodeA = procA.createEntry(INode.class, NodeAAM.class.getName());

            List<InetSocketAddress> targetsA = new ArrayList<InetSocketAddress>(2);
            targetsA.add(ipC); //nm1
            targetsA.add(ipB); //nm2

            List<InetSocketAddress> targetsB = new ArrayList<InetSocketAddress>(1);
            targetsB.add(ipA);

            List<InetSocketAddress> targetsC = new ArrayList<InetSocketAddress>(1);
            targetsC.add(ipA);

            nodeC.call().initNode("C",ipC, targetsC);
            nodeB.call().initNode("B",ipB, targetsB);
            nodeA.call().initNode("A",ipA, targetsA);

            nodeC.queue().run();
            nodeB.queue().run();
            nodeA.queue().run();

            world.run();
            world.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
