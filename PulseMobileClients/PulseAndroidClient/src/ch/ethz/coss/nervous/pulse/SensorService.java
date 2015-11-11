package ch.ethz.coss.nervous.pulse;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import ch.ethz.coss.nervous.pulse.model.AccReading;
import ch.ethz.coss.nervous.pulse.model.LightReading;
import ch.ethz.coss.nervous.pulse.model.NoiseReading;
import ch.ethz.coss.nervous.pulse.model.Visual;
import ch.ethz.coss.nervous.pulse.model.VisualLocation;
import ch.ethz.coss.nervous.pulse.sensor.NoiseSensor;
import ch.ethz.coss.nervous.pulse.sensor.NoiseSensor.NoiseListener;
import ch.ethz.coss.nervous.pulse.utils.Utils;

public class SensorService extends Service implements SensorEventListener,
		NoiseListener {
	private static final String DEBUG_TAG = "SensorService";

	public static final String BROADCAST_READING_ACTION = "";

	private SynchWriter out;
	private SensorEvent event;
	private NoiseReading noiseReading;
	private Context context;
	private Intent intent = new Intent(BROADCAST_READING_ACTION);
	SensorValueUpdaterTask sensorValueUpdaterTask;
	private final Handler handler = new Handler();
	Timer timer;
	public long updateInterval;

	public SensorService(SynchWriter out, long writingInterval, Context context) {
		this.out = out;
		this.context = context;
		this.updateInterval = writingInterval;
		this.context = context;
//		initTimer();

	}

	public void initTimer() {

		sensorValueUpdaterTask = new SensorValueUpdaterTask(context);
		timer = new Timer();
		timer.schedule(sensorValueUpdaterTask, updateInterval, updateInterval);

	}

	@Override
	public synchronized void onSensorChanged(final SensorEvent event) {
		// //System.out.println("OnSensorChanged called");

		this.event = event;
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			Visual reading = new AccReading(Application.uuid.toString(),event.values[0], event.values[1],
					event.values[2], System.currentTimeMillis(),
					new VisualLocation(GPSLocation.getInstance(context)
							.getLocation()));
			System.out.println("Inside TYPE_ACCELEROMETER");
			Log.d(DEBUG_TAG, reading.toString());

			if (intent == null)
				//System.out.println("Intent is null");

			intent.putExtra("AccReading", reading);
			context.sendBroadcast(intent);

			Log.d(DEBUG_TAG, reading.toString());

		} else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			
			if(Constants.DUMMY_DATA_COLLECT){
				new Thread() {
					public void run() {
						Visual reading = new LightReading(Application.uuid.toString(),event.values[0],
								System.currentTimeMillis(), -1, new VisualLocation(Utils.generateRandomCitiesGPSCoords()));
						intent.putExtra("LightReading", reading);

						Application.pushReadingToServer(reading,context);
						context.sendBroadcast(intent);
						
					}

				}.start();
			} else {
				Visual reading = new LightReading(Application.uuid.toString(),event.values[0],
						System.currentTimeMillis(), -1, new VisualLocation(
								GPSLocation.getInstance(context)
										.getLocation()));
				intent.putExtra("LightReading", reading);
				context.sendBroadcast(intent);
				System.out.println("Inside TYPE_Light");
				Log.d(DEBUG_TAG, reading.toString());
			}


		} else {
			this.event = null;
			//System.out.println("OnSensorChanged called. But unknown Sensor "
//					+ event.sensor.getName());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onAccuracyChanged(android.hardware
	 * .Sensor, int)
	 */

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void noiseSensorDataReady(final long recordTime, float rms,
			final float spl, float[] bands) {
		
		if(Constants.DUMMY_DATA_COLLECT){
			new Thread() {
				public void run() {
					
					noiseReading = new NoiseReading(Application.uuid.toString(),Double.parseDouble(String.format("%.2f", spl)), recordTime, -1,
							new VisualLocation(Utils.generateRandomCitiesGPSCoords()));
					Application.pushReadingToServer(noiseReading, context);
					intent.putExtra("NoiseReading", noiseReading);
					context.sendBroadcast(intent);
					
				}

			}.start();
		} else {	
					noiseReading = new NoiseReading(Application.uuid.toString(),Double.parseDouble(String.format("%.2f", spl)), recordTime, -1,
							new VisualLocation(GPSLocation.getInstance(context)
									.getLocation()));
					Log.d(DEBUG_TAG,
							"Noise data collected - " + noiseReading.toString());

					intent.putExtra("NoiseReading", noiseReading);
					context.sendBroadcast(intent);
					Log.d(DEBUG_TAG, "Noise data collected");
		}
	}

	class SensorValueUpdaterTask extends TimerTask {

		Context context;

		public SensorValueUpdaterTask(Context context) {
			this.context = context;
		}

		public synchronized void run() {

			if (event != null) {
				if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					Visual reading = new AccReading(Application.uuid.toString(),event.values[0],
							event.values[1], event.values[2],
							System.currentTimeMillis(), new VisualLocation(
									GPSLocation.getInstance(context)
											.getLocation()));
					// out.send(reading);

					System.out.println("Inside SensorValueUpdaterTask TYPE_ACCELEROMETER");
					Log.d(DEBUG_TAG, reading.toString());
				} else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
					LightReading reading = new LightReading(Application.uuid.toString(),event.values[0],
							System.currentTimeMillis(), -1, new VisualLocation(
									GPSLocation.getInstance(context)
											.getLocation()));
					System.out.println("Inside SensorValueUpdaterTask TYPE_Light");
					Log.d(DEBUG_TAG, reading.toString());
				} else {
					event = null;
					System.out
							.println("OnSensorChanged called. But unknown Sensor "
									+ event.sensor.getName());
				}

			} else {
				if (noiseReading != null) {
					Log.d(DEBUG_TAG, noiseReading.toString());

					NoiseSensor sensorNoise = new NoiseSensor();
					//System.out.println("Sensor Noise activated");
					sensorNoise.clearListeners();
					sensorNoise.addListener(SensorService.this);
					// Noise sensor doesn't really make sense with less than
					// 500ms
					sensorNoise.startRecording(500);
				}

			}

		}

		synchronized void stop() {

		}
	}

	private Runnable broadcastReadings = new Runnable() {
		public void run() {
			handler.postDelayed(this, 10000); // 10 seconds
		}
	};

	@Override
	public void onStart(Intent intent, int startId) {
		handler.removeCallbacks(broadcastReadings);
		handler.postDelayed(broadcastReadings, 1000); // 1 second

	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void reset(){
		this.event = null;
	}

	public void cancelTask() {
		if (sensorValueUpdaterTask != null) {
			sensorValueUpdaterTask.cancel();
			sensorValueUpdaterTask = null;
		}

		if (timer != null) {
			timer.cancel();
			timer = null;
		}

	}

	@Override
	public void onDestroy() {
		handler.removeCallbacks(broadcastReadings);
		cancelTask();
		super.onDestroy();
	}

}
