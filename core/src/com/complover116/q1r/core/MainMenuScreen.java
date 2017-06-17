package com.complover116.q1r.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by complover116 on 25.05.2015 for QAR-1 Reloaded
 */
public class MainMenuScreen implements Screen {

	static volatile String loadStep;
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
	
	boolean startButtonPressed = false;
	
	
	//The state machine!
	static final int STATE_OFFLINE_IDLE = 0; // Offline mode: does not look for others at all
	static final int STATE_ONLINE_PINGING = 1; // Listening for incoming pings and pinging
	static final int STATE_ONLINE_NOTALONE = 2;
	
	static int state = STATE_OFFLINE_IDLE;
	
	public MainMenuScreen() {

	}
	
	void startButtonPressed() {
		switch(state) {
			case STATE_OFFLINE_IDLE:
				state = STATE_ONLINE_PINGING;
				UI_StartButtonColor = Color.RED;
				UI_StartButtonPingSpeed = 0.5f;
				break;
			case STATE_ONLINE_PINGING:
				state = STATE_OFFLINE_IDLE;
				UI_StartButtonColor = Color.GRAY;
				UI_StartButtonPingSpeed = 0;
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

		if (loaded == 2) {
			Resources.load();
			for (int i = 0; i < 100; i++) {
				Settings.ttimes[i] = 0;
			}
			if(loaded == 0)
			Resources.Music_Offline.play();
			// Settings.benchmark();
			// loaded = 2;
			return;
		}
		if (loaded == 1) {
			Q1R.batch.begin();
			Q1R.batch.draw(Resources.textures.get("splashscreen"), 208, 175);
			Q1R.batch.end();
			loaded = 2;
			return;
		}
		if (loaded == -1) {
			Q1R.batch.begin();
			Q1R.batch.draw(Resources.textures.get("ERROR"), 208+(float)(Math.random()*2), 175+(float)(Math.random()*2));
			Q1R.batch.end();
			return;
		}
		
		if(!startButtonPressed && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			startButtonPressed = true;
			startButtonPressed();
		}
		if(startButtonPressed && !Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
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
		if(UI_StartButtonPing>1) UI_StartButtonPing -= 1;
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
	}

	public void dispose() {

	}

	public void show() {

	}

	public void hide() {

	}

	public void pause() {

	}

	public void resume() {

	}

	public void resize(int width, int height) {
		Q1R.viewport.update(width, height);
	}
}
