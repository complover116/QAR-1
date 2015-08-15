package com.complover116.q1r;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GameWorld {
	/***
	 * Contains the current map data
	 */
	public static ArrayList<Platform> platforms = new ArrayList<Platform>();
	/***
	 * Contains references to every player entity to be ticked
	 */
	public static ArrayList<PlayerEnt> players = new ArrayList<PlayerEnt>();
	
	public static void init() {
		platforms.add(new Platform(new Rectangle(100, 300, 600, 50), 0));
		players.add(new PlayerEnt());
	}
	public static void render() {
		//RENDER PLATFORMS
		Q1R.shapeRenderer.begin(ShapeType.Line);
		Q1R.shapeRenderer.setColor(1, 1, 1, 1);
		for(int i = 0; i < platforms.size(); i++) {
			Platform plat = platforms.get(i);
			Q1R.shapeRenderer.rect(plat.bounds.getX(), plat.bounds.getY(), plat.bounds.getWidth(), plat.bounds.getHeight());
		}
		Q1R.shapeRenderer.end();
		
		//RENDER PLAYER ENTITIES
		Q1R.batch.begin();
		for(int i = 0; i < players.size(); i ++) {
			Q1R.batch.draw(Resources.getImage(players.get(i).getImage()), players.get(i).x, players.get(i).y);
		}
		Q1R.batch.end();
	}
	public static void update(double deltaT) {
		for(int i = 0; i < players.size(); i ++) {
			players.get(i).tickPhysics(deltaT);;
		}
	}
}
