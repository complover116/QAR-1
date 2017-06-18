package com.complover116.q1r.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by complover116 on 25.05.2015 for QAR-1 Reloaded
 */
public class MainMenuScreen implements Screen {

	static volatile String loadStep = "Loading the image list...";
	static volatile byte loaded = 1;

	float UI_StartButtonX = 0;
	float UI_StartButtonY = 0;
	float UI_StartButtonSize = 0;
	float UI_StartButtonGoalX = 400;
	float UI_StartButtonGoalY = 300;
	float UI_StartButtonGoalSize = 150;
	float UI_StartButtonPingSpeed = 0; // s^-1
	float UI_StartButtonPing = 0; // 0 to 1 - progress
	Color UI_StartButtonColor = new Color(0.5f,0.5f,0.5f,1);
	
	BitmapFont UI_StatusFont;
	
	String UI_StatusText = "IDLE";
	
	boolean startButtonPressed = false;
	
	
	//The state machine!
	static final int STATE_OFFLINE_IDLE = 0; // Offline mode: network threads are halted
	static final int STATE_GOING_ONLINE = 1; // Network threads starting
	static final int STATE_ONLINE_PINGING = 2; // Listening for incoming pings and pinging
	static final int STATE_ONLINE_NOTALONE = 3; // Receiving pings from someone else
	
	static int state = STATE_OFFLINE_IDLE;
	
	public MainMenuScreen() {
		UI_StatusFont = new BitmapFont();
		UI_StatusFont.getData().setScale(2);
	}
	
	void startButtonPressed() {
		switch(state) {
			case STATE_OFFLINE_IDLE:
				break;
		}
	}
	void startButtonReleased() {
		
	}
	public void render(float deltaT) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Q1R.camera.update();
		Q1R.batch.setProjectionMatrix(Q1R.camera.combined);
		Q1R.shapeRenderer.setProjectionMatrix(Q1R.camera.combined);

		if (loaded == 1) {
			Resources.load();
			for (int i = 0; i < 100; i++) {
				Settings.ttimes[i] = 0;
			}
			Q1R.batch.begin();
			Q1R.batch.draw(Resources.textures.get("splashscreen"), 208, 175);
			Q1R.font.setColor(0,1,0,1);
			Q1R.font.getData().setScale(2);
			Q1R.font.draw(Q1R.batch, loadStep, 0, 60);
			Q1R.batch.end();
			// Settings.benchmark();
			// loaded = 2;
			return;
		}
		if(loaded == 3){
			Resources.Music_Offline.play();
			state = STATE_GOING_ONLINE;
			Network.start();
			loaded = 4;
			return;
		}
		if (loaded == -1) {
			Q1R.batch.begin();
			Q1R.batch.draw(Resources.textures.get("ERROR"), 208+(float)(Math.random()*2), 175+(float)(Math.random()*2));
			Q1R.batch.end();
			return;
		}
		switch (state) {
			case STATE_OFFLINE_IDLE:
				UI_StartButtonColor = Color.RED;
				UI_StartButtonPingSpeed = 0;
				UI_StatusText = "Offline: make sure you are connected to wifi! Tap to try again.";
				break;
			case STATE_GOING_ONLINE:
				UI_StartButtonColor = Color.YELLOW;
				UI_StartButtonPingSpeed = 0.25f;
				UI_StatusText = "Going online...";
				break;
			case STATE_ONLINE_PINGING:
				UI_StartButtonColor = Color.YELLOW;
				UI_StartButtonPingSpeed = 0.5f;
				UI_StatusText = "Looking for other players on the network...";
				break;
			case STATE_ONLINE_NOTALONE:
				UI_StartButtonColor = Color.GREEN;
				UI_StartButtonPingSpeed = 1f;
				UI_StatusText = "Quickplay ready: "+Network.peers.size()+" players found, hold the button to start!";
				break;
		}
		if(state == STATE_GOING_ONLINE && Network.status>=3) {
			state = STATE_ONLINE_PINGING;
		}
		if((state == STATE_GOING_ONLINE || state == STATE_ONLINE_PINGING || state == STATE_ONLINE_NOTALONE)&&Network.status<0) {
			//A network error has occurred
			state = STATE_OFFLINE_IDLE;
		}
		if(state == STATE_ONLINE_PINGING && Network.peers.size()>0) {
			state = STATE_ONLINE_NOTALONE;
		}
		if(state == STATE_ONLINE_NOTALONE && Network.peers.size() == 0) {
			state = STATE_ONLINE_PINGING;
		}
		if(!startButtonPressed && (Gdx.input.isKeyPressed(Input.Keys.SPACE)||Gdx.input.isTouched())) {
			startButtonPressed = true;
			startButtonPressed();
		}
		if(startButtonPressed && !(Gdx.input.isKeyPressed(Input.Keys.SPACE)||Gdx.input.isTouched())) {
			startButtonPressed = false;
			startButtonReleased();
		}
		
		//Start button animation
		UI_StartButtonX += (UI_StartButtonGoalX - UI_StartButtonX)/10;
		UI_StartButtonY += (UI_StartButtonGoalY - UI_StartButtonY)/10;
		UI_StartButtonSize += (UI_StartButtonGoalSize - UI_StartButtonSize)/10;
		if(UI_StartButtonPingSpeed>0) {
			UI_StartButtonPing += UI_StartButtonPingSpeed*deltaT;
		} else {
			UI_StartButtonPing += (0.75f - UI_StartButtonPing)/10;
		}
		if(UI_StartButtonPing>1) {
			UI_StartButtonPing -= 1;
			if(state == STATE_ONLINE_NOTALONE)
				Resources.playSound("ui/ping_notalone");
			else
				Resources.playSound("ui/ping_searching");
		}
		float startButtonRingMul = UI_StartButtonPing > 0.75? 1.1875f-(float)Math.pow(UI_StartButtonPing-0.75f, 2)*3 : 1+UI_StartButtonPing*0.25f;
		float startButtonSizeMul = UI_StartButtonPing < 0.5 ? 1 - UI_StartButtonPing + UI_StartButtonPing*UI_StartButtonPing*2 : 1;
		Gdx.gl.glEnable(GL20.GL_BLEND);

		Q1R.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		Q1R.shapeRenderer.setColor(UI_StartButtonColor);
		Q1R.shapeRenderer.circle(UI_StartButtonX, UI_StartButtonY, UI_StartButtonSize*startButtonSizeMul);
		Q1R.shapeRenderer.setColor(UI_StartButtonColor.r, UI_StartButtonColor.g, UI_StartButtonColor.b, UI_StartButtonColor.a*0.5f);
		Q1R.shapeRenderer.circle(UI_StartButtonX, UI_StartButtonY, UI_StartButtonSize*startButtonRingMul);
		Q1R.shapeRenderer.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Q1R.batch.begin();
		UI_StatusFont.setColor(UI_StartButtonColor);
		UI_StatusFont.draw(Q1R.batch, UI_StatusText, 20, 80);
		Q1R.batch.end();
	}

	public void dispose() {

	}

	public void show() {

	}

	public void hide() {

	}

	public void pause() {
		Network.stop();
		state = STATE_OFFLINE_IDLE;
	}

	public void resume() {
		state = STATE_GOING_ONLINE;
		Network.start();
	}

	public void resize(int width, int height) {
		Q1R.viewport.update(width, height);
	}
}
