package com.complover116.q1r.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import com.badlogic.gdx.Gdx;

public class NetReceivingThread implements Runnable {
	private static NetReceivingThread instance;
	private static Thread threadInstance;
	private static volatile boolean isRunning = false;
	
	int port;
	
	@Override
	public void run() {
		Gdx.app.log("Network", "Receiving thread started");
		try {
			DatagramSocket receivingSocket = new DatagramSocket(port);
			receivingSocket.setSoTimeout(3000);
			Gdx.app.log("Network", "Receiving socket created");
			Network.status += 1;
			while(Network.status != 2) {
				Thread.sleep(100);
				if(Network.status < -10) {
					isRunning = false;
					break;
				}
			}
			while(isRunning) {
				try{
					byte in[] = new byte[Network.PACKET_LENGTH];
					DatagramPacket packet = new DatagramPacket(in, Network.PACKET_LENGTH);
					receivingSocket.receive(packet);
					if(Network.status == 2) {
						Network.status = 3;
					}
					boolean peerExists = false;
					for(int i=0; i < Network.peers.size(); i ++) {
						NetPeer peer = Network.peers.get(i);
						if(peer.address.equals(packet.getAddress())) {
							peer.timeLastHeard = System.nanoTime();
							peerExists = true;
							if(in[0] == 1) 
								peer.readyToPlay = true;
							else
								peer.readyToPlay = false;
						}
						if((System.nanoTime() - peer.timeLastHeard) > Network.IDLE_TIMEOUT*1000000) {
							Network.peers.remove(peer);
							Gdx.app.log("Network", "Peer "+peer.address.toString()+" timed out");
							continue;
						}
					}
					if(packet.getAddress().toString().equals(Network.localAddress.toString())) continue; //This is my own packet
					if(!peerExists) {
						NetPeer peer = new NetPeer(packet.getAddress());
						Network.peers.add(peer);
						Gdx.app.log("Network", "Found new peer "+peer.address.toString());
					}
				} catch (SocketTimeoutException e) {
					//This probably means that the network is dead - we should at least receive our own packets
				}
			}
			receivingSocket.close();
		} catch (IOException e) {
			Gdx.app.error("Network", "Error in receiving thread:"+e.getMessage());
			e.printStackTrace();
			Network.status = -50;
			//TODO:Error handling!
		} catch (InterruptedException e) {
			Gdx.app.error("Network", "Receiving thread force terminated: "+e.getMessage());
			//e.printStackTrace();
			Network.status = -50;
			//TODO:Error handling!
		}
		
		isRunning = false;
		Gdx.app.log("Network", "Receiving thread exited");
	}
	private NetReceivingThread(int port){
		this.port = port;
	}
	static void start() {
		if(isRunning) {
			Gdx.app.error("Network", "NetReceivingThread.start() was called, while the thread was already running!");
			return;
		}
		isRunning = true;
		instance = new NetReceivingThread(Network.PORT);
		threadInstance = new Thread(instance, "Network receiving thread");
		threadInstance.start();
	}
	static void stop(){
		isRunning = false;
		threadInstance.interrupt();
	}
}
