package com.complover116.q1r.core;

import java.net.*;
import com.badlogic.gdx.Gdx;

class NetClient {

	static NetConnection server;
	private static DatagramSocket sock;
	private static InetAddress addr;
	private static int port = 26655;
	
	static volatile boolean clientRunning = false;
	static volatile boolean connected = false;
	
	public static void startClient() {
		if(clientRunning) {
			Gdx.app.log("Network", "Ignoring call to startClient() because a client is already running");
			return;
		}
		new Thread(new NetClientThread(), "Client Thread").start();
	}
	
	public static class NetClientThread implements Runnable {
		@Override
		public void run() {
			Gdx.app.log("Network", "Client initializing...");
			NetClient.clientRunning = true;
			try{
			addr = InetAddress.getByName("127.0.0.1");
			} catch (Exception e) {
				Gdx.app.log("Network" , "Could not resolve remote address");
				return;
			}
			try {
			sock = new DatagramSocket();
			sock.setSoTimeout(10);
			} catch (SocketException e) {
				e.printStackTrace();
				return;
			}
			
			byte[] buf = new byte[64];
			DatagramPacket dataIn = new DatagramPacket(buf, buf.length);
			Gdx.app.log("Network", "Client started! Chosen port:"+sock.getLocalPort());
			
			server = new NetConnection(addr, port, sock);			
			
			server.sendPacket(new NetPacket(false));
			
			while(NetClient.clientRunning) {
				try{
					sock.receive(dataIn);
					if(dataIn.getAddress().equals(addr) && dataIn.getPort() == port) {
						connected = true;
						server.timeSinceLastPacketReceived = 0;
						server.process(buf);
						//Then process the little chunks using a separate class
						
					} else {
						Gdx.app.log("Network", "Ignored packet from "+dataIn.getAddress().toString()+":"+dataIn.getPort()+" (Not the server)");
					}
					
				} catch (SocketTimeoutException e) {
					
				} catch (Exception e) {
					e.printStackTrace();
					NetClient.clientRunning = false;
				}
				server.update();
				if(server.dead) {
					NetClient.clientRunning = false;
				}
			}
			
			Gdx.app.log("Network", "Client shutting down...");
			
			sock.close();
			Gdx.app.log("Network", "Client is offline");
		}
	}
}
