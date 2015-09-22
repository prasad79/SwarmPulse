package ch.ethz.coss.nervous.pulse.socket;

import java.io.IOException;
import java.net.Socket;

import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;
import ch.ethz.coss.nervous.pulse.utils.Log;

public abstract class ConcurrentSocketWorker implements Runnable {
	@Override
	public abstract void run();

	protected Socket socket;
	protected PulseWebSocketServer pSocketServer;

	protected ConcurrentSocketWorker(Socket socket,
			PulseWebSocketServer pSocketServer) {
		this.socket = socket;
		this.pSocketServer = pSocketServer;
	}

	protected void cleanup() {
		try {
			if(socket != null)
			socket.close();
		} catch (IOException e) {

			Log.getInstance().append(Log.FLAG_ERROR,
					" Error in Socket connection.");
		}

		// try {
		// pSocketServer.stop();
		// } catch (IOException | InterruptedException e) {
		//
		// Log.getInstance().append(Log.FLAG_ERROR,
		// " Error in stopping Pulse WebSocketServer.");
		// }
	}
}
