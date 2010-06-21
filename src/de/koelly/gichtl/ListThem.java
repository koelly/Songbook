package de.koelly.gichtl;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListThem extends ListActivity{
	DataBaseHelper dbHelper = new DataBaseHelper(this);
	
	private static String TABLE_NAME;
	private static String[] FROM = { "name", "uric_acid",};
	private static String ORDER_BY = "name ASC";
	
	private Cursor getNames(){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null, ORDER_BY);
		startManagingCursor(cursor);		
		return cursor;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      Bundle bundle = this.getIntent().getExtras();
      TABLE_NAME = bundle.getString("category");
      
      Cursor cursor = getNames();
      Log.d("Gichtl", "Anzahl Eintraege fuer Liste: " + cursor.getCount());
      String[] names4list = new String[cursor.getCount()];
      final String[] names = new String[cursor.getCount()];
      
      int i = 0;
      while (cursor.moveToNext()){
    	  String name = cursor.getString(0);
    	  int uric_acid = cursor.getInt(1);
    	  Log.d("Gichtl", "Eintrag aus DB: " + name);
    	  names4list[i] = name + " (" + uric_acid + "mg/100g)";
    	  names[i++] = name;
      }
      
      String tmp = null;
      if (TABLE_NAME.equalsIgnoreCase("fish")){
    	  tmp = "Fisch";
      }else if (TABLE_NAME.equalsIgnoreCase("meat")){
    	  tmp = "Fleisch";
      }else if (TABLE_NAME.equalsIgnoreCase("fruits")){
    	  tmp = "Früchte";
      }else if (TABLE_NAME.equalsIgnoreCase("vegetables")){
    	  tmp = "Gemüse";
      }else  if (TABLE_NAME.equalsIgnoreCase("beverage")){
    	  tmp = "Getränke";
      }else if (TABLE_NAME.equalsIgnoreCase("miscellaneous")){
    	  tmp = "Vermischtes";
      }

      this.setTitle("Übersicht " + tmp);
      
      setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, names4list));
      ListView lv = getListView();
      lv.setTextFilterEnabled(true);
      
      dbHelper.close();

      lv.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        	//Quelle: http://www.balistupa.com/blog/2009/08/passing-data-or-parameter-to-another-activity-android/
        	Intent i = new Intent(ListThem.this, details.class);
        	Bundle bundle = new Bundle();
        	//bundle.putString("name", (String) ((TextView) view).getText());
        	bundle.putString("name", names[position]);
        	bundle.putString("table", TABLE_NAME);
        	i.putExtras(bundle);
        	startActivity(i);
        }
      });
    }

}