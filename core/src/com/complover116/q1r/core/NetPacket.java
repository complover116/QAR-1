package com.complover116.q1r.core;

import java.util.ArrayList;
/***
*			PACKETS
* Packets are 64-byte sized 
* First 8 bytes make up the header
* 1 - the packet type (Important/unimportant, Normal Packet/Reply packet, System packet, Acks contained, etc)
* 2 - the packet ID (If important) 
* 3-4 - packet acks
* 5 - amount of data chunks
* 6-8 - reserved for other data
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
	
	byte remainingSpace = 56;
	
	ArrayList<NetDataChunk> payload = new ArrayList<NetDataChunk>();
	
	public NetPacket(byte[] b) {
		this.isImportant = (b[0] & 1) != 0;
		this.isReplyPacket = (b[0] & 2) != 0;
		this.isSystem = (b[0] & 4) != 0;
		this.hasAck1 = (b[0] & 8) != 0;
		this.hasAck2 = (b[0] & 16) != 0;
		this.ID = b[1];
		this.ack1 = b[2];
		this.ack2 = b[3];
		
		int i = 8;
		//payload = new NetDataChunk[b[4]];
		for(int k = 0; k < b[4]; k++) {
			
			byte data[] = new byte[i];
			for(int j = 0; j < b[i]; j++) {
				data[j] = b[j+i+1];
			}
			i = i+b[i]+1;
			payload.add(new NetDataChunk(data));
		}
	}
	
	public NetPacket(boolean important) {
		this.isImportant = important;
	}
	
	public boolean pack(NetDataChunk chunk) {
		if(chunk.length >= remainingSpace) return false;
		this.payload.add(chunk);
		this.remainingSpace -= chunk.length+1;
		return true;
	}
	
	public byte[] toBytes() {
		byte b[] = new byte[64];
		b[0] = (byte)((this.isImportant ? 1:0) + (this.isReplyPacket ? 2:0) + (this.isSystem ? 4:0) + (this.hasAck1 ? 8:0) + (this.hasAck2 ? 16:0));
		b[1] = ID;
		b[2] = ack1;
		b[3] = ack2;
		b[4] = (byte)payload.size();
		
		int pos = 8;
		for(int i = 0; i < payload.size(); i ++) {
			b[pos] = payload.get(i).length;
			for(int j = 0; j < payload.get(i).length; j ++) {
				b[pos+1+j] = payload.get(i).data[j];
			}
			pos += payload.get(i).length + 1;
		}
		return b;
	}
}
