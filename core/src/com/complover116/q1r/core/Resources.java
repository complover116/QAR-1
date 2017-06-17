package com.complover116.q1r.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by complover116 on 25.05.2015 for QAR-1 Reloaded
 */
class Resources {
	public static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	
	private static final int LOADSTEP_IMAGELIST = 0;
	private static final int LOADSTEP_IMAGES = 1;
	private static final int LOADSTEP_SOUNDLIST = 2;
	private static final int LOADSTEP_SOUNDS = 3;
	private static final int LOADSTEP_MUSIC = 4;
	private static int loadStep = LOADSTEP_IMAGELIST;
	private static int loadIterator = 0;
	private static String imglist[];
	private static String soundlist[];
	
	public static Music Music_DM;
	public static Music Music_Offline;
	
	public static void loadVital() {
		textures.put("splashscreen", new Texture(Gdx.files.internal("img/Logo.png")));
		textures.put("ERROR", new Texture(Gdx.files.internal("img/ERROR.png")));
	}
	public static void updateMusicVolume() {
		Music_DM.setVolume(Settings.musicVolume / (float) 100);
		Music_Offline.setVolume(Settings.musicVolume / (float) 100);
	}
	public static void load() {
		if(loadStep == LOADSTEP_IMAGELIST) {
		Gdx.app.log("Resources", "Loading image list...");
		String imglistRaw = Gdx.files.internal("ImageList").readString();

		imglist = imglistRaw.split("\n");
		Gdx.app.log("Resources", "Found " + imglist.length + " image declarations");
		loadStep = LOADSTEP_IMAGES;
		return;
		}
		if(loadStep == LOADSTEP_IMAGES) {
			String imagename = imglist[loadIterator];
			imagename = imagename.trim();
			MainMenuScreen.loadStep = "Loading " + imagename;
			try {
				textures.put(imagename, new Texture(Gdx.files.internal("img/" + imagename + ".png")));
				Gdx.app.log("Resources", "Loaded " + imagename);
			} catch (Exception e) {
				Gdx.app.error("Resources", "Failed loading " + imagename);
				e.printStackTrace();
				MainMenuScreen.loaded = -1;

				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {

				}
				return;
			}
			loadIterator++;
			if(loadIterator==imglist.length) {
				loadIterator = 0;
				loadStep = LOADSTEP_SOUNDLIST;
				return;
			}
			return;
		}
		if(loadStep == LOADSTEP_SOUNDLIST) {
		Gdx.app.log("Resources", "Loading sound list...");

		String soundlistRaw = Gdx.files.internal("SoundList").readString();

		soundlist = soundlistRaw.split("\n");
		Gdx.app.log("Resources", "Found " + soundlist.length + " sound declarations");
		loadStep = LOADSTEP_SOUNDS;
		}
		if (loadStep == LOADSTEP_SOUNDS) {
			String soundname = soundlist[loadIterator];
			soundname = soundname.trim();
			MainMenuScreen.loadStep = "Loading " + soundname;
			try {
				sounds.put(soundname, Gdx.audio.newSound(Gdx.files.internal("sound/effects/" + soundname + ".ogg")));
				Gdx.app.log("Resources", "Loaded " + soundname);
			} catch (Exception e) {
				Gdx.app.error("Resources", "Failed loading " + soundname);
				MainMenuScreen.loaded = -1;
				return;
			}
			loadIterator++;
			if(loadIterator==soundlist.length) {
				loadIterator = 0;
				loadStep = LOADSTEP_MUSIC;
				return;
			}
			return;
		}
		if(loadStep == LOADSTEP_MUSIC) {
		Gdx.app.log("Resources", "Loading music...");
		try {
			Music_DM = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Q1R_DM.ogg"));
			Music_Offline = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Q1R_Offline.ogg"));
			
			Music_Offline.setLooping(true);
			Music_DM.setLooping(true);
		} catch (Exception e) {
			Gdx.app.error("Resources", "Failed loading music!");
			MainMenuScreen.loaded = -1;
			return;
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		MainMenuScreen.loaded = 3;
		}
	}

	public static Texture getImage(String name) {
		if (textures.containsKey(name)) {
			return textures.get(name);
		}
		Gdx.app.error("Resources", "");
		return null;
	}

	static void playSound(String name) {
		sounds.get(name).play(Settings.soundVolume / (float) 100);
	}
}
