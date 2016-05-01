package com.complover116.q1r.core;

import java.net.*;
import java.util.ArrayList;
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
	
	ArrayList<Byte> acksToSend = new ArrayList<Byte>();
	
	boolean dead = false;
	
	byte nextPacketID = -128;
	
	void update() {
		if(dead)return;
		float deltaT = ((float)(System.nanoTime() - lastCalledTick))/(float)1000000000;
		lastCalledTick = System.nanoTime();
		
		timeSinceLastPacketSent += deltaT;
		timeSinceLastPacketReceived += deltaT;
		
		
		//Resend failed packets
		for(int i = 0; i < 256; i ++) {
			if(awaitingAck[i] >= 0){
				awaitingAck[i] += deltaT;
				if(awaitingAck[i]>NetConstants.PACKET_ACK_TIMEOUT) {
					awaitingAck[i] = -1;
					Gdx.app.log("Network", "No ack received for important packet "+i+", resending");
					sendPacket(sentPackets[i]);
				}
			}
		}
		//Send packets in queue, if no packets in queue and time...PacketSent > 1 then send a keepalive
		
		
		if(timeSinceLastPacketSent>NetConstants.MAX_TIME_BETWEEN_PACKETS) {
			Gdx.app.debug("Network", "Sending a keepalive packet to "+addr.toString()+":"+port);
			sendPacket(new NetPacket(false));
		}
		
		if(timeSinceLastPacketReceived>NetConstants.HARD_TIMEOUT) {
			Gdx.app.log("Network", "Connection to "+addr.toString()+":"+port+" timed out");
			dead = true;
		}
		
	}
	
	public NetConnection(InetAddress addr, int port, DatagramSocket outSock) {
		this.addr = addr;
		this.port = port;
		this.outSock = outSock;
		
		lastCalledTick = System.nanoTime();
		for(int i = 0; i < 256; i ++) {
			awaitingAck[i] = -1;
		}
	}
	
	void process(byte[] buf) {
		NetPacket packet = new NetPacket(buf);
		if(packet.isImportant) {
		    Gdx.app.debug("Network", "Importand packet received, will send ack for "+(packet.ID+128));
			acksToSend.add(packet.ID);
		}
		if(packet.hasAck1) {
			awaitingAck[packet.ack1+128] = -1;
			//Gdx.app.log("Network", "Received ack for "+(packet.ack1+128));
		}
		if(packet.hasAck2) {
			awaitingAck[packet.ack2+128] = -1;
		}
		//Then split the data into chunks
	}
	
	public void sendPacket(NetPacket packet) {
		timeSinceLastPacketSent = 0;
		if(packet.isImportant) {
			sentPackets[nextPacketID+128] = packet;
			awaitingAck[nextPacketID+128] = 0;
			packet.ID = nextPacketID;
			nextPacketID++;
		}
		for(int i = 0; i < acksToSend.size(); i ++) {
			if(!packet.hasAck1) {
				packet.hasAck1 = true;
				packet.ack1 = acksToSend.get(i);
				//Gdx.app.log("Network", "Attaching ack for "+(acksToSend.get(i)+128));
				acksToSend.remove(i);
				continue;
			}
			if(!packet.hasAck2) {
				packet.hasAck2 = true;
				packet.ack2 = acksToSend.get(i);
				acksToSend.remove(i);
				continue;
			}
		}
		try{
		outSock.send(new DatagramPacket(packet.toBytes(), 64, addr, port));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
