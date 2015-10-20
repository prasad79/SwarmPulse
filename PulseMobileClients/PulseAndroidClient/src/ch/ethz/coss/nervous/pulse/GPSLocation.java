package ch.ethz.coss.nervous.pulse;

import java.util.Random;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import ch.ethz.coss.nervous.pulse.utils.Utils;

public class GPSLocation {

	private static GPSLocation _instance = null;

	private final Context mContext;

	// flag for GPS status
	public boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude = 0; // latitude
	double longitude = 0; // longitude

	// Declaring a Location Manager
	protected LocationManager locationManager;
	
	
	private boolean gps_enabled = false, network_enabled = false;

	public static GPSLocation getInstance(Context context) {
		if (_instance == null){

			_instance = new GPSLocation(context);
		}

		return _instance;
	}

	private GPSLocation(Context context) {
		this.mContext = context;

		locationManager = (LocationManager) mContext
				.getSystemService(mContext.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				10000, 100, mLocationListener);

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // don't start listeners if no provider is enabled
        if (!network_enabled) {
            Toast.makeText(mContext, "You location could not be determined. Please enable your Network Providers.", Toast.LENGTH_LONG).show();


        }

        //check network connectivity before refresh
        boolean checkConnection = isNetworkAvailable();
        if(!checkConnection){
            Toast.makeText(mContext, "Check your Network Connectivity", Toast.LENGTH_LONG).show();
        }
	}

	
	 //Method to check network connectivity
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
        = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
            //Log.d("network", "Network available:true");
            return true;
        } else {
            //Log.d("network", "Network available:false");
            return false;
        }
    }
	private final LocationListener mLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(final Location location) {
			
			latitude =  location.getLatitude();
			longitude = location.getLongitude();
			
			//System.out.println("onLocationChanged called - lat = "+latitude+", long = "+longitude);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

			//System.out.println("onStatusChanged called - lat = "+latitude+", long = "+longitude);

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			//System.out.println("onProviderEnabled called - lat = "+latitude+", long = "+longitude);

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			//System.out.println("onProviderDisabled called - lat = "+latitude+", long = "+longitude);

		}
	};

	/**
	 * Function to get the user's current location
	 * 
	 * @return
	 */
	public double[] getLocation() {

		return Utils.addNoiseToLocation(latitude, longitude);
			
	}
	
	/**
	 * Function to get the random cities location
	 * 
	 * @return
	 */
	public double[] getDummyLocation() {

		return Utils.generateRandomCitiesGPSCoords();
			
	}
	
	
	
	

	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

}
