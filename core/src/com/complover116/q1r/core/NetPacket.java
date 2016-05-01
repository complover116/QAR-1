/***
*			PACKETS
* Packets are 64-byte sized 
* First 8 bytes make up the header
* 1 - the packet type (Important/unimportant, Normal Packet/Reply packet, System packet etc.)
* 2 - the packet ID (If important) 
* 3-8 - reserved for acks and other data
*
* The rest 56 bytes are data in chunks
* 
* Each chunk has a tiny header of 1 byte determining its (type/size)?
***/
public class NetPacket {
	
	boolean isImportant;
	boolean isReplyPacket;
	
	boolean isSystem;
	
	byte ID;
	
	public NetPacket(byte[] b) {
		this.isImportant = (b[0] & 1) != 0;
		this.isReplyPacket = (b[0] & 2) != 0;
		this.isSystem = (b[0] & 4) != 0;
		this.ID = b[1];
	}
	
	public byte[] toBytes() {
		byte b[] = new byte[64];
		b[0] = (byte)((this.isImportant ? 1:0) + (this.isReplyPacket ? 2:0) + (this.isSystem ? 4:0));
		b[1] = ID;
		
		return b;
	}
}
