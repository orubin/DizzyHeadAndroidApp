package com.example.DizzyHead;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Log extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log);
        SharedPreferences sp = this.getSharedPreferences("prefile", 0);
        /*quantity+=sp.getInt("shot", 0)*0.04;
        quantity+=sp.getInt("chaser", 0)*0.02;
        quantity+=sp.getInt("half", 0)*0.5;
        quantity+=sp.getInt("third", 0)*0.33;
        if (quantity>record) editor.putFloat("record", quantity);
        editor.commit();*/
        if (sp.getFloat("totalpoints", 0)!=0){
        	EditText et = (EditText) findViewById(R.id.editrecord);
        	String text = Float.toString(sp.getFloat("totalpoints", 0));
        	et.setText(text.substring(0, text.indexOf(".")));
        }
        EditText et = (EditText) findViewById(R.id.editquantity);
        String text = Float.toString(sp.getFloat("points", 0));
    	et.setText(text.substring(0, text.indexOf(".")));
        int i=0;
        for(i=0;i<=DizzyHead.index;i+=2) addTableRow(DizzyHead.array[i],DizzyHead.array[i+1]);
        TextView tv = (TextView) findViewById(R.id.textView1);
        //String name = sp.getString("name", null);
        tv.setText("Your Rank: "+sp.getString("name", null));
        /*TextView tv = (TextView) findViewById(R.id.textView2);
        String shotschasers = Integer.toString(sp.getInt("shot", 0))+" Shots "+Integer.toString(sp.getInt("chaser", 0))+" Chasers ";
        String beers = Integer.toString(sp.getInt("third", 0))+" Third's "+Integer.toString(sp.getInt("half", 0))+" Halves ";
        tv.setText(shotschasers+beers);*/
        
   }
    private void addTableRow(String drink, String time) {
        final TableLayout table = (TableLayout) findViewById(R.id.my_table);
        final TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.table_row_item, null);

        TextView tv;
        // Fill out our cells
        tv = (TextView) tr.findViewById(R.id.cell_1);
        tv.setText(drink);
        tv = (TextView) tr.findViewById(R.id.cell_2);
        tv.setText(time);
        table.addView(tr);

        // Draw separator
        tv = new TextView(this);
        tv.setBackgroundColor(Color.parseColor("#80808080"));
        tv.setHeight(2);
        table.addView(tv);

        // If you use context menu it should be registered for each table row
        registerForContextMenu(tr);
    }
}
