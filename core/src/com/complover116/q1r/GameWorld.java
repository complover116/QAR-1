package com.complover116.q1r;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GameWorld {
	static ArrayList<Platform> platforms = new ArrayList<Platform>();
	public static void init() {
		platforms.add(new Platform(new Rectangle(100, 300, 600, 50), 0));
	}
	public static void render() {
		
		Q1R.shapeRenderer.begin(ShapeType.Line);
		Q1R.shapeRenderer.setColor(1, 1, 1, 1);
		for(int i = 0; i < platforms.size(); i++) {
			Platform plat = platforms.get(i);
			Q1R.shapeRenderer.rect(plat.bounds.getX(), plat.bounds.getY(), plat.bounds.getWidth(), plat.bounds.getHeight());
		}
		Q1R.shapeRenderer.end();
	}
	public static void update(double deltaT) {
		
	}
}
