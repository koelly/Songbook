package de.koelly.gichtl;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MySearchableActivity extends ListActivity{
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.search);

	    Intent intent = getIntent();
	    this.setTitle("Suche");

	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      //doMySearch(query);
	      //TODO Implement FTS3 Fulltext search
	     
	      Log.d("Gichtl Suche:", query);
	      DataBaseHelper dbHelper = new DataBaseHelper(this);
	      SQLiteDatabase db = dbHelper.getReadableDatabase();
	      Log.d("Gichtl Search query: ", "SELECT * FROM search WHERE name LIKE \"%" + query + "%\"");
	      Cursor cursor = db.rawQuery("SELECT * FROM search WHERE name LIKE \"%" + query + "%\"" , null);
	      
	      Log.d("Gichtl Anzahl Suchergebnisse: ", ""+cursor.getCount());
	      
	      cursor.moveToFirst();
	      String[] results = new String[cursor.getCount()];
	      final String[] tmp = new String[cursor.getCount()];
	      
	      int i = 0;

	      if (cursor.getCount() > 0){
		      do{
		    	  String name = cursor.getString(0);
		    	  String category = cursor.getString(1);
		    	  Log.d("Gichtl", "Eintrag aus DB: " + name);
		    	  tmp[i] = category;
		    	  results[i++] = name; 
		      } while (cursor.moveToNext());


		      setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, results));

	      } else {
	    	  Toast toast = Toast.makeText(this, "Nichts gefunden!", Toast.LENGTH_LONG);
	    	  toast.show();
	      }
	      ListView lv = getListView();
	      lv.setTextFilterEnabled(true);
	      lv.setBackgroundColor(Color.WHITE);
	      cursor.close();	      
	      
	      lv.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        	  String name = (String) ((TextView) view).getText();
	        	  String table = tmp[position];
      	  
	        	  	
		          Intent i = new Intent(MySearchableActivity.this, details.class);
		          Bundle bundle = new Bundle();
		          bundle.putString("name", name);
		          bundle.putString("table", table);
		          i.putExtras(bundle);
		          startActivity(i);
	          }
	      });

	      
	    }
	}

}
// -> http://developer.android.com/guide/topics/search/search-dialog.html