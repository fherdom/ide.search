package com.grafcan.ide.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class GeoOpenLayers001 extends Activity {
	
	private WebView browser;
	private TextView myTextView;	
	final Handler myHandler = new Handler();

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webkit);
		browser = (WebView) findViewById(R.id.webkit);
		myTextView = (TextView)findViewById(R.id.textView1);
		
		browser.getSettings().setLightTouchEnabled(true);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.addJavascriptInterface(new WebAppInterface(this), "AndroidFunction");
		//browser.addJavascriptInterface(new JavaScriptInterface(this), "AndroidFunction");
		
        browser.setVerticalScrollBarEnabled(false);
        browser.setHorizontalScrollBarEnabled(false);
		browser.loadUrl("file:///android_asset/geo002.html");
		
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
