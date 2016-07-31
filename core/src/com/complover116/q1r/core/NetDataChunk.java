package com.complover116.q1r.core;
import com.badlogic.gdx.Gdx;
import java.nio.ByteBuffer;

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
	public static final byte ID_PLAYERPOSVEL = 3;
	public static final byte ID_FIREEVENT = 4;
	
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
			case ID_PLAYERPOSVEL:
				PlayerPosVel.onReceive(chunk);
				return;
			case ID_FIREEVENT:
				FireEvent.onReceive(chunk);
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
			data = new byte[2];
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
	
	public static class PlayerPosVel extends NetDataChunk {
		/*
		* 1 - Player id (1-4)
		* 2-5 - player x
		* 6-9 - player y
		* 10-13 - player velx
		* 14-17 - player vely
		* 18 - movDir
		* 19 bytes!
		*/
		public PlayerPosVel(byte data[]) {super(data);}
		public PlayerPosVel(byte id) {
			data = new byte[20];
			ByteBuffer.wrap(data).put(ID_PLAYERPOSVEL)
			.put(id).putFloat(GameManager.players.get(id).ent.x)
			.putFloat(GameManager.players.get(id).ent.y)
			.putFloat(GameManager.players.get(id).ent.velX)
			.putFloat(GameManager.players.get(id).ent.velY)
			.put(GameManager.players.get(id).ent.moveDir)
			.put(GameManager.players.get(id).ent.inControl ? (byte)1 : (byte)0);
			length = (byte)data.length;
		}
		public static void onReceive(NetDataChunk chunk) {
			ByteBuffer in = ByteBuffer.wrap(chunk.data);
			//Skip the packet ID
			in.get();
			byte id = in.get();
			if(GameManager.players.size()<=id) return;
			if(GameManager.players.get(id).ent != null) {
				GameManager.players.get(id).ent.x = in.getFloat();
				GameManager.players.get(id).ent.y = in.getFloat();
				GameManager.players.get(id).ent.velX = in.getFloat();
				GameManager.players.get(id).ent.velY = in.getFloat();
				GameManager.players.get(id).ent.moveDir = in.get();
				GameManager.players.get(id).ent.inControl = in.get()==1;
			}
		}
		
		
	}
	
	public static class FireEvent extends NetDataChunk {
		public FireEvent(byte data[]) {super(data);}
		public FireEvent(byte player) {
			data = new byte[2];
			ByteBuffer.wrap(data).put(ID_FIREEVENT)
			.put(player);
			length = (byte)data.length;
		}
		public static void onReceive(NetDataChunk chunk) {
			//Gdx.app.log("Network", "Fire Event received");
			byte player = chunk.data[1];
			GameManager.players.get(player).ent.fire();
		}
		
	}
	
}
