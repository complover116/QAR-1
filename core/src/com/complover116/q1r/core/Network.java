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
	public static volatile byte status = 0;
	static int getPlayerCount() {
		int result = 0;
		for(NetPeer peer : peers) {
			if(peer.readyToPlay) result ++;
		}
		return result;
	}
	public static void start() {
		status = 0;
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
			Network.status = -126;
		}
		
		
	}
	public static void stop() {
		Gdx.app.log("Network", "Stopping network services...");
		Network.readyToPlay = false;
		NetReceivingThread.stop();
		NetSendingThread.stop();
		peers.clear();
		status = 0;
	}
}
