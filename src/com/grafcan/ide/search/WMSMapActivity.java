package com.grafcan.ide.search;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

import android.util.Log;
import android.view.View;
import android.support.v4.app.FragmentActivity;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class WMSMapActivity extends FragmentActivity implements OnItemSelectedListener {

	/*	GOAL:
	 *  Display a WMS overlay from OSGEO on top of a google base map.  
	 *  (The data is a white map with state boundaries.)
	 * 
	 *  GOTCHAS:
	 *  Add the google-play-services_lib as a build dependency
	 *  	Project=>Properties=>Android=>Library=>Add
	 *  
	 *  Create a debugging Maps API Key and add it to the manifest.
	 * 
	 */
	private GoogleMap mMap;
    private CheckBox mMyOverlayCheckbox;
    private TileOverlay wmsTileOverlay;
    private TileProvider wmsTileProvider;
    private String x="-16.2762";
    private String y="28.0886";
    private String name="Test";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        Bundle b = getIntent().getExtras();
        if (b != null) {
        	x = b.getString("x");
        	y = b.getString("y");
        	name = b.getString("name");
        }
        
        Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.layers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mMyOverlayCheckbox = (CheckBox) findViewById(R.id.my_overlay);
        
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            } else {
            	Log.e("WMSDEMO", "Map was null!");
            }
        }       
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setUpMap() {
    	wmsTileProvider = TileProviderFactory.getOsgeoWmsTileProvider();
        wmsTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(wmsTileProvider));
        wmsTileOverlay.setVisible(false);
        
        // Because the demo WMS layer we are using is just a white background map, switch the base layer
        // to satellite so we can see the WMS overlay.
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        
        //LatLng latLng = new LatLng(28.0886, -16.2762);
        LatLng latLng = new LatLng(Double.parseDouble(y), Double.parseDouble(x));
        Marker mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        mMarker.showInfoWindow();
        
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate, 2000, null);
    }

    /**
     * Called when the MyLocation checkbox is clicked.
     */
    public void onMyOverlayToggled(View view) {
        updateMyOverlay();
    }

    private void updateMyOverlay() {
        if (!checkReady()) {
            return;
        }
        if (mMyOverlayCheckbox.isChecked()) {
        	//wmsTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(wmsTileProvider));
        	wmsTileOverlay.setVisible(true);
        } else {
        	//wmsTileOverlay.remove();
        	wmsTileOverlay.setVisible(false);
        }
    }

    
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setLayer((String) parent.getItemAtPosition(position));
    }

    private void setLayer(String layerName) {
        if (!checkReady()) {
            return;
        }
        if (layerName.equals(getString(R.string.normal))) {
            mMap.setMapType(MAP_TYPE_NORMAL);
        } else if (layerName.equals(getString(R.string.hybrid))) {
            mMap.setMapType(MAP_TYPE_HYBRID);
        } else if (layerName.equals(getString(R.string.satellite))) {
            mMap.setMapType(MAP_TYPE_SATELLITE);
        } else if (layerName.equals(getString(R.string.terrain))) {
            mMap.setMapType(MAP_TYPE_TERRAIN);
        } else if (layerName.equals(getString(R.string.none))) {
            mMap.setMapType(MAP_TYPE_NONE);
        } else {
            Log.i("LDA", "Error setting layer with name " + layerName);
        }
    }

	
    public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

    @Override   
	public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);
    	return true;
    }
}