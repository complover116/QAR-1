package com.complover116.q1r;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class Projectile extends Entity {
	
	float x;
	float y;
	float velX;
	float velY;
	int color;
	
	double time = 0;
	
	public void tick(double deltaT) {
		this.x += this.velX*deltaT;
		this.y += this.velY*deltaT;
		
		time += deltaT;
		
		if(time>0.02) {
			time = 0;
			for(int i = 0; i < 3; i ++)
				if(Settings.ticktime<0.25)
				GameWorld.ents.add(new Particle(x, y, 4, 4, this.color, (float)Math.random()*200-100, (float)Math.random()*200-100, false, true));
if(Settings.ticktime<0.3)			
GameWorld.ents.add(new Particle(x, y, 8, 16, this.color, 0, 0, false, false));
		}
		
		for(int i = 0; i < GameWorld.platforms.size(); i ++) {
			if(new Rectangle(this.x,this.y, 16,16).overlaps(GameWorld.platforms.get(i).bounds)) {
				for(int j = 0; j < 8; j ++)
					GameWorld.ents.add(new Particle(x, y, 8, 8, this.color, (float)Math.random()*160-80, (float)Math.random()*160-80));
				this.isDead = true;
			}
		}
		for(int i = 0; i < GameWorld.players.size(); i ++) {
			if(GameWorld.players.get(i).getBB().overlaps(new Rectangle(this.x,this.y, 16,16))&&GameWorld.players.get(i).color!=this.color) {
				this.isDead = true;
				if(this.x>GameWorld.players.get(i).x)
				GameWorld.players.get(i).getHit(1, true);
				else
				GameWorld.players.get(i).getHit(1, false);
			}
		}
		
		if(this.x > 1000||this.x<-200||this.y>1000||this.y<-200){
this.isDead = true;
}
	}
	
	public void draw() {
		for(int i = 0; i < 10; i += 1) {
			Color col = PlayerEnt.colorFromID(color);
			Q1R.shapeRenderer.setColor(col.r, col.g, col.b, (float)0.05);
			Q1R.shapeRenderer.circle(x, y, 16+i*4);
		}
		
		Q1R.shapeRenderer.setColor(PlayerEnt.colorFromID(color));
		Q1R.shapeRenderer.rect(x-8,y-8,16,16);
	}
	
	public Projectile(float x, float y, float velX, float velY, int color) {
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
		this.color = color;
	}
}
