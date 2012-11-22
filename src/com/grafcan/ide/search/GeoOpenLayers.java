package com.grafcan.ide.search;

import android.app.Activity;
//import android.content.Context;
import android.os.Bundle;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
import android.view.Window;
import android.webkit.WebView;
//import org.json.JSONException;
//import org.json.JSONObject;

public class GeoOpenLayers extends Activity {
	
	//private static String PROVIDER = "gps";
	private WebView browser;
	//private LocationManager myLocationManager = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webkit);
		browser = (WebView) findViewById(R.id.webkit);
		//myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		browser.getSettings().setJavaScriptEnabled(true);
        browser.setVerticalScrollBarEnabled(false);
        browser.setHorizontalScrollBarEnabled(false);
		//browser.addJavascriptInterface(new Locater(), "locater");
		//browser.loadUrl("file:///android_asset/geo001.html");
		//browser.loadUrl("http://openlayers.org/dev/examples/mobile.html");
		browser.loadUrl("file:///android_asset/geo002.html");
	}

	@Override
	public void onResume() {
		super.onResume();
		//myLocationManager.requestLocationUpdates(PROVIDER, 0, 0, onLocation);
		//myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocation);
		//myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, onLocation);
	}

	@Override
	public void onPause() {
		super.onPause();
		//myLocationManager.removeUpdates(onLocation);
	}

	/*
	LocationListener onLocation = new LocationListener() {
		public void onLocationChanged(Location location) {
			StringBuilder buf = new StringBuilder("javascript:whereami(");
			buf.append(String.valueOf(location.getLatitude()));
			buf.append(",");
			buf.append(String.valueOf(location.getLongitude()));
			buf.append(")");
			browser.loadUrl(buf.toString());
		}

		public void onProviderDisabled(String provider) {
			// required for interface, not used
		}

		public void onProviderEnabled(String provider) {
			// required for interface, not used
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// required for interface, not used
		}
	};
	
	public class Locater {
		public String getLocation() throws JSONException {
			Location loc = myLocationManager.getLastKnownLocation(PROVIDER);
			if (loc == null) {
				return (null);
			}
			JSONObject json = new JSONObject();
			json.put("lat", loc.getLatitude());
			json.put("lon", loc.getLongitude());
			return (json.toString());
		}
	}
	*/
}
