package ch.ethz.coss.nervous.pulse;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import android.os.AsyncTask;

public class SynchWriter {
	
	
	ArrayList<Object> data = new ArrayList<Object>();
	OutputTask outTask = new OutputTask();
	String ipAddress;
	int port;
	boolean printTrace = false;

	public SynchWriter(String ipAddress, int port, int writingInterval)
			throws IOException {
		this.port = port;
		this.ipAddress = ipAddress;
	}

	public void send(Object o) {
		synchronized (data) {
			data.add(o);
		}

		new OutputTask().execute();
	}

	class OutputTask extends AsyncTask {

		ObjectOutputStream oos;

		private synchronized ObjectOutputStream getObjectOutputStream() {

			if (oos == null) {
				try {
					System.out.println("Before Writing to server at "
							+ ipAddress + ":" + port);
					@SuppressWarnings("resource")
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ipAddress, port), 1000);
					// System.out.println("Writing to server");
					OutputStream os = new BufferedOutputStream(
							socket.getOutputStream());
					oos = new ObjectOutputStream(os);
				} catch (Exception e) {
					System.out.println("Exception thrown here "
							+ e.getMessage());
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
				// System.out.println("oos is null");
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
			ArrayList<Object> buffer = null;

			synchronized (data) {
				buffer = (ArrayList<Object>) data.clone();
				data.clear();
			}

			 oos = getObjectOutputStream();

			if (oos == null)
				return false;

			try {
				for (Object o : buffer) {
					oos.writeObject(o);
				}
//				oos.flush();
			} catch (Exception e) {
				if (printTrace)
					e.printStackTrace();
				try {
					oos.close();
				} catch (Exception f) {
					if (printTrace)
						f.printStackTrace();
				} finally {
					oos = null;
					return false;
				}
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
