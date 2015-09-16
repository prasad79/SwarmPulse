package ch.ethz.coss.nervous.pulse;

import java.io.IOException;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import ch.ethz.coss.nervous.pulse.model.Visual;
import ch.ethz.coss.nervous.pulse.sensor.NoiseSensor;

public class Application extends android.app.Application {

	public static SynchWriter synchWriter;
	public static SensorService sensorService;
	static SensorManager sensorManager;

	public Application() {
	}

	@Override
	public void onCreate() {

		super.onCreate();

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
			System.out.println("Sensor Noise activated");
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

}
