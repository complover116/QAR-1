package com.complover116.q1r.core;


/***
*					DATA CHUNKS
* Data chunks are the actual payload of the packets
* Each data chunk contains data about a certain object
* Be it game entity data, game setting or player information
* Acks and network data are included in packet headers and are NOT in the data chunks
* The data chunks are produced by the game code and placed in a queue
* Connections pack them into packets and send them to their destination
* After unpacking they are handed over to the game code where they are processed
***/
public class NetDataChunk {
	byte data[];
	byte length;
	
	
	public NetDataChunk(byte[] data) {
		length = (byte)data.length;
		this.data = data;
	}
}
