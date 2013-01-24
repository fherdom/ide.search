package com.grafcan.ide.search;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends Activity {
	
	private static final String DEBUG_TAG= "GRAFCAN-IDE-Search";  
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu);
        Log.i(DEBUG_TAG, "init activity");
        
        Button btnGeoSearch = (Button)findViewById(R.id.btnGeoSearch);
        btnGeoSearch.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent iGeoSearch = new Intent(Menu.this, GeoSearch.class);
        		startActivity(iGeoSearch);
        		Log.i(DEBUG_TAG, "init GeoSearch");
        	}
        });
        
        Button btnReverseGeocoding = (Button)findViewById(R.id.btnReverseGeocoding);
        btnReverseGeocoding.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent iReverseGeocoding = new Intent(Menu.this, ReverseGeocoding.class);
        		startActivity(iReverseGeocoding);
        		Log.i(DEBUG_TAG, "init ReverseGeocoding");
        	}
        });
        
        Button btnGeoOpenLayers = (Button)findViewById(R.id.btnGeoOpenLayers);
        btnGeoOpenLayers.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent iGeoOpenLayers = new Intent(Menu.this, GeoOpenLayers001.class);
        		startActivity(iGeoOpenLayers);
        		Log.i(DEBUG_TAG, "init GeoOpenLayers 001");
        	}
        });
        
        Button btnGeoSearch001 = (Button)findViewById(R.id.btnGeoSearch001);
        btnGeoSearch001.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent iGeoSearch001 = new Intent(Menu.this, GeoSearch001.class);
        		startActivity(iGeoSearch001);
        		Log.i(DEBUG_TAG, "init GeoSearch001");
        	}
        });
                
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }
}