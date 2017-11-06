package dataflaskslite.test;

import dataflaskslite.api.Membership;
import dataflaskslite.api.Peer;
import dataflaskslite.common.Parameters;
import dataflaskslite.cyclon.Cyclon;
import dataflaskslite.cyclon.CyclonPassive;
import dataflaskslite.cyclon.CyclonPeer;

import java.util.HashMap;
import java.util.List;


public class DFMain {

	//Args: PeerID Address Port [List of Peers <PeerID Address Port>]
    public static void main(String ... args) throws  Exception {    	
    	if(args.length<3){
    		System.out.println("Insufficient number of arguments.");
    		System.exit(1);
    	}
    	Integer id = Integer.parseInt(args[0]);
		String ip = args[1];
		int port = Integer.parseInt(args[2]);
    	CyclonPeer thispeer = new CyclonPeer();
    	thispeer.initPeer(id,ip,port);
    	
    	HashMap<Integer,Peer> initialview = new HashMap<Integer, Peer>();
    	
    	for(int i = 3;i<args.length;i=i+3){
    		Integer tid = Integer.parseInt(args[i]);
    		String tip = args[i+1];
    		int tport = Integer.parseInt(args[i+2]);
    		CyclonPeer tpeer = new CyclonPeer();
    		tpeer.initPeer(tid,tip,tport);
    		if(!tpeer.equals(thispeer)){
    			initialview.put(tpeer.getId(), tpeer);
    		}
    	}
    	System.out.print("["+id+"] My initial view: ");
    	for(Peer p : initialview.values()){
    		System.out.print(p.getId()+" ");
    	}
    	System.out.print("\n");
    	
    	
        Membership membership = new Cyclon(thispeer,initialview);
        CyclonPassive listener = new CyclonPassive((Cyclon)membership, thispeer);
        listener.start();
        ((Cyclon)membership).start();

        //obtain/refresh the view every 5 seconds
        for(int i = 0; i < Parameters.NUMCYCLES; i++){

            List<Peer> view = membership.getView();
            System.out.print("["+id+"] My view is ("+view.size()+"): ");
            for(Peer p : view) {
                System.out.print(p.getId()+" ");
            }
            System.out.print("\n");
            Thread.sleep(5000);
        }
    }
}
