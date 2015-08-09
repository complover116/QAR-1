package com.complover116.quar1;

import java.awt.Color;
import java.awt.Graphics2D;


public class Particle implements TAD{
	public double x;
	public double y;
	public int shape;
	public Color color;
	public int lifetime;
	public Particle(double x, double y, int shape, Color color, int lt) {
		this.x = x;
		this.y = y;
		this.shape = shape;
		this.color = color;
		this.lifetime = lt;
	}
	@Override
	public void tick() {
		this.lifetime --;
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(this.color);
		switch(shape) {
		case 1:
			g2d.fillRect((int)(this.x - lifetime/2), (int)(this.y - lifetime/2), this.lifetime, this.lifetime);
		break;
		}
	}

	@Override
	public boolean isDead() {
		if(this.lifetime < 0) return true;
		return false;
	}
	
}
