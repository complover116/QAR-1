package com.complover116.q1r.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Q1R extends Game {
	public static Q1R game;
	
	public static SpriteBatch batch;
	public static ShapeRenderer shapeRenderer;
	public static OrthographicCamera camera;
	public static Viewport viewport;
	public static BitmapFont font;
	
	public static MainMenuScreen MMS;
	public static GameScreen GS;
	public static LobbyScreen LS;
	
	@Override
	public void create() {
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600);
		viewport = new ExtendViewport(800, 600, camera);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		Resources.loadVital();
		game = this;
		MMS = new MainMenuScreen();
		GS = new GameScreen();
		LS = new LobbyScreen();
		this.setScreen(MMS);
	}

}
