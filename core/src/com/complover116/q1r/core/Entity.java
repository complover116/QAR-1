package com.complover116.q1r.core;

abstract class Entity {
	public abstract void tick(double deltaT);

	public abstract void draw();

	boolean isDead = false;

	// TODO:Add networking functions
}
