package com.example.DizzyHead;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.DizzyHead.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;



public class Settings extends Activity{
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	int imageid = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	int age = sp.getInt("age", 0);
    	float weight=0;
    	try {
    		weight = sp.getFloat("weight", 0);
    	} catch (Exception e) {
    	    System.err.println(e.getMessage());
    	}
    	String country = sp.getString("country", null);
    	String name = sp.getString("fullname", null);
    	String email = sp.getString("email", null);
    	int gender = sp.getInt("gender",0);
    	setContentView(R.layout.settings);
    	if(age!=0&&weight!=0&&country!=null&&name!=null&&email!=null){
    		EditText et;
    		et = (EditText) findViewById(R.id.editname);
    		et.setText(name);
    		et = (EditText) findViewById(R.id.editemail);
    		et.setText(email);
    		et = (EditText) findViewById(R.id.editage);
    		et.setText(Integer.toString(age));
    		et = (EditText) findViewById(R.id.editweight);
    		et.setText(Float.toString(weight));
    		Button b = (Button) findViewById(R.id.selectcountry);
    		b.setText(country);
    		if (gender==0){
    			RadioButton rb = (RadioButton) findViewById(R.id.m);
    			rb.setChecked(true);
    		}
    		else{
    			RadioButton rb = (RadioButton) findViewById(R.id.f);
    			rb.setChecked(true);
    		}
    		int id = sp.getInt("image", 0);
    		ImageView image = null;
    		Resources res = this.getResources();
    		switch (id){
	    	case 1:{
	    		image = (ImageView) findViewById(R.id.imageView1);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a1h));
	    		break;
	    	}
	    	case 2:{
	    		image = (ImageView) findViewById(R.id.imageView2);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a2h));
	    		break;
	    	}
	    	case 3:{
	    		image = (ImageView) findViewById(R.id.imageView3);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a3h));
	    		break;
	    	}
	    	case 4:{
	    		image = (ImageView) findViewById(R.id.imageView4);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a4h));
	    		break;
	    	}
	    	case 5:{
	    		image = (ImageView) findViewById(R.id.imageView5);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a5h));
	    		break;
	    	}
	    	case 6:{
	    		image = (ImageView) findViewById(R.id.imageView6);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a6h));
	    		break;
	    	}
    	}
    			
    	}
        
   }
    public void Save(View view) {
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	EditText et;
    	et = (EditText) findViewById(R.id.editname);
		String name = et.getText().toString();
		et = (EditText) findViewById(R.id.editemail);
		String email = et.getText().toString();
    	et = (EditText) findViewById(R.id.editage);
		String age = et.getText().toString();
		et = (EditText) findViewById(R.id.editweight);
		String weight = et.getText().toString();
		Button b = (Button) findViewById(R.id.selectcountry);
		String country = (String) b.getText();
		int image = sp.getInt("image", 0);
		if(!(age.length()==0||weight.length()==0||country.contains("Select")==true||image==0||name.length()==0||email.length()==0)){
	    	editor.putString("fullname", name);
	    	editor.putString("email", email);
	    	editor.putInt("age",Integer.parseInt(age));
	    	editor.putFloat("weight",Float.parseFloat(weight));
	    	nameValuePairs.add(new BasicNameValuePair("weight",weight));
	    	editor.putString("country",country);
	    	editor.putInt("set", 1);
	    	editor.putInt("image", imageid);
	    	editor.commit();
	    	//SaveToDB();
		}
		else{
			editor.putInt("image", imageid);
			editor.commit();
		}
    	this.finish();
    }
    public void M(View view) {
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putInt("gender", 0);
    	editor.commit();

    }
    public void F(View view) {
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putInt("gender", 1);
    	editor.commit();
    }
    public void a1(View view) {
    	imageid = 1;
    	clearimages(1);
    	ImageView a1 = (ImageView) findViewById(R.id.imageView1);
    	Resources res = getResources();
    	a1.setImageDrawable(res.getDrawable(R.drawable.a1h));
    }
    public void a2(View view) {
    	imageid = 2;
    	clearimages(2);
    	ImageView a1 = (ImageView) findViewById(R.id.imageView2);
    	Resources res = getResources();
    	a1.setImageDrawable(res.getDrawable(R.drawable.a2h));
    }
    public void a3(View view) {
    	imageid = 3;
    	clearimages(3);
    	ImageView a1 = (ImageView) findViewById(R.id.imageView3);
    	Resources res = getResources();
    	a1.setImageDrawable(res.getDrawable(R.drawable.a3h));
    }
    public void a4(View view) {
    	imageid = 4;
    	clearimages(4);
    	ImageView a1 = (ImageView) findViewById(R.id.imageView4);
    	Resources res = getResources();
    	a1.setImageDrawable(res.getDrawable(R.drawable.a4h));
    }
    public void a5(View view) {
    	imageid = 5;
    	clearimages(5);
    	ImageView a1 = (ImageView) findViewById(R.id.imageView5);
    	Resources res = getResources();
    	a1.setImageDrawable(res.getDrawable(R.drawable.a5h));
    }
    public void a6(View view) {
    	imageid = 6;
    	clearimages(6);
    	ImageView a1 = (ImageView) findViewById(R.id.imageView6);
    	Resources res = getResources();
    	a1.setImageDrawable(res.getDrawable(R.drawable.a6h));
    }
    public void clearimages(int id){
    	SharedPreferences sp = this.getSharedPreferences("prefile", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	int imageid = sp.getInt("image", 0);
    	Resources res = getResources();
    	ImageView image = null;
    	switch (imageid){
	    	case 1:{
	    		image = (ImageView) findViewById(R.id.imageView1);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a1));
	    		break;
	    	}
	    	case 2:{
	    		image = (ImageView) findViewById(R.id.imageView2);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a2));
	    		break;
	    	}
	    	case 3:{
	    		image = (ImageView) findViewById(R.id.imageView3);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a3));
	    		break;
	    	}
	    	case 4:{
	    		image = (ImageView) findViewById(R.id.imageView4);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a4));
	    		break;
	    	}
	    	case 5:{
	    		image = (ImageView) findViewById(R.id.imageView5);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a5));
	    		break;
	    	}
	    	case 6:{
	    		image = (ImageView) findViewById(R.id.imageView6);
	    		image.setImageDrawable(res.getDrawable(R.drawable.a6));
	    		break;
	    	}
    	}
    	editor.putInt("image",id);
    	editor.commit();
    	
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (data!=null){
	    	Bundle b = data.getExtras();
	    	Button button =(Button) findViewById(R.id.selectcountry);
	    	button.setText((CharSequence) b.get("country"));
    	}
    }
    public void SelectCountry(View view) {
    	Intent i = new Intent(this, CountryList.class);
    	startActivityForResult(i, 0);
    }
    public void SaveToDB(){
    	Thread t = new Thread() {
            public void run() {
            	String result = "";
            	InputStream is = null;
            	//http post
            	try{
        	        HttpClient httpclient = new DefaultHttpClient();
        	        HttpPost httppost = new HttpPost("http://dizzy-head.com/NewUser.php");
        	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	        HttpResponse response = httpclient.execute(httppost);
        	        HttpEntity entity = response.getEntity();
        	        is = entity.getContent();
            	}catch(Exception e){
            		//Toast.makeText(getApplicationContext(), "Error in http connection "+e.toString(), 2).show();
            	}
            	//convert response to string
            	/*try{
            		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            	    StringBuilder sb = new StringBuilder();
            		String line = null;
            		while ((line = reader.readLine()) != null) {
            	                sb.append(line + "n");
            		}
            	    is.close();
            	    result=sb.toString();
            	}catch(Exception e){
            		System.out.println("Error converting result "+e.toString());
            	}*/
            }
        };
        t.start();
    }
    
}
