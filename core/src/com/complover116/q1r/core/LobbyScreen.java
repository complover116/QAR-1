package com.complover116.q1r.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by complover116 on 23.06.2015 for QAR-1 Reloaded
 */
public class LobbyScreen implements Screen {
	public ArrayList<GElement> gElements = new ArrayList<GElement>();
	public LobbyScreen() {
		gElements.add(new GSelector(0, 500, true, new String[]{"------", "Local Player", "BOT"}, 30, 200));
		gElements.add(new GSelector(200, 500, true, new String[]{"------", "Local Player", "BOT"}, 30, 200));
		gElements.add(new GSelector(400, 500, true, new String[]{"------", "BOT"}, 30, 200));
		gElements.add(new GSelector(600, 500, true, new String[]{"------", "BOT"}, 30, 200));
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Q1R.camera.update();
		Q1R.batch.setProjectionMatrix(Q1R.camera.combined);
		Q1R.batch.begin();
		Q1R.batch.draw(Resources.getImage("player1"), 25, 525);
		Q1R.batch.draw(Resources.getImage("player2"), 225, 525);
		Q1R.batch.draw(Resources.getImage("player3"), 425, 525);
		Q1R.batch.draw(Resources.getImage("player4"), 625, 525);
		Q1R.batch.end();
		for(int i = 0; i < gElements.size(); i ++) {
			gElements.get(i).render();
		}
		
		
		
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
