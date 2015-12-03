package com.complover116.q1r.core;

public abstract class GElement {
	public float x;
	public float y;
	public abstract void render();
	public GElement(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
