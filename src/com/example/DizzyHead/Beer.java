package com.example.DizzyHead;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Beer extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.beer);
	}
	
	public void exit(String name){
        Intent i = new Intent();
        i.putExtra("name",name);
        this.setResult(RESULT_OK, i);
		this.finish();
	}

	public void bottle(View view){
		exit("bottle");
	}

	public void glass(View view){
		exit("glass");
	}
}
