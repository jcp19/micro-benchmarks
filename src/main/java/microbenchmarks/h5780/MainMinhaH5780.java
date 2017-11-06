package microbenchmarks.h5780;

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
 * Message-message race arrival/sending
 * Scenario:
    1. BRS starts
    2. BRS asks the AZK for SaslAuthenticated security access (TRIG)
    3. BRS asks to join the existing cluster to CMaster (TRIG)
    4. CMaster rejects BRS request to join the cluster
    5. BRS initialization is aborted (BRS doesn't have the security access)
    6. AZK sends the security access to ARS
 *
 * Created by nunomachado on 08/04/17.
 */
public class MainMinhaH5780 {

    public static void main(String args[]) {
        try {
            World world = new Simulation();

            Host hostA = world.createHost();
            Process procA = hostA.createProcess();
            InetSocketAddress ipA = new InetSocketAddress(hostA.getAddress(),2183);
            Entry<INode> nodeA = procA.createEntry(INode.class, NodeAZK.class.getName());

            Host hostC = world.createHost();
            Process procC = hostC.createProcess();
            InetSocketAddress ipC = new InetSocketAddress(hostC.getAddress(),2181);
            Entry<INode> nodeC = procC.createEntry(INode.class, NodeCCM.class.getName());

            Host hostB = world.createHost();
            Process procB = hostB.createProcess();
            InetSocketAddress ipB = new InetSocketAddress(hostB.getAddress(),2182);
            Entry<INode> nodeB = procB.createEntry(INode.class, NodeBRS.class.getName());

            List<InetSocketAddress> targets = new ArrayList(2);
            targets.add(ipC); //master address
            targets.add(ipA); //ZK address

            nodeC.call().initNode("C",ipC, null);
            nodeA.call().initNode("A",ipA, null);
            nodeB.call().initNode("B",ipB, targets);

            nodeA.queue().run();
            nodeC.queue().run();
            nodeB.queue().run();

            world.run();
            world.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
