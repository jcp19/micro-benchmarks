package microbenchmarks.h5780;

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
public class MainH5780 {

    public static void main(String args[]){
        InetSocketAddress ipA = new InetSocketAddress("127.0.0.1",2181);
        InetSocketAddress ipB = new InetSocketAddress("127.0.0.1",2182);
        InetSocketAddress ipC = new InetSocketAddress("127.0.0.1",2183);

        NodeAZK nodeA = new NodeAZK("A", ipA);
        nodeA.start();

        NodeCCM nodeC = new NodeCCM("C", ipC);
        nodeC.start();

        List<InetSocketAddress> targets = new ArrayList(2);
        targets.add(ipC); //master address
        targets.add(ipA); //ZK address
        NodeBRS nodeB = new NodeBRS("B", ipB, targets);
        nodeB.start();

    }
}
