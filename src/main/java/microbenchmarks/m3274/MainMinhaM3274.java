package microbenchmarks.m3274;

import microbenchmarks.common.*;
import pt.minha.api.Entry;
import pt.minha.api.Host;
import pt.minha.api.Process;
import pt.minha.api.World;
import pt.minha.api.sim.Simulation;

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
public class MainMinhaM3274 {

    public static void main(String args[]) {
        try {
            World world = new Simulation();

            Host hostC = world.createHost();
            Process procC = hostC.createProcess();
            InetSocketAddress ipC = new InetSocketAddress(hostC.getAddress(),2181);
            Entry<INode> nodeC = procC.createEntry(INode.class, NodeCNM.class.getName());

            Host hostB = world.createHost();
            Process procB = hostB.createProcess();
            InetSocketAddress ipB = new InetSocketAddress(hostB.getAddress(),2182);
            Entry<INode> nodeB = procB.createEntry(INode.class, NodeBRM.class.getName());

            Host hostA = world.createHost();
            Process procA = hostA.createProcess();
            InetSocketAddress ipA = new InetSocketAddress(hostA.getAddress(),2183);
            Entry<INode> nodeA = procA.createEntry(INode.class, NodeAAM.class.getName());


            List<InetSocketAddress> targetsA = new ArrayList<InetSocketAddress>(1);
            targetsA.add(ipC);

            List<InetSocketAddress> targetsB = new ArrayList<InetSocketAddress>(1);
            targetsB.add(ipC);

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
