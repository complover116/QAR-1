package com.complover116.q1r.core;

public abstract class Entity {
	public abstract void tick(double deltaT);

	public abstract void draw();

	public boolean isDead = false;

	// TODO:Add networking functions
}
