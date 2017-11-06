package dataflaskslite.api;

import pt.minha.api.sim.Global;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by mm on 10/10/2016.
 */
@Global
public interface Peer {

    int getId();
    InetSocketAddress getIP();
    void initPeer(int id, String ip, int port);
    void initPeer(int id, String ip, int port, List<String> peers);
    void initPeerStream(DataInputStream dis) throws IOException;
    void runPeer();
}
