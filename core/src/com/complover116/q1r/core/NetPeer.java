package com.complover116.q1r.core;

import java.net.InetAddress;

public class NetPeer {
	public NetPeer(InetAddress address2) {
		this.address = address2;
		timeLastHeard = System.nanoTime();
	}
	public InetAddress address;
	public long timeLastHeard;
}
