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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GeoOpenLayers001 extends Activity {
	
	private static final String DEBUG_TAG= "GRAFCAN-IDE-GeoOpenLayers001";
	private static ProgressDialog dialog;
	
	private WebView browser;
	//private TextView myTextView;	
	private ListView mListView;
	
	final Handler myHandler = new Handler();

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webkit);
		
		browser = (WebView) findViewById(R.id.webkit);
		//myTextView = (TextView)findViewById(R.id.textView1);
		mListView = (ListView) findViewById(R.id.list);
		
		browser.getSettings().setLightTouchEnabled(true);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.addJavascriptInterface(new WebAppInterface(this), "AndroidFunction");
		//browser.addJavascriptInterface(new JavaScriptInterface(this), "AndroidFunction");
		
        browser.setVerticalScrollBarEnabled(false);
        browser.setHorizontalScrollBarEnabled(false);
		browser.loadUrl("file:///android_asset/geo002.html");
		
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	          String query = intent.getStringExtra(SearchManager.QUERY);
	          showResults(query);
		}

		
		browser.setWebChromeClient(new WebChromeClient() {
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.d("MyApplication", cm.message() + " -- From line "
			                         + cm.lineNumber() + " of "
			                         + cm.sourceId() );
			    return true;
			  }
			});
		
	}
		
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
	        	Toast.makeText(GeoOpenLayers001.this, "No hay resultados", Toast.LENGTH_LONG).show();  
	        	finish();
	        }
			NodeList nodes = doc.getElementsByTagName("row");
			for (int i = 0; i < nodes.getLength(); i++) {							
				HashMap<String, String> map = new HashMap<String, String>();	
				Element e = (Element)nodes.item(i);
	        	map.put("id", XMLfunctions.getValue(e, "id"));
	        	map.put("nombre", XMLfunctions.getValue(e, "nombre"));
	        	map.put("localizacion", XMLfunctions.getValue(e, "localizacion"));
	        	
	        	// Agregar lat, lon
	        	map.put("lat", XMLfunctions.getValue(e, "y"));
	        	map.put("lon", XMLfunctions.getValue(e, "x"));
	        	
	        	mylist.add(map);			
			}
			
            //mTextView.setText(getString(R.string.results, Integer.toString(numResults)));
            
	        ListAdapter adapter = new SimpleAdapter(GeoOpenLayers001.this, mylist, R.layout.itemlistplaceholder, 
	                        new String[] {"nombre", "localizacion"}, 
	                        new int[] {R.id.item_title, R.id.item_subtitle});
	        mListView.setAdapter(adapter);
	        
	        mListView.setTextFilterEnabled(true);	
	        mListView.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        		
	        		// Dibujar lat, long en openlayer
	        		//browser.loadUrl("javascript:addPoint(lat, lng)");
	        		
	        		@SuppressWarnings("unchecked")
	        		HashMap<String, String> o = (HashMap<String, String>) mListView.getItemAtPosition(position);
	        		Toast.makeText(GeoOpenLayers001.this, "ID '" + o.get("id") + "' was clicked. (" + o.get("lat") +  ", " + o.get("lon") + ")", Toast.LENGTH_LONG).show();
	        		
	        		
	        		/*
	        		@SuppressWarnings("unchecked")
					HashMap<String, String> o = (HashMap<String, String>) mListView.getItemAtPosition(position);	        		
	        		//Toast.makeText(Main.this, "ID '" + o.get("id") + "' was clicked.", Toast.LENGTH_LONG).show();
	        		final Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW,
	        					Uri.parse("geo:0,0?q=http://visor.grafcan.es/busquedas/toponimiakml/1/10/android/1/"+ o.get("id") + "/"));
	        		startActivity(myIntent);
	        		*/
				}
			});
			dialog.dismiss();
		}
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(GeoOpenLayers001.this, "", "Cargando resultados ...");
		}
    }
	
	/*
	public class JavaScriptInterface {
		Context mContext;

	    JavaScriptInterface(Context c) {
	        mContext = c;
	    }
	    
	    public void showToast(String webMessage){	    	
	    	final String msgeToast = webMessage;
	    	Log.d("GeoOpen", "ShowToast");
	    	myHandler.post(new Runnable() {
	             public void run() {
	                 // This gets executed on the UI thread so it can safely modify Views
	                 myTextView.setText(msgeToast);
	             }
	         });
	       Toast.makeText(mContext, webMessage, Toast.LENGTH_SHORT).show();
	    }
    }
	*/
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.open_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
            	//browser.loadUrl("javascript:addGML()");
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }
}
