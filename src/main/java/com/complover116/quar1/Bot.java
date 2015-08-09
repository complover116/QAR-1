package com.complover116.quar1;

public class Bot extends Player{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1553929552636792635L;
	
	public Bot(int x, int y, int skin) {
		super(x, y, skin);
		if(Config.botDifficulty>=3){
			accuracy = 0.03;
		} else {
			accuracy = 0.5;
		}
	}
	int time = 0;
	
	//Bot's margin of shot:
	double accuracy;
	
	
	@Override
	public void tick() {
		super.tick();
		if(!Loader.isServer) return;
		time ++;
		if(this.health < 0) {
			this.x = -100;
			this.y = -10000;
			return;
		}
		//SPAWN
		if(!this.joined&&this.health>1) {
			this.spawn();
			this.joined = true;
			this.jump();
		}
		
		//AI
		
		//RANDOM JUMPS
		if(Config.botDifficulty<6&&this.onGround&&Math.random()>0.99) {
			
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
		
		// AIMING CODE
		if(Config.botDifficulty>1)
		for(int i = 0; i < 4; i ++) {
		Player target = CurGame.lvl.players.get(i);
		double goalHeading = Math.atan2(target.x - this.x, target.y - this.y);
		double fireDir;
		if(this.looksRight) {
			fireDir = Math.atan2(15+this.horizMov*speedX, this.yVel);
		} else {
			fireDir = Math.atan2(-15+this.horizMov*speedX, this.yVel);
		}
		if(fireDir<goalHeading + accuracy && fireDir>goalHeading-accuracy) {
			fire();
		}
		}
		else {
			if(Math.random()>0.95) {
				fire();
			}
		}
	}
	@Override
	public boolean isBot() {
		return true;
	}
	public void jump() {
		if(this.jumpsLeft < 1){return;}
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
