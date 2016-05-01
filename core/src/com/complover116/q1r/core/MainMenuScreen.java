package com.complover116.q1r.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by complover116 on 25.05.2015 for QAR-1 Reloaded
 */
public class MainMenuScreen implements Screen {

	static volatile String loadStep;
	static public ArrayList<Button> buttons = new ArrayList<Button>();
	static int curselect = -1;
	static int nextMode = 0;
	static int state = 1;
	static int curScreen = 0;
	static volatile byte loaded = 1;
	static boolean lastClick = false;

	static class Button {
		Rectangle rect;
		String img;
		float offSetX = 256;

		public Button(Rectangle ses, String image) {
			rect = ses;
			img = image;
		}
	}

	static abstract class CustomButton extends Button {

		public CustomButton(Rectangle ses, String image) {
			super(ses, image);
		}

		public abstract void draw(float X, float Y);

		public abstract void mouseMove(float X, float Y);
	}

	public MainMenuScreen() {
		MainMenu();
	}

	public static void MainMenu() {
		buttons.clear();
		buttons.add(new Button(new Rectangle(546, 0, 256, 64), "interface/Exit"));
		buttons.add(new Button(new Rectangle(546, 64, 256, 64), "interface/Settings"));
		buttons.add(new Button(new Rectangle(546, 128, 256, 64), "interface/Play"));
		curScreen = 0;
	}
	public static void LobbyMenu() {
		buttons.clear();
		buttons.add(new Button(new Rectangle(546, 0, 256, 64), "interface/Back"));
		buttons.add(new Button(new Rectangle(546, 64, 256, 64), "interface/Play-Offline"));
		buttons.add(new Button(new Rectangle(546, 128, 256, 64), "interface/Play-Host"));
		buttons.add(new Button(new Rectangle(546, 192, 256, 64), "interface/Play-Join"));
		curScreen = 42;
	}
	public static void GameMenu() {
		buttons.clear();
		buttons.add(new Button(new Rectangle(546, 0, 256, 64), "interface/Exit"));
		buttons.add(new Button(new Rectangle(546, 64, 256, 64), "interface/Settings"));
		buttons.add(new Button(new Rectangle(546, 128, 256, 64), "interface/Resume"));
		buttons.add(new Button(new Rectangle(546, 192, 256, 64), "interface/Restart"));
		curScreen = 0;
	}

	public static void SettingsMenu() {
		buttons.clear();
		buttons.add(new Button(new Rectangle(546, 0, 256, 64), "interface/Back"));
		buttons.add(new Button(new Rectangle(546, 64, 256, 64), "interface/Audio"));
		buttons.add(new CustomButton(new Rectangle(546, 128, 256, 64), "interface/Sensitivity") {
			@Override
			public void draw(float X, float Y) {
				Q1R.batch.draw(Resources.textures.get("interface/SliderScale"), X, Y);
				Q1R.batch.draw(Resources.textures.get("interface/SliderSlide"),
						X + 60 + (Settings.tiltSensitivity / 20 * 170), Y + 20);
			}

			@Override
			public void mouseMove(float X, float Y) {
				if (Gdx.input.isTouched() && X > 65 && X < 65 + 170) {
					Settings.tiltSensitivity = (X - 65) / 170 * 20;
					Resources.updateMusicVolume();
				}
			}
		});
		curScreen = 1;
	}

	public static void SoundMenu() {
		buttons.clear();
		buttons.add(new Button(new Rectangle(546, 0, 256, 64), "interface/Back"));
		buttons.add(new CustomButton(new Rectangle(546, 64, 256, 64), "interface/Music") {
			@Override
			public void draw(float X, float Y) {
				Q1R.batch.draw(Resources.textures.get("interface/SliderScale"), X, Y);
				Q1R.batch.draw(Resources.textures.get("interface/SliderSlide"),
						X + 60 + (Settings.musicVolume / 100 * 170), Y + 20);
			}

			@Override
			public void mouseMove(float X, float Y) {
				if (Gdx.input.isTouched() && X > 65 && X < 65 + 170) {
					Settings.musicVolume = (X - 65) / 170 * 100;
					Resources.updateMusicVolume();
				}
			}
		});
		buttons.add(new CustomButton(new Rectangle(546, 128, 256, 64), "interface/SFX") {
			@Override
			public void draw(float X, float Y) {
				Q1R.batch.draw(Resources.textures.get("interface/SliderScale"), X, Y);
				Q1R.batch.draw(Resources.textures.get("interface/SliderSlide"),
						X + 60 + (Settings.soundVolume / 100 * 170), Y + 20);
			}

			@Override
			public void mouseMove(float X, float Y) {
				if (Gdx.input.isTouched() && X > 65 && X < 65 + 170) {
					Settings.soundVolume = (X - 65) / 170 * 100;
				}
			}
		});
		curScreen = 11;
	}
	
	public static void renderOverlay(float deltaT, boolean ingame) {
		int newselect = curselect;
		
		
		if (state == 0) {
			Vector2 unp = Q1R.viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i).rect.contains(unp.x, unp.y)) {
					if (buttons.get(i) instanceof CustomButton) {
						((CustomButton) buttons.get(i)).mouseMove(unp.x - buttons.get(i).rect.getX(),
								unp.y - buttons.get(i).rect.getY());
					} else {
						newselect = i;
					}
				}
			}
		}
		if (newselect != curselect) {
			curselect = newselect;
			Resources.playSound("hurt2");
		}
		boolean menu = false;
		for(int i = 0; i < GameManager.players.size(); i ++){
			if(GameManager.players.get(i).controller != null && GameManager.players.get(i).controller.getButton(0))
				menu = true;
		}
		if (Gdx.input.isTouched() || menu) {
			if (newselect > -1 && !lastClick) {
				lastClick = true;
				Resources.playSound("fire1");
			}
		} else {
			if (lastClick) {
				lastClick = false;
				if (curScreen == 0) {
					if (newselect == 0) {
						state = -1;
						nextMode = -100;
					}
					if (newselect == 1) {
						state = -1;
						nextMode = 2;
					}
					if (newselect == 2) {
						state = -1;
						nextMode = 25565;
					}
					/*if (newselect == 3) {
						state = -1;
						nextMode = 1337;
					}*/
				}
				if (curScreen == 1) {
					if (newselect == 0) {
						state = -1;
						nextMode = 1;
					}
					if (newselect == 1) {
						state = -1;
						nextMode = 11;
					}
					if (newselect == 2) {
						state = -1;
						nextMode = 12;
					}
				}
				if (curScreen == 42) {
					if (newselect == 0) {
						state = -1;
						nextMode = 1;
						Q1R.game.setScreen(Q1R.MMS);
						NetServer.serverRunning = false;
					}
					if (newselect == 1) {
						nextMode = 25566;
						state = -1;
					}
					if (newselect == 2) {
						state = -1;
						
						//TEMP!!!!
						NetServer.startServer();
					}
				}
				if (curScreen == 11) {
					if (newselect == 0) {
						state = -1;
						nextMode = 2;
					}
				}
			}
		}
		boolean allready = true;
		for (int i = 0; i < buttons.size(); i++) {
			if (state == 1) {
				if (buttons.get(i).offSetX > 0) {
					allready = false;
					buttons.get(i).offSetX -= (1000 + i * 100) * deltaT;
				} else {
					buttons.get(i).offSetX = 0;
				}
			}
			if (state == -1) {
				if (buttons.get(i).offSetX < 300) {
					allready = false;
					buttons.get(i).offSetX += (1000 + i * 100) * deltaT;
				} else {
					buttons.get(i).offSetX = 300;
				}
			}
		}
		if (state == 1 && allready)
			state = 0;
		if (state == -1 && allready) {
			if (nextMode == -100) {
				if(ingame) {
					Q1R.game.setScreen(Q1R.MMS);
					Resources.Music_DM.stop();
					MainMenu();
				} else
				Gdx.app.exit();
			}
			if (nextMode == 2) {
				SettingsMenu();
			}
			if (nextMode == 1) {
				if(ingame)
				GameMenu();
				else
				MainMenu();
			}
			if (nextMode == 11) {
				SoundMenu();
			}
			/*if (nextMode == 1337) {
				GameMaFnager.prepareLocal();
				nextMode = 25565;
			}*/
			if (nextMode == 25565) {
			
			if(ingame){
			Resources.Music_Offline.stop();
			Resources.Music_DM.play();
			GameScreen.menuShown = false;
			}
			else{
				LobbyMenu();
				Q1R.game.setScreen(Q1R.LS);
				
				
				}
			}
			if(nextMode == 25566){
				//This is for starting a local game
				GameParams pars = new GameParams();
				pars.players[0] = (byte)((GSelector)Q1R.LS.gElements.get(0)).selection;
				pars.players[1] = (byte)((GSelector)Q1R.LS.gElements.get(1)).selection;
				pars.players[2] = (byte)((GSelector)Q1R.LS.gElements.get(2)).selection;
				pars.players[3] = (byte)((GSelector)Q1R.LS.gElements.get(3)).selection;
				GameManager.prepareLocal(pars);
				Resources.Music_Offline.stop();
				Resources.Music_DM.play();
				//Resources.Music_DM.setPosition(85);
				Q1R.game.setScreen(Q1R.GS);
				GameScreen.menuShown = false;
				GameMenu();
			}
			state = 1;
		}

		Q1R.batch.begin();
		
		for (int i = 0; i < buttons.size(); i++) {
			if (curselect == i) {
				Q1R.batch.draw(Resources.textures.get(buttons.get(i).img),
						buttons.get(i).rect.x - 32 + buttons.get(i).offSetX, buttons.get(i).rect.y);
			} else {
				Q1R.batch.draw(Resources.textures.get(buttons.get(i).img),
						buttons.get(i).rect.x + buttons.get(i).offSetX, buttons.get(i).rect.y);
				if (buttons.get(i) instanceof CustomButton) {
					((CustomButton) buttons.get(i)).draw(buttons.get(i).rect.x + buttons.get(i).offSetX,
							buttons.get(i).rect.y);
				}
			}
		}
		Q1R.batch.end();
	}
	
	public void render(float deltaT) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Q1R.camera.update();
		Q1R.batch.setProjectionMatrix(Q1R.camera.combined);

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
		
		
		Q1R.batch.begin();
		Q1R.batch.draw(Resources.textures.get("interface/background"), (float)(Math.random()*4), (float)(Math.random()*0));
		if(Gdx.app.getType() == ApplicationType.Android){
			
		//TODO: TEMP!
		if(Gdx.input.getPitch()<-Settings.tiltSensitivity)
		Q1R.batch.draw(Resources.textures.get("controls/right_on"), 400-Gdx.input.getPitch()*2, 100);
		else if (Gdx.input.getPitch()>Settings.tiltSensitivity)
		Q1R.batch.draw(Resources.textures.get("controls/left_on"), 400-Gdx.input.getPitch()*2, 100);
		else
		Q1R.batch.draw(Resources.textures.get("controls/fire_on"), 400-Gdx.input.getPitch()*2, 100);
		}
		Q1R.batch.end();
		
		
		renderOverlay(deltaT, false);
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
