package ch.ethz.coss.nervous.pulse.socket;

import java.sql.Connection;
import java.sql.SQLException;

import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;
import ch.ethz.coss.nervous.pulse.utils.Log;

public abstract class SqlFetchWorker implements Runnable {
	@Override
	public abstract void run();

	protected PulseWebSocketServer pSocketServer;
	protected Connection connection;

	protected SqlFetchWorker(
			PulseWebSocketServer pSocketServer, Connection connection) {
		this.pSocketServer = pSocketServer;
		this.connection = connection;
	}

	
	protected void cleanup() {
		try {
			connection.close();
		} catch (SQLException e) {
			Log.getInstance().append(Log.FLAG_ERROR,
					" Error in closing sql connection.");
		}
	}

}
