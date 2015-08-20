package com.complover116.q1r;

import com.badlogic.gdx.graphics.Color;

public class Particle extends Entity {
	float x;
	float y;
	float size;
	float fadeSpeed;
	int color;
	
	boolean gravity = false;
	
	float velX;
	float velY;
	
	boolean glow = true;
	public void draw() {
		if(this.size<=0) return;
		Q1R.shapeRenderer.setColor(PlayerEnt.colorFromID(color));
		if(glow){
			for(int i = 0; i < 10; i += 1) {
				Color col = PlayerEnt.colorFromID(color);
				Q1R.shapeRenderer.setColor(col.r, col.g, col.b, (float)0.1);
				Q1R.shapeRenderer.rect(x-size/2-size/10*i,y-size/2-size/10*i,size+size/5*i,size+size/5*i);
			}
		}
		//Q1R.shapeRenderer.setColor(PlayerEnt.colorFromID(color));
		//Q1R.shapeRenderer.rect(x-size/2,y-size/2,size,size);
	}

	@Override
	public void tick(double deltaT) {
		//Gdx.app.log("Entities", "Particle ticked "+size);
		this.x += this.velX*deltaT;
		this.y += this.velY*deltaT;
		
		if(this.gravity){
			this.velY -= 600*deltaT;
		}
		
		this.size -= fadeSpeed*deltaT;
		if(this.size<=0) {
			//Gdx.app.log("Entities", "Particle died");
			this.isDead = true;
		}
	}
	
	public Particle(float x, float y, float size, float fadeSpeed, int color, float velX, float velY) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.fadeSpeed = fadeSpeed;
		this.color = color;
		this.velX = velX;
		this.velY = velY;
	}
	public Particle(float x, float y, float size, float fadeSpeed, int color, float velX, float velY, boolean gravity) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.fadeSpeed = fadeSpeed;
		this.color = color;
		this.velX = velX;
		this.velY = velY;
		this.gravity = gravity;
	}
	
	public Particle(float x, float y, float size, float fadeSpeed, int color) {
		this(x,y,size,fadeSpeed,color,0,0);
		
	}
	public Particle(float x, float y, int color) {
		this(x, y, 4, 8, color);
	}
}
