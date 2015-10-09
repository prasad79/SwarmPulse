package ch.ethz.coss.nervous.pulse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import ch.ethz.coss.nervous.pulse.model.Visual;
import ch.ethz.coss.nervous.pulse.sensor.NoiseSensor;
import java.util.UUID;

public class Application extends android.app.Application {
	public static final String PREFS_NAME = "PulsePrefs";
	public static SynchWriter synchWriter;
	public static SensorService sensorService;
	static SensorManager sensorManager;
	public static UUID uuid = UUID.randomUUID();
	private File dir;
	
	public Application() {
	}

	@Override
	public void onCreate() {

		super.onCreate();
		
		
		 // Restore preferences
	       SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	       long msb = settings.getLong("uuid_msb", 0);
	       long lsb = settings.getLong("uuid_msb", 0);
	       
	       if(msb != 0 && lsb != 0){
	    		uuid = new UUID(msb, lsb);
	    		//system.out.println("MSB and LSB present");
	       }else {
	    	   //system.out.println("MSB and LSB not present");
	    	   uuid = UUID.randomUUID();
	    	   SharedPreferences.Editor editor = settings.edit();
	    	   editor.putLong("uuid_msb", uuid.getMostSignificantBits());
	    	   editor.putLong("uuid_lsb", uuid.getLeastSignificantBits());
	    	   editor.commit();
	       }
	
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				handleUncaughtException(thread, e);
			}
		});

	}

	protected static void initSensorService(Context context) {
		sensorManager = (SensorManager) context
				.getSystemService(SENSOR_SERVICE);
		try {
			synchWriter = new SynchWriter("129.132.255.27", 8445, 1000);
			// synchWriter = new SynchWriter("10.3.24.208", 8445, 1000);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		sensorService = new SensorService(synchWriter, 1000, context);

	}

	protected static void stopSensor() {
		sensorService.cancelTask();
	}

	protected static void registerListener(int type) {

		if (type == Sensor.TYPE_LIGHT) {
			Application.sensorManager.registerListener(sensorService,
					sensorManager.getDefaultSensor(type),
					SensorManager.SENSOR_DELAY_GAME);
		} else {
			NoiseSensor sensorNoise = new NoiseSensor();
			//system.out.println("Sensor Noise activated");
			sensorNoise.clearListeners();
			sensorNoise.addListener(Application.sensorService);
			Application.sensorService.initTimer();
			// Noise sensor doesn't really make sense with less than
			// 500ms
			sensorNoise.startRecording(500);
		}
	}

	protected static void unregisterSensorListeners() {
		if (sensorManager != null)
			sensorManager.unregisterListener(sensorService,
					sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
	}

	private void handleUncaughtException(Thread thread, Throwable e) {
		e.printStackTrace();
		System.exit(0);
	}

	public static void pushReadingToServer(Visual reading) {
		synchWriter.send(reading);
	}
	
	
	private synchronized void storeVMConfig() {
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		//system.out.println("11");
		try {
			File file = new File(dir, "PULSE/config");
			if (!file.exists()) {
				//system.out.println("22");
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			//system.out.println("33");
			dos = new DataOutputStream(fos);
			//system.out.println("44");
			dos.writeLong(uuid.getMostSignificantBits());
			//system.out.println("55");
			dos.writeLong(uuid.getLeastSignificantBits());
			dos.flush();
			dos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Cleanup
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException ex) {
				}
			}
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	
	private synchronized boolean loadVMConfig() {
		boolean success = true;
		FileInputStream fis = null;
		DataInputStream dis = null;
		try {
			//system.out.println("1");
			File file = new File(dir, "PULSE/config");
			//system.out.println("2");
			if (!file.exists()) {
				//system.out.println("3");
				return false;
			}
			//system.out.println("4");
			fis = new FileInputStream(file);
			//system.out.println("5");
			dis = new DataInputStream(fis);
			//system.out.println("6");
			uuid = new UUID(dis.readLong(), dis.readLong());
			//system.out.println("7");
			dis.close();
		} catch (IOException e) {
			success = false;
		} finally {
			// Cleanup
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ex) {
				}
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException ex) {
				}
			}
		}
		return success;
	}
	


}
