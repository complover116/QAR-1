package com.complover116.quar1;

public class Editor {
	public static int posX = 0;
	public static int posY = 0;
	public static String mapname = "autosave";
	public static int color = 0;
	public static void initialize() {
		Render.loadStep = "Entering edit mode...";
		Loader.initialized = false;
		//Load tools
		//Load map/create new
		mapname = GUI.askString("Load map", "Please, specify a map name. If it does not exist, a new one will be created");
		Render.loadStep = "Loading map "+mapname;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(CurGame.lvl.load(mapname)) {
			
		} else {
			CurGame.lvl.loadMap(Map.map1);
		}
		Loader.initialized = true;
		Loader.editMode = true;
	}
	public static void save() {
		Loader.initialized = false;
		Render.loadStep = "Saving...";
		mapname = GUI.askString("Save", "Input the filename");
		Render.loadStep = "Saving to "+mapname;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CurGame.lvl.save(mapname);
		Loader.initialized = true;
	}
}
