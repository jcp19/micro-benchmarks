package microbenchmarks.m5009;

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
public class MainMinhaM5009 {

    public static void main(String args[]) {
        try {
            World world = new Simulation();

            Host hostC = world.createHost();
            Process procC = hostC.createProcess();
            InetSocketAddress ipC = new InetSocketAddress(hostC.getAddress(),2181);
            Entry<INode> nodeC = procC.createEntry(INode.class, NodeCAM.class.getName());

            Host hostB = world.createHost();
            Process procB = hostB.createProcess();
            InetSocketAddress ipB = new InetSocketAddress(hostB.getAddress(),2182);
            Entry<INode> nodeB = procB.createEntry(INode.class, NodeBNM.class.getName());

            Host hostA = world.createHost();
            Process procA = hostA.createProcess();
            InetSocketAddress ipA = new InetSocketAddress(hostA.getAddress(),2183);
            Entry<INode> nodeA = procA.createEntry(INode.class, NodeARM.class.getName());

            List<InetSocketAddress> targetsA = new ArrayList<InetSocketAddress>(1);
            targetsA.add(ipB);

            List<InetSocketAddress> targetsB = new ArrayList<InetSocketAddress>(1);
            targetsB.add(ipC);

            nodeC.call().initNode("C",ipC,null);
            nodeB.call().initNode("B",ipB, targetsB);
            nodeA.call().initNode("A",ipA, targetsA);

            nodeC.queue().run();
            nodeA.queue().run();
            nodeB.queue().run();

            world.run();
            world.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
