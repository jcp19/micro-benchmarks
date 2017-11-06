package dataflaskslite.cyclon;

import dataflaskslite.api.Peer;

import java.io.*;
import java.util.ArrayList;


public class CyclonMessage {

	public ArrayList<Peer> list;
	public int type;
	public CyclonPeer sender;
	
	public class TYPE {
		public static final int REQUEST = 0;
		public static final int RESPONSE = 2;	
	}
	
	
	public CyclonMessage(ArrayList<Peer> p,int t, Peer sender){
		this.type = t;
		this.sender = (CyclonPeer)sender;
		this.list = new ArrayList<Peer>();
		for(Peer tp : p){
			this.list.add((Peer)tp);
		}
	}
	
	public CyclonMessage(byte[] packet){
		try{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet));
		this.list = new ArrayList<Peer>();
		this.type = dis.readInt();
		this.sender = new CyclonPeer();
		this.sender.initPeerStream(dis);
		int listSize = dis.readInt();
		for(int i=0;i<listSize;i++){
			String tip = dis.readUTF();
			int tport = dis.readInt();
			int tid = dis.readInt();
			Peer np = new CyclonPeer();
			np.initPeer(tid,tip,tport);
			this.list.add(np);
		}
		dis.close();
		}
		catch(Exception e){
			System.out.println("ERROR in CyclonMessage Constructor.");
		}

	}
	

	
	public byte[] encodeMessage(){
		byte[] res = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baos);
			out.writeInt(this.type);
			this.sender.encode(out);
			out.writeInt(this.list.size());
			for(Peer p : this.list){
				out.writeUTF(p.getIP().getHostName());
				out.writeInt(p.getIP().getPort());
				out.writeInt(p.getId());
			}
			out.flush();
			res = baos.toByteArray();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
}
