package ch.ethz.coss.nervous.pulse.socket;

import java.sql.Connection;

import ch.ethz.coss.nervous.pulse.PulseTimeMachineRequest;
import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;
import ch.ethz.coss.nervous.pulse.sql.SqlConnection;
import ch.ethz.coss.nervous.pulse.sql.SqlRequestWorker;
import ch.ethz.coss.nervous.pulse.sql.SqlSetup;

public class SqlFetchWorkerFactory {
	
	SqlConnection sqlco;
	SqlSetup sqlse;
	PulseTimeMachineRequest ptmRequest;

	public SqlFetchWorkerFactory(SqlConnection sqlco, SqlSetup sqlse) {
		this.sqlco = sqlco;
		this.sqlse = sqlse;
	}

	public SqlFetchWorker createWorker(PulseWebSocketServer pSocketServer, PulseTimeMachineRequest ptmRequest){
		SqlRequestWorker srWorker = new SqlRequestWorker(pSocketServer,
				sqlco.getConnection(), sqlse, ptmRequest);
		return srWorker;
	}
}
