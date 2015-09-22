package ch.ethz.coss.nervous.pulse.socket;

import java.net.Socket;

import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;

public abstract class ConcurrentSocketWorkerFactory {

	public abstract ConcurrentSocketWorker createWorker(Socket socket,
			PulseWebSocketServer pSocketServer);
	
}
