package com.grafcan.ide.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ReverseGeocoding extends ListActivity {
	
	private static final String DEBUG_TAG= "GRAFCAN-IDE-ReverseGeocoding";
	
	private static ProgressDialog dialog;
	private ListView lv;
	private EditText txtSearch;
	private Button btnSearch;
	private TextView txtView;
	
	private Location loc;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.listplaceholder);
        
        txtSearch =(EditText)findViewById(R.id.txtSearch);
        txtView   = (TextView)findViewById(android.R.id.empty);
    	btnSearch = (Button)findViewById(R.id.btnSearch);
    	
    	txtSearch.setSingleLine();
        txtSearch.setInputType(InputType.TYPE_NULL);
    	
        lv=(ListView)findViewById(android.R.id.list);
        btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				DownloadWebPageTask task = new DownloadWebPageTask();
				task.execute();
			}
        });
        
        loc = (Location) new Location("example");
        //loc.setLatitude(28.4537849);
		//loc.setLongitude(-16.2678241);
        loc.setLatitude(28.49083690);
        loc.setLongitude(-16.23956506);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (loc != null) {
    		txtView.setText(loc.toString());
    		txtSearch.setText(loc.getLongitude() + ", " + loc.getLatitude());
    	}
        LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				//makeUseOfNewLocation(location);
				loc = location;
				txtView.setText(loc.toString());
				txtSearch.setText(loc.getLongitude() + ", " + loc.getLatitude());
				Log.i(DEBUG_TAG, "onLocationChanged... " + loc.toString());
			}
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
          };
        // Register the listener with the Location Manager to receive location updates
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
    	@Override
    	protected String doInBackground(String... params) {
			//String xml = XMLfunctions.getSearchXML();
    		//String texto = params[0];
    		Log.i(DEBUG_TAG, "doInBackground... ");
    		//String xml = XMLfunctions.getSearchXMLByText(texto);
			//String xml = XMLfunctions.getSearchXMLByTextDesic(texto);
			String xml = XMLfunctions.getSearchXMLByTextLoc(loc);
			return xml;
		}
		
		@Override
		protected void onPostExecute(String result) {
			ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	        Document doc = XMLfunctions.XMLfromString(result);
	        //int numResults = XMLfunctions.numSearchResults(doc);
	        //if((numResults <= 0)){
	        //	Toast.makeText(ReverseGeocoding.this, "No hay resultados", Toast.LENGTH_LONG).show();  
	        //	finish();
	        //}
			NodeList nodes = doc.getElementsByTagName("row");
			for (int i = 0; i < nodes.getLength(); i++) {							
				HashMap<String, String> map = new HashMap<String, String>();	
				Element e = (Element)nodes.item(i);
	        	map.put("id", XMLfunctions.getValue(e, "ID"));
	        	map.put("nombre", XMLfunctions.getValue(e, "NombreVia"));
	        	if (XMLfunctions.getValue(e, "NombreVia") == "")
	        		map.put("nombre", XMLfunctions.getValue(e, "NombrePOI"));
	        	String localizacion = "";
	        	localizacion = XMLfunctions.getValue(e, "Entidad") + " " + XMLfunctions.getValue(e, "Nucleo") + " (" + XMLfunctions.getValue(e, "Distancia") + " m.)";
	        	map.put("localizacion", localizacion);
	        	mylist.add(map);			
			}
	        ListAdapter adapter = new SimpleAdapter(ReverseGeocoding.this, mylist, R.layout.itemlistplaceholder, 
	                        new String[] {"nombre", "localizacion"}, 
	                        new int[] {R.id.item_title, R.id.item_subtitle});
	        setListAdapter(adapter);
	        lv.setTextFilterEnabled(true);	
	        lv.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
	        		@SuppressWarnings("unchecked")
					HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);	        		
	        		//Toast.makeText(Main.this, "ID '" + o.get("id") + "' was clicked.", Toast.LENGTH_LONG).show();
	        		final Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW,
	        					Uri.parse("geo:0,0?q=http://visor.grafcan.es/busquedas/toponimiakml/1/10/android/1/"+ o.get("id") + "/"));
	        		startActivity(myIntent);
				}
			});
			dialog.dismiss();
		}
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ReverseGeocoding.this, "", "Cargando resultados ...");
		}
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }

}