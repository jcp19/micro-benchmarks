package dataflaskslite.cyclon;

import dataflaskslite.api.Membership;
import dataflaskslite.api.Peer;
import dataflaskslite.common.Parameters;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CyclonPeer implements Peer {

	private int myid;
	private InetSocketAddress myip;
	private int age;
	private Membership membership;

	public CyclonPeer(){}

	public void initPeer(int id, String ip, int port){
		this.myid = id;
		this.myip = new InetSocketAddress(ip,port);
		this.age = 0;
	}

	public void initPeer(int id, String ip, int port, List<String> peers) {
		this.myid = id;
		this.myip = new InetSocketAddress(ip,port);
		this.age = 0;
		System.out.println("["+myid+"] Started ("+myid+", "+myip+")");
		Collections.shuffle(peers,new Random());


		HashMap<Integer,Peer> initialview = new HashMap<Integer,Peer>();
		for(int i = 0; initialview.size() < Parameters.VIEWSIZE && i < peers.size(); i++){
			String p = peers.get(i);
			String delims = "[ ]+";
			String[] tokens = p.split(delims);
			Integer tid = Integer.parseInt(tokens[0]);
			String tip = tokens[1];
			int tport = Integer.parseInt(tokens[2]);
			CyclonPeer tpeer = new CyclonPeer();
			tpeer.initPeer(tid,tip,tport);
			if(tpeer.getId()!=this.myid){
				initialview.put(tpeer.getId(), tpeer);
			}
		}
		System.out.print("["+myid+"] My initial view: ");
		for(Peer p : initialview.values()){
			System.out.print(p.getId()+" ");
		}
		System.out.print("\n");

		this.membership = new Cyclon(this,initialview);
	}


	//DECODE
	public void initPeerStream(DataInputStream dis) throws IOException{
		this.age = dis.readInt();
		String tip = dis.readUTF();
		int tport = dis.readInt();
		this.myid = dis.readInt();
		this.myip = new InetSocketAddress(tip,tport);
	}

	public int getId() {
		return myid;
	}

	public InetSocketAddress getIP() {
		return myip;
	}


	public void runPeer() {

		try {
			CyclonPassive listener = new CyclonPassive((Cyclon)membership,this);
			listener.start();
			((Cyclon)membership).start();

			//obtain/refresh the view every 5 seconds
			for(int i = 0; i < Parameters.NUMCYCLES; i++){
				List<Peer> view = membership.getView();
				System.out.print("=== ["+myid+"] Iteration "+i+": my view is ");
				for(Peer p : view) {
					System.out.print(p.getId()+" ");
				}
				System.out.print("\n");//*/
				Thread.sleep(Parameters.INTERVAL);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public int getAge(){
		return this.age;
	}

	public void age(){
		this.age = this.age + 1;
	}

	@Override
	public boolean equals(Object o){
		boolean res = false;
		if (this==o){
			res = true;
		}
		else{
			if(o instanceof CyclonPeer){
				CyclonPeer tmp = (CyclonPeer) o;
				res =  (tmp.getId() == this.getId());
			}
		}
		return res;
	}

	@Override
	public Object clone(){
		CyclonPeer tmp = new CyclonPeer();
		tmp.initPeer(this.myid,this.myip.getHostName(),this.myip.getPort());
		return tmp;
	}

	public void encode(DataOutputStream out){
		try {
			out.writeInt(this.age);
			out.writeUTF(this.myip.getHostName());
			out.writeInt(this.myip.getPort());
			out.writeInt(this.myid);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
