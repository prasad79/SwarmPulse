	package ch.ethz.coss.nervous.pulse.sql;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;
import ch.ethz.coss.nervous.pulse.model.LightReading;
import ch.ethz.coss.nervous.pulse.model.NoiseReading;
import ch.ethz.coss.nervous.pulse.model.TextVisual;
import ch.ethz.coss.nervous.pulse.model.Visual;
import ch.ethz.coss.nervous.pulse.socket.ConcurrentSocketWorker;
import ch.ethz.coss.nervous.pulse.utils.Log;

public class SqlUploadWorker extends ConcurrentSocketWorker {

	Connection connection;
	SqlSetup sqlse;

	public SqlUploadWorker(Socket socket, PulseWebSocketServer ps,
			Connection connection, SqlSetup sqlse) {
		super(socket, ps);
		this.connection = connection;
		this.sqlse = sqlse;
	}

	@Override
	public void run() {
		// InputStream is;
		ObjectInputStream in = null;
		try {
			 in = new ObjectInputStream(
					new BufferedInputStream(socket.getInputStream()));
			boolean connected = true;
			while (connected) {
				connected &= !socket.isClosed();

				Visual reading = null;
				JsonObject feature = null;
				try {

					reading = (Visual) in.readObject();
					feature = new JsonObject();

					feature.addProperty("type", "Feature");
					// JsonArray featureList = new JsonArray();
					// iterate through your list
					// for (ListElement obj : list) {
					// {"geometry": {"type": "Point", "coordinates":
					// [-94.149, 36.33]}
					JsonObject point = new JsonObject();
					point.addProperty("type", "Point");
					// construct a JSONArray from a string; can also use an
					// array or list
					JsonArray coord = new JsonArray();
					coord.add(new JsonPrimitive(reading.location.latnLong[0]));
					coord.add(new JsonPrimitive(reading.location.latnLong[1]));
					point.add("coordinates", coord);
					feature.add("geometry", point);
					JsonObject properties = new JsonObject();
					if (reading.type == 0) {
						System.out.println("Reading instance of light");
						properties.addProperty("readingType", "" + 0);
						properties.addProperty("level", ""
								+ ((LightReading) reading).lightVal);
					} else if (reading.type == 1) {
						properties.addProperty("readingType", "" + 1);
						properties.addProperty("level", ""
								+ ((NoiseReading) reading).soundVal);
					} else if (reading.type == 2) {
						properties.addProperty("readingType", "" + 2);
						properties.addProperty("message", ""
								+ ((TextVisual) reading).textMsg);
					} else {
						System.out.println("Reading instance not known");
					}
					feature.add("properties", properties);

				} catch (JsonParseException e) {
					System.out.println("can't save json object: "
							+ e.toString());
				}
				// output the result
				System.out.println("featureCollection=" + feature.toString());

				String message = feature.toString();
				 pSocketServer.sendToAll(message);

			}

		} catch (EOFException e) {
//			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING,
					"EOFException occurred, but ignored it for now.");
		} catch (IOException e) {
			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING,
					"Opening data stream from socket failed");
		}  catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING, "Generic error");
		} finally {
			cleanup();
			try {
				in.close();
				in = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();;
			}
		}
	}

	protected void cleanup() {
		super.cleanup();
		try {
			connection.close();
		} catch (SQLException e) {
			Log.getInstance().append(Log.FLAG_ERROR,
					" Error in closing connection.");
		}
	}
}
