package com.example.DizzyHead;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.DateTimeKeyListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class DizzyHead extends Activity {
    static String array[] = new String[100];
    static int index = 0;
    static int TotalTime = 0;
    final Handler mHandler = new Handler();
    EditText etglobal = null;
    Thread t = null;
    ArrayList<NameValuePair> nameValuePairs = null;
    private int[] birdsArray = {R.drawable.bird1, R.drawable.bird2, R.drawable.bird3new, R.drawable.bird4, R.drawable.bird5};
    private int[] batsArray = {R.drawable.bat1, R.drawable.bat2, R.drawable.bat3, R.drawable.bat4, R.drawable.bat5};
    private int[] bunnysArray = {R.drawable.bunny1, R.drawable.bunny2, R.drawable.bunny3, R.drawable.bunny4, R.drawable.bunny5};
    private int[] fishArray = {R.drawable.fish1, R.drawable.fish2, R.drawable.fish3, R.drawable.fish4, R.drawable.fish5};
    private int[] ducksArray = {R.drawable.duck1, R.drawable.duck2, R.drawable.duck3, R.drawable.duck4, R.drawable.duck5};
    private int[] lionsArray = {R.drawable.lion1, R.drawable.lion2, R.drawable.lion3, R.drawable.lion4, R.drawable.lion5};
    
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
        	if (TotalTime>0) {
        		TotalTime--;
        		showtimer();
        		updatepic();
        		mHandler.postDelayed(mUpdateResults, 1000);
        	}
        	else{
        		etglobal.setText("00:00");
    			t.stop();
    			t=null;
    			timer();
        	}
        }
    };
    public void start(){
    	t = new Thread() {
            public void run() {
                 mHandler.postDelayed(mUpdateResults,0);
            }
        };
        t.start();
    }
    public void update(){
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("scoretot",Integer.toString((int)sp.getFloat("totalpoints", 0))));
    	nameValuePairs.add(new BasicNameValuePair("scoreday",Integer.toString((int)sp.getFloat("points", 0))));
    	nameValuePairs.add(new BasicNameValuePair("fbid",sp.getString("fbid", null)));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE,TotalTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        nameValuePairs.add(new BasicNameValuePair("soberin",dateFormat.format(cal.getTime())));
    	t = new Thread() {
    		public void run() {
            	try{
                	HttpClient httpclient = new DefaultHttpClient();
        	        HttpPost httppost = new HttpPost("http://dizzy-head.com/Update.php");
        	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	        httpclient.execute(httppost);
        	        nameValuePairs = null;
    	        }catch(Exception e){
            		Toast.makeText(getApplicationContext(), "Error in http connection "+e.toString(), 2).show();
            	}
            }
        };
        t.start();
    }
    public void timer(){
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putBoolean("time", false);
    }
    public void showtimer(){
    	String hours2=null,minutes2=null;
		if(TotalTime/60<10) hours2=new String("0"+TotalTime/60);
		else hours2 = Integer.toString(TotalTime/60);
		if(TotalTime%60<10) minutes2=new String("0"+TotalTime%60);
		else minutes2 = Integer.toString(TotalTime%60);
		etglobal.setText(hours2+":"+minutes2);
    }
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	etglobal = (EditText) findViewById(R.id.time);
    	SharedPreferences.Editor editor = sp.edit();
        if (sp.getBoolean("time", true)){
        	start();
        	editor.putBoolean("time", false);
        }
        else showtimer();
    	long date = sp.getLong("date", 0);
    	Calendar calendar1 = Calendar.getInstance();
    	if (date==0) editor.putLong("date", calendar1.getTimeInMillis());
    	else{
    		long diff = calendar1.getTimeInMillis() - date;
    	    if( (diff / (60 * 1000)) > 360){
    	    	editor.putInt("shot",0);
    	    	editor.putInt("chaser",0);
    	    	editor.putInt("half",0);
    	    	editor.putInt("third",0);
    	    	editor.putFloat("quantity",0);
    	    	editor.putLong("date", calendar1.getTimeInMillis());
    	    }
    	}
    	editor.commit();
    	updatepic();
    	TextView et = (TextView) findViewById(R.id.title);
        et.setText(sp.getString("name", null));
   }
    
    public void settings(View view) {
    	Intent i = new Intent(this, Settings.class);
    	startActivityForResult(i, 0);
    }
    
    public void info(View view) {
    	Intent i = new Intent(this, Info.class);
    	startActivity(i);
    }
    
    public void Shot(View view) {
        SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	int set = sp.getInt("set", 0);
    	if (set==0){
    		Toast.makeText(getApplicationContext(), "Please fill in your details in settings page!", 2).show();
    		return;
    	}
    	int count = sp.getInt("shot", 0);
    	editor.putInt("shot",++count);
    	editor.commit();
    	Date d = new Date();
    	array[index] = "Shot";
    	index++;
    	String hours = null;
    	String minutes = null;
    	if(d.getHours()<10) hours=new String("0"+d.getHours());
    	else hours = Integer.toString(d.getHours());
    	if(d.getMinutes()<10) minutes=new String("0"+d.getMinutes());
    	else minutes = Integer.toString(d.getMinutes());
    	array[index] = hours+":"+minutes;
    	index++;
    	widmark(20, 0);
    	
    }
    public void Chaser(View view) {
        SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	int set = sp.getInt("set", 0);
    	if (set==0){
    		Toast.makeText(getApplicationContext(), "Please fill in your details in settings page!", 2).show();
    		return;
    	}
    	int count = sp.getInt("chaser", 0);
    	editor.putInt("chaser",++count);
    	editor.commit();
    	Date d = new Date();
    	array[index] = "Chaser";
    	index++;
    	String hours = null;
    	String minutes = null;
    	if(d.getHours()<10) hours=new String("0"+d.getHours());
    	else hours = Integer.toString(d.getHours());
    	if(d.getMinutes()<10) minutes=new String("0"+d.getMinutes());
    	else minutes = Integer.toString(d.getMinutes());
    	array[index] = hours+":"+minutes;
    	index++;
    	widmark(10, 0);
    }
    public void HalfBeer() {
        SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	int set = sp.getInt("set", 0);
    	if (set==0){
    		Toast.makeText(getApplicationContext(), "Please fill in your details in settings page!", 2).show();
    		return;
    	}
    	int count = sp.getInt("half", 0);
    	editor.putInt("half",++count);
    	editor.commit();
    	Date d = new Date();
    	array[index] = "1/2 Beer";
    	index++;
    	String hours = null;
    	String minutes = null;
    	if(d.getHours()<10) hours=new String("0"+d.getHours());
    	else hours = Integer.toString(d.getHours());
    	if(d.getMinutes()<10) minutes=new String("0"+d.getMinutes());
    	else minutes = Integer.toString(d.getMinutes());
    	array[index] = hours+":"+minutes;
    	index++;
    	widmark(30, 0);
    }
    public void ThirdBeer() {
        SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	int set = sp.getInt("set", 0);
    	if (set==0){
    		Toast.makeText(getApplicationContext(), "Please fill in your details in settings page!", 2).show();
    		return;
    	}
    	int count = sp.getInt("third", 0);
    	editor.putInt("third",++count);
    	editor.commit();
    	Date d = new Date();
    	array[index] = "1/3 Beer";
    	index++;
    	String hours = null;
    	String minutes = null;
    	if(d.getHours()<10) hours=new String("0"+d.getHours());
    	else hours = Integer.toString(d.getHours());
    	if(d.getMinutes()<10) minutes=new String("0"+d.getMinutes());
    	else minutes = Integer.toString(d.getMinutes());
    	array[index] = hours+":"+minutes;
    	index++;
    	widmark(20, 0);
    }
    public void beer(View view){
        SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	int set = sp.getInt("set", 0);
    	if (set==0){
    		Toast.makeText(getApplicationContext(), "Please fill in your details in settings page!", 2).show();
    		return;
    	}
    	Intent i = new Intent(this, Beer.class);
    	startActivityForResult(i, 1);
    }
    
    public void widmark(int ml,int time){
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	int gender = sp.getInt("gender", 0);
    	float weight = sp.getFloat("weight", 0);
    	String country = sp.getString("country",null);
    	if (country.startsWith("United")) weight /= 2.20462262185;
    	float widmark=0;
    	if(gender==0) widmark = (float) 0.68;
    	else widmark = (float) 0.55;
    	float hours = (float) (((0.079*ml)/(widmark*weight))/(0.015));
    	float bac = (float) (0.079*ml)/(widmark*weight);
    	TotalTime+=hours*60;
    	SharedPreferences.Editor editor = sp.edit();
    	Float points = sp.getFloat("points", 0); 
    	editor.putFloat("points", points+bac*100);
    	Float totalpoints = sp.getFloat("totalpoints", 0); 
    	editor.putFloat("totalpoints", totalpoints+bac*100);
    	editor.commit();
		if (t==null){
			start();
		}
		showtimer();
		postmsg();
		points();
		update();
    }
    public void postmsg(){
    	AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
    	builder2.setTitle("Dizzy Head");
    	builder2.setIcon(R.drawable.icon);
    	if(TotalTime>800 && TotalTime<870){
    		builder2.setMessage("Don’t Forget To Post Your Score On Facebook!");
    	}
    	else if(TotalTime>700 && TotalTime <830){
    		builder2.setMessage("I Hope You Are Not Drinking Alone!");
    	}
    	else if(TotalTime>450 && TotalTime<500){
    		builder2.setMessage("Even my Grandmother Drinks More!");
    	}
    	else if(TotalTime>300 && TotalTime<380){
    		builder2.setMessage("Feeling bit Dizzy??");
    	}
    	else if(TotalTime>220 && TotalTime<300){
    		builder2.setMessage("Warning! Do Not Try To Drive!");
    	}
    	else return;
		builder2.setCancelable(false);
		builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		           }
		       })
	       	;
		AlertDialog alert2 = builder2.create();
		alert2.show();
    }
    public void points() {
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	TextView et = (TextView) findViewById(R.id.title);
    	if (sp.getFloat("totalpoints", 0)>800) return;
    	else if (sp.getFloat("totalpoints", 0)>690&&et.getText().toString().startsWith("Addicted")){
        	et.setText("Alcoholic Anonymous");
        	intent();
        }
        else if (sp.getFloat("totalpoints", 0)>540&&et.getText().toString().startsWith("Advanced Alcoholic")){
        	et.setText("Addicted");
        	intent();
        }
        else if (sp.getFloat("totalpoints", 0)>440&&et.getText().toString().startsWith("Alcoholic")){
        	et.setText("Advanced Alcoholic");
        	intent();
        }
        else if (sp.getFloat("totalpoints", 0)>340&&et.getText().toString().startsWith("Beginner Alcoholic")){
        	et.setText("Alcoholic");
        	intent();
        }
        else if (sp.getFloat("totalpoints", 0)>240&&et.getText().toString().startsWith("Heavy Drinker")){
        	et.setText("Beginner Alcoholic");
        	intent();
        }
        else if (sp.getFloat("totalpoints", 0)>170&&et.getText().toString().startsWith("Drinker")){
        	et.setText("Heavy Drinker");
        	intent();
        }
        else if (sp.getFloat("totalpoints", 0)>110&&et.getText().toString().startsWith("Adult")){
        	et.setText("Drinker");
        	intent();
        }
        else if (sp.getFloat("totalpoints", 0)>90&&et.getText().toString().startsWith("Mature")){
        	et.setText("Adult");
        	intent();
        }
        else if (sp.getFloat("totalpoints", 0)>70&&et.getText().toString().startsWith("Little Girl")){
        	et.setText("Mature");
        	intent();
        }
        else if (sp.getFloat("totalpoints", 0)>50&&et.getText().toString().startsWith("Child")){
        	et.setText("Little Girl");
        	intent();
        }
        else if (sp.getFloat("totalpoints", 0)>20&&et.getText().toString().startsWith("Grandma")){
        	et.setText("Child");
        	intent();
        }

    }
    
    public void analysis(View view) {
    	Intent i = new Intent(this, Log.class);
    	startActivity(i);
    }
    
    public void intent(){
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	TextView et = (TextView) findViewById(R.id.title);
    	editor.putString("name", et.getText().toString());
    	editor.commit();
    	Intent i = new Intent(this, Congrats.class);
    	startActivity(i);
    }
    public void drive(View view) {
    	AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
    	builder2.setTitle("Dizzy Head");
		if (TotalTime<120){
			builder2.setMessage("Go Baby Go");
			builder2.setIcon(R.drawable.ok);
		}
		else if (TotalTime<160){
			builder2.setMessage("Be Careful, You Are On The Limit");
			builder2.setIcon(R.drawable.police);
		}
		else{
			builder2.setMessage("Do Not Try To Drive!");
			builder2.setIcon(R.drawable.handcuffs);
		}
		builder2.setCancelable(false);
		builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		           }
		       })
	       	;
		AlertDialog alert2 = builder2.create();
		alert2.show();
    	
    }

	public void facebook(View view) {
		
    	Intent i = new Intent(this, Share.class);
    	startActivityForResult(i, 3);
	}
	
	public void friends(View view) {
    	Intent i = new Intent(this, FriendsOLD.class);
    	startActivityForResult(i,4);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (requestCode==1){
        	if (data!=null){
    	    	Bundle b = data.getExtras();
    	    	if (b.get("name").toString().startsWith("bottle"))
	    			ThirdBeer();
    	    	else if (b.get("name").toString().startsWith("glass"))
    	    		HalfBeer();
	    				
        	}
    	}
    	else if (requestCode==0){
    		updatepic();
    	}
    	else{
    		if (data!=null){
    	   		SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    			String shotschasers = "I Drank "+Integer.toString(sp.getInt("shot", 0))+" Shots, "+Integer.toString(sp.getInt("chaser", 0))+" Chasers, ";
    		    String beers = Double.toString(sp.getInt("third", 0)*0.33+sp.getInt("half", 0)*0.5)+ " Litres of Beer, ";
    		    String wine = Integer.toString(sp.getInt("wine", 0))+" Glasses of wine, And earned ";
    		    String points = Float.toString(sp.getFloat("points", 0)).substring(0, Float.toString(sp.getFloat("points", 0)).indexOf("."))+ " alcohol points today.";
    	    	Bundle b = data.getExtras();
    	    	if (b.get("name").toString().startsWith("facebook")){
	    	   		 Intent i = new Intent(this, FacebookPost.class);
	    	   		 i.setType("text/plain");
	    	   		 i.putExtra("msg", "Dizzy Head - How much did i drink?");
	    	   		 i.putExtra("text", shotschasers+beers+wine+points);
	    	    	 startActivity(i);
    	    	}
    	    	else if (b.get("name").toString().startsWith("others")){
	    	    	 final Intent intent = new Intent(Intent.ACTION_SEND);
	    	   		 intent.setType("text/plain");
	    	   		 intent.putExtra(Intent.EXTRA_SUBJECT, "Dizzy Head - How much did i drink?");
	    	   		 intent.putExtra(Intent.EXTRA_TEXT, shotschasers+beers+wine+points);
	    	   		 startActivity(Intent.createChooser(intent, "Share To:"));
    	    	}
        	}
    	}
    }
    public void wine(View view) {
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	int set = sp.getInt("set", 0);
    	if (set==0){
    		Toast.makeText(getApplicationContext(), "Please fill in your details in settings page!", 2).show();
    		return;
    	}
    	int count = sp.getInt("wine", 0);
    	editor.putInt("third",++count);
    	editor.commit();
    	Date d = new Date();
    	array[index] = "Wine";
    	index++;
    	String hours = null;
    	String minutes = null;
    	if(d.getHours()<10) hours=new String("0"+d.getHours());
    	else hours = Integer.toString(d.getHours());
    	if(d.getMinutes()<10) minutes=new String("0"+d.getMinutes());
    	else minutes = Integer.toString(d.getMinutes());
    	array[index] = hours+":"+minutes;
    	index++;
        widmark(13,0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.about:
    		Intent i = new Intent(this, About.class);
        	startActivity(i);
    		return true;    		
    	case R.id.exit:
    		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
    		builder2.setMessage("Are you sure you want to exit?");
    		builder2.setCancelable(false);
    		builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    		           public void onClick(DialogInterface dialog, int id) {
    		        	   DizzyHead.this.finish();
    		           }
    		       })
    		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
    		           public void onClick(DialogInterface dialog, int id) {
    		                dialog.cancel();
    		           }
    		       });
    		AlertDialog alert2 = builder2.create();
    		alert2.show();
    		return true;
    	case R.id.points:
    		i = new Intent(this, Points.class);
        	startActivity(i);
    	}
    	return false;
	}
    public void updatepic(){
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	int imageid = sp.getInt("image", 0);
    	Resources res = getResources();
    	ImageView image = (ImageView) findViewById(R.id.face);
    	int[] array = null;
    	switch (imageid){
	    	case 0:{
	    		array = fishArray;
	    		break;
	    	}	
	    	case 1:{
	    		array = batsArray;
	    		break;
	    	}
	    	case 2:{
	    		array = bunnysArray;
	    		break;
	    	}
	    	case 3:{
	    		array = fishArray;
	    		break;
	    	}
	    	case 4:{
	    		array = birdsArray;
	    		break;
	    	}
	    	case 5:{
	    		array = ducksArray;
	    		break;
	    	}
	    	case 6:{
	    		array = lionsArray;
	    		break;
	    	}
    	}
    	if (TotalTime<120){
    		image.setImageDrawable(res.getDrawable(array[0]));
    	}
    	else if (TotalTime<230){
    		image.setImageDrawable(res.getDrawable(array[1]));
    	}
    	else if (TotalTime<320){
    		image.setImageDrawable(res.getDrawable(array[2]));
    	}
    	else if (TotalTime<450){
    		image.setImageDrawable(res.getDrawable(array[3]));
    	}
    	else{
    		image.setImageDrawable(res.getDrawable(array[4]));
    	}
    }
    
   }