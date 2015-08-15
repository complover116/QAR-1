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
	volatile boolean jump = true;
	volatile boolean fire = false;
	
	boolean facingLeft = false;
	byte jumpsleft = 0;
	int color = 1;
	double fireDelay = 1;
	
	Rectangle getBB() {
		return new Rectangle(x,y,32,32);
	}
	
	void fire () {
		if(fireDelay < 0) {
			int xvel = 0;
			if(this.facingLeft)
				xvel = -600;
			else
				xvel = 600;
			GameWorld.ents.add(new Projectile(this.x + 16, this.y + 16, xvel+this.velX, this.velY));
		}
	}
	public void tickPhysics(double deltaT) {
		if(this.fireDelay>=0)
		this.fireDelay = this.fireDelay - deltaT;
		
		if(this.fire) {
			fire();
			this.fire = false;
		}
		
		this.velY -= 2000*deltaT;
		
		if(this.jump&&this.jumpsleft>0) {
			this.jumpsleft --;
			this.velY = 750;
		}
		this.jump = false;
		
		if(this.moveDir==1)this.facingLeft = false;
		if(this.moveDir==-1)this.facingLeft = true;
		
		this.velX = this.moveDir*500;
		
		
		
		float newY = (float) (this.y + this.velY*deltaT);
		float newX = (float) (this.x + this.velX*deltaT);
		
		float topCollision = 0;
		boolean moveToPlan = true;
		
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
			//TODO;Fix weird teleporting bug
			//Caused by checking against old y
			if(GameWorld.platforms.get(i).bounds.overlaps(new Rectangle(newX,this.y,32,32))) {
				moveToPlan = false;
				if(this.velX>0){
					this.x = GameWorld.platforms.get(i).bounds.getX()-32;
				} else {
					this.x = GameWorld.platforms.get(i).bounds.getX()+GameWorld.platforms.get(i).bounds.getWidth();
				}
			}
		}
		if(moveToPlan) this.x = newX;
		if(topCollision==0) {
			this.y=newY;
		} else {
			if(this.velY<0) {
				this.jumpsleft = 1;
			}
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
	
	public PlayerEnt(int color) {
		this.color = color;
	}
}
