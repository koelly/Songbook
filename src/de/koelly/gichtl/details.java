package de.koelly.gichtl;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class details extends Activity{
	
	DataBaseHelper dbHelper = new DataBaseHelper(this);
	String nameOfMeal;
	private String TABLE_NAME = "";
//	private static String[] FROM = {" * "};
//	private static String ORDER_BY = "";
//	private static String SELECTION = "name = ?";
	//String[] SELECTION_ARGS = new String[]{nameOfMeal};
	String[] SELECTION_ARGS = new String[] {nameOfMeal,};
	
	
	private Cursor getDetails(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Log.d("Gichtl","nameOfMEal: " + nameOfMeal);
		Log.d("Gichtl","SELECTION_ARGS: " + SELECTION_ARGS[0]);
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE name = \"" + nameOfMeal + "\";" , null);
		startManagingCursor(cursor);
		cursor.moveToFirst();
		Log.d("Gichtl", "Cursor hat Elemnte: " + cursor.getCount());
		db.close();
		return cursor;
	}

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
            
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        
        Bundle bundle = this.getIntent().getExtras();
        nameOfMeal = bundle.getString("name");
        TABLE_NAME = bundle.getString("table");
        
        Cursor cursor = getDetails();
        //String name = cursor.getString(1);
        String uric_acid = cursor.getString(2);
        int uric_acid_per_portion = cursor.getInt(3);
        int average_portion = cursor.getInt(4);
        int signal = cursor.getInt(5);
        cursor.close();
        
        int bgColor = 0;
        switch (signal){
        case 1:
        	bgColor = Color.GREEN;
        	break;
        case 2:
        	bgColor = Color.YELLOW;
        	break;
        case 3:
        	bgColor = Color.RED;
        	break;
        }
        
        
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.rl);
        rl.setBackgroundColor(bgColor);

        TextView tv1 = (TextView) findViewById(R.id.uric_acid);
        tv1.setText("Harnsäure in 100g:    " + uric_acid + "mg");
        
        TextView tv2 = (TextView) findViewById(R.id.uric_acid_per_portion);
        tv2.setText("In einer " + average_portion + "g Portion befinden sich also ca. " + uric_acid_per_portion + "mg Harnsäure.");

        this.setTitle(nameOfMeal);
        dbHelper.close();

    }
}