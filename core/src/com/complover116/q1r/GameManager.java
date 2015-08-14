package com.complover116.q1r;

import com.badlogic.gdx.Gdx;

public class GameManager {
	public static void prepareLocal() {
		Gdx.app.log("GameManager", "Preparing local client...");
		GameWorld.init();
	}
}
