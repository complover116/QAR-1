package com.complover116.q1r.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class GameManager {
	public static volatile ArrayList<Player> players = new ArrayList<Player>();

	final static byte FLAG_ISHOST = 1;
	final static byte FLAG_LISTEN = 2;
	final static byte FLAG_SENDKEYS = 4;
	final static byte FLAG_DOLOCAL = 8;
	final static byte FLAG_DOREMOTE = 16;

	final static byte OFFLINE = FLAG_ISHOST + FLAG_DOLOCAL;


	public static volatile boolean gameStarting = false;
	
	public static byte mode = OFFLINE;
	
	public static boolean isHosting = false;
	
	public static boolean isClient = false;

	public static void prepareLocal() {
		players.clear();
		Gdx.app.log("GameManager", "Preparing local client...");
		Resources.Music_Offline.stop();
		Resources.Music_DM.play();
		//Resources.Music_DM.setPosition(85);
		Q1R.game.setScreen(Q1R.GS);
		GameScreen.menuShown = false;
		
		
		GameWorld.init();
		
		for(int i = 0; i < 4; i ++) {
			switch(GameParams.players[i]) {
				case LobbyScreen.PLAYER_LOCAL:
					PlayerEnt ent = new PlayerEnt(i+1);
					GameWorld.players.add(ent);
					players.add(new Player(ent, i+1, (byte)players.size()));
				break;
				case LobbyScreen.PLAYER_BOT:
					PlayerEnt ent2 = new PlayerEnt(i+1);
					GameWorld.players.add(ent2);
					if(GameManager.isClient)
					players.add(new Player(ent2, 0, (byte)players.size()));
					else
					players.add(new Player(ent2, 5, (byte)players.size()));
				break;
			}
		} 
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
