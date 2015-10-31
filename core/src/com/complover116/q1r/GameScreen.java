package com.complover116.q1r;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;

public class GameScreen implements Screen {
	Q1R game;

	int WIDTH = 0;
	int HEIGHT = 0;
	
	double time = 5;
	
	static boolean menuShown = false;
	
	public GameScreen(Q1R game) {
		this.game = game;

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {

		for (int i = 0; i < GameManager.players.size(); i++)
			GameManager.players.get(i).tick();
		if(!menuShown)
		GameWorld.update(delta);
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
		if (Gdx.app.getType() == ApplicationType.Android && time > 0) {

			Q1R.batch.begin();
			if (time > 1)
				Q1R.batch.setColor(1, 1, 1, 1);
			else
				Q1R.batch.setColor(1, 1, 1, (float) time);
			Q1R.batch.draw(Resources.getImage("interface/AndroidControls"), 0, 0);
			Q1R.batch.end();
			Q1R.batch.setColor(1, 1, 1, 1);
			time -= delta;
		}
		
		GameWorld.render();
		
		//MENU CODE
		if((GameWorld.buttons[5].isPressed||Gdx.input.isKeyPressed(Input.Keys.ESCAPE))&&!menuShown){
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
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
