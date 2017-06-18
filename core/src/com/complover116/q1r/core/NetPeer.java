package com.complover116.q1r.core;

import java.net.InetAddress;

public class NetPeer {
	NetPeer(InetAddress address2) {
		this.address = address2;
		timeLastHeard = System.nanoTime();
	}
	InetAddress address;
	long timeLastHeard;
	static final int STATUS_IDLE = 0;
	static final int STATUS_WILLINGTOPLAY = 1;
	static final int STATUS_JOINING_GAME = 2;
	int status = 0;
}
