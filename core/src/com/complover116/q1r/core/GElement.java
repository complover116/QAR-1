package com.complover116.q1r.core;

abstract class GElement {
	float x;
	float y;
	public abstract void render();
	GElement(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
