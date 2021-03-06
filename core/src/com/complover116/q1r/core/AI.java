package com.complover116.q1r.core;

class AI {

	private static final double accuracy = 0.03;

	static void tickFor(PlayerEnt ply, double deltaT) {
		// JOIN CODE

		// RANDOM JUMPS
		if (ply.jumpsleft > 0 && Math.random() > 0.90) {
			ply.jump = true;
		}

		// RANDOM MOVEMENT
		if (Math.random() > 0.95) {
			ply.moveDir = -1;
		}
		if (Math.random() > 0.95) {
			ply.moveDir = 1;
		}

		// AIMING
		for (int i = 0; i < GameWorld.players.size(); i++) {
			
			PlayerEnt target = GameWorld.players.get(i);
			if(!target.spawned) continue;
			if(target.color != ply.color){
			double goalHeading = Math.atan2(target.x - ply.x, target.y - ply.y);
			double fireDir;
			int xvel;
			if (ply.facingLeft)
				xvel = -600;
			else
				xvel = 600;
			fireDir = Math.atan2(ply.velX + xvel, ply.velY);
			if (fireDir < goalHeading + accuracy && fireDir > goalHeading - accuracy) {
				ply.fire = true;
			}
			}
		}
	}
}
