package com.complover116.q1r.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

class GameManager {
	public static volatile ArrayList<Player> players = new ArrayList<Player>();

	private final static byte FLAG_ISHOST = 1;
	private final static byte FLAG_LISTEN = 2;
	private final static byte FLAG_SENDKEYS = 4;
	private final static byte FLAG_DOLOCAL = 8;
	private final static byte FLAG_DOREMOTE = 16;

	private final static byte OFFLINE = FLAG_ISHOST + FLAG_DOLOCAL;


	static volatile boolean gameStarting = false;
	
	public static byte mode = OFFLINE;
	
	static boolean isHosting = false;
	
	static boolean isClient = false;

	static void prepareLocal() {
		players.clear();
		Gdx.app.log("GameManager", "Preparing local client...");
		Resources.Music_Offline.stop();
		Resources.Music_DM.play();
		//Resources.Music_DM.setPosition(85);
		Q1R.game.setScreen(Q1R.GS);
		GameScreen.menuShown = false;
		
		
		GameWorld.init();

		//TODO:Currently hardcoded to 1 player and 1 bot
		PlayerEnt ent = new PlayerEnt(1);
		GameWorld.players.add(ent);
		players.add(new Player(ent, 1, (byte)players.size()));
		PlayerEnt ent2 = new PlayerEnt(2);
		GameWorld.players.add(ent2);
		players.add(new Player(ent2, 5, (byte)players.size()));

		Gdx.app.log("GameManager", "Connecting controllers...");
		for (Controller controller : Controllers.getControllers()) {
		    Gdx.app.log("Controllers", controller.getName());
		    for(int i = 0; i < players.size(); i ++ )
			{
				if(players.get(i).connectionType == Player.CONNECTION_LOCAL) {
					if(players.get(i).controller == null) {
						Gdx.app.log("GameManager", "Controller "+controller.getName()+" mapped to player "+(i+1));
						players.get(i).controller = controller;
						break;
					}
				}
			}
		}
		
	}
}
