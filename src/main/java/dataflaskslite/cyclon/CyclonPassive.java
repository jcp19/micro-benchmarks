package dataflaskslite.cyclon;

import dataflaskslite.api.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclonPassive extends Thread{
	
	private Cyclon cyc;
	private DatagramSocket ss;
	private ExecutorService exService;
	
	public CyclonPassive(Cyclon c, Peer myself){
		this.setName("CyclonPassive");
		this.cyc = c;
		this.exService = Executors.newFixedThreadPool(1);
		try {
			this.ss = new DatagramSocket(myself.getIP().getPort());//, myself.getIP().getAddress());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in DatagramSocket set up.");
			System.exit(1);
		}
	}
	
	private class Worker extends Thread{
		private byte[] p;
		private Cyclon cyc;
		
		public Worker(byte[] p, Cyclon c){
			this.setName("CyclonPassive-Worker");
			this.p = p;
			this.cyc = c;
		}
		public void run(){
			CyclonMessage msg;
			try {
				msg = new CyclonMessage(p);
				this.cyc.processMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in parsing message.");
			}
		}
	}
	
	@Override
	public void run() {
		//Waits for incoming packets and asks Worker to process them.
		while (true) {
			try {
				DatagramPacket packet = new DatagramPacket(new byte[65500],65500);
				this.ss.receive(packet);
				byte[] data = packet.getData();
				this.exService.submit(new Worker(data,this.cyc));
			} catch (SocketException e) {
				System.out.println("Cyclon Thread Server disconnected!");
			} catch (IOException e) {
				System.out.println("Cyclon Thread ERROR in run()!");
			}
		}	

	}
	
}
