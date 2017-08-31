package com.example.DizzyHead;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



public class Points extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.points);
        addTableRow("Grandma","0 - 2");
        addTableRow("Child","3 - 6");
        addTableRow("Little Girl","7 - 8");
        addTableRow("Mature","9 - 10");
        addTableRow("Adult","11 - 12");
        addTableRow("Drinker","13 - 17");
        addTableRow("Heavy Drinker","18 - 24");
        addTableRow("Beginner Alcoholic","25 - 34");
        addTableRow("Alcoholic","35 - 44");
        addTableRow("Advanced Alcoholic","45 - 54");
        addTableRow("Addicted","55 - 69");
        addTableRow("Alcoholic Anonymous","70...");
	}
	public void addTableRow(String first, String second){
        final TableLayout table = (TableLayout) findViewById(R.id.my_table);
        final TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.table_row_item2, null);

        TextView tv;
        // Fill out our cells
        tv = (TextView) tr.findViewById(R.id.cell_1);
        tv.setText(first);
        tv = (TextView) tr.findViewById(R.id.cell_2);
        tv.setText(second);
        table.addView(tr);

        // Draw separator
        tv = new TextView(this);
        tv.setBackgroundColor(Color.parseColor("#80808080"));
        tv.setHeight(2);
        table.addView(tv);
	}
        
}
