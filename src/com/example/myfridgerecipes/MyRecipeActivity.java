package com.example.myfridgerecipes;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;



import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class MyRecipeActivity  extends Activity {
	
	String ingredientList = new String();
    String sourceurl = new String() ;
    String sourcename = new String();
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    
	
	
    Button urlButton;
	
	//getUrl
	String getUrl = "http://api.yummly.com/v1/api/recipe/";
	
	// Your Facebook APP ID
    private static String APP_ID = "580122302059283"; // Replace your App ID here
 
    // Instance of Facebook Class
    private Facebook facebook;
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    
    private RatingBar ratingBar; 
	
	// JSON node keys
	private static final String TAG_RECIPENAME = "recipeName";
	private static final String TAG_RATING = "rating";
	private static final String TAG_ID = "id";
	private static final String TAG_INGREDIENTLINES = "ingredientLines";
	private static final String TAG_IMAGE = "images";
	private static final String TAG_TOTALTIME = "totalTime";
	private static final String TAG_YIELD = "yield";
	private static final String TAG_SOURCE = "source";
	Button logout;
	ImageButton share;
	
	//@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);
     
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String nameLabel = in.getStringExtra(TAG_RECIPENAME);
        String ratingLabel = in.getStringExtra(TAG_RATING);
        String idLabel = in.getStringExtra(TAG_ID);
        
        int ratingValue = Integer.parseInt(ratingLabel);
        Log.d("RATING VALUE",""+ratingValue);
        
        
        getUrl = getUrl + idLabel +"?_app_id=150f3a36&_app_key=e2476f53e6b5deead0a8189f45365986";
        
        if(isConnected()){
            
            Log.d("YAAAAY", "connected");
        }
        else
        {
    	Log.d("NOOOOO", "not connected");
    	}
            
        
        new HttpAsyncTask().execute(getUrl);
        
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.recipeName_label);
        
        
        lblName.setText(nameLabel);
        
       int val= Integer.parseInt(ratingLabel); 
        
        facebook = new Facebook(APP_ID);
        mAsyncRunner = new AsyncFacebookRunner(facebook);
        
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setRating(val);
        urlButton =(Button)findViewById(R.id.sourceurl_label);
       
       logout = (Button)findViewById(R.id.logout_button);
       share = (ImageButton)findViewById(R.id.share_recipe);
        /**
		 * Logout button Click event
		 * */
		/*logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("Logout", "button Clicked");
				facebookLogout();
			}
		});

        */
        
        
    }
	
	public static String GET(String getUrl){
    	Log.d("GET","INSIDEYAY");
    	InputStream inputStream = null;
        String result = "";
        try {
                
                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                
                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(getUrl));
                
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
            
            ImageView imageView =(ImageView)findViewById(R.id.image_label);
            TextView ingredientView =(TextView)findViewById(R.id.ingredient_label);
            TextView timeView =(TextView)findViewById(R.id.time_label);
            TextView yieldView =(TextView)findViewById(R.id.yield_label);
            TextView sourceNameView =(TextView)findViewById(R.id.sourcename_label);
            
            Log.d("POSTEXECUTE","BEFORE TRY");
            try {
            	Log.d("POSTEXECUTE","inside TRY");        
            		JSONObject json = new JSONObject(result);
            		JSONArray ingredientArray = json.getJSONArray("ingredientLines");
            		for(int i=0;i<ingredientArray.length();i++)
            		{
            			ingredientList = ingredientArray.get(i).toString() + "\n" + ingredientList;
            		}
            		
            		JSONArray imageArray = json.getJSONArray("images");
            		JSONObject im = imageArray.getJSONObject(0);
            		String image = im.getString("hostedLargeUrl");

            		JSONObject su = json.getJSONObject("source");
            		
            		sourceurl = su.getString("sourceRecipeUrl");
            		sourcename = su.getString("sourceDisplayName");
            		
            		
            		
            		
            		//String ingredientLines = json.getString(TAG_INGREDIENTLINES);
            		            
            		                       		            
            		            
                				//String image = json.getString(TAG_IMAGE);
                				String totalTime = json.getString(TAG_TOTALTIME);
                				String yield = json.getString(TAG_YIELD);
                				//String source = json.getString(TAG_SOURCE);
                				
                				Log.d("IL",ingredientList);
                				Log.d("IM",image);
                				Log.d("TT",totalTime);
                				Log.d("Y",yield);
                				Log.d("S",sourceurl);
                				Log.d("SN",sourcename);
                				
                				/*URL url = new URL(image);
                				Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                				imageView.setImageBitmap(bmp);
                				*/
                				//imageLoad();
                				ingredientView.setText(ingredientList);
                				timeView.setText(totalTime);
                				yieldView.setText(yield);
                				sourceNameView.setText(sourcename);
                				
                				
                				
                    }
                catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }           
    	}	
    
    }
    
    
    
    		
	   
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	
	
	
	public void imageLoad()
	{

		try {
			  ImageView i = (ImageView) findViewById(R.id.image_label);
			  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png").getContent());
			  i.setImageBitmap(bitmap); 
			}
		catch (Exception e) {
			  e.printStackTrace();
			}
		
	}
	
	

	//OPENS WEB VIEW 
		
	public void recipeDetail(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("URLKEY", sourceurl);
        startActivity(intent);          
    }
	
	//OPENS BUY INGREDIENT ACTIVITY
	
	public void buyIngredients(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        
        startActivity(intent);          
    }
	
	//LOGIN AND SHARE ON FACEBOOK
	
	public void facebookLoginShare(View view) {
		mPrefs = getPreferences(MODE_PRIVATE);
	    String access_token = mPrefs.getString("access_token", null);
	    long expires = mPrefs.getLong("access_expires", 0);
	 
	    if (access_token != null) {
	        facebook.setAccessToken(access_token);
	        Log.d("FB Sessions", "" + facebook.isSessionValid());
	        postToWall();
	        
			//logout.setVisibility(View.VISIBLE);
			
			
			//share.setVisibility(View.GONE);
			
	       
	        
	    }
	 
	    if (expires != 0) {
	        facebook.setAccessExpires(expires);
	    }
	 
	    if (!facebook.isSessionValid()) {
	        facebook.authorize(this,
	                new String[] { "email", "publish_stream" },
	                new DialogListener() {
	 
	                    @Override
	                    public void onCancel() {
	                        // Function to handle cancel event
	                    }
	 
	                    @Override
	                    public void onComplete(Bundle values) {
	                        // Function to handle complete event
	                        // Edit Preferences and update facebook acess_token
	                        SharedPreferences.Editor editor = mPrefs.edit();
	                        editor.putString("access_token",
	                                facebook.getAccessToken());
	                        editor.putLong("access_expires",
	                                facebook.getAccessExpires());
	                        editor.commit();
	                    }
	 
	                    @Override
	                    public void onError(DialogError error) {
	                        // Function to handle error
	 
	                    }
	 
	                    @Override
	                    public void onFacebookError(FacebookError fberror) {
	                        // Function to handle Facebook errors
	 
	                    }
	 
	                });
	    }

    }
	
	//POST ON USERS WALL
	public void postToWall() {
		// post on user's wall.
		facebook.dialog(this, "feed", new DialogListener() {

			@Override
			public void onFacebookError(FacebookError e) {
			}

			@Override
			public void onError(DialogError e) {
			}

			@Override
			public void onComplete(Bundle values) {
						}

			@Override
			public void onCancel() {
			}
		});

	}
	
	
	/*public void facebookLogout() {
	    mAsyncRunner.logout(this, new RequestListener() {
	        @Override
	        public void onComplete(String response, Object state) {
	            Log.d("Logout from Facebook", response);
	            if (Boolean.parseBoolean(response) == true) {
	                // User successfully Logged out
	            	runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// make Login button visible
							
							share.setVisibility(View.VISIBLE);
							
							logout.setVisibility(View.GONE);

							
						}

					});

	            	
	            	
	            }
	        }
	 
	        @Override
	        public void onIOException(IOException e, Object state) {
	        }
	 
	        @Override
	        public void onFileNotFoundException(FileNotFoundException e,
	                Object state) {
	        }
	 
	        @Override
	        public void onMalformedURLException(MalformedURLException e,
	                Object state) {
	        }
	 
	        @Override
	        public void onFacebookError(FacebookError e, Object state) {
	        }
	    });
	}
	*/
}

