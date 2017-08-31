package com.example.DizzyHead;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Share extends Activity{

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
	}
	
	public void exit(String name){
        Intent i = new Intent();
        i.putExtra("name",name);
        this.setResult(RESULT_OK, i);
		this.finish();
	}

	public void facebook(View view){
		exit("facebook");
	}

	public void others(View view){
		exit("others");
	}
}
