package ch.ethz.coss.nervous.pulse;

import java.io.IOException;

import org.java_websocket.WebSocketImpl;

import ch.ethz.coss.nervous.pulse.socket.PulseConcurrentServer;
import ch.ethz.coss.nervous.pulse.sql.SqlConnection;
import ch.ethz.coss.nervous.pulse.sql.SqlSetup;
import ch.ethz.coss.nervous.pulse.sql.SqlUploadWorkerFactory;
import ch.ethz.coss.nervous.pulse.utils.Log;

public class PulseServer {

	public static void main(String[] args) throws InterruptedException,
			IOException {
		WebSocketImpl.DEBUG = true;

		// Load configuration from custom path or current directory
		if (args.length > 0) {
			config = Configuration.getInstance(args[0]);
		} else {
			config = Configuration.getInstance();
		}

		// Set up logging
		Log log = Log.getInstance(config.getLogDisplayVerbosity(),
				config.getLogWriteVerbosity(), config.getLogPath());
		log.append(Log.FLAG_INFO, "Reading configuration file done");

		log.append(Log.FLAG_INFO, "Reading configuration file done");
		// Set up SQL connection
		SqlConnection sqlco = new SqlConnection(config.getSqlHostname(),
				config.getSqlUsername(), config.getSqlPassword(),
				config.getSqlPort(), config.getSqlDatabase());
		log.append(Log.FLAG_INFO,
				"Establishing connection to SQL database done");

		// Set up SQL tables
		SqlSetup sqlse = new SqlSetup(sqlco.getConnection(), config);
		sqlse.setupTables();

		// Create factory which creates workers for uploading to the SQL
		// database
		SqlUploadWorkerFactory factory = new SqlUploadWorkerFactory(sqlco,
				sqlse);

		PulseWebSocketServer pWebSocketServer = new PulseWebSocketServer(8446);
		pWebSocketServer.start();
		// Start server
		PulseConcurrentServer server = new PulseConcurrentServer(8445,
				pWebSocketServer, config.getServerThreads(), factory);
		Thread serverThread = new Thread(server);
		serverThread.start();
		log.append(Log.FLAG_INFO, "PulseConcurrentServer Started");

		boolean running = true;

		while (running) {
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				log.append(Log.FLAG_WARNING, "Server execution interrupted");
				running = false;
			}
		}

		// Stop server
		server.stop();

		log.append(Log.FLAG_INFO, "Server terminated");

	}

	public static int smartphonesPort = 8445;
	public static Configuration config;
}