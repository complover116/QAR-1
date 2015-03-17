package com.complover116.quar1;

public class Bot extends Player{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1553929552636792635L;
	
	public Bot(int x, int y, int skin) {
		super(x, y, skin);
	}
	int time = 0;
	@Override
	public void tick() {
		super.tick();
		time ++;
		
		//SPAWN
		if(!this.joined&&this.time>200&&this.health>1) {
			this.spawn();
			this.joined = true;
			this.jump();
		}
		if(this.onGround) {
			jump();
			
		}
		if(this.time > 230) {
			this.time = 200;
			switch((int)(Math.random()*3)) {
			case 0:
				this.looksRight = true;
				this.horizMov = 1;
			break;
			case 1:
				this.looksRight = false;
				this.horizMov = -1;
			break;
			case 2:
				jump();
			break;
			}
		}
		for(int i = 0; i < 4; i ++) {
		Player target = CurGame.lvl.players.get(i);
		double goalHeading = Math.atan2(target.x - this.x, target.y - this.y);
		double fireDir;
		if(this.looksRight) {
			fireDir = Math.atan2(15+this.horizMov*speedX, this.yVel);
		} else {
			fireDir = Math.atan2(-15+this.horizMov*speedX, this.yVel);
		}
		if(fireDir<goalHeading + 0.03 && fireDir>goalHeading-0.03) {
			fire();
		}
		}
	}
	@Override
	public boolean isBot() {
		return true;
	}
	public void jump() {
		SoundHandler.playSound("/sound/effects/jump.wav");
		jumpsLeft -= 1;
		yVel = -20;
		if(!this.onGround){
			
			for(int i = 0; i < 10; i ++)
			CurGame.lvl.TADs.add(new Particle(this.x+32-Math.random()*32,this.y+32-Math.random()*32,1, getSkinColor(), 10));
		}
		onGround = false;
	}
}
