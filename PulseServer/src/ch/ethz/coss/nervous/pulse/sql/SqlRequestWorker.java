package ch.ethz.coss.nervous.pulse.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.coss.nervous.pulse.PulseTimeMachineRequest;
import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;
import ch.ethz.coss.nervous.pulse.socket.ConcurrentSocketWorker;
import ch.ethz.coss.nervous.pulse.socket.SqlFetchWorker;
import ch.ethz.coss.nervous.pulse.utils.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class SqlRequestWorker extends SqlFetchWorker {

	SqlSetup sqlse;
	PulseTimeMachineRequest ptmRequest;

	public SqlRequestWorker(PulseWebSocketServer ps,
			Connection connection, SqlSetup sqlse, PulseTimeMachineRequest ptmRequest) {
		super(ps, connection);
		this.sqlse = sqlse;
		this.ptmRequest = ptmRequest;
	}

	@Override
	public void run() {
		try {
			
				JsonObject feature = null;
				try {

					/***** SQL get ********/
					// Insert data
					PreparedStatement datastmt = sqlse
							.getSensorValuesFetchStatement(connection,
									ptmRequest.readingType == 0 ? 4
											: (ptmRequest.readingType == 1 ? 8 : 10), ptmRequest.startTime, ptmRequest.endTime);
					ResultSet rs = datastmt.executeQuery();
					feature = new JsonObject();
					feature.addProperty("type", "Feature");
					while (rs.next()) {

					
						String lat = rs.getString("lat");
						String lon = rs.getString("lon");

						feature = new JsonObject();
						feature.addProperty("type", "Feature");
						JsonObject point = new JsonObject();
						point.addProperty("type", "Point");
						JsonArray coord = new JsonArray();
						coord.add(new JsonPrimitive(lat));
						coord.add(new JsonPrimitive(lon));
						point.add("coordinates", coord);
						feature.add("geometry", point);

						JsonObject properties = new JsonObject();
						if (ptmRequest.readingType == 0) {
							String luxVal = rs.getString("Light");
							System.out.println("Reading instance of light");
							properties.addProperty("readingType", "" + 0);
							properties.addProperty("level",luxVal);
						} else if (ptmRequest.readingType == 1) {
							String soundVal = rs.getString("Decibel");
							properties.addProperty("readingType", "" + 1);
							properties.addProperty("level", soundVal);
						} else if (ptmRequest.readingType == 2) {
							String message = rs.getString("Message");
							properties.addProperty("readingType", "" + 2);
							properties.addProperty("message", message);
						} else {
							System.out.println("Reading instance not known");
						}
						feature.add("properties", properties);
						System.out.println(" GEOJSON = "+ feature.toString());
						pSocketServer.sendToSocket(ptmRequest.webSocket, ptmRequest.requestID, feature.toString());
					}
					/*************/

				} catch (JsonParseException e) {
					System.out.println("can't save json object: "
							+ e.toString());
				}
				

			

		
		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING, "Generic error");
		} finally {
			cleanup();
		}
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		
	}
}
