package com.complover116.quar1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;


public class ClientThread implements Runnable {
	public class TimeoutThread implements Runnable {

		@Override
		public void run() {
			System.out.println("Client Timeout Thread has started...");
			while(Loader.run) {
				timeout ++;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(timeout > ClientThread.timeoutHigh) {
					SoundHandler.playSound("/sound/effects/error1.wav");
					JOptionPane.showMessageDialog(null, "No message received for "+ClientThread.timeoutHigh/10+" seconds.\nConnection is considered terminated.", "Disconnected", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
			System.out.println("Client Timeout Thread has stopped...");
		}
		
	}
	public static int timeout = 0;
	public static final int timeoutLow = 10;
	public static final int timeoutHigh = 50;
	static DatagramSocket socket;
	public ClientThread() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		byte out[] = new byte[256];
		out[0] = 124;
		try {
			DatagramPacket outgoing = new DatagramPacket(out, out.length, Config.server, 1141);
			socket.send(outgoing);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Unknown network error", "Network error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		Loader.initialized = true;
		new Thread(new TimeoutThread()).start();
		while(Loader.run) {
			byte in[] = new byte[256];
			DatagramPacket incoming = new DatagramPacket(in, in.length);
			try {
				socket.receive(incoming);
				timeout = 0;
				switch(in[0]) {
				case 1:
					ByteBuffer data = ByteBuffer.wrap(in, 2, 254);
					CurGame.lvl.players.get(in[1]).update(data);
				break;
				case 2:
					ByteBuffer data2 = ByteBuffer.wrap(in, 1, 255);
					//for(int i = 0; i < 256; i ++) System.out.print(in[i]+":");
					CurGame.lvl.TADs.add(new Projectile(data2));
				break;
				case 3:
					switch(in[1]) {
					case 1:
						CurGame.lvl.loadMap(Map.map1);
					break;
					case 2:
						CurGame.lvl.loadMap(Map.map2);
					break;
					}
				break;
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(Config.netTick);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void sendKey(int key, boolean state) {
		byte out[] = new byte[256];
		if(state) {
			out[0] = 100;
		} else {
			out[0] = 101;
		}
		ByteBuffer.wrap(out, 1, 255).putInt(key);
			DatagramPacket outgoing = new DatagramPacket(out, out.length,
					Config.server, 1141);
			try {
				socket.send(outgoing);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Unknown network error", "Network error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
	}
}
