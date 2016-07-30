package com.complover116.q1r.core;
import com.badlogic.gdx.Gdx;

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
	
	public static final byte ID_GAMECONFIG = 1;
	public static final byte ID_GAMESTATEUPDATE = 2;
	
	public NetDataChunk(byte[] data) {
		length = (byte)data.length;
		this.data = data;
	}
	
	public NetDataChunk() {
		
	}
	
	public static void process(NetDataChunk chunk) {
		switch(chunk.data[0]) {
			case ID_GAMECONFIG:
				//Gdx.app.log("Network", "Received ID_GAMECONFIG");
				GameConfig.onReceive(chunk);
				return;
			case ID_GAMESTATEUPDATE:
				GameStateUpdate.onReceive(chunk);
				return;
			default:
				Gdx.app.log("Network", "ERROR: Unknown data chunk ID "+chunk.data[0]+"!");
				return;
		}
	}
	
	public void onReceive() {};
	
	//All the possible chunk types are here
	public static class GameConfig extends NetDataChunk {
		public GameConfig(byte data[]) {super(data);}
		public GameConfig() {
			data = new byte[5];
			data[0] = ID_GAMECONFIG;
			data[1] = GameParams.players[0];
			data[2] = GameParams.players[1];
			data[3] = GameParams.players[2];
			data[4] = GameParams.players[3];
			length = (byte)data.length;
		}
		public static void onReceive(NetDataChunk chunk) {
			GameParams.players[0] = chunk.data[1];
			GameParams.players[1] = chunk.data[2];
			GameParams.players[2] = chunk.data[3];
			GameParams.players[3] = chunk.data[4];
		}
		
	}
	public static class GameStateUpdate extends NetDataChunk {
		public GameStateUpdate(byte data[]) {super(data);}
		public static final byte STATE_START = 1;
		public static final byte STATE_PAUSE = 2;
		public GameStateUpdate(byte state) {
			data = new byte[5];
			data[0] = ID_GAMESTATEUPDATE;
			data[1] = state;
			length = (byte)data.length;
		}
		public static void onReceive(NetDataChunk chunk) {
			switch(chunk.data[1]) {
				case 1:
					GameManager.gameStarting = true;
				break;
			}
		}
		
	}
}
