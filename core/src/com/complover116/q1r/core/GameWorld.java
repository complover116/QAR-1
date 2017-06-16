package com.complover116.q1r.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;

class GameWorld {

	
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
	
	private static int leadingScore = 0;
	private static int secondScore = 0;
	
	public static void init() {
		platforms.clear();
		players.clear();
		ents.clear();
		leadingScore = 0;
		secondScore = 0;
		
		platforms.add(new Platform(new Rectangle(0, 0, 10, 600), 0));
		platforms.add(new Platform(new Rectangle(790, 0, 10, 600), 0));
		platforms.add(new Platform(new Rectangle(0, 0, 800, 10), 0));

		platforms.add(new Platform(new Rectangle(100, 100, 100, 10), 0));
		platforms.add(new Platform(new Rectangle(10, 200, 100, 10), 0));

		platforms.add(new Platform(new Rectangle(100, 300, 600, 10), 0));
		platforms.add(new Platform(new Rectangle(400, 310, 10, 100), 0));
		

		// Debug entities go there
		// ents.add(new Projectile(100,200,200,100));
	}

	public static void render() {

		// RENDER PLATFORMS
		Q1R.shapeRenderer.begin(ShapeType.Line);
		
		//RENDER DEBUG BUTTON OUTLINE
		/*Q1R.shapeRenderer.setColor(0, 1, 1, 1); for(int i = 0; i <
		buttons.length; i ++) { if(buttons[i].isPressed)
		Q1R.shapeRenderer.setColor(1, 0, 0, 1); else
		Q1R.shapeRenderer.setColor(0, 1, 1, 0.5f);
		Q1R.shapeRenderer.rect(buttons[i].bounds.x, buttons[i].bounds.y,
		buttons[i].bounds.width, buttons[i].bounds.height); }
		*/
		Q1R.shapeRenderer.setColor(1, 1, 1, 1);
        for (Platform plat : platforms) {
            Q1R.shapeRenderer.rect(plat.bounds.getX(), plat.bounds.getY(), plat.bounds.getWidth(),
                    plat.bounds.getHeight());
        }
		Q1R.shapeRenderer.end();

		// RENDER PLAYER ENTITIES
		Q1R.batch.begin();
        for (PlayerEnt player1 : players) {
            Q1R.batch.draw(Resources.getImage(player1.getImage()), player1.x, player1.y);

			
			/*
			 * Q1R.font.setColor(PlayerEnt.colorFromID(players.get(i).color));
			 * Q1R.font.draw(Q1R.batch, ""+players.get(i).ply.score,
			 * players.get(i).x+10, players.get(i).y+40);
			 */
        }

		Q1R.batch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Q1R.shapeRenderer.begin(ShapeType.Filled);
        for (PlayerEnt player : players) {

            if (player.time > 1000) {
                float size = ((player.time - 1000) * 100);
                float x = player.x + 16;
                float y = player.y + 16;
                for (int i = 0; i < 10; i += 1) {
                    Color col = PlayerEnt.colorFromID(player.color);
                    Q1R.shapeRenderer.setColor(col.r, col.g, col.b, (float) 0.1);
                    Q1R.shapeRenderer.ellipse(x - size / 2 - size / 10 * i, y - size / 2 - size / 10 * i, size + size / 5 * i,
                            size + size / 5 * i);
                }
            }

            if (player.ply.score > secondScore && player.spawned) {
                float size = (float) Math.random() * 10 + 2 * (player.ply.score - secondScore < 12 ? player.ply.score - secondScore : 12);
                float x = player.x + 16;
                float y = player.y + 16;
                for (int i = 0; i < 10; i += 1) {
                    Color col = PlayerEnt.colorFromID(player.color);
                    Q1R.shapeRenderer.setColor(col.r, col.g, col.b, (float) 0.1);
                    Q1R.shapeRenderer.rect(x - size / 2 - size / 10 * i, y - size / 2 - size / 10 * i, size + size / 5 * i,
                            size + size / 5 * i);
                }
            }
        }
		// RENDER ENTITIES
        for (Entity ent : ents) ent.draw();

		Q1R.shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

	}

	public static void update(double deltaT) {
		
		// Check if restarting (DEPRECATED, now restart using the menu)
		/*if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSLASH) || buttons[4].isPressed) {
			GameManager.prepareLocal();
		}*/
		
			for(int i = 0; i < GameManager.players.size(); i ++) {
				if(GameManager.players.get(i).ent.isDead) {
					PlayerEnt ent = new PlayerEnt(i+1);
					GameWorld.players.add(ent);
					GameManager.players.get(i).ent = ent;
					ent.ply = GameManager.players.get(i);
				}
				if(GameManager.players.get(i).score > leadingScore) {
					leadingScore = GameManager.players.get(i).score;
				}
				if(GameManager.players.get(i).score > secondScore && GameManager.players.get(i).score < leadingScore) {
					secondScore = GameManager.players.get(i).score;
				}
			}
		
		for (int i = players.size() - 1; i > -1; i--) {
			if (players.get(i).isDead)
				players.remove(i);
			else
				players.get(i).tickPhysics(deltaT);
		}
		for (int i = ents.size() - 1; i > -1; i--) {
			if (ents.get(i).isDead)
				ents.remove(i);
			else
				ents.get(i).tick(deltaT);
		}
	}
}
