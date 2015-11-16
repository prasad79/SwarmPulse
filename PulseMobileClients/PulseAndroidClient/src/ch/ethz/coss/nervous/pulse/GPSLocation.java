/*******************************************************************************
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ETH Zürich.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Contributors:
 *     Prasad Pulikal - prasad.pulikal@gess.ethz.ch  - Initial design and implementation
 *******************************************************************************/
package ch.ethz.coss.nervous.pulse;

import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import ch.ethz.coss.nervous.pulse.utils.Utils;

public class GPSLocation {

	public static boolean GPS_AVAILABLE = false;
	public static boolean CONNECTION_AVAILABLE = false;

	private static GPSLocation _instance = null;

	private final Context mContext;
	private boolean loc_initialized;

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

	public static GPSLocation getInstance(Context context) {
		if (_instance == null || !GPS_AVAILABLE) {

			_instance = new GPSLocation(context);
		}

		return _instance;
	}

	private GPSLocation(final Context context) {
		this.mContext = context;

		locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, mLocationListener);

		if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			showLocationAlert();
			locationManager.removeUpdates(mLocationListener);
			locationManager = null;
			_instance = null;
			return;
		} else
			GPS_AVAILABLE = true;

		// check network connectivity before refresh
		CONNECTION_AVAILABLE = isNetworkAvailable();
		if (!CONNECTION_AVAILABLE) {
			Toast.makeText(mContext, "Please check your Network Connectivity.", Toast.LENGTH_LONG).show();
		}
	}

	private void showLocationAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Location settings disabled"); // GPS not found
		builder.setMessage(
				"This application requires the usage of location features. Please change your location settings."); // Want
		// to
		// enable?
		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				mContext.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});
		builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				System.exit(0);
			}
		});
		builder.create().show();
		Toast.makeText(mContext, "You location could not be determined. Please enable your Network Providers.",
				Toast.LENGTH_LONG).show();

	}

	// Method to check network connectivity
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
			// Log.d("network", "Network available:true");
			return true;
		} else {
			// Log.d("network", "Network available:false");
			return false;
		}
	}

	private final LocationListener mLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(final Location location) {

			latitude = location.getLatitude();
			longitude = location.getLongitude();

			if (!loc_initialized) {
				loc_initialized = true;
				locationManager.removeUpdates(mLocationListener);
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 100, mLocationListener);

			}

			// System.out.println("onLocationChanged called - lat =
			// "+latitude+", long = "+longitude);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

			// System.out.println("onStatusChanged called - lat = "+latitude+",
			// long = "+longitude);

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			// System.out.println("onProviderEnabled called - lat =
			// "+latitude+", long = "+longitude);

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			// System.out.println("onProviderDisabled called - lat =
			// "+latitude+", long = "+longitude);

		}
	};

	/**
	 * Function to get the user's current location
	 * 
	 * @return
	 */
	public double[] getLocation() {

		// if(latitude == 0 && longitude == 0)
		// return null;

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
	 */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

}
