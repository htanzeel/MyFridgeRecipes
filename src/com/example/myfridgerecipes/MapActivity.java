package com.example.myfridgerecipes;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class MapActivity extends Activity implements LocationListener{
	private int userIcon,shopIcon,otherIcon;
	private GoogleMap theMap;
	private LocationManager locMan;
	private Marker userMarker;
	private Marker[] placeMarkers;
	private final int MAX_PLACES = 20;
	private MarkerOptions[] places;
	
	@Override
	public void onLocationChanged(Location location) {
	    Log.v("MyMapActivity", "location changed");
	    updatePlaces();
	}
	@Override
	public void onProviderDisabled(String provider){
	    Log.v("MyMapActivity", "provider disabled");
	}
	@Override
	public void onProviderEnabled(String provider) {
	    Log.v("MyMapActivity", "provider enabled");
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	    Log.v("MyMapActivity", "status changed");
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		userIcon=R.drawable.red_point;
		shopIcon=R.drawable.green_point;
		otherIcon=R.drawable.purple_point;
		if(theMap==null){
		    //map not instantiated yet
			theMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.the_map)).getMap();
			theMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			placeMarkers = new Marker[MAX_PLACES];
		
		}
		if(theMap != null){
		    //ok - proceed
		}
		updatePlaces();
	}
	
	private void updatePlaces(){
		//update location
		if(userMarker!=null) userMarker.remove();
		locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		double lat = lastLoc.getLatitude();
		double lng = lastLoc.getLongitude();
		LatLng lastLatLng = new LatLng(lat, lng);
		userMarker = theMap.addMarker(new MarkerOptions()
	    .position(lastLatLng)
	    .title("You are here")
	    .icon(BitmapDescriptorFactory.fromResource(userIcon))
	    .snippet("Your last recorded location"));
		theMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);
		String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
			    "json?location="+lat+","+lng+
			    "&rankby=distance&sensor=true" +
			    "&types=grocery_or_supermarket"+
			    "&key=AIzaSyCu6f3GvmcUNg4LnmemLCxcAVYSEaf0-pw";
		new GetPlaces().execute(placesSearchStr);
		locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);

	}
	private class GetPlaces extends AsyncTask<String, Void, String> {
		//fetch and parse place data
		StringBuilder placesBuilder = new StringBuilder();
		@Override
		protected String doInBackground(String... placesURL) {
		    //fetch places
			//process search parameter string(s)
			for (String placeSearchURL : placesURL) {
			//execute search
				HttpClient placesClient = new DefaultHttpClient();
				try {
				    //try to fetch the data
					HttpGet placesGet = new HttpGet(placeSearchURL);
					HttpResponse placesResponse = placesClient.execute(placesGet);
					StatusLine placeSearchStatus = placesResponse.getStatusLine();
					if (placeSearchStatus.getStatusCode() == 200) {
						//we have an OK response
						HttpEntity placesEntity = placesResponse.getEntity();
						InputStream placesContent = placesEntity.getContent();
						InputStreamReader placesInput = new InputStreamReader(placesContent);
						BufferedReader placesReader = new BufferedReader(placesInput);
						String lineIn;
						while ((lineIn = placesReader.readLine()) != null) {
						    placesBuilder.append(lineIn);
						}
						}
				}
				catch(Exception e){
				    e.printStackTrace();
				}
				
			}
		return placesBuilder.toString();
		}
		
		protected void onPostExecute(String result) {
		    //parse place data returned from Google Places
			if(placeMarkers!=null){
			    for(int pm=0; pm<placeMarkers.length; pm++){
			        if(placeMarkers[pm]!=null)
			            placeMarkers[pm].remove();
			    }
			}
			
			try {
			    //parse JSON
				JSONObject resultObject = new JSONObject(result);
				JSONArray placesArray = resultObject.getJSONArray("results");
				places = new MarkerOptions[placesArray.length()];
				//loop through places
				for (int p=0; p<placesArray.length(); p++) {
				    //parse each place
					boolean missingValue=false;
					LatLng placeLL=null;
					String placeName="";
					String vicinity="";
					int currIcon = otherIcon;
					try{
					    //attempt to retrieve place data values
						missingValue=false;
						JSONObject placeObject = placesArray.getJSONObject(p);
						JSONObject loc = placeObject.getJSONObject("geometry").getJSONObject("location");
						placeLL = new LatLng(
							    Double.valueOf(loc.getString("lat")),
							    Double.valueOf(loc.getString("lng")));
						JSONArray types = placeObject.getJSONArray("types");
						for(int t=0; t<types.length(); t++){
						    //what type is it
							String thisType=types.get(t).toString();
							if(thisType.contains("grocery_or_supermarket")){
							    currIcon = shopIcon;
							}
						}
						vicinity = placeObject.getString("vicinity");
						placeName = placeObject.getString("name");

					}
					catch(JSONException jse){
					    missingValue=true;
					    jse.printStackTrace();
					}
					if(missingValue)    places[p]=null;
					else{
					    places[p]=new MarkerOptions()
					    .position(placeLL)
					    .title(placeName)
					    .icon(BitmapDescriptorFactory.fromResource(currIcon))
					    .snippet(vicinity);
					}
				}
			}
			catch (Exception e) {
			    e.printStackTrace();
			}
		
			if(places!=null && placeMarkers!=null){
			    for(int p=0; p<places.length && p<placeMarkers.length; p++){
			        //will be null if a value was missing
			        if(places[p]!=null)
			            placeMarkers[p]=theMap.addMarker(places[p]);
			    }
			}
			
			
		}
		
	}
	@Override
	protected void onResume() {
	    super.onResume();
	    if(theMap!=null){
	        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
	    }
	}
	@Override
	protected void onPause() {
	    super.onPause();
	    if(theMap!=null){
	        locMan.removeUpdates(this);
	    }
	}

}


