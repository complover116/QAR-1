package com.complover116.q1r;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class GameManager {
	public static volatile ArrayList<Player> players = new ArrayList<Player>();
	
	final static byte FLAG_ISHOST = 1;
	final static byte FLAG_LISTEN = 2;
	final static byte FLAG_SENDKEYS = 4;
	final static byte FLAG_DOLOCAL = 8;
	final static byte FLAG_DOREMOTE = 16;
	
	final static byte OFFLINE = FLAG_ISHOST+FLAG_DOLOCAL;
	
	public static byte mode = OFFLINE;
	
	
	
	public static void prepareLocal() {
		players.clear();
		Gdx.app.log("GameManager", "Preparing local client...");
		GameWorld.init();
		players.add(new Player(GameWorld.players.get(0),1));
		players.add(new Player(GameWorld.players.get(1),2));
	}
}
