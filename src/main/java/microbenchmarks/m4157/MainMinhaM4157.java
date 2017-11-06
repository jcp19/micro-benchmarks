package microbenchmarks.m4157;

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
public class MainMinhaM4157 {

    public static void main(String args[]) {
        try {
            World world = new Simulation();

            Host hostB = world.createHost();
            Process procB = hostB.createProcess();
            InetSocketAddress ipB = new InetSocketAddress(hostB.getAddress(),2182);
            Entry<INode> nodeB = procB.createEntry(INode.class, NodeBRM.class.getName());

            Host hostA = world.createHost();
            Process procA = hostA.createProcess();
            InetSocketAddress ipA = new InetSocketAddress(hostA.getAddress(),2183);
            Entry<INode> nodeA = procA.createEntry(INode.class, NodeAAM.class.getName());

            List<InetSocketAddress> targetsA = new ArrayList<InetSocketAddress>(1);
            targetsA.add(ipB);

            nodeA.call().initNode("A",ipA, targetsA);
            nodeB.call().initNode("B",ipB, null);

            nodeB.queue().run();
            nodeA.queue().run();

            world.run();
            world.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
