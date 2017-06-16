package com.complover116.q1r.core;

import java.net.*;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

/***					NETSERVER
* The netserver contains the data that a server must know to function
* The data there is primarily used by the server thread
* Each cycle the server does the following:
*
* 1) Check for incoming packets
*	a) If there is a packet from a known source the time...LastReceived becomes 0
*	   Acks are read and the data gets split into chunks and processed
*	b) If there is a packet from an unknown source
*		- If it's a connection request - create a connection and reply
*		- If not - tell the client that he is not connnected (He probably timed out)
*	c) If there are no packets for 10 ms, skip this step
* 2) Tick the connections
* 3) Sort through outbound data, split it into packets and send each packet to a connection
***/
class NetServer {
	private static DatagramSocket sock;
	
	static ArrayList<NetConnection> clients = new ArrayList<NetConnection>();
	
	static volatile boolean serverRunning = false;
	
	public static void startServer() {
		if(serverRunning) {
			Gdx.app.log("Network", "Ignoring call to startServer() because a server is already running");
			return;
		}
		new Thread(new NetServerThread(), "Server Thread").start();
	}
	public static void broadcast(NetDataChunk chunk) {
		for(NetConnection client : clients)
			client.queueChunk(chunk);
	}
	public static void broadcastImportant(NetDataChunk chunk) {
		for(NetConnection client : clients)
			client.queueImportantChunk(chunk);
	}
	public static class NetServerThread implements Runnable {
		@Override
		public void run() {
			Gdx.app.log("Network", "Server initializing...");
			NetServer.serverRunning = true;
			try {
			sock = new DatagramSocket(26655);
			sock.setSoTimeout(10);
			} catch (SocketException e) {
				e.printStackTrace();
				return;
			}
			
			byte[] buf = new byte[64];
			DatagramPacket dataIn = new DatagramPacket(buf, buf.length);
			Gdx.app.log("Network", "Server started!");
			while(NetServer.serverRunning) {
				try{
					sock.receive(dataIn);
					boolean isFromClient = false;
                    for (NetConnection client : clients) {
                        if (client.addr.equals(dataIn.getAddress()) && client.port == dataIn.getPort()) {
                            client.timeSinceLastPacketReceived = 0;
                            isFromClient = true;
                            client.process(buf);
                            //Then process the little chunks using a separate class

                            break;
                        }
                    }
					
					if(!isFromClient) {
						clients.add(new NetConnection(dataIn.getAddress(), dataIn.getPort(), sock));
						Gdx.app.log("Network", "Received a packet from unknown machine, adding "+dataIn.getAddress().toString()+":"+dataIn.getPort()+" to the list of clients");
					}
					
					
					
				} catch (SocketTimeoutException e) {
					
				} catch (Exception e) {
					e.printStackTrace();
					NetServer.serverRunning = false;
				}
				HostNetworking.tick();
				for(int i = 0; i < clients.size(); i++) {
					clients.get(i).update();
					if(clients.get(i).dead) {
						Gdx.app.log("Network", "Disconnecting client "+clients.get(i).addr.toString()+":"+clients.get(i).port);
						clients.remove(i);
					}
				}
			}
			Gdx.app.log("Network", "Server shutting down...");
			
			sock.close();
			Gdx.app.log("Network", "Server is offline");
		}
	}
}
