import java.net.*;

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
	
	InetAddress addr;
	int port;
	float timeSinceLastPacketSent = 0;
	float timeSinceLastPacketReceived = 0;
	boolean sentPackets[] = new boolean[256];
	boolean receivedPackets[] = new boolean[256];
	void update(float deltaT) {
		timeSinceLastPacketSent += deltaT;
		timeSinceLastPacketReceived += deltaT;
		
		
		//Send packets in queue, if no packets in queue and time...PacketSent > 1 then send a keepalive
		
		
	}
}
