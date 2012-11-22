package com.grafcan.ide.search;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Search extends ListActivity {
	
	Button btnSearch;
	EditText txtSearch;
	ListView lstSearch;
	int count = 0;
	
	String[] names = {
			"Elliot", "Ashley", "Brenda","AAA","ZZZ",
			"Elliot", "Ashley", "Brenda","AAA","ZZZ"
	};
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listplaceholder);
        
        btnSearch = (Button)findViewById(R.id.btnSearch);
        txtSearch = (EditText)findViewById(R.id.txtSearch);
        lstSearch = (ListView)findViewById(R.id.lstSearch);
        
        
        txtSearch.setSingleLine();
        //txtSearch.setInputType(InputType.TYPE_NULL);
        txtSearch.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        
        btnSearch.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				txtSearch.setText("Has clicado" + ++count + " veces.");
				
			}
        	
        });
        
        /*
        ArrayAdapter<String> adapter;
    	adapter = new ArrayAdapter<String>(
    			this,
    			R.id.lstSearch,
    			names);
    	setListAdapter(adapter);
    	*/
        
    }
}