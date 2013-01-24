package com.grafcan.ide.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GeoSearch001 extends Activity {

	private static final String DEBUG_TAG= "GRAFCAN-IDE-GeoSearch001";
	private static ProgressDialog dialog;
	
    private TextView mTextView;
    private ListView mListView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.geosearch001);

        mTextView = (TextView) findViewById(R.id.text);
        mListView = (ListView) findViewById(R.id.list);
        
        //mTextView.setText(getString(R.string.click_menu));
        
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          String query = intent.getStringExtra(SearchManager.QUERY);
          showResults(query);
        } else {
        	onSearchRequested();
        }
    }
    
    /**
     * 
     * @param query The search query
     */
    private void showResults(String query) {
    	DownloadWebPageTask task = new DownloadWebPageTask();
		task.execute(query);
		
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
    	@Override
    	protected String doInBackground(String... params) {
			//
    		String texto = params[0];
    		Log.i(DEBUG_TAG, "doInBackground... " + texto);
    		String xml = XMLfunctions.getSearchXMLByText(texto);
			return xml;
		}
		
		@Override
		protected void onPostExecute(String result) {
			ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	        Document doc = XMLfunctions.XMLfromString(result);
	        int numResults = XMLfunctions.numSearchResults(doc);
	        if((numResults <= 0)){
	        	Toast.makeText(GeoSearch001.this, "No hay resultados", Toast.LENGTH_LONG).show();  
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
			
            mTextView.setText(getString(R.string.results, Integer.toString(numResults)));
            
	        ListAdapter adapter = new SimpleAdapter(GeoSearch001.this, mylist, R.layout.itemlistplaceholder, 
	                        new String[] {"nombre", "localizacion"}, 
	                        new int[] {R.id.item_title, R.id.item_subtitle});
	        mListView.setAdapter(adapter);
	        
	        mListView.setTextFilterEnabled(true);	
	        mListView.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
	        		@SuppressWarnings("unchecked")
					HashMap<String, String> o = (HashMap<String, String>) mListView.getItemAtPosition(position);	        		
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
			dialog = ProgressDialog.show(GeoSearch001.this, "", "Cargando resultados ...");
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }
	
}
