package de.koelly.gichtl;

import java.io.IOException;
import de.koelly.gichtl.DataBaseHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class gichtl extends Activity implements OnClickListener {
	ProgressDialog myProgressDialog = null;
	DataBaseHelper myDbHelper = new DataBaseHelper(this);
	private static final int DATABASE_VERSION = 9;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final String PREFERENCES = "gichtl.prf";
        SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);

        /*
         * Check if database is up to date
         */
        int currentDbVersion;
        currentDbVersion = settings.getInt("currentDbVersion", 0);
        if (currentDbVersion < DATABASE_VERSION){
        	// delete old DB
        	// create DB
        	// create search index
        	// --> Alternativ: Funktion upgrade()
	        SharedPreferences.Editor settingsEditor = settings.edit();
	        settingsEditor.putInt("currentDbVersion", DATABASE_VERSION);
	        settingsEditor.commit();
        }
 
        /*
         * Open or create Database
         */
        
        try {
        	myDbHelper.createDataBase();
        } catch (IOException ioe) {
        	throw new Error("Unable to create database");
        }
 
        try {
        	myDbHelper.openDataBase();	
        }catch(SQLException sqle){
        	throw sqle;
        }
        
        /*
         * If this is first run, or DB changed: update search index
         */
        boolean firstRun = true;        
        firstRun = settings.getBoolean("firstRun", true);
         if (firstRun){

        	 myProgressDialog = ProgressDialog.show(this,"Bitte warten...", "Suchindex wird erstellt.", true);
        	 new Thread() {
        		 public void run() {
        			 try{
        				 myDbHelper.createSearchIndex();
        			 } catch (Exception e) { }
        			 myProgressDialog.dismiss();
        		 }
        	 }.start();
	      
	        SharedPreferences.Editor settingsEditor = settings.edit();
	        settingsEditor.putBoolean("firstRun", false);
	        settingsEditor.commit();
         }
	        
        Button b_meat = (Button) findViewById(R.id.meat);
        b_meat.setOnClickListener(this);
        
        Button b_fish = (Button) findViewById(R.id.fish);
        b_fish.setOnClickListener(this);
        
        Button b_fruits = (Button) findViewById(R.id.fruits);
        b_fruits.setOnClickListener(this);
        
        Button b_vegetables = (Button) findViewById(R.id.vegetables);
        b_vegetables.setOnClickListener(this);
        
        Button b_beverage = (Button) findViewById(R.id.beverage);
        b_beverage.setOnClickListener(this);
        
        Button b_miscellaneous = (Button) findViewById(R.id.miscellaneous);
        b_miscellaneous.setOnClickListener(this);
        
        Button b_search = (Button) findViewById(R.id.search);
        b_search.setOnClickListener(this);
        
    }

	@Override
	public void onClick(View v) {
    	Intent i = new Intent(gichtl.this, ListThem.class);
    	Bundle bundle = new Bundle();
    	bundle.clear();
    	
		switch (v.getId()){
			case R.id.fish:
	        	bundle.putString("category", "fish");
	        	i.putExtras(bundle);
	        	startActivity(i);				
	        	break;
			
			case R.id.meat:
	        	bundle.putString("category", "meat");
	        	i.putExtras(bundle);
	        	startActivity(i);				
	        	break;	
	        	
			case R.id.vegetables:
	        	bundle.putString("category", "vegetables");
	        	i.putExtras(bundle);
	        	startActivity(i);				
	        	break;
			
			case R.id.fruits:
	        	bundle.putString("category", "fruits");
	        	i.putExtras(bundle);
	        	startActivity(i);				
	        	break;
	        	
			case R.id.beverage:
	        	bundle.putString("category", "beverage");
	        	i.putExtras(bundle);
	        	startActivity(i);				
	        	break;
			
			case R.id.miscellaneous:
	        	bundle.putString("category", "miscellaneous");
	        	i.putExtras(bundle);
	        	startActivity(i);				
	        	break;
			case R.id.search:
				onSearchRequested();
				break;
		}
	}
}