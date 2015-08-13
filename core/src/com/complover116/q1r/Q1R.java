package com.complover116.q1r;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Q1R extends Game {
	public static SpriteBatch batch;
    public static OrthographicCamera camera;
    public static Viewport viewport;
	
	@Override
	public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,600);
        viewport = new FitViewport(800, 600, camera);
		batch = new SpriteBatch();
        Resources.loadVital();
        this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
