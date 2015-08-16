package com.complover116.q1r;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
	
	/***
	 * Contains reference to any non-player entity to be ticked and drawn
	 */
	public static ArrayList<Entity> ents = new ArrayList<Entity>();
	public static void init() {
		platforms.add(new Platform(new Rectangle(100, 300, 600, 10), 0));
		platforms.add(new Platform(new Rectangle(400, 310, 10, 100), 0));
		players.add(new PlayerEnt(1));
		players.add(new PlayerEnt(2));
		
		
		//Debug entities go there
		//ents.add(new Projectile(100,200,200,100));
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
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Q1R.shapeRenderer.begin(ShapeType.Filled);
		//RENDER ENTITIES
		for(int i = 0; i < ents.size(); i ++)
			ents.get(i).draw();
		
		Q1R.shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
	}
	public static void update(double deltaT) {
		for(int i = 0; i < players.size(); i ++) {
			players.get(i).tickPhysics(deltaT);;
		}
		for(int i = ents.size()-1; i > -1; i --) {
			if(ents.get(i).isDead)
				ents.remove(i);
			else
				ents.get(i).tick(deltaT);;
		}
	}
}
