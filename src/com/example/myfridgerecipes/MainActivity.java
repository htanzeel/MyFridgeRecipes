package com.example.myfridgerecipes;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


public class MainActivity extends Activity implements OnItemSelectedListener{

	public final static String EXTRA_MESSAGE = "com.example.fridgerecipes.MESSAGE";
	public TableLayout ScrollTableLayout;
	Button button_search;
	ImageButton button_add;
	ArrayList<EditText> totalIngredients = new ArrayList<EditText>();
	EditText newIngredientEditView;
	ArrayList<String> ingredientListArray= new ArrayList<String>();
	
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	DrawerLayout mydrawer;
	
	Spinner cuisineSpinner; 
	Spinner courseSpinner;
	Spinner holidaySpinner;
	Spinner timeSpinner;
	Spinner allergySpinner;
	Spinner dietSpinner;
	 
	String[] spinnerArray = new String[6];
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 // get list items from strings.xml
        drawerListViewItems = getResources().getStringArray(R.array.items);
        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_listview_item, drawerListViewItems));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                );
 
        // Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true); 
        // just styling option add shadow the right edge of the drawer
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        mydrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mydrawer.openDrawer(Gravity.LEFT);
        
        //CUISINE SPINNER
        cuisineSpinner =(Spinner) findViewById(R.id.CuisineSpinner);
        ArrayAdapter<CharSequence> cuisineAdp = ArrayAdapter.createFromResource(this, R.array.cuisineArray, R.layout.spinner_item);
        cuisineAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineSpinner.setAdapter(cuisineAdp);
        cuisineSpinner.setOnItemSelectedListener(this);
        
        //COURSESPINNER
        courseSpinner =(Spinner) findViewById(R.id.CourseSpinner);
        ArrayAdapter<CharSequence> courseAdp = ArrayAdapter.createFromResource(this, R.array.courseArray, R.layout.spinner_item);
        courseAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdp);
        courseSpinner.setOnItemSelectedListener(this);
        
        //HOLIDAYSPINNER
        holidaySpinner =(Spinner) findViewById(R.id.HolidaySpinner);
        ArrayAdapter<CharSequence> holidayAdp = ArrayAdapter.createFromResource(this, R.array.holidayArray, R.layout.spinner_item);
        holidayAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holidaySpinner.setAdapter(holidayAdp);
        holidaySpinner.setOnItemSelectedListener(this);
        //TIMESPINNER
        timeSpinner =(Spinner) findViewById(R.id.TimeSpinner);
        ArrayAdapter<CharSequence> timeAdp = ArrayAdapter.createFromResource(this, R.array.timeArray, R.layout.spinner_item);
        timeAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdp);
        timeSpinner.setOnItemSelectedListener(this);
        
        //ALLERGYSPINNER

        allergySpinner =(Spinner) findViewById(R.id.AllergySpinner);
        ArrayAdapter<CharSequence> allergyAdp = ArrayAdapter.createFromResource(this, R.array.allergyArray, R.layout.spinner_item);
        allergyAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        allergySpinner.setAdapter(allergyAdp);
        allergySpinner.setOnItemSelectedListener(this);
        //DIETSPINNER
        
        dietSpinner =(Spinner) findViewById(R.id.DietSpinner);
        ArrayAdapter<CharSequence> dietAdp = ArrayAdapter.createFromResource(this, R.array.dietArray, R.layout.spinner_item);
        dietAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietSpinner.setAdapter(dietAdp);
        dietSpinner.setOnItemSelectedListener(this);
        
        ScrollTableLayout = (TableLayout)findViewById(R.id.ScrollTableLayout) ;
        button_search= (Button) findViewById(R.id.button_search);
        button_add= (ImageButton) findViewById(R.id.button_add);
        button_add.setOnClickListener(button_add_listener);

	
	}
	
public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
		
		int spinnerId = parent.getId();
        switch (spinnerId) 
        {
                case R.id.CuisineSpinner:
                spinnerArray[0]=cuisineSpinner.getSelectedItem().toString();
                Log.d("SELECTEDITEM",spinnerArray[0]);
                break;
                case R.id.CourseSpinner:
                	spinnerArray[1]=courseSpinner.getSelectedItem().toString();
                	Log.d("SELECTEDITEM",spinnerArray[1]);
                break;
                case R.id.HolidaySpinner:
                	spinnerArray[2]=holidaySpinner.getSelectedItem().toString();
                	Log.d("SELECTEDITEM",spinnerArray[2]);
                break;
                case R.id.TimeSpinner:
                	spinnerArray[3]=timeSpinner.getSelectedItem().toString();
                	Log.d("SELECTEDITEM",spinnerArray[3]);
                break;
                case R.id.AllergySpinner:
                	spinnerArray[4]=allergySpinner.getSelectedItem().toString();
                	Log.d("SELECTEDITEM",spinnerArray[4]);
                break;
                case R.id.DietSpinner:
                	spinnerArray[5]=dietSpinner.getSelectedItem().toString();
                	Log.d("SELECTEDITEM",spinnerArray[5]);
                break;
            
        }
		
		
		
	}
	
	public void onNothingSelected(AdapterView<?> parent) {
		int spinnerId = parent.getId();
        switch (spinnerId) 
        {
                case R.id.CuisineSpinner:
                spinnerArray[0]="Select";
                break;
                case R.id.CourseSpinner:
                	spinnerArray[1]="Select";
                break;
                case R.id.HolidaySpinner:
                	spinnerArray[2]="Select";
                break;
                case R.id.TimeSpinner:
                	spinnerArray[3]="Select";
                break;
                case R.id.AllergySpinner:
                	spinnerArray[4]="Select";
                break;
                case R.id.DietSpinner:
                	spinnerArray[5]="Select";
                break;
            
        }

    }
	
	
	@Override
	protected void onResume() {
	    super.onResume(); 

	    mydrawer.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	            mydrawer.openDrawer(Gravity.LEFT);
	        }
	    }, 1000);
	}
	
	public void searchRecipe(View view) {
        Intent intent = new Intent(this, SearchRecipeActivity.class);
        for(int j=0;j<totalIngredients.size();j++)
        {
        	Log.d(Integer.toString(j),totalIngredients.get(j).getText().toString());
        }
               
        for(int i =0;i<totalIngredients.size();i++)
        {
        	
        	String value = totalIngredients.get(i).getText().toString();
        	if(value.contains(" "))
        	{
        	String[] temp = value.split(" ");
        	String finalval = temp[0] + "%20"+ temp[1];
        	ingredientListArray.add(finalval);
        	}
        	else
        	{
        		ingredientListArray.add(value);
        	}
        }        
       
        Bundle b = new Bundle();
        b.putStringArrayList("ingredientKey",ingredientListArray);
        b.putStringArray("spinnerKey",spinnerArray);
        intent.putExtras(b);
        startActivity(intent);
        
        /*EditText editText = (EditText) findViewById(R.id.extraIngredient);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);*/
    }

	   private void updateIngredient()
	    {
	    	LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	View newIngredientRow = inflater.inflate(R.layout.ingredients, null);
	    	newIngredientEditView = (EditText) newIngredientRow.findViewById(R.id.extraIngredient);   	
	    	ImageButton button_delete = (ImageButton) newIngredientRow.findViewById(R.id.button_delete);
	    	button_delete.setOnClickListener(button_delete_listener);
	    	ScrollTableLayout.addView(newIngredientRow);	
	    	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    	imm.showSoftInput(newIngredientEditView, InputMethodManager.SHOW_IMPLICIT);
	    }
	   public OnClickListener button_add_listener = new OnClickListener(){

		   @Override
			public void onClick(View theView) {
				
			   updateIngredient();
			  totalIngredients.add(newIngredientEditView);
			}
	   };
	   
	   public OnClickListener button_delete_listener = new OnClickListener(){

		   @Override
			public void onClick(View v) {
				
			// row is your row, the parent of the clicked button
	            View row = (View) v.getParent();
	            TableRow rowTable = (TableRow)row;
	            
	            EditText test = (EditText)rowTable.getChildAt(0);
	            String testValue = new String();
	            testValue=test.getText().toString();
	            Log.d("DELETEDedittext",testValue);
	            // container contains all the rows, you could keep a variable somewhere else to the container which you can refer to here
	            ViewGroup container = ((ViewGroup)row.getParent());
	            // delete the row and invalidate your view so it gets redrawn
	            container.removeView(row);
	            for(int i =0;i<totalIngredients.size();i++)
	            {
	            	if(totalIngredients.get(i).getText().toString().equals(testValue))
	            			{
	            				totalIngredients.remove(i);
	            			}
	            }
	            container.invalidate();
			}
	   };

	   
	 @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        actionBarDrawerToggle.onConfigurationChanged(newConfig);
	    }
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	         // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
	        // then it has handled the app icon touch event
	        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }
	 @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	         actionBarDrawerToggle.syncState();
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	 private class DrawerItemClickListener implements ListView.OnItemClickListener {
	        @Override
	        public void onItemClick(AdapterView parent, View view, int position, long id) {
	            Toast.makeText(MainActivity.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
	            drawerLayout.closeDrawer(drawerListView);
	 
	        }
	    }
}
