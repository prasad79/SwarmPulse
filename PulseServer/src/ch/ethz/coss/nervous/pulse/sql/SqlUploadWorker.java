/*******************************************************************************
 *     SwarmPulse - A service for collective visualization and sharing of mobile 
 *     sensor data, text messages and more.
 *
 *     Copyright (C) 2015 ETH Zürich, COSS
 *
 *     This file is part of SwarmPulse.
 *
 *     SwarmPulse is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SwarmPulse is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SwarmPulse. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * 	Author:
 * 	Prasad Pulikal - prasad.pulikal@gess.ethz.ch  - Initial design and implementation
 *******************************************************************************/
package ch.ethz.coss.nervous.pulse.sql;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;
import ch.ethz.coss.nervous.pulse.socket.ConcurrentSocketWorker;
import ch.ethz.coss.nervous.pulse.utils.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class SqlUploadWorker extends ConcurrentSocketWorker {

	Connection connection;
	SqlSetup sqlse;

	public SqlUploadWorker(Socket socket, PulseWebSocketServer ps, Connection connection, SqlSetup sqlse) {
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
			in = new DataInputStream(socket.getInputStream());
			boolean connected = true;
			while (connected) {
				connected &= !socket.isClosed();
				JsonObject featureCollection = new JsonObject();
				JsonArray features = new JsonArray();
				JsonObject feature = null;
				try {
					// String json = in.readUTF();

					// StringBuffer json = new StringBuffer();
					// String tmp;
					String json = null;
					try {
						// while ((tmp = in.read()) != null) {
						// json.append(tmp);
						// }

						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte buffer[] = new byte[1024];
						for (int s; (s = in.read(buffer)) != -1;) {
							baos.write(buffer, 0, s);
						}
						byte result[] = baos.toByteArray();
						json = new String(result);

						// use inputLine.toString(); here it would have whole
						// source
						in.close();
					} catch (MalformedURLException me) {
						System.out.println("MalformedURLException: " + me);
					} catch (IOException ioe) {
						System.out.println("IOException: " + ioe);
					}
					System.out.println("JSON STRING = " + json);
					System.out.println("JSON Length = " + json.length());

					if (json.length() <= 0)
						continue;
					//Edit JSON STRING
					if(json.indexOf("HTTP") != -1) {
						json = json.substring(json.indexOf("{"));
						System.out.println("NEW JSON STRING = " + json);
						System.out.println("NEW JSON Length = " + json.length());
						connected = false;
					}
					
					JsonObject jsonObj = null;
					try {
						jsonObj = new JsonParser().parse(json).getAsJsonObject();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
//					if (reading == null || reading.location == null)
//						continue;
//					else if (reading.location.latnLong[0] == 0 && reading.location.latnLong[1] == 0)
//						continue;
					
					
					if(jsonObj == null)
						continue;
					
					coord.add(new JsonPrimitive(jsonObj.get("lat").toString()));
					coord.add(new JsonPrimitive(jsonObj.get("long").toString()));

					point.add("coordinates", coord);
					feature.add("geometry", point);

					JsonObject properties = new JsonObject();
					long id = Long.parseLong(jsonObj.get("id").toString());
					long timestamp = Long.parseLong(jsonObj.get("timestamp").toString());
					long volatility = Long.parseLong(jsonObj.get("volatility").toString());
					String uuid = jsonObj.get("uuid").toString();
					if (id == 1) { //Accelerometer
						// System.out.println("Reading instance of Accelerometer");
						properties.addProperty("readingType", "" + id);
						double x = Double.parseDouble(jsonObj.get("x").toString());
						double y = Double.parseDouble(jsonObj.get("y").toString());
						double z = Double.parseDouble(jsonObj.get("z").toString());
						double magnitude = Math.sqrt(x*x + y*y + z*z);
						properties.addProperty("level", "" + magnitude);
					} else if (id == 3) { //Light
						properties.addProperty("readingType", "" + id);
						properties.addProperty("level", "" + Float.parseFloat(jsonObj.get("lux").toString()));
					} else if (id == 4) { //Gyroscope
						properties.addProperty("readingType", "" + id);
						double x = Double.parseDouble(jsonObj.get("x").toString());
						double y = Double.parseDouble(jsonObj.get("y").toString());
						double z = Double.parseDouble(jsonObj.get("z").toString());
						double magnitude = Math.sqrt(x*x + y*y + z*z);
						properties.addProperty("level", "" + magnitude);
					} else if (id == 5) { // Noise
						properties.addProperty("readingType", "" + id);
						properties.addProperty("level", "" + Float.parseFloat(jsonObj.get("Db").toString()));
					} else if (id == 7) { // Temperature
						properties.addProperty("readingType", "" + id);
						properties.addProperty("level", "" + Float.parseFloat(jsonObj.get("celsius").toString()));
					} else {
						// System.out.println("Reading instance not known");
					}
					properties.addProperty("recordTime", timestamp);
					
					properties.addProperty("volatility",  volatility);
					feature.add("properties", properties);
					features.add(feature);
					featureCollection.add("features", features);

					if (volatility != 0) {
						/***** SQL insert ********/
						// Insert data
						 System.out.println("before uploading SQL - reading uuid = "+uuid);
						 System.out.println("Reading volatility = "+volatility);
						PreparedStatement datastmt = sqlse.getSensorInsertStatement(connection, id);
						if (datastmt != null) {
							// System.out.println("datastmt - " +
							// datastmt.toString());
							List<Integer> types = sqlse.getArgumentExpectation(id);
							datastmt.setString(1, uuid);
							datastmt.setLong(2, timestamp);
							datastmt.setLong(3, volatility);
							datastmt.setDouble(4, Double.parseDouble(jsonObj.get("lat").toString()));
							datastmt.setDouble(5, Double.parseDouble(jsonObj.get("long").toString()));
							if (id == 1) {
								double x = Double.parseDouble(jsonObj.get("x").toString());
								double y = Double.parseDouble(jsonObj.get("y").toString());
								double z = Double.parseDouble(jsonObj.get("z").toString());
								double magnitude = Math.sqrt(x*x + y*y + z*z);
								x /= magnitude;
								y /= magnitude;
								z /= magnitude;
								datastmt.setDouble(6, x);
								datastmt.setDouble(7, y);
								datastmt.setDouble(8, z);
								//datastmt.setDouble(9, Integer.parseInt(jsonObj.get("Mercalli").toString()));
								datastmt.setDouble(9, calculateMercalliValue(magnitude));
								datastmt.setDouble(10, magnitude);
							} else if (id == 3) {
								datastmt.setDouble(6, Double.parseDouble(jsonObj.get("lux").toString()));
							} else if (id == 4) {
								double x = Double.parseDouble(jsonObj.get("x").toString());
								double y = Double.parseDouble(jsonObj.get("y").toString());
								double z = Double.parseDouble(jsonObj.get("z").toString());
								double magnitude = Math.sqrt(x*x + y*y + z*z);
								x /= magnitude;
								y /= magnitude;
								z /= magnitude;
								datastmt.setDouble(6, x);
								datastmt.setDouble(7, y);
								datastmt.setDouble(8, z);
								datastmt.setDouble(9, magnitude);
							} else if (id == 5) {
								datastmt.setDouble(6, Double.parseDouble(jsonObj.get("Db").toString()));
							} else if (id == 7) {
								datastmt.setDouble(6, Double.parseDouble(jsonObj.get("celsius").toString()));
							} else if (id == 8) {
								datastmt.setDouble(6, Double.parseDouble(jsonObj.get("msg").toString()));
							}
							// System.out.println("datastmt after populating - "
							// + datastmt.toString());

							datastmt.addBatch();
							datastmt.executeBatch();
							datastmt.close();
						}
						/*************/

					}

				} catch (JsonParseException e) {
					System.out.println("can't save json object: " + e.toString());
				} catch (Exception e) {
					System.out.println("General Exception: " + e.toString());
				}
				// output the result
				// System.out.println("featureCollection=" +
				// featureCollection.toString());

				String message = featureCollection.toString();
				pSocketServer.sendToAll(message);

			}

		} catch (EOFException e) {
			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING, "EOFException occurred, but ignored it for now.");
		} catch (IOException e) {
			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING, "Opening data stream from socket failed");
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
				
			}
		}
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		try {
			connection.close();
		} catch (SQLException e) {
			Log.getInstance().append(Log.FLAG_ERROR, " Error in closing connection.");
		}
	}
	
	private int calculateMercalliValue(double magnitude) {
		final float G = 9.81f;
		if(magnitude > 2*G) return 12;
		if(magnitude > G) return 11;
		if(magnitude > 0.5*G) return 10;
		if(magnitude > 0.2*G) return 9;
		if(magnitude > 0.1*G) return 8;
		if(magnitude > 0.05*G) return 7;
		if(magnitude > 0.02*G) return 6;
		if(magnitude > 0.01*G) return 5;
		if(magnitude > 0.005*G) return 4;
		if(magnitude > 0.002*G) return 3;
		if(magnitude > 0.001*G) return 2;
		return 1;
	}
}
