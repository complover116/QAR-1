package com.complover116.quar1;

import java.net.InetAddress;

public class Config {
	public static int netTick = 0;
	public static InetAddress server;
	public static String address = "localhost";
	public static final String version = "v1.6";
	
	/***
	 * Difficulty:
	 * -1 - No AI;
	 * 0 - Random dumbfire AI - moves around randomly;
	 * 1 - AI has a fire direction preference, but does not actually aim;
	 * 2 - AI starts to move a bit better and attempts to face the player;
	 * 3 - AI becomes very accurate. Previously the only level available.
	 * 4 - AI movement is not random anymore, now attempts to evade projectiles;
	 * 5 - Maximum difficulty: AI deliberately hunts the player, following him around and aiming perfectly;
	 */
	public static int botDifficulty = -1;
}
