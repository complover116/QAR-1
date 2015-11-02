package com.complover116.q1r.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class PlayerEnt {

	Player ply;

	int time = 0;

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
			return Color.BLACK;
		}
	}

	float x = (float) (Math.random() * 700 + 50);
	float y = (float) (Math.random() * 500 + 50);
	float velX = 0;
	float velY = 0;

	// INPUT VARIABLES!
	// Volatile because they are accessed from network threads
	volatile byte moveDir = 0;
	volatile boolean jump = true;
	volatile boolean fire = false;

	boolean facingLeft = false;
	byte jumpsleft = 0;
	int color = 1;
	double fireDelay = 1;
	int health = 4;
	boolean inControl = true;
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
			GameWorld.ents.add(new Projectile(this.x + 16, this.y + 16, xvel + this.velX, this.velY, this.color));
		}
	}

	public void tickPhysics(double deltaT) {
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
		}
		this.jump = false;

		if (this.moveDir == 1)
			this.facingLeft = false;
		if (this.moveDir == -1)
			this.facingLeft = true;

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
	}

	public String getImage() {
		if (facingLeft)
			return "player" + color + "_left";
		else
			return "player" + color;
	}

	public PlayerEnt(int color) {
		this.color = color;
	}

	public void getHit(int amount, boolean left) {
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
			for (int i = 0; i < 50; i++) {
				GameWorld.ents.add(new Particle(x, y, 8, 4, this.color, (float) (Math.random() * 800 - 400),
						(float) (Math.random() * 800 - 100), false, true));
			}
			this.isDead = true;
			Resources.playSound("explode");
		}
	}
}
