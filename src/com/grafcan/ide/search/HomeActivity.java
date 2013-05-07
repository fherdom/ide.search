package com.grafcan.ide.search;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class HomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dashboardc);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}	 

	public void clicked(View view) {
		Intent i = null;
		Button button = (Button) view;
		
		switch (button.getId())  {
		case R.id.button1:
			i = new Intent(HomeActivity.this, GeoSearch001.class);
			break;
		case R.id.button2:
			break;
		case R.id.button3:
			break;
		case R.id.button4:
			break;
		default:
			break;
		}
		if(i != null) {
			startActivity(i);
		}
	}
}