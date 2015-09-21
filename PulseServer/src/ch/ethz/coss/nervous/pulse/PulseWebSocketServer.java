package ch.ethz.coss.nervous.pulse;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class PulseWebSocketServer extends WebSocketServer {
	
	private ArrayList<WebSocket> timeMachineConnectionsList = new ArrayList<WebSocket>(); 

	public PulseWebSocketServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public PulseWebSocketServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("/**************New Connection**********************/");
		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ " has joined in to received the pulse pushes!");
		System.out.println("Total Connections = "+connections().size());
		System.out.println("/************************************/");
			
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("/**************Connection Disconnected**********************/");
		System.out.println(conn + " has disconnected");
		System.out.println("Total Connections = "+connections().size());
		System.out.println("/************************************/");
		
		if(timeMachineConnectionsList.contains(conn))
			timeMachineConnectionsList.remove(conn);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("/**************New Messages**********************/");
		System.out.println(conn + ": " + message);
		System.out.println("WebSocket Connection : "+conn.toString());
		System.out.println("/************************************/");
		
		
		if(message.contains("type=")){
			int type = Integer.parseInt(message.substring(message.indexOf("=")+1));
			switch(type){
			case 0:
					timeMachineConnectionsList.remove(conn);
				System.out.println("type=0");
				break;
			case 1:	

				if(!timeMachineConnectionsList.contains(conn))
					timeMachineConnectionsList.add(conn);
				
				System.out.println("type=1");
				break;
			}
			
		}
	}

	@Override
	public void onFragment(WebSocket conn, Framedata fragment) {
		System.out.println("received fragment: " + fragment);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a
			// specific websocket
		}
	}

	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public void sendToAll(String text) {
		System.out.println("sendToAll Text - " + text);
		Collection<WebSocket> con = connections();
		synchronized (con) {
			for (WebSocket c : con) {
				if(!timeMachineConnectionsList.contains(c)){
					c.send(text);
					System.out.println("sent Text - " + text);
				}else{
					System.out.println("Not sending message to this connection because it is in time-machine state");	
				}
			
			}

		}
	}
	
	
	/**
	 * Sends data to specific Websocket connection. Used for Time-machine implementation
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public void sendToSocket(String text) {
		System.out.println("sendToAll Text - " + text);
		Collection<WebSocket> con = connections();
		synchronized (con) {
			for (WebSocket c : con) {
				c.send(text);
				System.out.println("sent Text - " + text);
			}

		}
	}
	

}