package com.complover116.q1r.core;

/***
* Since networking is happening in a separate thread,
* This class is used to collect information to send to clients
* And to hold data about the things that need to be sent
*
***/
class HostNetworking {
	//float lastx[] = new float[4];
	//float lasty[] = new float[4];
	float lastvelX[] = new float[4];
	float lastvelY[] = new float[4];
	
	
	private static long lastCalledTick = 0;
	private static float lastSentPlayerPos = 0;
	public static void tick() {
	
		float deltaT = ((float)(System.nanoTime() - lastCalledTick))/(float)1000000000;
		lastCalledTick = System.nanoTime();
		lastSentPlayerPos += deltaT;
		//Update player positions and velocities
		//TODO: Instantly send if velocity changed significantly
		
		for(byte i = 0; i < GameManager.players.size(); i ++) {
				if(GameManager.players.get(i).ent.movementUpdated) {
					NetServer.broadcast(new NetDataChunk.PlayerPosVel(i));
					GameManager.players.get(i).ent.movementUpdated = false;
				}
			}
		
		if(lastSentPlayerPos>0.2) {
			lastSentPlayerPos = 0;
			for(byte i = 0; i < GameManager.players.size(); i ++) {
				NetServer.broadcast(new NetDataChunk.PlayerPosVel(i));
			}
		}
	}
}
