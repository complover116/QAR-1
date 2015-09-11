package com.complover116.q1r;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player {
	
	/***
	 * This is a bot.
	 */
	final static byte CONNECTION_BOT = -1;
	/***
	 * Player is playing on the same machine
	 */
	final static byte CONNECTION_LOCAL = 0;
	//TODO:Local control keys
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
		
		switch(cont) {
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
			if(true) {
				connectionType = CONNECTION_BOT;
			}
		}
		this.ent = entity;
		this.controlScheme = cont;
	}
	public void tick() {
		if(this.connectionType == CONNECTION_LOCAL) {
			if(Gdx.input.isKeyJustPressed(key_up)) {
				ent.jump = true;
			}
			if(Gdx.input.isKeyPressed(key_right)) {
				ent.moveDir=1;
			} else if(Gdx.input.isKeyPressed(key_left)) {
				ent.moveDir = -1;
			} else ent.moveDir = 0;
			if(Gdx.input.isKeyJustPressed(key_down)) {
				ent.fire = true;
			}
		} else if(this.connectionType == CONNECTION_BOT) {
			AI.tickFor(ent, 0.1);
		}
	}
}
