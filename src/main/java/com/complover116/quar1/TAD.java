package com.complover116.quar1;

import java.awt.Graphics2D;

public interface TAD {
	public abstract void tick();
	public abstract void draw(Graphics2D g2d);
	public abstract boolean isDead();
}
