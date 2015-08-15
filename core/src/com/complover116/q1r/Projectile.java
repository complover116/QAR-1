package com.complover116.q1r;

import com.badlogic.gdx.math.Rectangle;

public class Projectile extends Entity {
	
	float x;
	float y;
	float velX;
	float velY;
	int color;
	
	
	public void tick(double deltaT) {
		this.x += this.velX*deltaT;
		this.y += this.velY*deltaT;
		
		for(int i = 0; i < GameWorld.platforms.size(); i ++) {
			if(new Rectangle(this.x,this.y, 16,16).overlaps(GameWorld.platforms.get(i).bounds)) {
				this.isDead = true;
			}
		}
	}
	
	public void draw() {
		Q1R.shapeRenderer.setColor(1, 0, 0, 1);
		Q1R.shapeRenderer.rect(x-8,y-8,16,16);
	}
	
	public Projectile(float x, float y, float velX, float velY) {
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
	}
}
