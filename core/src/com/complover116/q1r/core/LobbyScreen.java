package com.complover116.q1r.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by complover116 on 23.06.2015 for QAR-1 Reloaded
 */
public class LobbyScreen implements Screen {

	public LobbyScreen() {
		
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Q1R.camera.update();
		Q1R.batch.setProjectionMatrix(Q1R.camera.combined);
		Q1R.batch.begin();
		
		
		
		Q1R.batch.end();
		
		MainMenuScreen.renderOverlay(delta, false);
	}

	@Override
	public void resize(int width, int height) {
		Q1R.viewport.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
