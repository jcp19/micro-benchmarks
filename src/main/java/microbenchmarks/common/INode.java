package microbenchmarks.common;

import pt.minha.api.sim.Global;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by nunomachado on 07/04/17.
 */
@Global
public interface INode {
    void run();
    void initNode(String id, InetSocketAddress ip, List<InetSocketAddress> targets);
}
