import java.net.*;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

/***					NETSERVER
* The netserver contains the data that a server must know to function
* The data there is primarily used by the server thread
* Each cycle the server does the following:
*
* 1) Check for incoming packets
*	a) If there is a packet from a known source the time...LastReceived becomes 0
*	   Acks are read and the data gets split into chunks and processed
*	b) If there is a packet from an unknown source
*		- If it's a connection request - create a connection and reply
*		- If not - tell the client that he is not connnected (He probably timed out)
*	c) If there are no packets for 10 ms, skip this step
* 2) Tick the connections
* 3) Sort through outbound data, split it into packets and send each packet to a connection
***/
class NetServer {
	static DatagramSocket sock;
	
	static ArrayList<NetConnection> clients = new ArrayList<NetConnection>();
	
	static volatile boolean serverRunning = true;
	
	
	public static class NetServerThread implements Runnable {
		@Override
		public void run() {
			Gdx.app.log("Network", "Server initializing...");
			try {
			sock = new DatagramSocket(26655);
			sock.setSoTimeout(10);
			} catch (SocketException e) {
				e.printStackTrace();
				return;
			}
			
			byte[] buf = new byte[256];
			DatagramPacket dataIn = new DatagramPacket(buf, buf.length);
			Gdx.app.log("Network", "Server started!");
			while(NetServer.serverRunning) {
				try{
					sock.receive(dataIn);
					boolean isFromClient = false;
					for(int i = 0; i < clients.size(); i++) {
						if(clients.get(i).addr.equals(dataIn.getAddress()) && clients.get(i).port == dataIn.getPort()) {
							clients.get(i).timeSinceLastPacketReceived = 0;
							isFromClient = true;
							break;
						}
					}
					
					if(!isFromClient) {
						//Check if this is a connection request
					}
					
					
					
				} catch (SocketTimeoutException e) {
					
				} catch (Exception e) {
					e.printStackTrace();
					NetServer.serverRunning = false;
				}
				
				
			}
			Gdx.app.log("Network", "Server shutting down...");
			
			sock.close();
			Gdx.app.log("Network", "Server is offline");
		}
	}
}
