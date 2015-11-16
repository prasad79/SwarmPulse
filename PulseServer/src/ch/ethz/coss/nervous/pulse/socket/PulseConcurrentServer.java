/*******************************************************************************
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ETH Zurich.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Contributors:
 *     Prasad Pulikal - prasad.pulikal@gess.ethz.ch  - Initial design and implementation
 *******************************************************************************/
package ch.ethz.coss.nervous.pulse.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	public PulseConcurrentServer(int port, PulseWebSocketServer pWebSocketServer, int numThreads,
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
					Log.getInstance().append(Log.FLAG_INFO, "Connection refused: server is closing");
				} else {
					Log.getInstance().append(Log.FLAG_ERROR, "Connection refused: error accepting");
				}
				success = false;
			} catch (Exception e) {
				e.printStackTrace();
				if (isStopped()) {
					Log.getInstance().append(Log.FLAG_INFO, "Connection refused: server is closing");
				} else {
					Log.getInstance().append(Log.FLAG_ERROR, "Connection refused: error accepting");
				}
				success = false;
			}
			if (success) {
				try {
					this.threadPool.execute(factory.createWorker(csocket, pWebSocketServer));
				} catch (Exception e) {
					Log.getInstance().append(Log.FLAG_ERROR, "Threadpool execution failure");
				}
			}
		}
		threadPool.shutdown();
		Log.getInstance().append(Log.FLAG_INFO, "Server threading pool is shut down");
	}

	private synchronized boolean isStopped() {
		return stopped;
	}

	public synchronized void stop() {
		stopped = true;
		try {
			ssocket.close();

		} catch (IOException e) {
			Log.getInstance().append(Log.FLAG_ERROR, "Can't close the server on port: " + String.valueOf(sport));
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
			System.out.println("Pulse Server started on ip: " + ssocket.getLocalSocketAddress() + " and port: "
					+ ssocket.getLocalPort());
		} catch (IOException e) {
			stopped = true;
			Log.getInstance().append(Log.FLAG_ERROR, "Can't open the server on port: " + String.valueOf(sport));
		}
	}

	// private synchronized void createWebSocketServer() {
	// // TODO Auto-generated method stub
	// try {
	// pWebSocketServer = new PulseWebSocketServer(webSocketPort);
	// } catch (UnknownHostException e) {
	// Log.getInstance().append(Log.FLAG_ERROR,
	// "Pulse WebSocketServer Error: "+e.getMessage());
	// }
	// }

}
