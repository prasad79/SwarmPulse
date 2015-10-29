package ch.ethz.coss.nervous.pulse.sql;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;
import ch.ethz.coss.nervous.pulse.model.LightReading;
import ch.ethz.coss.nervous.pulse.model.NoiseReading;
import ch.ethz.coss.nervous.pulse.model.TextVisual;
import ch.ethz.coss.nervous.pulse.model.Visual;
import ch.ethz.coss.nervous.pulse.socket.ConcurrentSocketWorker;
import ch.ethz.coss.nervous.pulse.utils.Log;
import flexjson.JSONDeserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class SqlUploadWorker extends ConcurrentSocketWorker {

	Connection connection;
	SqlSetup sqlse;

	public SqlUploadWorker(Socket socket, PulseWebSocketServer ps,
			Connection connection, SqlSetup sqlse) {
		super(socket, ps);
		this.connection = connection;
		this.sqlse = sqlse;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// InputStream is;
		DataInputStream in = null;
		try {
			in = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			boolean connected = true;
			while (connected) {
				connected &= !socket.isClosed();
				Visual reading = null;
				JsonObject featureCollection = new JsonObject();
				JsonArray features = new JsonArray();
				JsonObject feature = null;
				try {
//					String json =  in.readUTF();
					
					StringBuffer json = new StringBuffer();
		            String tmp; 
		        	try {
		            while ((tmp = in.readLine()) != null) {
		            	json.append(tmp);
		            }
		            
		            
		            //use inputLine.toString(); here it would have whole source
		            in.close();
		        	   } catch (MalformedURLException me) {
		                   System.out.println("MalformedURLException: " + me);
		               } catch (IOException ioe) {
		                   System.out.println("IOException: " + ioe);
		               }
					System.out.println("JSON STRING = "+json);
					System.out.println("JSON Length = "+json.length());
					reading = new JSONDeserializer<Visual>().deserialize(new String(json), Visual.class);       
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
						//System.out.println("Reading instance of light");
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
						//System.out.println("Reading instance not known");
					}
					properties.addProperty("recordTime", reading.timestamp);
					feature.add("properties", properties);
					features.add(feature);
					featureCollection.add("features", features);
					/***** SQL insert ********/
					// Insert data
					//System.out.println("before uploading SQL - reading uuid = "+reading.uuid);
					PreparedStatement datastmt = sqlse
							.getSensorInsertStatement(connection,reading.type);
					if (datastmt != null) {
						//System.out.println("datastmt - " + datastmt.toString());
						List<Integer> types = sqlse
								.getArgumentExpectation((long)reading.type);
						datastmt.setString(1, reading.uuid);
						if (reading.type == 0) {
							datastmt.setLong(2, reading.timestamp);
							datastmt.setDouble(3,
									((LightReading) reading).lightVal);
							datastmt.setDouble(4, reading.location.latnLong[0]);
							datastmt.setDouble(5, reading.location.latnLong[1]);
						} else if (reading.type == 1) {
							datastmt.setLong(2, reading.timestamp);
							datastmt.setDouble(3,
									((NoiseReading) reading).soundVal);
							datastmt.setDouble(4, reading.location.latnLong[0]);
							datastmt.setDouble(5, reading.location.latnLong[1]);
						} else if (reading.type == 2) {
							datastmt.setLong(2, reading.timestamp);
							datastmt.setString(3,
									((TextVisual) reading).textMsg);
							datastmt.setDouble(4, reading.location.latnLong[0]);
							datastmt.setDouble(5, reading.location.latnLong[1]);
						}
						//System.out.println("datastmt after populating - "
//								+ datastmt.toString());

						datastmt.addBatch();
						// for (SensorData sd : sensorValues) {
						// try {
						// datastmt.setBytes(1, uuid);
						// datastmt.setLong(2, sd.getRecordTime());
						//
						// Iterator<Boolean> iterBool =
						// sd.getValueBoolList().iterator();
						// Iterator<Integer> iterInteger =
						// sd.getValueInt32List().iterator();
						// Iterator<Long> iterLong =
						// sd.getValueInt64List().iterator();
						// Iterator<Float> iterFloat =
						// sd.getValueFloatList().iterator();
						// Iterator<Double> iterDouble =
						// sd.getValueDoubleList().iterator();
						// Iterator<String> iterString =
						// sd.getValueStringList().iterator();
						//
						// int counter = 3;
						// for (Integer type : types) {
						// switch (type) {
						// case SqlSetup.TYPE_BOOL:
						// datastmt.setBoolean(counter, iterBool.next());
						// break;
						// case SqlSetup.TYPE_INT32:
						// datastmt.setInt(counter, iterInteger.next());
						// break;
						// case SqlSetup.TYPE_INT64:
						// datastmt.setLong(counter, iterLong.next());
						// break;
						// case SqlSetup.TYPE_FLOAT:
						// datastmt.setFloat(counter, iterFloat.next());
						// break;
						// case SqlSetup.TYPE_DOUBLE:
						// datastmt.setDouble(counter, iterDouble.next());
						// break;
						// case SqlSetup.TYPE_STRING:
						// datastmt.setString(counter, iterString.next());
						// break;
						// default:
						// break;
						// }
						// counter++;
						// }
						// datastmt.addBatch();
						// } catch (NoSuchElementException e) {
						// Log.getInstance().append(Log.FLAG_WARNING,
						// "Sensor data type mismatch with database");
						// }
						// }
						// Add sensor data to the database
						datastmt.executeBatch();
						datastmt.close();
					}
					/*************/

				} catch (JsonParseException e) {
					System.out.println("can't save json object: "
							+ e.toString());
				}
				// output the result
				//System.out.println("featureCollection=" + featureCollection.toString());

				String message = featureCollection.toString();
				pSocketServer.sendToAll(message);

			}

		} catch (EOFException e) {
			 e.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING,
					"EOFException occurred, but ignored it for now.");
		} catch (IOException e) {
			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING,
					"Opening data stream from socket failed");
		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING, "Generic error");
		} finally {
			cleanup();
			try {
				in.close();
				in = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				;
			}
		}
	}

	@Override
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
