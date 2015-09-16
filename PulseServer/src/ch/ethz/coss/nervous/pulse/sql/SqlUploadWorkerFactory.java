package ch.ethz.coss.nervous.pulse.sql;

import java.net.Socket;

import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;
import ch.ethz.coss.nervous.pulse.socket.ConcurrentSocketWorker;
import ch.ethz.coss.nervous.pulse.socket.ConcurrentSocketWorkerFactory;

public class SqlUploadWorkerFactory extends ConcurrentSocketWorkerFactory {

	SqlConnection sqlco;
	SqlSetup sqlse;

	public SqlUploadWorkerFactory(SqlConnection sqlco, SqlSetup sqlse) {
		this.sqlco = sqlco;
		this.sqlse = sqlse;
	}

	@Override
	public ConcurrentSocketWorker createWorker(Socket socket,
			PulseWebSocketServer pSocketServer) {
		SqlUploadWorker suwo = new SqlUploadWorker(socket, pSocketServer,
				sqlco.getConnection(), sqlse);
		return suwo;
	}

}
