package com.complover116.q1r.core;

/***
*			PACKETS
* Packets are 64-byte sized 
* First 8 bytes make up the header
* 1 - the packet type (Important/unimportant, Normal Packet/Reply packet, System packet, Acks contained, etc)
* 2 - the packet ID (If important) 
* 3-4 - packet acks
* 5-8 - reserved for other data
*
* The rest 56 bytes contain payload data in chunks
* 
* Each chunk has a tiny header of 1 byte determining its size?
***/
public class NetPacket {
	
	boolean isImportant;
	boolean isReplyPacket;
	
	boolean isSystem;
	
	boolean hasAck1, hasAck2;
	
	byte ID, ack1, ack2;
	
	public NetPacket(byte[] b) {
		this.isImportant = (b[0] & 1) != 0;
		this.isReplyPacket = (b[0] & 2) != 0;
		this.isSystem = (b[0] & 4) != 0;
		this.hasAck1 = (b[0] & 8) != 0;
		this.hasAck2 = (b[0] & 16) != 0;
		this.ID = b[1];
		this.ack1 = b[2];
		this.ack2 = b[3];
	}
	
	public NetPacket(boolean important) {
		this.isImportant = important;
	}
	
	public byte[] toBytes() {
		byte b[] = new byte[64];
		b[0] = (byte)((this.isImportant ? 1:0) + (this.isReplyPacket ? 2:0) + (this.isSystem ? 4:0) + (this.hasAck1 ? 8:0) + (this.hasAck2 ? 16:0));
		b[1] = ID;
		b[2] = ack1;
		b[3] = ack2;
		return b;
	}
}
