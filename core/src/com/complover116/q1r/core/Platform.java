package com.complover116.q1r.core;

import com.badlogic.gdx.math.Rectangle;

public class Platform {
	private int type;
	public Rectangle bounds;

	public int getType() {
		return type;
	}

	public Platform(Rectangle bounds, int type) {
		this.bounds = bounds;
		this.type = type;
	}
}
