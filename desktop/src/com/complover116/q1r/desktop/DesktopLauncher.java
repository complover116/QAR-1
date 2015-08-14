package com.complover116.q1r.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.complover116.q1r.Q1R;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "QAR-1";
        config.width = 800;
        config.height = 600;
        config.resizable = true;
		new LwjglApplication(new Q1R(), config);
	}
}
