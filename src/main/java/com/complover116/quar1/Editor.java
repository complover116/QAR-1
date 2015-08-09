package com.complover116.quar1;

public class Editor {
	public static int posX = 0;
	public static int posY = 0;
	public static int color = 0;
	public static void initialize() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Load tools
		//Load map/create new
		Loader.initialized = true;
		Loader.editMode = true;
	}
}
