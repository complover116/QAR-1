package com.complover116.q1r.core;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;

class Player {
	int score = 0;
	int streak = 0;
	
	Controller controller;
	
	/***
	 * This is a bot.
	 */
	private final static byte CONNECTION_BOT = -1;
	/***
	 * Player is playing on the same machine
	 */
	final static byte CONNECTION_LOCAL = 0;
	// TODO:Local control keys
	private int key_up;
	private int key_left;
	private int key_right;
	private int key_down;
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

	byte connectionType;

	PlayerEnt ent;

	Player(PlayerEnt entity, int cont, byte ID) {
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
			connectionType = CONNECTION_BOT;
		}
		this.ent = entity;
	}
	void tick() {
	if(!GameManager.isClient)
		if (this.connectionType == CONNECTION_LOCAL) {
			if (Gdx.app.getType() == ApplicationType.Android) {
				if (Gdx.input.getPitch()<-Settings.tiltSensitivity) {
					ent.moveDir = 1;
				} else if (Gdx.input.getPitch()>Settings.tiltSensitivity) {
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
				//Hardcoded for xbox 360 controllers
				if(controller != null){
				if(controller.getButton(0)) {
					ent.jump = true;
				}
				if(controller.getButton(2)) {
					ent.fire = true;
				}
				if(controller.getAxis(1)>0.5) {
					ent.moveDir = 1;
				} else if(controller.getAxis(1)<-0.5){
					ent.moveDir = -1;
				} else {
					ent.moveDir = 0;
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
			}
		} else if (this.connectionType == CONNECTION_BOT) {
			AI.tickFor(ent);
		}
	}
}
