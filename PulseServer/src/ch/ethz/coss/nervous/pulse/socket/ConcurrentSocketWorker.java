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
 ********************************************************************************/
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

	protected ConcurrentSocketWorker(Socket socket, PulseWebSocketServer pSocketServer) {
		this.socket = socket;
		this.pSocketServer = pSocketServer;
	}

	protected void cleanup() {
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {

			Log.getInstance().append(Log.FLAG_ERROR, " Error in Socket connection.");
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
