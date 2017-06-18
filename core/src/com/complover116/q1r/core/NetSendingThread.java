package com.complover116.q1r.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;

public class NetSendingThread implements Runnable {
	private static NetSendingThread instance;
	private static Thread threadInstance;
	private static volatile boolean isRunning = false;
	
	@Override
	public void run() {
		Gdx.app.log("Network", "Sending thread started");
		try {
			DatagramSocket sendingSocket = new DatagramSocket();
			//Network.localAddress = sendingSocket.getLocalAddress();
			Gdx.app.log("Network", "Sending socket created");
			Network.status += 1;
			while(isRunning) {
				byte out[] = new byte[Network.PACKET_LENGTH];
				out[0] = (byte) (Network.readyToPlay ? 1 : 0);
				sendingSocket.send(new DatagramPacket(out, Network.PACKET_LENGTH,
						InetAddress.getByName("255.255.255.255"), Network.PORT));
				Thread.sleep(1000);
			}
			sendingSocket.close();
		} catch (IOException e) {
			Gdx.app.error("Network", "Error in sending thread:"+e.getMessage());
			e.printStackTrace();
			Network.status = -100;
			//TODO:Error handling!
		} catch (InterruptedException e2) {
			Gdx.app.error("Network", "Sending thread force terminated:"+e2.getMessage());
			//e2.printStackTrace();
			Network.status = -100;
		}
		Gdx.app.log("Network", "Sending thread exited");
	}
	private NetSendingThread(){}
	static void start() {
		if(isRunning) {
			Gdx.app.error("Network", "NetReceivingThread.start() was called, while the thread was already running!");
			return;
		}
		isRunning = true;
		instance = new NetSendingThread();
		threadInstance = new Thread(instance, "Network sending thread");
		threadInstance.start();
	}
	static void stop() {
		isRunning = false;
		threadInstance.interrupt();
	}
}