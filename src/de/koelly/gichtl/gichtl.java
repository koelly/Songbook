package de.koelly.gichtl;

/*
 * Gichtl - Die Harnsäure Datenbank
 * URLs: http://www.koelly.de/blog/gichtl-app/
 * Copyright (C) 2010, Christopher Köllmayr
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

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
import android.widget.ImageView;

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

        ImageView iv_meat = (ImageView)findViewById(R.id.meat);
        iv_meat.setOnClickListener(this);
        
        ImageView iv_fish = (ImageView)findViewById(R.id.fish);
        iv_fish.setOnClickListener(this);
        
        ImageView iv_fruit = (ImageView)findViewById(R.id.fruit);
        iv_fruit.setOnClickListener(this);
        
        ImageView iv_vegetable = (ImageView)findViewById(R.id.vegetable);
        iv_vegetable.setOnClickListener(this);
        
        ImageView iv_beverage = (ImageView)findViewById(R.id.beverage);
        iv_beverage.setOnClickListener(this);
        
        ImageView iv_miscellaneous = (ImageView)findViewById(R.id.miscellaneous);
        iv_miscellaneous.setOnClickListener(this);
        
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
	        	
			case R.id.vegetable:
	        	bundle.putString("category", "vegetables");
	        	i.putExtras(bundle);
	        	startActivity(i);				
	        	break;
			
			case R.id.fruit:
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