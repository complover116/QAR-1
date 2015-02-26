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
		double targetX = 100;
		double targetY = 100;
		//SPAWN
		if(!this.joined&&this.time>200) {
			this.spawn();
			this.joined = true;
			this.jump();
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
