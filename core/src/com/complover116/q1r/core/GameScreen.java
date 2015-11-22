package com.complover116.q1r.core;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen implements Screen {

	int WIDTH = 0;
	int HEIGHT = 0;
	
	double time = 0;
	
	static boolean menuShown = false;
	
	public static AndroidButton buttons[] = new AndroidButton[3];
	
	public GameScreen() {
		
		
		// LEFT
		buttons[0] = new AndroidButton(new Rectangle(0, 0, 128, 128));
		// RIGHT
		buttons[1] = new AndroidButton(new Rectangle(672, 0, 128, 128));
		// MENU
		buttons[2] = new AndroidButton(new Rectangle(32, 536, 64, 64));
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {

		for (int i = 0; i < GameManager.players.size(); i++)
			GameManager.players.get(i).tick();
		if(!menuShown){
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].update();
			}
			GameWorld.update(delta);
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Q1R.camera.update();

		Gdx.gl.glViewport(0, 0, WIDTH, HEIGHT);

		Q1R.shapeRenderer.getProjectionMatrix().idt().setToOrtho2D(0, 0, WIDTH, HEIGHT);
		Q1R.shapeRenderer.getTransformMatrix().idt();
		Q1R.batch.getProjectionMatrix().idt().setToOrtho2D(0, 0, WIDTH, HEIGHT);
		Q1R.batch.getTransformMatrix().idt();
		// PLAYER INFO RENDER CODE
		for (int i = 0; i < GameManager.players.size(); i++)
			GameManager.players.get(i).draw();
		
		
		Q1R.viewport.update(WIDTH, HEIGHT);
		Q1R.shapeRenderer.setProjectionMatrix(Q1R.camera.combined);
		Q1R.batch.setProjectionMatrix(Q1R.camera.combined);
		//TICK TIMES CALCULATION
		double spaaps = 0;
		for (int i = 0; i < 99; i++) {
			spaaps += Settings.ttimes[i];
			Settings.ttimes[i] = Settings.ttimes[i + 1];
		}
		Settings.ttimes[99] = delta;
		spaaps += delta;
		Settings.ticktime = (float) (spaaps / 10);

		Q1R.batch.begin();
		Q1R.font.draw(Q1R.batch, "Tick Time:" + Math.floor(Settings.ticktime * 100) / 100, 50, 50);
		Q1R.batch.end();
		
		GameWorld.render();
		
		//TODO: If android only
		if(Gdx.app.getType() == ApplicationType.Android) {
			Q1R.batch.begin();
			Q1R.batch.draw(Resources.getImage("controls/menu"), 32, 536);
			
			if(buttons[0].isPressed)
			Q1R.batch.draw(Resources.getImage("controls/jump_on"), 0, 0);
			else
			Q1R.batch.draw(Resources.getImage("controls/jump"), 0, 0);
			
			if(buttons[1].isPressed)
			Q1R.batch.draw(Resources.getImage("controls/fire_on"), 672, 0);
			else
			Q1R.batch.draw(Resources.getImage("controls/fire"), 672, 0);
			
			Q1R.batch.end();
		}
		
		
		//MENU CODE
		if((buttons[2].isPressed||Gdx.input.isKeyPressed(Input.Keys.ESCAPE))&&!menuShown){
		menuShown = true;
		Resources.Music_DM.pause();
		Resources.Music_Offline.play();
		}
		if(menuShown) {MainMenuScreen.renderOverlay(delta, true);}
	}

	@Override
	public void resize(int width, int height) {
		Q1R.viewport.update(width, height);
		WIDTH = width;
		HEIGHT = height;
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {}

}
