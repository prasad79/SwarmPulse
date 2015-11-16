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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import ch.ethz.coss.nervous.pulse.model.Visual;
import ch.ethz.coss.nervous.pulse.utils.Utils;
import flexjson.JSONSerializer;

public class SynchWriter {

	ArrayList<Object> data = new ArrayList<Object>();
	OutputTask outTask = new OutputTask();
	String ipAddress;
	int port;
	boolean printTrace = true;

	public SynchWriter(String ipAddress, int port, int writingInterval) throws IOException {
		this.port = port;
		this.ipAddress = ipAddress;
	}

	Context mContext = null;

	public void send(Object o, Context context) {
		mContext = context;
		synchronized (data) {
			data.add(o);
		}

		new OutputTask().execute();
	}

	class OutputTask extends AsyncTask<Object, Object, Object> {

		DataOutputStream oos;
		boolean exceptionFlag;

		private synchronized DataOutputStream getObjectOutputStream() {

			if (oos == null) {
				try {
					// System.out.println("Before Writing to server at "
					// + ipAddress + ":" + port);
					@SuppressWarnings("resource")
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ipAddress, port), 1000);
					// //System.out.println("Writing to server");
					OutputStream os = new BufferedOutputStream(socket.getOutputStream());
					oos = new DataOutputStream(os);
				} catch (Exception e) {
					System.out.println("Exception thrown here " + e.getMessage());
					exceptionFlag = true;

					e.printStackTrace();
					if (printTrace)
						e.printStackTrace();
					try {
						if (oos != null) {
							oos.close();
						}
					} catch (Exception f) {
						if (printTrace)
							f.printStackTrace();
					} finally {
						oos = null;
					}
				}
			} else {
				// //System.out.println("oos is null");
			}
			return oos;
		}

		synchronized void stop() {
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				if (printTrace)
					e.printStackTrace();
			}
		}

		@SuppressWarnings("finally")
		@Override
		protected Boolean doInBackground(Object... params) {
			ArrayList<Visual> buffer = null;

			synchronized (data) {
				buffer = (ArrayList<Visual>) data.clone();
				data.clear();
			}

			oos = getObjectOutputStream();

			if (oos == null)
				return false;

			try {
				for (Visual o : buffer) {
					String json = new JSONSerializer().deepSerialize(o);

					// System.out.println("SENDING JSON -- " + json);
					byte[] jsonBytes = json.getBytes();
					oos.write(jsonBytes, 0, jsonBytes.length);

					// oos.writeUTF(json);

				}

				// oos.flush();
			} catch (Exception e) {
				exceptionFlag = true;
				if (printTrace)
					e.printStackTrace();

			} finally {
				try {
					oos.close();
				} catch (Exception f) {
					if (printTrace)
						f.printStackTrace();
				} finally {
					oos = null;
				}
			}
			return true;
		}

		protected void onPostExecute(Object obj) {
			Utils.dismissProgress();

			if (!Constants.DUMMY_DATA_COLLECT) {

				if (exceptionFlag)
					Toast.makeText(mContext,
							"There was a problem sharing the data. Please check your internte connection or try again later.",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(mContext, "The data has been shared succesfully.", Toast.LENGTH_SHORT).show();
			}

		}
	}

	public void stop() {
		outTask.stop();
	}

	public void setServerAddress(String address) {
		this.ipAddress = address;
	}

	public void setServerPort(int port) {
		this.port = port;
	}

}
