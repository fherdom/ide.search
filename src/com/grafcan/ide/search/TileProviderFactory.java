package com.grafcan.ide.search;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import com.grafcan.ide.search.WMSTileProvider;

import android.util.Log;

public class TileProviderFactory {
	
	public static WMSTileProvider getOsgeoWmsTileProvider() {
		
		/*
		final String OU_WMS = 
				"http://idecan3.grafcan.es/ServicioWMS/OrtoUrb?" + 
				"&version=1.1.1" +  			
	    		"&request=GetMap" +
	    		"&layers=OU" +
	    		"&bbox=%f,%f,%f,%f" +
	    		"&width=256" +
	    		"&height=256" +
	    		"&reaspect=false" + 
	    		"&srs=EPSG:4326" +
	    		"&format=image/jpeg" +
	    		"&ID=androidfelix";
	    */
		
		final String CALLEJERO_WMS = 
				"http://idecan3.grafcan.es/ServicioWMS/Callejero?" + 
				"&version=1.1.1" +  			
	    		"&request=GetMap" +
	    		"&layers=WMS_CA" +
	    		"&bbox=%f,%f,%f,%f" +
	    		"&width=256" +
	    		"&height=256" +
	    		"&reaspect=false" + 
	    		"&srs=EPSG:4326" +
	    		"&format=image/png" + 
				"&transparent=true";
		
		WMSTileProvider tileProvider = new WMSTileProvider(256,256) {
        	
	        @Override
	        public synchronized URL getTileUrl(int x, int y, int zoom) {
	        	double[] bbox = getBoundingBox(x, y, zoom);
	        	//String s = String.format(Locale.US, OU_WMS, bbox[MINX],bbox[MINY], bbox[MAXX], bbox[MAXY]);
	            String s = String.format(Locale.US, CALLEJERO_WMS, bbox[MINX],bbox[MINY], bbox[MAXX], bbox[MAXY]);
	            Log.d("WMSDEMO", s);
	            URL url = null;
	            try {
	                url = new URL(s);
	            } catch (MalformedURLException e) {
	                throw new AssertionError(e);
	            }
	            return url;
	        }
		};
		return tileProvider;
	}
}
