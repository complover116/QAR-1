package com.complover116.q1r.core;

import java.net.InetAddress;

public class NetPeer {
	NetPeer(InetAddress address2) {
		this.address = address2;
		timeLastHeard = System.nanoTime();
	}
	InetAddress address;
	long timeLastHeard;
	boolean readyToPlay = false;
}
