package com.complover116.quar1;

import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JOptionPane;

public class Loader {
	public static boolean isServer = false;
	public static boolean initialized = false;
	public static boolean run = true;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//TESTER.JAR v2 stuff
		/*
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					MainInit.main(new String[]{});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//fuuuuuuuuuuu
				}
			}
			
		}).start();*/
		
		//Initia
		Map.init();
		CurGame.lvl.loadMap(Map.map2);
		GUI.init();
		System.out.println("...SOS...");
		String in = GUI.askString("Connection setup", "To join a game, type an ip address. To host one, type \"host\"");
		Render.loadStep = "Loading resources...";
		new Thread(new TickerThread()).start();
		ResourceContainer.load();
		//THIS IS TEMPORARY, WILL REPLACE WITH GUI STUFF
		while(!initialized) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		initialized = false;
		if(in.equalsIgnoreCase("host")) {
			GUI.askBots();
			Render.loadStep = "Hosting a game...";
			new Thread(new ServerThread()).start();
			isServer = true;
		} else {
			Render.loadStep = "Connecting to "+in+"...";
			try{
			Config.address = in;
			Config.server = InetAddress.getByName(Config.address);
			new Thread(new ClientThread()).start();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Could not connect to "+in+", maybe you mistyped?", "Connection error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		
		
	}

}
