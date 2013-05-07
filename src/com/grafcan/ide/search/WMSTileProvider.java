package com.grafcan.ide.search;

import java.net.URLEncoder;

import com.google.android.gms.maps.model.UrlTileProvider;

public abstract class WMSTileProvider extends UrlTileProvider {
	
    // Size of square world map in meters, using WebMerc projection.
    protected static final int MINX = 0;
    protected static final int MAXX = 2;
    protected static final int MINY = 1;
    protected static final int MAXY = 3;
    
    // cql filters
    private String cqlString = ""; 
    
    private static final double RAD2GRAD = 57.295779513082320876798154814105;
    
    // Construct with tile size in pixels, normally 256, see parent class.
    public WMSTileProvider(int x, int y) {
    	super(x, y);
    }
    
    protected String getCql() {
    	return URLEncoder.encode(cqlString);
    }
    
    public void setCql(String c) {
    	cqlString = c;
    }
     
    // Return a web Mercator bounding box given tile x/y indexes and a zoom
    // level.
	protected double[] getBoundingBox(int x, int y, int zoom) {
		double tiles = Math.pow(2,zoom);
    	double circunferencia = 256 * tiles;
    	double PixelsPerDegree = circunferencia / 360;
    	double PixelsPerRadian = circunferencia / (2 * Math.PI);
    	double FalsoEste = circunferencia/2;
    	double FalsoNorte = circunferencia/2;
    	double minx = (x*256-FalsoEste)/ PixelsPerDegree;
		double ul_Lat_aux = (((y-1)+1)*256-FalsoNorte)/(-1*PixelsPerRadian);
		double maxy = (2*Math.atan(Math.exp(ul_Lat_aux)) - ( Math.PI/2)) * RAD2GRAD;
    	double maxx = ((x+1)*256-FalsoEste)/ PixelsPerDegree;
		double br_Lat_aux = ((y+1)*256-FalsoNorte)/(-1*PixelsPerRadian);
		double miny = (2*Math.atan(Math.exp(br_Lat_aux)) - ( Math.PI/2)) * RAD2GRAD;
		double[] bbox = new double[4];
    	bbox[MINX] = minx;
    	bbox[MINY] = miny;
    	bbox[MAXX] = maxx;
    	bbox[MAXY] = maxy;
    	return bbox;
    }
}