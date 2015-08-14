package com.complover116.q1r;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by complover116 on 25.05.2015 for QAR-1 Reloaded
 */
public class MainMenuScreen implements Screen {
	Q1R game;
    public ArrayList<Button> buttons = new ArrayList<Button>();
    int curselect = -1;
    int nextMode = 0;
    int state = 1;
    int curScreen = 0;
    byte loaded = 1;
    boolean lastClick = false;
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
    public MainMenuScreen(Q1R gmae) {
    	game = gmae;
        MainMenu();
    }
    public void MainMenu() {
        buttons.clear();
        buttons.add(new Button(new Rectangle(500,64,256,64), "interface/Exit"));
        buttons.add(new Button(new Rectangle(500,128,256,64), "interface/Settings"));
        buttons.add(new Button(new Rectangle(500,192,256,64), "interface/Play"));
        curScreen = 0;
    }
    public void SettingsMenu() {
        buttons.clear();
        buttons.add(new Button(new Rectangle(500,64,256,64), "interface/Back"));
        buttons.add(new Button(new Rectangle(500,128,256,64), "interface/Audio"));
        buttons.add(new Button(new Rectangle(500,192,256,64), "interface/Video"));
        curScreen = 1;
    }
    public void SoundMenu() {
        buttons.clear();
        buttons.add(new Button(new Rectangle(500,64,256,64), "interface/Back"));
        buttons.add(new CustomButton(new Rectangle(500, 128, 256, 64), "interface/Music") {
            @Override
            public void draw(float X, float Y) {
                Q1R.batch.draw(Resources.textures.get("interface/SliderScale"), X, Y);
                Q1R.batch.draw(Resources.textures.get("interface/SliderSlide"), X+60+(Settings.musicVolume/100*170), Y+20);
            }

            @Override
            public void mouseMove(float X, float Y) {
                if(Gdx.input.isTouched() && X>65 && X<65+170){
                    Settings.musicVolume = (X - 65)/170*100;
                }
            }
        });
        buttons.add(new CustomButton(new Rectangle(500, 192, 256, 64), "interface/SFX") {
            @Override
            public void draw(float X, float Y) {
                Q1R.batch.draw(Resources.textures.get("interface/SliderScale"), X, Y);
                Q1R.batch.draw(Resources.textures.get("interface/SliderSlide"), X+60+(Settings.soundVolume/100*170), Y+20);
            }

            @Override
            public void mouseMove(float X, float Y) {
                if(Gdx.input.isTouched() && X>65 && X<65+170){
                    Settings.soundVolume = (X - 65)/170*100;
                }
            }
        });
        curScreen = 11;
    }
    public void render(float deltaT) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Q1R.camera.update();
        Q1R.batch.setProjectionMatrix(Q1R.camera.combined);

        if(loaded == 1) {
            Q1R.batch.begin();
            Q1R.batch.draw(Resources.textures.get("splashscreen"), 208, 175);
            Q1R.batch.end();

            loaded = 2;
            return;
        }
        if(loaded == 2) {
            Resources.load();
            loaded = 0;
            return;
        }
            int newselect = -1;
        if(state == 0) {
            Vector3 unp = Q1R.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            for (int i = 0; i < buttons.size(); i++) {
                if (buttons.get(i).rect.contains(unp.x, unp.y)) {
                    if(buttons.get(i) instanceof CustomButton) {
                        ((CustomButton)buttons.get(i)).mouseMove(unp.x - buttons.get(i).rect.getX(), unp.y - buttons.get(i).rect.getY());
                    } else {
                        newselect = i;
                    }
                }
            }
        }
        if(newselect != curselect) {
            curselect = newselect;
            //TODO: Replace with real sounds
            Resources.playSound("hurt2");
        }
        if(Gdx.input.isTouched()) {
            if(newselect>-1&&!lastClick) {
                lastClick = true;
                //TODO: Replace with real sounds
                Resources.playSound("fire1");
            }
        } else {
            if (lastClick) {
                lastClick = false;
                if(curScreen == 0) {
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
                }
                if(curScreen == 1) {
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
                if(curScreen == 11) {
                    if (newselect == 0) {
                        state = -1;
                        nextMode = 2;
                    }
                }
            }
        }
        boolean allready = true;
        for(int i = 0; i < buttons.size(); i ++) {
            if(state == 1) {
                if (buttons.get(i).offSetX > 0){
                    allready = false;
                    buttons.get(i).offSetX -= (1000 + i * 100) * deltaT;
                } else {
                    buttons.get(i).offSetX = 0;
                }
            }
            if(state == -1) {
                if (buttons.get(i).offSetX < 300){
                    allready = false;
                    buttons.get(i).offSetX += (1000 + i * 100) * deltaT;
                } else {
                    buttons.get(i).offSetX = 300;
                }
            }
        }
        if(state == 1 && allready) state = 0;
        if(state == -1 && allready) {
            if(nextMode == -100){
                Gdx.app.exit();
            }
            if(nextMode == 2){
                SettingsMenu();
            }
            if(nextMode == 1){
                MainMenu();
            }
            if(nextMode == 11){
                SoundMenu();
            }
            if(nextMode == 25565){
            	//TODO:TEMP! Replace with LobbyScreen once that is ready!;
            	GameManager.prepareLocal();
                game.setScreen(new GameScreen(game));
            }
            state = 1;
        }


        Q1R.batch.begin();
        Q1R.batch.draw(Resources.textures.get("interface/background"), 0, 0);
        for(int i = 0; i < buttons.size(); i ++) {
            if (curselect == i) {
                Q1R.batch.draw(Resources.textures.get(buttons.get(i).img), buttons.get(i).rect.x - 32 + buttons.get(i).offSetX, buttons.get(i).rect.y);
            } else {
                Q1R.batch.draw(Resources.textures.get(buttons.get(i).img), buttons.get(i).rect.x + buttons.get(i).offSetX, buttons.get(i).rect.y);
                if (buttons.get(i) instanceof CustomButton) {
                    ((CustomButton) buttons.get(i)).draw(buttons.get(i).rect.x + buttons.get(i).offSetX, buttons.get(i).rect.y);
                }
            }
        }
        Q1R.batch.end();
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
