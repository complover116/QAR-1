package com.complover116.q1r.core;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen implements Screen {
	private static final int BUTTON_JUMP = 0;
	private static final int BUTTON_FIRE = 1;

	private int WIDTH = 0;
	private int HEIGHT = 0;
	private int RIGHTBORDER = 0;
	
	double time = 0;
	
	static float gameSpeed = 1;
	
	private float lag = 0;
	private int lagamount = 0;
	
	static boolean menuShown = false;
	
	static AndroidButton buttons[] = new AndroidButton[3];
	
	GameScreen() {
		
		buttons[BUTTON_JUMP] = new AndroidButton(new Rectangle(WIDTH-128, 128, 128, 128));
		// RIGHT
		buttons[BUTTON_FIRE] = new AndroidButton(new Rectangle(WIDTH-128, 0, 128, 128));
		// MENU
		// This will be replaced
		buttons[2] = new AndroidButton(new Rectangle(32, 536, 64, 64));
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		if(lag>0)lag -= delta;
		if(delta > 0.03) {
			lagamount = (int)Math.ceil(0.03/delta*100);
			delta = 0.03f;
			lag = 1;
		}
		if(gameSpeed < 1) {
			gameSpeed += delta/2;
		}
		if(gameSpeed > 1) {
			gameSpeed = 1;
		}
		float inGameDelta = delta*gameSpeed;
		for (int i = 0; i < GameManager.players.size(); i++)
			GameManager.players.get(i).tick();
		if(!menuShown){
			for (AndroidButton button : buttons) {
				button.update();
			}
			//This pretty much updates the entire world
			GameWorld.update(inGameDelta);
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
		//Will be replaced by pop-up text and by visual effects
		/*for (int i = 0; i < GameManager.players.size(); i++)
			GameManager.players.get(i).draw();*/
		
			

		Q1R.viewport.update(WIDTH, HEIGHT);
		Q1R.shapeRenderer.setProjectionMatrix(Q1R.camera.combined);
		Q1R.batch.setProjectionMatrix(Q1R.camera.combined);


		//TODO: If android only
		if(Gdx.app.getType() == ApplicationType.Android) {
			Q1R.batch.begin();
			Q1R.batch.draw(Resources.getImage("controls/menu"), 32, 536);
			
			if(buttons[0].isPressed)
			Q1R.batch.draw(Resources.getImage("controls/jump_on"), RIGHTBORDER-128, 128);
			else
			Q1R.batch.draw(Resources.getImage("controls/jump"), RIGHTBORDER-128, 128);
			
			if(buttons[1].isPressed)
			Q1R.batch.draw(Resources.getImage("controls/fire_on"), RIGHTBORDER-128, 0);
			else
			Q1R.batch.draw(Resources.getImage("controls/fire"), RIGHTBORDER-128, 0);
			
			Q1R.batch.end();
		}

		
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
		if(lag>0) {
			Q1R.font.draw(Q1R.batch, "LAG ("+lagamount+"% game speed)", 50, 80);
		}
		Q1R.font.draw(Q1R.batch, "Tick Time:" + Math.floor(Settings.ticktime * 100) / 100, 50, 50);
		Q1R.batch.end();
		
		GameWorld.render();
		
		
		
		
		//MENU CODE
		boolean menu = false;
		
		for(int i = 0; i < GameManager.players.size(); i ++){
			if(GameManager.players.get(i).controller != null && GameManager.players.get(i).controller.getButton(7))
				menu = true;
		}
		
		if((menu||buttons[2].isPressed||Gdx.input.isKeyPressed(Input.Keys.ESCAPE))&&!menuShown){
		menuShown = true;
		Resources.Music_DM.pause();
		Resources.Music_Offline.play();
		}
		if(menuShown) {
			//MainMenuScreen.renderOverlay(delta, true);
			
		}
	}

	@Override
	public void resize(int width, int height) {
		Q1R.viewport.update(width, height);
		WIDTH = width;
		HEIGHT = height;
		RIGHTBORDER = (int)(Q1R.viewport.getWorldWidth()/2)+400;
		if(buttons[BUTTON_JUMP] != null)
		buttons[BUTTON_JUMP].bounds = new Rectangle(RIGHTBORDER-128, 128, 128, 128);
		if(buttons[BUTTON_FIRE] != null)
		buttons[BUTTON_FIRE].bounds = new Rectangle(RIGHTBORDER-128, 0, 128, 128);
		
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
