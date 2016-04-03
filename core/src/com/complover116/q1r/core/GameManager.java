package com.complover116.q1r.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class GameManager {
	public static volatile ArrayList<Player> players = new ArrayList<Player>();

	final static byte FLAG_ISHOST = 1;
	final static byte FLAG_LISTEN = 2;
	final static byte FLAG_SENDKEYS = 4;
	final static byte FLAG_DOLOCAL = 8;
	final static byte FLAG_DOREMOTE = 16;

	final static byte OFFLINE = FLAG_ISHOST + FLAG_DOLOCAL;

	public static byte mode = OFFLINE;

	public static void prepareLocal(GameParams pars) {
		players.clear();
		Gdx.app.log("GameManager", "Preparing local client...");
		GameWorld.init();
		
		for(int i = 0; i < 4; i ++) {
			switch(pars.players[i]) {
				case LobbyScreen.PLAYER_LOCAL:
					PlayerEnt ent = new PlayerEnt(i+1);
					GameWorld.players.add(ent);
					players.add(new Player(ent, i+1));
				break;
				case LobbyScreen.PLAYER_BOT:
					PlayerEnt ent2 = new PlayerEnt(i+1);
					GameWorld.players.add(ent2);
					players.add(new Player(ent2, 5));
				break;
			}
		} 
	}
}
