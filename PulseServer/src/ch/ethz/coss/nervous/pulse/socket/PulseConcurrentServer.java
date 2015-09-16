package ch.ethz.coss.nervous.pulse.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;
import ch.ethz.coss.nervous.pulse.utils.Log;

public class PulseConcurrentServer implements Runnable {

	private int sport = 0;
	private int webSocketPort = 0;
	private ServerSocket ssocket = null;
	private PulseWebSocketServer pWebSocketServer = null;
	private boolean stopped = false;
	private Thread runThread = null;
	private ExecutorService threadPool;
	private ConcurrentSocketWorkerFactory factory;

	public PulseConcurrentServer(int port,
			PulseWebSocketServer pWebSocketServer, int numThreads,
			ConcurrentSocketWorkerFactory factory) {
		this.sport = port;
		this.pWebSocketServer = pWebSocketServer;
		this.threadPool = Executors.newFixedThreadPool(numThreads);
		this.factory = factory;
	}

	@Override
	public void run() {
		synchronized (this) {
			runThread = Thread.currentThread();
		}

		createSocket();
		// createWebSocketServer();
		// pWebSocketServer.start();

		while (!isStopped()) {
			boolean success = false;
			Socket csocket = null;
			try {
				System.out.println("Before listening");
				csocket = ssocket.accept();
				success = true;
			} catch (IOException e) {
				if (isStopped()) {
					Log.getInstance().append(Log.FLAG_INFO,
							"Connection refused: server is closing");
				} else {
					Log.getInstance().append(Log.FLAG_ERROR,
							"Connection refused: error accepting");
				}
				success = false;
			} catch (Exception e) {
				e.printStackTrace();
				if (isStopped()) {
					Log.getInstance().append(Log.FLAG_INFO,
							"Connection refused: server is closing");
				} else {
					Log.getInstance().append(Log.FLAG_ERROR,
							"Connection refused: error accepting");
				}
				success = false;
			}
			if (success) {
				try {
					this.threadPool.execute(factory.createWorker(csocket,
							pWebSocketServer));
				} catch (Exception e) {
					Log.getInstance().append(Log.FLAG_ERROR,
							"Threadpool execution failure");
				}
			}
		}
		threadPool.shutdown();
		Log.getInstance().append(Log.FLAG_INFO,
				"Server threading pool is shut down");
	}

	private synchronized boolean isStopped() {
		return stopped;
	}

	public synchronized void stop() {
		stopped = true;
		try {
			ssocket.close();

		} catch (IOException e) {
			Log.getInstance().append(Log.FLAG_ERROR,
					"Can't close the server on port: " + String.valueOf(sport));
		}

		// try {
		// pWebSocketServer.stop();
		// } catch (IOException | InterruptedException e) {
		// Log.getInstance().append(Log.FLAG_ERROR,
		// "Can't close the WebSocketServer");
		// }
	}

	private synchronized void createSocket() {
		try {
			ssocket = new ServerSocket(sport);
			System.out.println("Socket port = " + sport);
			System.out.println("Pulse Server started on ip: "
					+ ssocket.getLocalSocketAddress() + " and port: "
					+ ssocket.getLocalPort());
		} catch (IOException e) {
			stopped = true;
			Log.getInstance().append(Log.FLAG_ERROR,
					"Can't open the server on port: " + String.valueOf(sport));
		}
	}

	// private synchronized void createWebSocketServer() {
	// // TODO Auto-generated method stub
	// try {
	// pWebSocketServer = new PulseWebSocketServer(webSocketPort);
	// } catch (UnknownHostException e) {
	// Log.getInstance().append(Log.FLAG_ERROR,
	// "Pulse WebSocketServer Error:  "+e.getMessage());
	// }
	// }

}
