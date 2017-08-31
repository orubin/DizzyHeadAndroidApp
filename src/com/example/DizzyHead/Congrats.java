package com.example.DizzyHead;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

public class Congrats extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.congrats);
        TextView tv = (TextView) findViewById(R.id.name);
        SharedPreferences sp = this.getSharedPreferences("prefile", 0);
        tv.setText(sp.getString("name", null));
        RunAnimations();

	}
    public boolean onTouchEvent(MotionEvent event){
        this.finish();
        return true;
    } 
	
	private void RunAnimations() {
		AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(6000);
        set.addAnimation(animation);
        TextView tv = (TextView) findViewById(R.id.name);
        tv.setAnimation(set);
	}
	
}
