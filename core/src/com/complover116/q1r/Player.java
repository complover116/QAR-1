package com.complover116.q1r;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Player {
	int score = 0;
	int streak = 0;
	int trickshots = 0;

	/***
	 * This is a bot.
	 */
	final static byte CONNECTION_BOT = -1;
	/***
	 * Player is playing on the same machine
	 */
	final static byte CONNECTION_LOCAL = 0;
	// TODO:Local control keys
	int key_up;
	int key_left;
	int key_right;
	int key_down;

	int controlScheme = 1;
	/***
	 * We have direct connection to the player machine, and we are not the host
	 */
	final static byte CONNECTION_MESH = 1;
	/***
	 * We are the host, player is connected to us as a client
	 */
	final static byte CONNECTION_CLIENT = 2;
	/***
	 * We know this player exists, but we are only connected through the host
	 */
	final static byte CONNECTION_INDIRECT = 3;

	public byte connectionType;

	PlayerEnt ent;

	public Player(PlayerEnt entity, int cont) {
		connectionType = CONNECTION_LOCAL;
		entity.ply = this;
		switch (cont) {
		case 1:
			this.key_up = Input.Keys.W;
			this.key_down = Input.Keys.S;
			this.key_left = Input.Keys.A;
			this.key_right = Input.Keys.D;
			break;
		case 2:
			this.key_up = Input.Keys.UP;
			this.key_down = Input.Keys.DOWN;
			this.key_left = Input.Keys.LEFT;
			this.key_right = Input.Keys.RIGHT;
			break;
		case 5:
			if (true) {
				connectionType = CONNECTION_BOT;
			}
		}
		this.ent = entity;
		this.controlScheme = cont;
	}

	public void draw() {
		Q1R.shapeRenderer.setColor(PlayerEnt.colorFromID(ent.color));
		int X = 0;
		int Y = 0;

		switch (ent.color) {
		case 1:
			X = 0;
			Y = 600;
			break;
		case 2:
			X = 0;
			Y = 80;
			break;
		case 3:
			X = 780;
			Y = 600;
			break;
		case 4:
			X = 780;
			Y = 80;
			break;
		}
		Q1R.shapeRenderer.begin(ShapeType.Line);
		for (int i = 0; i < 4; i++) {
			Q1R.shapeRenderer.rect(X, Y - 20 - i * 20, 20, 20);
		}
		Q1R.shapeRenderer.end();

		Q1R.shapeRenderer.begin(ShapeType.Filled);
		//Color col = PlayerEnt.colorFromID(ent.color);
		for (int i = 0; i < 4; i++) {
			if (ent.health > i)
				Q1R.shapeRenderer.rect(X, Y - 20 - i * 20, 20, 20);
		}
		Q1R.shapeRenderer.end();
		// Q1R.batch.begin();
		// Q1R.font.draw(Q1R.batch, "Score:"+score, X, Y-100);
		// Q1R.batch.end();
	}

	public void tick() {
		if (this.connectionType == CONNECTION_LOCAL) {
			if (Gdx.app.getType() == ApplicationType.Android) {
				if (Gdx.input.getPitch()<-4) {
					ent.moveDir = 1;
				} else if (Gdx.input.getPitch()>4) {
					ent.moveDir = -1;
				} else {
					ent.moveDir = 0;
				}
				if (GameScreen.buttons[0].isPressed) {
					ent.jump = true;
				}
				if (GameScreen.buttons[1].isPressed) {
					ent.fire = true;
				}
			} else {
				if (Gdx.input.isKeyJustPressed(key_up)) {
					ent.jump = true;
				}
				if (Gdx.input.isKeyPressed(key_right)) {
					ent.moveDir = 1;
				} else if (Gdx.input.isKeyPressed(key_left)) {
					ent.moveDir = -1;
				} else
					ent.moveDir = 0;
				if (Gdx.input.isKeyJustPressed(key_down)) {
					ent.fire = true;
				}
			}
		} else if (this.connectionType == CONNECTION_BOT) {
			AI.tickFor(ent, 0.1);
		}
	}
}
