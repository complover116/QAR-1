package com.complover116.q1r.core;

import java.net.*;
import com.badlogic.gdx.Gdx;

/***
*				CONNECTION
* A connection describes a known remote machine
* Data chunks are distrubuted into normal packets
* Normal packets are sent each connection tick
* Reply packets are sent instantly, ignoring the ticks
* If no packets were sent for MAX_TIME_BETWEEN_PACKETS then a dummy packet is sent
* A dummy packet is also sent if we need to ack receival, but there is no outgoing data
* If no packets were received for SOFT_TIMEOUT unimportant data will no longer be sent
* The player instance will remain in the game and if the connection is restored he will be able to play
* The connection is removed if no packets are received for HARD_TIMEOUT
* If the connection is removed the player instance is also deleted (The player will have been timed out)
***/
public class NetConnection {
	
	DatagramSocket outSock;
	InetAddress addr;
	int port;
	long lastCalledTick = 0;
	float timeSinceLastPacketSent = 1337;
	float timeSinceLastPacketReceived = 0;
	NetPacket sentPackets[] = new NetPacket[256];
	float awaitingAck[] = new float[256];
	
	byte nextPacketID = -127;
	
	void update() {
		float deltaT = ((float)(System.nanoTime() - lastCalledTick))/(float)1000000000;
		lastCalledTick = System.nanoTime();
		
		timeSinceLastPacketSent += deltaT;
		timeSinceLastPacketReceived += deltaT;
		
		
		//Resend failed packets
		for(int i = 0; i < 256; i ++) {
			if(awaitingAck[i] >= 0){
				awaitingAck[i] += deltaT;
				
			}
		}
		//Send packets in queue, if no packets in queue and time...PacketSent > 1 then send a keepalive
		
		
		if(timeSinceLastPacketSent>NetConstants.MAX_TIME_BETWEEN_PACKETS) {
			Gdx.app.log("Network", "Sending a keepalive packet to "+addr.toString()+":"+port);
			sendPacket(new NetPacket(false));
		}
		
	}
	
	public NetConnection(InetAddress addr, int port, DatagramSocket outSock) {
		this.addr = addr;
		this.port = port;
		this.outSock = outSock;
		
		lastCalledTick = System.nanoTime();
	}
	
	public void sendPacket(NetPacket packet) {
		timeSinceLastPacketSent = 0;
		if(packet.isImportant) {
			sentPackets[nextPacketID+127] = packet;
			awaitingAck[nextPacketID+127] = 0;
			packet.ID = nextPacketID;
			nextPacketID++;
		}
		try{
		outSock.send(new DatagramPacket(packet.toBytes(), 64, addr, port));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
