package com.complover116.q1r;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by complover116 on 25.05.2015 for QAR-1 Reloaded
 */
public class Resources {
    public static HashMap<String, Texture> textures = new HashMap<String, Texture>();
    public static HashMap<String, Sound> sounds = new HashMap<String, Sound>();

    public static void loadVital() {
        textures.put("splashscreen", new Texture(Gdx.files.internal("img/Logo.png")));
    }
    public static void load() {

        Gdx.app.log("Resources", "Loading image list...");
        String imglistRaw = Gdx.files.internal("ImageList").readString();

        String imglist[] = imglistRaw.split("\n");
        Gdx.app.log("Resources", "Found "+imglist.length+" image declarations");

        for(String imagename : imglist) {
            imagename = imagename.trim();
            try {
                textures.put(imagename, new Texture(Gdx.files.internal("img/" + imagename+".png")));
                Gdx.app.log("Resources", "Loaded "+imagename);
            } catch (Exception e) {
                Gdx.app.error("Resources", "Failed loading "+imagename);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {

            }
        }

        Gdx.app.log("Resources", "Loading sound list...");

        String soundlistRaw = Gdx.files.internal("SoundList").readString();

        String soundlist[] = soundlistRaw.split("\n");
        Gdx.app.log("Resources", "Found "+soundlist.length+" sound declarations");

        for(String soundname : soundlist) {
            soundname = soundname.trim();
            try {
                sounds.put(soundname, Gdx.audio.newSound(Gdx.files.internal("sound/effects/" + soundname+".wav")));
                Gdx.app.log("Resources", "Loaded "+soundname);
            } catch (Exception e) {
                Gdx.app.error("Resources", "Failed loading "+soundname);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {

            }
        }

    }

    public static Texture getImage(String name) {
        if(textures.containsKey(name)) {
            return textures.get(name);
        }
        Gdx.app.error("Resources", "");
        return null;
    }
    static void playSound(String name) {
        sounds.get(name).play(Settings.soundVolume/(float)100);
    }
}
