package ch.ethz.coss.nervous.pulse;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class PulseWebSocketServer extends WebSocketServer {

	public PulseWebSocketServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public PulseWebSocketServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		this.sendToAll("new connection: " + handshake.getResourceDescriptor());
		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ " has joined in to received the pulse pushes!");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		this.sendToAll(conn + " has disconnected!");
		System.out.println(conn + " has disconnected");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		this.sendToAll(message);
		System.out.println(conn + ": " + message);
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
				c.send(text);
				System.out.println("sent Text - " + text);
			}

		}
	}

}