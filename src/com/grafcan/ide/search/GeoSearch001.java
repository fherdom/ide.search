/*
 * 
 * 
 * http://developer.android.com/intl/es/guide/topics/search/search-dialog.html
 * 
 * TODO:
 * - initial menu like app del tiempo
 * + retrieve lat, lon from initial query
 * + show searchs store in db, add date
 * + show in wmsdemo (http://www.sgoliver.net/blog/?p=3271)
 * - add empty history option
 * 
 * */
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
    private SearchSQLiteHelper dbh;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.geosearch001);
        mTextView = (TextView) findViewById(R.id.text);
        mListView = (ListView) findViewById(R.id.list);
        handleIntent(getIntent());
        dbh = new SearchSQLiteHelper(this, "DBSearch", null, 1);
        showHistoryResults();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
    
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          String query = intent.getStringExtra(SearchManager.QUERY);
          showResults(query);
        }
        else {
        	onSearchRequested();
        }
    }
    
    private void showHistoryResults() {
    	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    	SQLiteDatabase db = dbh.getReadableDatabase();
    	Cursor c = db.rawQuery("select codigo, nombre, localizacion, clasificacion, x, y, date_add, count(*) " +
    			"from tblbusqueda " +
    			"group by codigo, nombre, localizacion, clasificacion, x, y " +
    			"order by nombre", null);
    	if (c.moveToFirst()) {
    	     do {
    	    	 HashMap<String, String> map = new HashMap<String, String>();
    	    	 String codigo = c.getString(0);
    	         map.put("id", c.getString(0));
    	         map.put("nombre", c.getString(1));
    	         map.put("localizacion", c.getString(2));
    	         map.put("clasificacion", c.getString(3));
    	         map.put("x", c.getString(4));
 	        	 map.put("y", c.getString(5));
 	        	 map.put("date_add", c.getString(6));
 	        	 map.put("total", c.getString(7));
    	         mylist.add(map);
    	         Log.i(DEBUG_TAG, "retrieve from db.. " + codigo);
    	     } while(c.moveToNext());
    	     c.close();    	     
    	}
    	ShowListResults(mylist);
    	
    }
    
    private void ShowListResults(ArrayList<HashMap<String, String>> mylist) {
    	ListAdapter adapter = new SimpleAdapter(GeoSearch001.this, mylist, R.layout.itemlistplaceholder, 
                					new String[] {"nombre", "localizacion"}, 
                					new int[] {R.id.item_title, R.id.item_subtitle});
    	mListView.setAdapter(adapter);
    	mListView.setTextFilterEnabled(true);
    	mListView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        		@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) mListView.getItemAtPosition(position);	        		
        		SQLiteDatabase db = dbh.getWritableDatabase();
        		if(db != null) {
        			db.execSQL("INSERT INTO tblbusqueda (codigo, nombre, localizacion, clasificacion, x, y) " +
                            "VALUES (" + o.get("id") + 
                            ", '" + o.get("nombre") + "'" +
                            ", '" + o.get("localizacion") + "'" +
                            ", '" + o.get("clasificacion") + "'" +
                            ", '" + o.get("x") + "'" +
                            ", '" + o.get("y") + "'" +
                            ")");
        			db.close();
        		}
        		Log.i(DEBUG_TAG, "Start WMS activity Map");
        		Intent intent = new Intent(GeoSearch001.this, WMSMapActivity.class);
        		Bundle b = new Bundle();
        		b.putString("x", o.get("x"));
        		b.putString("y", o.get("y"));
        		b.putString("name", o.get("nombre"));
        		intent.putExtras(b); 
        		startActivity(intent);
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
	        	map.put("clasificacion", XMLfunctions.getValue(e, "clasificacion"));
	        	map.put("x", XMLfunctions.getValue(e, "x"));
	        	map.put("y", XMLfunctions.getValue(e, "y"));
	        	mylist.add(map);			
			}
			mTextView.setText(getString(R.string.results, Integer.toString(numResults)));
			ShowListResults(mylist);
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
