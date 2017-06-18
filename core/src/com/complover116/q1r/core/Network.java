package com.complover116.q1r.core;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class Network {
	public static final int IDLE_PING_DELAY = 500;
	public static final int IDLE_TIMEOUT = 2000;
	public static final int PORT = 26678;
	public static final int PACKET_LENGTH = 64;
	public static final ArrayList<NetPeer> peers = new ArrayList<NetPeer>();
	static InetAddress localAddress;
	static boolean readyToPlay = false;
	static int players = 0;
	
	static final int STATUS_OFFLINE = 0;
	static final int STATUS_STARTING = 1;
	static final int STATUS_STOPPING = 100;
	static final int STATUS_PINGING = 4;
	static final int STATUS_FINALIZING = 5;
	static final int STATUS_CHECKING_CONNECTION = 3;
	static final int ERROR_OWN_ADDR_UNKNOWN = -100;
	static final int ERROR_RECEIVING_THREAD = -50;
	static final int ERROR_SENDING_THREAD = -25;
	static volatile int status = STATUS_OFFLINE;
	static int getPlayerCount() {
		int result = 0;
		for(NetPeer peer : peers) {
			if(peer.status == NetPeer.STATUS_WILLINGTOPLAY || peer.status == NetPeer.STATUS_JOINING_GAME) result ++;
		}
		return result;
	}
	static int getJoiningPlayerCount() {
		int result = 0;
		for(NetPeer peer : peers) {
			if(peer.status == NetPeer.STATUS_JOINING_GAME) result ++;
		}
		return result;
	}
	static void finalizeGame() {
		//Forget the peers that are not playing with us
		for(int i=0; i < Network.peers.size(); i ++) {
			if(peers.get(i).status != NetPeer.STATUS_WILLINGTOPLAY)
				peers.remove(i);
		}
		status = STATUS_FINALIZING;
	}
	public static void start() {
		status = STATUS_STARTING;
		Gdx.app.log("Network", "Starting network services...");
		try {
			Socket tmpSock = new Socket("192.168.1.1", 80);
			localAddress = tmpSock.getLocalAddress();
			tmpSock.close();
			Gdx.app.log("Network", "Local address:"+localAddress.toString());
			NetReceivingThread.start();
			NetSendingThread.start();
		} catch (Exception e) {
			Gdx.app.error("Network", "Could not determine own address - P2P impossible");
			Network.status = ERROR_OWN_ADDR_UNKNOWN;
		}
		
		
	}
	public static void stop() {
		Gdx.app.log("Network", "Stopping network services...");
		Network.readyToPlay = false;
		NetReceivingThread.stop();
		NetSendingThread.stop();
		peers.clear();
		status = STATUS_STOPPING;
	}
	/***
	 * Restart the connection process
	 */
	static void reset() {
		readyToPlay = false;
		peers.clear();
		status = STATUS_PINGING;
	}
}
