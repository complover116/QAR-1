package com.complover116.q1r;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {
	Q1R game;
	
	double time = 3;
	public GameScreen(Q1R game) {
		this.game = game;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		if(Gdx.app.getType() == ApplicationType.Android&&time>0) {
			
			Q1R.batch.begin();
			if(time < 0)
			if(time>1)
				Q1R.batch.setColor(1, 1, 1, 0);
			else
				Q1R.batch.setColor(1, 1, 1, (float) time);
			Q1R.batch.draw(Resources.getImage("intefrace/AndroidControls"), 0, 0);
			Q1R.batch.end();
			time -= delta;
		}
		for(int i = 0; i < GameManager.players.size(); i++)
			GameManager.players.get(i).tick();
		GameWorld.update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Q1R.camera.update();
        Q1R.batch.setProjectionMatrix(Q1R.camera.combined);
        Q1R.shapeRenderer.setProjectionMatrix(Q1R.camera.combined);
		GameWorld.render();
	}

	@Override
	public void resize(int width, int height) {
		Q1R.viewport.update(width, height);
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
