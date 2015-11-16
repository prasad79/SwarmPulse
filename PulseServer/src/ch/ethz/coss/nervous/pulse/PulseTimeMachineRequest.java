/*******************************************************************************
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ETH Zürich.
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
package ch.ethz.coss.nervous.pulse;

import org.java_websocket.WebSocket;

public class PulseTimeMachineRequest {
	public static long ID_COUNTER = 0;
	public long requestID = 0;
	public int readingType = 0;
	public long startTime = 0;
	public long endTime = 0;
	public WebSocket webSocket = null;
	public boolean isNull;

	public PulseTimeMachineRequest(boolean isNull) {
		this.isNull = isNull;
	}

	public PulseTimeMachineRequest(long requestID, int readingType, long startTime, long endTime, WebSocket webSocket) {
		this.requestID = ID_COUNTER++;
		this.readingType = readingType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.webSocket = webSocket;
	}

	public PulseTimeMachineRequest(String request, WebSocket webSocket) {
		requestID++;
		// System.out.println("String request = "+request);
		String[] tokens = request.split(",");

		this.readingType = Integer.parseInt(tokens[1]);
		this.startTime = Long.parseLong(tokens[2]);
		this.endTime = Long.parseLong(tokens[3]);
		this.webSocket = webSocket;

		print();
	}

	public void print() {
		// System.out.println("/*************PulseTimeMachineRequest*******************/");
		// System.out.println("Request ID - "+requestID);
		// System.out.println("ReadingType - "+readingType);
		// System.out.println("StartTime - "+startTime);
		// System.out.println("EndTime - "+endTime);
		// System.out.println("/*******************************************************/");
	}

}