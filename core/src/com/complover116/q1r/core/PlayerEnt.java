package com.complover116.q1r.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class PlayerEnt {

	Player ply;

	float time = -1337;
	boolean spawned = false;

	public static Color colorFromID(int colorid) {
		switch (colorid) {
		case 1:
			return Color.BLUE;
		case 2:
			return Color.RED;
		case 3:
			return Color.GREEN;
		case 4:
			return Color.YELLOW;
		default:
			return Color.WHITE;
		}
	}

	volatile float x = (float) (Math.random() * 700 + 50);
	volatile float y = (float) (Math.random() * 500 + 50);
	volatile float velX = 0;
	volatile float velY = 0;
	volatile boolean movementUpdated = false;

	// INPUT VARIABLES!
	// Volatile because they are accessed from network threads
	volatile byte moveDir = 0;
	volatile byte prevMoveDir = 0;
	volatile boolean jump = true;
	volatile boolean fire = false;

	boolean facingLeft = false;
	byte jumpsleft = 0;
	int color = 1;
	double fireDelay = 1;
	int health = 4;
	volatile boolean inControl = true;
	boolean isDead = false;

	Rectangle getBB() {
		return new Rectangle(x, y, 32, 32);
	}

	void fire() {
		if (fireDelay < 0) {
			this.fireDelay = 0.5;
			Resources.playSound("firenew" + (int) (Math.random() * 3 + 1));
			int xvel = 0;
			if (this.facingLeft)
				xvel = -600;
			else
				xvel = 600;
			GameWorld.ents.add(new Projectile(this.x + 16, this.y + 16, xvel + this.velX, this.velY, this.color, ply));
		}
	}

	public void tickPhysics(double deltaT) {
	if(time != -1337) {
		if(time < 0&&time > -2) {
			for (int i = 0; i < 5; i++) {
				float dx = (float)Math.random()*400-200;
				float dy = (float)Math.random()*400-200;
				
			GameWorld.ents.add(new Particle(x+16+dx, y+16+dy, 8, 8/(0-time), this.color, -dx/(0-time),
					-dy/(0-time), false, false));
			}
		}
		if(!spawned && time > 0) {
			spawned = true;
			for (int i = 0; i < 60; i++) {
				float dx = (float)Math.random()*200-100;
				float dy = (float)Math.random()*200-100;
			GameWorld.ents.add(new Particle(x+16, y+16, 8, 8, this.color, -dx,
					-dy, false, false));
			}
		}
		
		time += deltaT;
	}
		if(!spawned&&time!=-1337) return;
		if (this.fireDelay >= 0)
			this.fireDelay = this.fireDelay - deltaT;

		if (this.fire) {
			fire();
			this.fire = false;
		}

		this.velY -= 2000 * deltaT;

		if (this.jump && this.jumpsleft > 0) {
			this.jumpsleft--;
			this.velY = 750;
			this.movementUpdated = true;
		}
		this.jump = false;
		
		if(GameManager.isHosting) {
			if(this.moveDir != this.prevMoveDir) {
				this.prevMoveDir = this.moveDir;
				this.movementUpdated = true;
			}
		}
		
		if (this.moveDir == 1)
			this.facingLeft = false;
		if (this.moveDir == -1)
			this.facingLeft = true;

		//if(!GameManager.isClient)
		if (this.inControl)
			this.velX = this.moveDir * 500;
		else
			this.velX += this.moveDir * 1000 * deltaT;

		float newY = (float) (this.y + this.velY * deltaT);
		float newX = (float) (this.x + this.velX * deltaT);

		float topCollision = 0;
		boolean moveToPlan = true;

		for (int i = 0; i < GameWorld.platforms.size(); i++) {
			if (GameWorld.platforms.get(i).bounds.overlaps(new Rectangle(this.x, newY, 32, 32))) {
				if (this.velY < 0) {
					if (topCollision < GameWorld.platforms.get(i).bounds.getY()
							+ GameWorld.platforms.get(i).bounds.getHeight()) {
						topCollision = GameWorld.platforms.get(i).bounds.getY()
								+ GameWorld.platforms.get(i).bounds.getHeight();
					}
				} else {
					if (topCollision < GameWorld.platforms.get(i).bounds.getY() - 32) {
						topCollision = GameWorld.platforms.get(i).bounds.getY() - 32;
					}
				}
			}
			if (GameWorld.platforms.get(i).bounds.overlaps(new Rectangle(newX, this.y, 32, 32))) {
				moveToPlan = false;
				if (this.velX > 0) {
					this.x = GameWorld.platforms.get(i).bounds.getX() - 32;
				} else {
					this.x = GameWorld.platforms.get(i).bounds.getX() + GameWorld.platforms.get(i).bounds.getWidth();
				}
			}
		}
		if (moveToPlan)
			this.x = newX;
		if (topCollision == 0) {
			this.y = newY;
		} else {
			if (this.velY < 0) {
				this.inControl = true;
				this.jumpsleft = 1;
			}
			this.y = topCollision;
			this.velY = 0;
		}
		if(time == -1337) {
			time = -4;
		}
		if(time > 1000) {
			for (int i = 0; i < 1; i++) {
				GameWorld.ents.add(new Particle(x+16, y+16, 8, 4, this.color, (float) (Math.random() * 800 - 400),
						(float) (Math.random() * 1000 - 400), false, true));
			}
		}
		if(time > 1000.5) {
			for (int i = 0; i < 150; i++) {
				GameWorld.ents.add(new Particle(x, y, 8, 4, this.color, (float) (Math.random() * 800 - 400),
						(float) (Math.random() * 800 - 100), false, true));
			}
			this.isDead = true;
			Resources.playSound("explode");
		}
		if(ply.streak>1) {
			GameWorld.ents.add(new Particle(x+(float)Math.random()*32, y+30, ply.streak<5 ? ply.streak : 5, 4, this.color, 0,
						(float) (Math.random() * 100), false, false));
		}
		if(y < -100) {
			getHit(1, false);
		}
	}

	public String getImage() {
		if(!spawned) {
			return "null";
		}
		if (facingLeft)
			return "player" + color + "_left";
		else
			return "player" + color;
	}

	public PlayerEnt(int color) {
		this.color = color;
	}

	public void getHit(int amount, boolean left) {
	
		if(GameManager.isHosting) {
			this.movementUpdated = true;
		}
		
		ply.streak = 0;
		Resources.playSound("hurtnew" + (int) (Math.random() * 3 + 1));
		for (int i = 0; i < 20; i++) {
			GameWorld.ents.add(new Particle(x, y, 8, 4, this.color, (float) (Math.random() * 400 - 200),
					(float) (Math.random() * 400 - 50), false, true));
		}
		this.inControl = false;
		this.velX = 400;
		if (left)
			this.velX = -400;
		this.velY = 800;
		this.health = this.health - amount;
		if (this.health <= 0) {
			this.time = 1000;
			if(true) {
				GameScreen.gameSpeed = 0.05f;
			}
		}
	}
}
