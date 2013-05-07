package com.grafcan.ide.search;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;


public class XMLfunctions {
	
	private static final String DEBUG_TAG= "GRAFCAN-IDE-XMLfunctions";

	public final static Document XMLfromString(String xml){
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
        	
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(xml));
	        doc = db.parse(is); 
	        
		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
            return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}
		       
        return doc;
        
	}
	
	/** Returns element value
	  * @param elem element (it is XML tag)
	  * @return Element value otherwise empty String
	  */
	 public final static String getElementValue( Node elem ) {
	     Node kid;
	     if( elem != null){
	         if (elem.hasChildNodes()){
	             for( kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling() ){
	                 if((kid.getNodeType() == Node.TEXT_NODE) || (kid.getNodeType() == Node.CDATA_SECTION_NODE)) {
	                     return kid.getNodeValue().trim();
	                 }
	             }
	         }
	     }
	     return "";
	 }
		 
	 public static String getXML(){	 
			String line = null;

			try {
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://p-xr.com/xml");

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				line = EntityUtils.toString(httpEntity);
				
				/*
				String html = "";
				for (String url: params) {
					DefaultHttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet(url);
					try {
						HttpResponse response = client.execute(httpget);
						InputStream content = response.getEntity().getContent();
						BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
						String s = "";
						while ((s = buffer.readLine()) != null) {
							html += s;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Log.i(TAG, html);
				return html;
				*/
				
				
				
			} catch (UnsupportedEncodingException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (MalformedURLException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (IOException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			}

			return line;

	}
	 
	public static int numResults(Document doc){		
		Node results = doc.getDocumentElement();
		int res = -1;
		
		try{
			res = Integer.valueOf(results.getAttributes().getNamedItem("count").getNodeValue());
		}catch(Exception e ){
			res = -1;
		}
		
		return res;
	}

	public static String getValue(Element item, String str) {		
		NodeList n = item.getElementsByTagName(str);		
		return XMLfunctions.getElementValue(n.item(0));
	}
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * */
	
	 public static String getSearchXML(){	 
			String line = null;

			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				//HttpPost httpPost = new HttpPost("http://visor.grafcan.es/busquedas/toponimoxml/1/10/?texto=chineguas");
				HttpGet httpGet = new HttpGet("http://visor.grafcan.es/busquedas/toponimoxml/1/10/?texto=castillo");
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				//line = EntityUtils.toString(httpEntity);
				line = EntityUtils.toString(httpEntity, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (MalformedURLException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (IOException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			}
			return line;
	}

	 public static String getSearchXMLByText(String texto){	 
			String line = null;
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				//HttpPost httpPost = new HttpPost("http://visor.grafcan.es/busquedas/toponimoxml/1/10/?texto=chineguas");
				String url = "http://visor.grafcan.es/busquedas/toponimoxmlandroid/1/10/?texto=" + URLEncoder.encode(texto);
				Log.i(DEBUG_TAG, "getSearchXMLByText... " + url);
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				//line = EntityUtils.toString(httpEntity);
				line = EntityUtils.toString(httpEntity, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (MalformedURLException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (IOException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			}
			return line;
	}

	 public static String getSearchXMLByTextDesic(String texto){	 
			String line = null;
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String url = "http://visor.grafcan.es/busquedas/GeoSearch/1/10?texto=" + URLEncoder.encode(texto);
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				line = EntityUtils.toString(httpEntity, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (MalformedURLException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (IOException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			}
			return line;
	}
	 
	 public static String getSearchXMLByTextLoc(Location loc){	 
			String line = null;
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				//HttpPost httpPost = new HttpPost("http://visor.grafcan.es/busquedas/toponimoxml/1/10/?texto=chineguas");
				//String url = "http://visor.grafcan.es/busquedas/toponimoxmlandroid/1/10/?texto=" + URLEncoder.encode(texto);
				String url = "http://visor.grafcan.es/busquedas/ReverseGeocoding?lng=" + loc.getLongitude() + "&lat=" + loc.getLatitude();
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				//line = EntityUtils.toString(httpEntity);
				line = EntityUtils.toString(httpEntity, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (MalformedURLException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (IOException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			}
			return line;
	}
	 

	 public static int numSearchResults(Document doc){		
			Node results = doc.getDocumentElement();
			int res = -1;
			
			try{
				res = Integer.valueOf(results.getAttributes().getNamedItem("total").getNodeValue());
			}catch(Exception e ){
				res = -1;
			}
			return res;
		}

	 public Address getAddressForLocation(Context context, Location location) throws IOException {

		    if (location == null) {
		        return null;
		    }

		    double latitude = location.getLatitude();
		    double longitude = location.getLongitude();
		    int maxResults = 1;

		    Geocoder gc = new Geocoder(context, Locale.getDefault());
		    List<Address> addresses = gc.getFromLocation(latitude, longitude, maxResults);

		    if (addresses.size() == 1) {
		        return addresses.get(0);
		    } else {
		        return null;
		    }
		}	 
	 
}

