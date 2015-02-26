package com.complover116.quar1;

import javax.sound.sampled.Clip;

public class SoundHandler {
	public static boolean playSound(String name) {
		if(ResourceContainer.sounds.get(name)!= null){
		ResourceContainer.sounds.get(name).stop();
		ResourceContainer.sounds.get(name).setFramePosition(0);
		ResourceContainer.sounds.get(name).start();
		return true;
		}
		System.err.println("Sound "+name+" was to be played, but it was not loaded!");
		return false;
	}
	public static boolean startLoop(String name) {
		if(ResourceContainer.sounds.get(name)!= null){
		ResourceContainer.sounds.get(name).loop(Clip.LOOP_CONTINUOUSLY);
		return true;
		}
		System.err.println("Sound "+name+" was to be played, but it was not loaded!");
		return false;
	}
	public static boolean stop(String name) {
		if(ResourceContainer.sounds.get(name)!= null){
			ResourceContainer.sounds.get(name).stop();
			return true;
			}
			System.err.println("Sound "+name+" was to be played, but it was not loaded!");
			return false;
	}
}
