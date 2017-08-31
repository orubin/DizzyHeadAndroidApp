package com.example.DizzyHead;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class FriendsList extends Activity {
	StringBuilder choicesString = new StringBuilder();
	String friends[] = null;
	String myfriends[] = null;
	public void exit(View view){
		ListView lv = (ListView) findViewById(R.id.listView1);
		SparseBooleanArray a = lv.getCheckedItemPositions();
		
		int j=0;
		for (int i = 0; i < a.size(); i++)
        {
            if(a.valueAt(i) == true)
            	j++;
        }
		myfriends = new String[j];
		j=0;
		for (int i = 0; i < a.size(); i++)
        {
            if(a.valueAt(i) == true){
            	myfriends[j] = Integer.toString(a.keyAt(i));
            	j++;
            }
                
        }
		Intent i = new Intent();
		Bundle bundle = new Bundle();   
        bundle.putStringArray("myfriends",myfriends);
        i.putExtras(bundle);
        this.setResult(RESULT_OK, i);
		this.finish();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.friendslist);
		  Bundle b = this.getIntent().getExtras();
		  friends = b.getStringArray("friends");
		  myfriends = b.getStringArray("myfriends");
		  
		  ListView lv = (ListView) findViewById(R.id.listView1);
		  lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, friends));
		  lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		  lv.setTextFilterEnabled(true);
		  for (int i = 0; i < myfriends.length; i++){
			  for (int j = 0; j < friends.length; j++){
				  String currentItem = (String) lv.getItemAtPosition(j);
				  if (currentItem.compareTo(myfriends[i])==0)
				  	lv.setItemChecked(j, true);
			  }
		  }
		  
	}

}
