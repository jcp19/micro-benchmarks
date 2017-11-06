package dataflaskslite.cyclon;


import dataflaskslite.api.Membership;
import dataflaskslite.api.Peer;
import dataflaskslite.common.Parameters;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;


public class Cyclon extends Thread implements Membership {

	private Random grandom;
	
	private Peer myself;
	private Set<Integer> myview;
	private HashMap<Integer,Peer> idToPeer;
	private ArrayList<Peer> sentPeerData;
	private DatagramSocket sendersocket;
	
	public Cyclon(Peer myself){
		this.setName("Cyclon");
		this.myself = myself;
		this.myview = new HashSet<Integer>(Parameters.VIEWSIZE);
		this.idToPeer = new HashMap<Integer, Peer>();
		this.sentPeerData = new ArrayList<Peer>();
		this.grandom = new Random();
		try {
			this.sendersocket = new DatagramSocket();
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
	}
	
	public Cyclon(Peer myself, HashMap<Integer,Peer> initialView ){
		this.setName("Cyclon");
		this.myself = myself;
		this.idToPeer = initialView;
		this.myview = new HashSet<Integer>(initialView.keySet());
		this.sentPeerData = new ArrayList<Peer>();
		this.grandom = new Random();
		try {
			this.sendersocket = new DatagramSocket();
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
	}
	
    public List<Peer> getView() {
        return new ArrayList<Peer>(this.idToPeer.values());
    }
    
    
    private synchronized void removeOlderGlobal(){
		Peer toremove = null;
		for (Peer pe : this.idToPeer.values()){
			if(toremove == null){
				toremove = pe;
			}
			else{
				if(((CyclonPeer)pe).getAge()>((CyclonPeer)toremove).getAge()){
					toremove = pe;
				}
			}
		}
		this.idToPeer.remove(toremove.getId());
		this.myview = new HashSet<Integer>(idToPeer.keySet());
	}
    
	
	private synchronized void insertSentToView(ArrayList<Peer> source){
		while(this.idToPeer.size()< Parameters.VIEWSIZE && source.size()>0){
			Peer tmp = source.remove(0);
			if(!tmp.equals(myself)){
				Peer current = this.idToPeer.get(tmp.getId());
				if(current!=null){
					int currentage = ((CyclonPeer)current).getAge();
					if(currentage > ((CyclonPeer)tmp).getAge()){
						this.idToPeer.put(tmp.getId(), tmp);
					}
				}
				else{
					this.idToPeer.put(tmp.getId(), tmp);
				}
			}
		}
		this.myview = new HashSet<Integer>(idToPeer.keySet());
	}
    
    public synchronized void processMessage(CyclonMessage m){
		if(m==null){
			System.out.println("Cyclon processMessage MESSAGE is NULL!");
			return;
		}	
		ArrayList<Peer> source = new ArrayList<Peer>();
		for(Peer ptmp : m.list){
			if(!ptmp.equals(myself)){
				source.add((Peer)((CyclonPeer)ptmp).clone());
			}
		}
		if(m.type==CyclonMessage.TYPE.RESPONSE){
			this.insertSentToView(source);
			this.insertSentToView();
			while(this.myview.size() > Parameters.VIEWSIZE){
				this.removeOlderGlobal();
			}
			this.sentPeerData = new ArrayList<Peer>();
		}
		else{
			if(m.type==CyclonMessage.TYPE.REQUEST){
				//System.out.println("["+myself.getId()+"] R("+myself.getId()+","+m.sender.getId()+")");
				System.out.println(m.sender.getId()+"-"+myself.getId());
				ArrayList<Peer> tosend = this.selectToSendGlobal(m.sender);
				ArrayList<Peer> tofillemptyview = new ArrayList<Peer>();
				for(Peer p : tosend){
					if(!p.equals(this.myself)){
						tofillemptyview.add((Peer)((CyclonPeer)p).clone());
					}
				}
				Peer target = null;
				for(Peer rec : m.list){
					if(rec.equals(m.sender)){
						target = (Peer)((CyclonPeer)rec).clone();
					}
				}
				this.insertSentToView(source);
				this.insertSentToView(tofillemptyview);
				while(this.myview.size() > Parameters.VIEWSIZE){
					this.removeOlderGlobal();
				}
				CyclonMessage msgglobal = new CyclonMessage(tosend,CyclonMessage.TYPE.RESPONSE,this.myself);
				this.sendMsg(target, msgglobal);
			}
		}
    }

    
    
    private synchronized int sendMsg(Peer p, CyclonMessage psg){
		try {
			byte[] toSend = psg.encodeMessage();
			DatagramPacket packet = new DatagramPacket(toSend,toSend.length,p.getIP());
			sendersocket.send(packet);
			//System.out.println("["+myself.getId()+"] S("+myself.getId()+","+p.getId()+")");
			return 0;
		} catch (IOException e) {
			System.out.println("ERROR send Message in PEER. "+e.getMessage()+" IP:PORT " + p.getIP().getHostName()+":"+ p.getIP().getPort());
		}
		return 1;
	}
    
    private synchronized Peer getOlderGlobal(){
		Peer older = null;
		for(Integer p : this.myview){
			Peer pe = idToPeer.get(p);
			if(older == null){
				older = pe;
			}
			else{
				if(((CyclonPeer)pe).getAge()>((CyclonPeer)older).getAge()){
					older = pe;
				}
			}
		}
		return older;
	}
	
	private synchronized void ageGlobal(){
		for (Integer p : this.myview){
			Peer pe = idToPeer.get(p);
			((CyclonPeer)pe).age();
		}
	}
    
	private synchronized ArrayList<Peer> selectToSendGlobal(Peer p){
		Peer myinfo = new CyclonPeer();
		myinfo.initPeer(this.myself.getId(),this.myself.getIP().getHostName(),this.myself.getIP().getPort());
		ArrayList<Peer> res = new ArrayList<Peer>();
		res.add(myinfo);

		ArrayList<Peer> tmp = new ArrayList<Peer>();
		if(!this.myview.isEmpty()) {
			for (Integer pid : this.myview) {
				if (pid != p.getId()) {
					tmp.add((Peer) ((CyclonPeer) idToPeer.get(pid)).clone());
				}
			}
		}
		else{
			for (Peer pe : this.sentPeerData) {
				if (pe.getId() != p.getId()) {
					tmp.add((Peer) ((CyclonPeer) pe).clone());
				}
			}
		}
		Collections.shuffle(tmp,this.grandom);
		for (Peer pe : tmp){
			if(res.size() < Parameters.GOSSIPSIZE){
				res.add(pe);
				this.idToPeer.remove(pe.getId());
			}
		}
		this.myview = new HashSet<Integer>(idToPeer.keySet());
		return res;
	}
	
	private synchronized void insertSentToView(){
		while(this.myview.size() < Parameters.VIEWSIZE && this.sentPeerData.size() > 0){
			Peer tmp = this.sentPeerData.remove(0);
			this.idToPeer.put(tmp.getId(), tmp);
		}
		this.myview = new HashSet<Integer>(idToPeer.keySet());
	}
	
    @Override
    public void run() {
    	int cycles = 0;
    	while(cycles < Parameters.NUMCYCLES){
    		try {
    			Thread.sleep(Parameters.INTERVAL);
    			cycles++;
    			synchronized(this){
    				this.insertSentToView();
    				this.ageGlobal();
    				Peer target = this.getOlderGlobal();
    				this.myview.remove(target.getId());
    				this.idToPeer.remove(target.getId());
    				ArrayList<Peer> toglobal = this.selectToSendGlobal(target);
    				this.sentPeerData = new ArrayList<Peer>();
    				for (Peer p : toglobal){
    					this.sentPeerData.add(p);
    				}
    				this.sentPeerData.remove(this.myself);
					CyclonMessage msgglobal = new CyclonMessage(toglobal,CyclonMessage.TYPE.REQUEST,this.myself);
    				this.sendMsg(target, msgglobal);
    			}
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }	
}
