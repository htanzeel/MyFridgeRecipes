package com.example.myfridgerecipes;


import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;


import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SearchRecipeActivity extends ListActivity {

String searchUrl = "http://api.yummly.com/v1/api/recipes?_app_id=150f3a36&_app_key=e2476f53e6b5deead0a8189f45365986";


private static final String TAG_MATCHES = "matches";
private static final String TAG_ID = "id";
private static final String TAG_RECIPENAME = "recipeName";
private static final String TAG_RATING = "rating";
private static final String TAG_MATCH_COUNT ="totalMatchCount";
//private static final String TAG_INGREDIENTS = "ingredients";

ListAdapter adapter;
//Hashmap for ListView
	ArrayList<HashMap<String, String>> matchList = new ArrayList<HashMap<String, String>>();
	ArrayList<String> ingredientList = new ArrayList<String>();
	String[] spinnerArray = new String[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.activity_search_recipe);
        //Intent intent = getIntent();
        Bundle b=this.getIntent().getExtras();
        ingredientList=b.getStringArrayList("ingredientKey");
        spinnerArray=b.getStringArray("spinnerKey");
        
        
        for(int i=0;i<6;i++)
        {
        	Log.d("SPINNERVALUE",spinnerArray[i]);
        }

        int length = ingredientList.size();
        Log.d("FINAL INGREDIENTLENGTHVALUE",String.valueOf(length));
        
        for(int i=0;i<ingredientList.size();i++)
        {
        	searchUrl=searchUrl+"&allowedIngredient[]=";
        	searchUrl=searchUrl+ingredientList.get(i);
        	
        }
        
        //CUISINE URL APPENDING
        if(!spinnerArray[0].equals("Select"))
        {
        	searchUrl=searchUrl+"&allowedCuisine[]=cuisine%5Ecuisine-";
        	
        	if(spinnerArray[0].equals("American"))
        	{
        	searchUrl=searchUrl+"american";
        	}
        	else if(spinnerArray[0].equals("Asian"))
        	{
        	searchUrl=searchUrl+"asian";
        	}
        	else if(spinnerArray[0].equals("Barbecue"))
        	{
        	searchUrl=searchUrl+"barbecue-bbq";
        	}
        	else if(spinnerArray[0].equals("Cajun and Creole"))
        	{
        	searchUrl=searchUrl+"cajun";
        	}
        	else if(spinnerArray[0].equals("Chinese"))
        	{
        	searchUrl=searchUrl+"chinese";
        	}
        	else if(spinnerArray[0].equals("Cuban"))
        	{
        	searchUrl=searchUrl+"cuban";
        	}
        	else if(spinnerArray[0].equals("English"))
        	{
        	searchUrl=searchUrl+"english";
        	}
        	else if(spinnerArray[0].equals("French"))
        	{
        	searchUrl=searchUrl+"french";
        	}
        	else if(spinnerArray[0].equals("German"))
        	{
        	searchUrl=searchUrl+"german";
        	}
        	else if(spinnerArray[0].equals("Greek"))
        	{
        	searchUrl=searchUrl+"greek";
        	}
        	else if(spinnerArray[0].equals("Hawaiin"))
        	{
        	searchUrl=searchUrl+"hawaiin";
        	}
        	else if(spinnerArray[0].equals("Hungarian"))
        	{
        	searchUrl=searchUrl+"hungarian";
        	}
        	else if(spinnerArray[0].equals("Indian"))
        	{
        	searchUrl=searchUrl+"indian";
        	}
        	else if(spinnerArray[0].equals("Irish"))
        	{
        	searchUrl=searchUrl+"irish";
        	}
        	else if(spinnerArray[0].equals("Italian"))
        	{
        	searchUrl=searchUrl+"italian";
        	}
        	else if(spinnerArray[0].equals("Japanese"))
        	{
        	searchUrl=searchUrl+"japanese";
        	}
        	else if(spinnerArray[0].equals("Mediterranean"))
        	{
        	searchUrl=searchUrl+"mediterranean";
        	}
        	else if(spinnerArray[0].equals("Mexican"))
        	{
        	searchUrl=searchUrl+"mexican";
        	}
        	else if(spinnerArray[0].equals("Moroccan"))
        	{
        	searchUrl=searchUrl+"moroccan";
        	}
        	else if(spinnerArray[0].equals("Portugese"))
        	{
        	searchUrl=searchUrl+"portugese";
        	}
        	else if(spinnerArray[0].equals("Southern and Soul Food"))
        	{
        	searchUrl=searchUrl+"southern";
        	}
        	else if(spinnerArray[0].equals("Southwestern"))
        	{
        	searchUrl=searchUrl+"southwestern";
        	}
        	else if(spinnerArray[0].equals("Spanish"))
        	{
        	searchUrl=searchUrl+"spanish";
        	}
        	else if(spinnerArray[0].equals("Swedish"))
        	{
        	searchUrl=searchUrl+"swedish";
        	}
        	else
        	{
        	searchUrl=searchUrl+"thai";
        	}
        	
        }
        else
        {
        	//do nothing
        }
        
        //COURSE URL APPENDING
        if(!spinnerArray[1].equals("Select"))
        {
        	searchUrl=searchUrl+"&allowedCourse[]=course%5Ecourse-";
        	if(spinnerArray[1].equals("Appetizers"))
        	{
        		searchUrl=searchUrl+"Appetizers";
        	}
        	else if(spinnerArray[1].equals("Breads"))
        	{
        		searchUrl=searchUrl+"Breads";
        	}
        	else if(spinnerArray[1].equals("Breakfast and Brunch"))
        	{
        		searchUrl=searchUrl+"Breakfast%20and%20Brunch";
        	}
        	else if(spinnerArray[1].equals("Beverages"))
        	{
        		searchUrl=searchUrl+"Beverages";
        	}
        	else if(spinnerArray[1].equals("Cockatils"))
        	{
        		searchUrl=searchUrl+"Cockatils";
        	}
        	else if(spinnerArray[1].equals("Condiments and Sauces"))
        	{
        		searchUrl=searchUrl+"Condiments%20and%20Sauces";
        	}
        	else if(spinnerArray[1].equals("Desserts"))
        	{
        		searchUrl=searchUrl+"Desserts";
        	}
        	else if(spinnerArray[1].equals("Lunch and Snacks"))
        	{
        		searchUrl=searchUrl+"Lunch%20and%20Snacks";
        	}
        	else if(spinnerArray[1].equals("Main Dishes"))
        	{
        		searchUrl=searchUrl+"Main%20Dishes";
        	}
        	else if(spinnerArray[1].equals("Salads"))
        	{
        		searchUrl=searchUrl+"Salads";
        	}
        	else if(spinnerArray[1].equals("Side Dishes"))
        	{
        		searchUrl=searchUrl+"Side%20Dishes";
        	}
        	else if(spinnerArray[1].equals("Soups"))
        	{
        		searchUrl=searchUrl+"Soups";
        	}
        }
        else
        {
        	//dosomething
        }
        
        //HOLIDAY URL APPENDING
        
        if(!spinnerArray[2].equals("Select"))
        {
        	searchUrl=searchUrl+"&allowedHoliday[]=holiday%5Eholiday-";
        	if(spinnerArray[2].equals("Christmas"))
        	{
        		searchUrl=searchUrl+"christmas";
        	}
        	else if(spinnerArray[2].equals("Halloween"))
        	{
        		searchUrl=searchUrl+"halloween";
        	}
        	else if(spinnerArray[2].equals("New Year"))
        	{
        		searchUrl=searchUrl+"new-year";
        	}
        	else if(spinnerArray[2].equals("Summer"))
        	{
        		searchUrl=searchUrl+"summer";
        	}
        	else if(spinnerArray[2].equals("Thanksgiving"))
        	{
        		searchUrl=searchUrl+"thanksgivukkah";
        	}
        	else if(spinnerArray[2].equals("Winter"))
        	{
        		searchUrl=searchUrl+"winter";
        	}
        }
        else
        {
        	//dosomething
        }
        
        //TIME URL APPENDING
        if(!spinnerArray[3].equals("Select"))
        {
        	searchUrl = searchUrl + "&maxTotalTimeInSeconds=";
        	if(spinnerArray[3].equals("Less than 15 minutes"))
        	{
        		searchUrl=searchUrl+"900";
        	}
        	else if(spinnerArray[3].equals("Less than 30 minutes"))
        	{
        		searchUrl=searchUrl+"1800";
        	}
        	else if(spinnerArray[3].equals("Less than 45 minutes"))
        	{
        		searchUrl=searchUrl+"2700";
        	}
        	else if(spinnerArray[3].equals("Less than 1 hour"))
        	{
        		searchUrl=searchUrl+"3600";
        	}
        	else if(spinnerArray[3].equals("Less than 2 hours"))
        	{
        		searchUrl=searchUrl+"7200";
        	}
        	else if(spinnerArray[3].equals("Less than 3 hours"))
        	{
        		searchUrl=searchUrl+"10800";
        	}
        }
        else
        {
        // do nothing	
        }
        
        //ALLERGY URL APPENDING
        
        if(!spinnerArray[4].equals("Select"))
        {
        	if(spinnerArray[4].equals("Diary"))
        	{
        		searchUrl=searchUrl+"&allowedAllergy[]=396%5EDiary-Free";
        	}
        	else if(spinnerArray[4].equals("Egg"))
        	{
        		searchUrl=searchUrl+"&allowedAllergy[]=397%5EEgg-Free";
        	}
        	else if(spinnerArray[4].equals("Gluten"))
        	{
        		searchUrl=searchUrl+"&allowedAllergy[]=393%5EGluten-Free";
        	}
        	else if(spinnerArray[4].equals("Peanut"))
        	{
        		searchUrl=searchUrl+"&allowedAllergy[]=394%5EPeanut-Free";
        	}
        	else if(spinnerArray[4].equals("Seafood"))
        	{
        		searchUrl=searchUrl+"&allowedAllergy[]=398%5ESeafood-Free";
        	}
        	else if(spinnerArray[4].equals("Seasame"))
        	{
        		searchUrl=searchUrl+"&allowedAllergy[]=399%5ESeasame-Free";
        	}
        	else if(spinnerArray[4].equals("Soy"))
        	{
        		searchUrl=searchUrl+"&allowedAllergy[]=400%5ESoy-Free";
        	}
        	else if(spinnerArray[4].equals("Sulfite"))
        	{
        		searchUrl=searchUrl+"&allowedAllergy[]=401%5ESulfite-Free";
        	}
        	else if(spinnerArray[4].equals("Tree Nut"))
        	{
        		searchUrl=searchUrl+"&allowedAllergy[]=395%5ETree%20Nut-Free";
        	}
        	else
        	{
        		searchUrl=searchUrl+"&allowedAllergy[]=392%5EWheat-Free";
        	}
        }
        else
        {
        	//do something
        	
        }
        
        
        //DIET URL APPENDING
        if(!spinnerArray[5].equals("Select"))
        {
        	if(spinnerArray[5].equals("Lacto Vegetarian"))
        	{
        		searchUrl=searchUrl+"&allowedDiet[]=388%5ELacto%20vegetarian";
        	}
        	else if(spinnerArray[5].equals("Ovo Vegetarian"))
        	{
        		searchUrl=searchUrl+"&allowedDiet[]=389%5EOvo%20vegetarian";
        	}
        	else if(spinnerArray[5].equals("Pescetarian"))
        	{
        		searchUrl=searchUrl+"&allowedDiet[]=390%5EPescetarian";
        	}
        	else if(spinnerArray[5].equals("Vegan"))
        	{
        		searchUrl=searchUrl+"&allowedDiet[]=386%5EVegan";
        	}
        	else
        	{
        		searchUrl=searchUrl+"&allowedDiet[]=387%5ELacto-ovo%20vegetarian";
        	}
        	
        }
        else
        {
        	//do something
        }
        Log.d("FINAL VALUE",searchUrl);
        
        if(isConnected()){
            
            Log.d("YAAAAY", "connected");
        }
        else
        {
    	Log.d("NOOOOO", "not connected");
    	}
            
        
        new HttpAsyncTask().execute(searchUrl);
        
        adapter = new SimpleAdapter(this, matchList,
				R.layout.activity_selected_recipe,
				new String[] { TAG_RECIPENAME, TAG_RATING,TAG_ID}, new int[] {
						R.id.RecipeName, R.id.Rating,R.id.Id});
        
                
 
    }
    
    public static String GET(String searchUrl){
    	Log.d("GET","INSIDEYAY");
    	InputStream inputStream = null;
        String result = "";
        try {
                
                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                
                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(searchUrl));
                
                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                
                // convert inputstream to string
                if(inputStream != null)
                        result = convertInputStreamToString(inputStream);
                else
                        result = "Did not work!";
        
        } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
        }
        
        return result;
}
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
    	Log.d("INPUTSTREAM","CONVERTING");
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        
        inputStream.close();
        return result;
        
    }
        
    public boolean isConnected(){
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) 
                        return true;
                else
                        return false;        
    }
    
    

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
              
            return GET(urls[0]);
        }

    
 // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            Log.d("POSTEXECUTE","BEFORE TRY");
            try {
            	Log.d("POSTEXECUTE","inside TRY");        
            		JSONObject json = new JSONObject(result);
                            
                            //String str = "";
                            
            		int matchcount = Integer.parseInt(json.getString(TAG_MATCH_COUNT));
            		
                            JSONArray matches = json.getJSONArray("matches");
                            
                            
                           
                            for(int i=0;i<matches.length();i++)
                            {
                            	JSONObject m = matches.getJSONObject(i);
                            	String recipeName = m.getString(TAG_RECIPENAME);
                				String rating = m.getString(TAG_RATING);
                				String id = m.getString(TAG_ID);
                				// creating new HashMap
                				HashMap<String, String> map = new HashMap<String, String>();
                				
                				// adding each child node to HashMap key => value
                				map.put(TAG_RECIPENAME, recipeName);
                				map.put(TAG_RATING, rating);
                				map.put(TAG_ID, id);
                				matchList.add(map);
                				Log.d("hashmap","CREATED");
                				Log.d("FINAL ID VALUE",id);
                            }
                         
                            setListAdapter(adapter);

                    		// selecting single ListView item
                    		ListView lv = getListView();
                    		Log.d("LISTVIEW","CREATED");
                    		TextView noresult =(TextView)findViewById(R.id.NoResultBox);
                    		if(matchcount==0)
                    		{
                    			noresult.setVisibility(View.VISIBLE);
                    		}
                    		// Launching new screen on Selecting Single ListItem
                    		lv.setOnItemClickListener(new OnItemClickListener() {

                    			@Override
                    			public void onItemClick(AdapterView<?> parent, View view,
                    					int position, long id) {
                    				// getting values from selected ListItem
                    				String name = ((TextView) view.findViewById(R.id.RecipeName)).getText().toString();
                    				String ratevalue = ((TextView) view.findViewById(R.id.Rating)).getText().toString();
                    				String idvalue = ((TextView) view.findViewById(R.id.Id)).getText().toString();
                    				
                    				// Starting new intent
                    				Intent in = new Intent(getApplicationContext(), MyRecipeActivity.class);
                    				in.putExtra(TAG_RECIPENAME, name);
                    				in.putExtra(TAG_RATING, ratevalue);
                    				in.putExtra(TAG_ID, idvalue);
                    				
                    				startActivity(in);

                    			}
                    		});
                         

                            
                            
                    } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }
            
            

    		
    		
    }
            
            
   }
}
    
