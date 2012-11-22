package com.grafcan.ide.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GeoSearch extends ListActivity {
	
	private static final String DEBUG_TAG= "GRAFCAN-IDE-GeoSearch";
	
	private static ProgressDialog dialog;
	private ListView lv;
	private EditText txtSearch;
	private Button btnSearch;
	
	//private TextView txtView;
	//private static final int MNU_OPC1 = 1;
	//private static final int MNU_OPC2 = 2;
	//private Location loc;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.listplaceholder);
    	
    	txtSearch=(EditText)findViewById(R.id.txtSearch);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        //txtView = (TextView)findViewById(android.R.id.empty);
        lv=(ListView)findViewById(android.R.id.list);
        
    	txtSearch.setSingleLine();
        txtSearch.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        
        btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (txtSearch.getText().length() == 0){
					Toast.makeText(GeoSearch.this, "Introduce una dirección",
							Toast.LENGTH_LONG).show();
				} else {
					DownloadWebPageTask task = new DownloadWebPageTask();
					task.execute(txtSearch.getText().toString());
				}
			}
        });
    }
    
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
    	@Override
    	protected String doInBackground(String... params) {
			//String xml = XMLfunctions.getSearchXML();
    		String texto = params[0];
    		
    		Log.i(DEBUG_TAG, "doInBackground... " + texto);
    		
    		String xml = XMLfunctions.getSearchXMLByText(texto);
			//String xml = XMLfunctions.getSearchXMLByTextDesic(texto);
			//String xml = XMLfunctions.getSearchXMLByTextLoc(texto, loc);
			return xml;
		}
		
		@Override
		protected void onPostExecute(String result) {
			ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	        Document doc = XMLfunctions.XMLfromString(result);
	        int numResults = XMLfunctions.numSearchResults(doc);
	        if((numResults <= 0)){
	        	Toast.makeText(GeoSearch.this, "No hay resultados", Toast.LENGTH_LONG).show();  
	        	finish();
	        }
			NodeList nodes = doc.getElementsByTagName("row");
			for (int i = 0; i < nodes.getLength(); i++) {							
				HashMap<String, String> map = new HashMap<String, String>();	
				Element e = (Element)nodes.item(i);
	        	map.put("id", XMLfunctions.getValue(e, "id"));
	        	map.put("nombre", XMLfunctions.getValue(e, "nombre"));
	        	map.put("localizacion", XMLfunctions.getValue(e, "localizacion"));
	        	mylist.add(map);			
			}
			
	        ListAdapter adapter = new SimpleAdapter(GeoSearch.this, mylist, R.layout.itemlistplaceholder, 
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
			dialog = ProgressDialog.show(GeoSearch.this, "", "Cargando resultados ...");
		}
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }

}