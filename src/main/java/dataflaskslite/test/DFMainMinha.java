package dataflaskslite.test;

import dataflaskslite.api.Peer;
import dataflaskslite.common.Parameters;
import dataflaskslite.cyclon.CyclonPeer;
import org.slf4j.LoggerFactory;
import pt.minha.api.Entry;
import pt.minha.api.World;
import pt.minha.api.sim.Simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nunomachado on 03/11/17.
 */
public class DFMainMinha {

    public static void main(String[] args){
        //list of peers
        int npeers = Parameters.NUMPEERS;
        List<String> peers = new ArrayList<String>(npeers);
        LoggerFactory.getLogger("pt.minha.TRACER").isInfoEnabled();

        try {
            World world = new Simulation();

            Entry<Peer>[] peerEntries = world.createEntries(npeers, Peer.class, CyclonPeer.class.getName());
            //get host names
            for(int i = 0; i < peerEntries.length; i++) {
                String peerStr = i+" "+peerEntries[i].getProcess().getHost().getAddress().getCanonicalHostName()+" "+(50001+i);
                peers.add(peerStr);
            }

            //init peers
            for(int i = 0; i < peerEntries.length; i++){
                peerEntries[i].call().initPeer(i, peerEntries[i].getProcess().getHost().getAddress().getCanonicalHostName(), 50001+i, peers);
            }

            //run peers
            for(int i = 0; i < peerEntries.length; i++){
                peerEntries[i].queue().runPeer();
            }
            world.runAll(peerEntries);

            world.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
