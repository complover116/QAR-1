package com.complover116.q1r.core;

import java.net.DatagramSocket;
import java.net.SocketException;

import com.badlogic.gdx.Gdx;

public class NetReceivingThread implements Runnable {
	static NetReceivingThread instance;
	static volatile boolean isRunning = false;
	
	int port;
	
	@Override
	public void run() {
		Gdx.app.log("Network", "Receiving thread started");
		try {
			DatagramSocket receivingSocket = new DatagramSocket(port);
			while(isRunning) {
				
			}
		} catch (SocketException e) {
			Gdx.app.error("Network", "Error in receiving thread:"+e.getMessage());
			e.printStackTrace();
			//TODO:Error handling!
		}
		Gdx.app.log("Network", "Receiving thread exited");
	}
	private NetReceivingThread(int port){
		this.port = port;
	}
	public static void start() {
		isRunning = true;
		new Thread(new NetReceivingThread(Network.PORT), "Network receiving thread").start();
	}
}
