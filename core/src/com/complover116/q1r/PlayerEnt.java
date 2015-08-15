package com.complover116.q1r;

import com.badlogic.gdx.math.Rectangle;

public class PlayerEnt {
	float x = 400;
	float y = 400;
	float velX = 0;
	float velY = 0;
	
	//INPUT VARIABLES!
	//Volatile because they are accessed from network threads
	volatile byte moveDir = 0;
	volatile boolean jump = false;
	volatile boolean fire = false;
	
	boolean facingLeft = false;
	
	int color = 1;
	
	Rectangle getBB() {
		return new Rectangle(x,y,32,32);
	}
	
	public void tickPhysics(double deltaT) {
		this.velY -= 1500*deltaT;
		
		float newY = (float) (this.y + this.velY*deltaT);
		float newX = (float) (this.x + this.velX*deltaT);
		
		float topCollision = 0;
		
		for(int i = 0; i < GameWorld.platforms.size(); i ++) {
			if(GameWorld.platforms.get(i).bounds.overlaps(new Rectangle(this.x,newY,32,32))){
				if(this.velY<0){
					if(topCollision<GameWorld.platforms.get(i).bounds.getY()+GameWorld.platforms.get(i).bounds.getHeight()){
						topCollision = GameWorld.platforms.get(i).bounds.getY()+GameWorld.platforms.get(i).bounds.getHeight();
					}
				} else {
					if(topCollision>GameWorld.platforms.get(i).bounds.getY()){
						topCollision = GameWorld.platforms.get(i).bounds.getY();
					}
				}
			}
		}
		if(topCollision==0) {
			this.y=newY;
		} else {
			this.y=topCollision;
			this.velY = 0;
		}
	}
	
	public String getImage() {
		if(facingLeft)
			return "player"+color+"_left";
		else
			return "player"+color;
	}
}
