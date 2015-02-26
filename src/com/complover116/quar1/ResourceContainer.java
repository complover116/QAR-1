package com.complover116.quar1;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.sound.sampled.Clip;

public class ResourceContainer {
	public static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	public static HashMap<String, Clip> sounds = new HashMap<String, Clip>();
	public static void load() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResourceLoader rsld = new ResourceLoader();
		rsld.execute();
		
	}
}
