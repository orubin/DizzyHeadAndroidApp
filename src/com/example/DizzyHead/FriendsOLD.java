package com.example.DizzyHead;

/*
 import java.io.BufferedReader;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.net.MalformedURLException;
 import java.util.ArrayList;
 import java.util.Map.Entry;
 import org.apache.http.HttpEntity;
 import org.apache.http.HttpResponse;
 import org.apache.http.NameValuePair;
 import org.apache.http.client.HttpClient;
 import org.apache.http.client.entity.UrlEncodedFormEntity;
 import org.apache.http.client.methods.HttpPost;
 import org.apache.http.impl.client.DefaultHttpClient;
 import org.apache.http.message.BasicNameValuePair;
 import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;

 import com.facebook.android.AsyncFacebookRunner;
 import com.facebook.android.AsyncFacebookRunner.RequestListener;
 import com.facebook.android.Facebook;
 import com.facebook.android.FacebookError;
 import com.facebook.android.Util;

 import android.app.Activity;
 import android.app.AlertDialog;
 import android.app.ProgressDialog;
 import android.content.DialogInterface;
 import android.content.SharedPreferences;
 import android.graphics.Color;
 import android.os.AsyncTask;
 import android.os.Bundle;
 import android.view.View;
 import android.view.View.OnClickListener;
 import android.widget.EditText;
 import android.widget.ProgressBar;
 import android.widget.TableLayout;
 import android.widget.TableRow;
 import android.widget.TextView;
 import android.widget.Toast;

 public class FriendsOLD extends Activity{
 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
 TableRow[] trA ;
 static int rowCount=-1;
 ProgressDialog myProgressDialog = null;
 String email = null;
 String newstatus = null;
 CharSequence[] friends = null;
 CharSequence[] friendsID = null;
 @Override
 public void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 getFriends(null,null,null);
 setContentView(R.layout.friends);
 SharedPreferences sp = this.getSharedPreferences("friends", 0);
 trA = new TableRow[sp.getAll().size()+5];
 for (Entry<String, ?> entry : sp.getAll().entrySet()) {
 Object val = entry.getValue();
 addTableRow(val.toString());
 }
 }

 public void Search(final String email) {
 new DownloadFilesTask().execute(email);
 }

 public void Add(View view){
 final AlertDialog.Builder alert = new AlertDialog.Builder(this);
 final EditText input = new EditText(this);
 input.setText("or.rubin@hotmail.com");
 alert.setView(input);
 alert.setTitle("Enter Your Friend's E-mail:");
 alert.setPositiveButton("Search!", new DialogInterface.OnClickListener() {
 public void onClick(DialogInterface dialog, int whichButton) {
 String value = input.getText().toString().trim();
 Toast tag = Toast.makeText(getApplicationContext(), "Searching for your friend..." ,Toast.LENGTH_LONG);
 tag.setDuration(7);
 tag.show();
 Search(value);
 }
 });

 alert.setNegativeButton("Cancel",
 new DialogInterface.OnClickListener() {
 public void onClick(DialogInterface dialog, int whichButton) {
 dialog.cancel();
 }
 });
 alert.show();	

 }

 private void addTableRow(String friend) {
 final TableLayout table = (TableLayout) findViewById(R.id.my_table);
 final TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.friends_table_item, null);

 rowCount++;
 trA[rowCount] = tr;
 TextView tv;
 // Fill out our cells
 tv = (TextView) tr.findViewById(R.id.cell_1);
 String words[] = friend.split("\n");
 String text = null;
 if (!words[2].equalsIgnoreCase("0"))
 text = words[0]+"\nScore: "+words[2]+"\nLast Drink: "+words[3]+"\nSober in "+words[4];
 else text = words[0];
 tv.setText(text);
 table.addView(tr);

 tr.setBackgroundColor(10802158);
 tr.setTag(rowCount);
 tr.setOnClickListener(myProgressBarShower);


 // Draw separator
 tv = new TextView(this);
 tv.setBackgroundColor(Color.parseColor("#80808080"));
 tv.setHeight(2);
 table.addView(tv);

 // If you use context menu it should be registered for each table row
 registerForContextMenu(tr);
 }

 OnClickListener myProgressBarShower = new OnClickListener(){
 String status = null;
 // @Override
 public void onClick(final View v) {
 myProgressDialog = ProgressDialog.show(FriendsOLD.this, "Please wait...", "Fetching friend's status...", true);
 final int id = (Integer) v.getTag();
 new Thread() {
 public void run() {
 TableRow tr = trA[id];
 // Fill out our cells
 TextView tv = (TextView) tr.findViewById(R.id.cell_1);
 String a[] = tv.getText().toString().split("\n");
 String my_email = a[1];
 InputStream is = null;
 try{
 HttpClient httpclient = new DefaultHttpClient();
 HttpPost httppost = new HttpPost("http://www.dizzy-head.com/Search.php");
 nameValuePairs.add(new BasicNameValuePair("email",my_email));
 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 HttpResponse response = httpclient.execute(httppost);
 HttpEntity entity = response.getEntity();
 is = entity.getContent();
 }catch(Exception e){

 }
 //convert response to string
 try{
 BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
 StringBuilder sb = new StringBuilder();
 String line = null;
 while ((line = reader.readLine()) != null) {
 sb.append(line + "n");
 }
 is.close();
 status = sb.toString();
 }catch(Exception e){

 }

 try{
 JSONArray jArray = new JSONArray(status);
 JSONObject json_data = jArray.getJSONObject(0);
 newstatus = json_data.getString("name")+"'s Last drink was at: "+json_data.getString("drink")
 + ", And will be sober in "+json_data.getString("timer")+" minutes and has "+json_data.getString("score")+ " points.";
 }catch(JSONException e){

 }
 myProgressDialog.dismiss();
 new show().execute(newstatus);
 }
 }.start();
 }
 };

 private class show extends AsyncTask<String, Integer, Long> {

 @Override
 protected Long doInBackground(String... params) {
 // TODO Auto-generated method stub
 return null;
 }
 protected void onPostExecute(Long result) {
 showstatus(newstatus);
 }

 }

 public void showstatus(String status){
 final AlertDialog.Builder alert = new AlertDialog.Builder(FriendsOLD.this);
 alert.setTitle(status);
 alert.setPositiveButton("Alrighty Then!", new DialogInterface.OnClickListener() {
 public void onClick(DialogInterface dialog, int whichButton) {
 dialog.cancel();
 }
 });
 alert.show();
 }
 private class DownloadFilesTask extends AsyncTask<String, Integer, Long> {
 String friend = null;
 protected void onProgressUpdate(Integer... progress) {
 setProgress(progress[0]);
 }

 protected void onPostExecute(Long result) {
 SharedPreferences sp = getApplicationContext().getSharedPreferences("friends", 0);
 SharedPreferences.Editor editor = sp.edit();
 editor.putString(Integer.toString(rowCount), friend);
 editor.commit();
 }

 @Override
 protected Long doInBackground(String... params) {
 InputStream is = null;
 try{
 HttpClient httpclient = new DefaultHttpClient();
 HttpPost httppost = new HttpPost("http://www.dizzy-head.com/Search.php");
 nameValuePairs.add(new BasicNameValuePair("email",params[0]));
 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 HttpResponse response = httpclient.execute(httppost);
 HttpEntity entity = response.getEntity();
 is = entity.getContent();
 }catch(Exception e){

 }
 //convert response to string
 try{
 BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
 StringBuilder sb = new StringBuilder();
 String line = null;
 while ((line = reader.readLine()) != null) {
 sb.append(line + "n");
 }
 is.close();
 friend = sb.toString();
 }catch(Exception e){

 }
 try{
 JSONArray jArray = new JSONArray(friend);
 for(int i=0;i<jArray.length();i++){
 JSONObject json_data = jArray.getJSONObject(i);
 friend = json_data.getString("name")+"\n"+json_data.getString("email")+"\n"+
 json_data.getString("score")+"\n"+json_data.getString("drink")+"\n"+json_data.getString("timer");
 }
 }catch(JSONException e){

 }
 return null;
 }
 }

 @SuppressWarnings("null")
 public void getFriends(CharSequence[] charFriendsNames,String[] sFriendsID, ProgressBar progbar) {
 try{
 //Pass arrays to store data
 friends = charFriendsNames;
 friendsID = sFriendsID;
 //pb = progbar;
 //Log.d(TAG, "Getting Friends!");
 //Create Request with Friends Request Listener
 AsyncFacebookRunner mAsyncRunner = null;
 Facebook facebookClient;
 String APP_ID = "163122417088005";
 facebookClient = new Facebook(APP_ID);
 //facebookClient.authorize(this, new String[] {"publish_stream", "read_stream", "offline_access"},this);
 //String response = facebookClient.request("me");
 //Bundle parameters = new Bundle();
 mAsyncRunner = new AsyncFacebookRunner(facebookClient);
 mAsyncRunner.request("me/friends", new FriendsRequestListener());

 } catch (Exception e) {
 //Log.d(TAG, "Exception: " + e.getMessage());
 }
 }


 private class FriendsRequestListener implements RequestListener {
 String friendData;

 //Method runs when request is complete
 public void onComplete(String response, Object state) {
 //Log.d(TAG, "FriendListRequestONComplete");
 //Create a copy of the response so i can be read in the run() method.
 friendData = response; 
 try {
 //Parse JSON Data
 JSONObject json;
 json = Util.parseJson(friendData);

 //Get the JSONArry from our response JSONObject
 JSONArray friendArray = json.getJSONArray("data");

 //Loop through our JSONArray
 int friendCount = 0;
 String fId, fNm;
 JSONObject friend;
 for (int i = 0;i<friendArray.length();i++){
 //Get a JSONObject from the JSONArray
 friend = friendArray.getJSONObject(i);
 //Extract the strings from the JSONObject
 fId = friend.getString("id");
 fNm = friend.getString("name");
 //Set the values to our arrays
 friendsID[friendCount] = fId;
 friends[friendCount] = fNm;
 friendCount ++;
 //Log.d("TEST", "Friend Added: " + fNm);
 }

 //Remove Progress Bar
 //pb.setVisibility(ProgressBar.GONE);

 } catch (JSONException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 } catch (FacebookError e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 }
 }

 public void onIOException(IOException e, Object state) {
 // TODO Auto-generated method stub

 }

 public void onFileNotFoundException(FileNotFoundException e,
 Object state) {
 // TODO Auto-generated method stub

 }

 public void onMalformedURLException(MalformedURLException e,
 Object state) {
 // TODO Auto-generated method stub

 }

 public void onFacebookError(FacebookError e, Object state) {
 // TODO Auto-generated method stub

 }

 }


 }
 */
/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;

import com.facebook.android.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;

public class FriendsOLD extends Activity {

	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	public static final String APP_ID = "163122417088005";

	private LoginButton mLoginButton;
	private Button mRequestButton;
	String nameArray[] = null;
	String idArray[] = null;
	String myFriends[] = null;
	private ProgressDialog dialog = null;
	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;
	String SelectedFriend = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends);
		mLoginButton = (LoginButton) findViewById(R.id.login);
		mRequestButton = (Button) findViewById(R.id.add);

		mFacebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		SessionEvents.addLogoutListener(new SampleLogoutListener());
		mLoginButton.init(this, mFacebook);
		updateList();

		mRequestButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mAsyncRunner.request("me/friends", new SampleRequestListener());
				dialog = ProgressDialog.show(FriendsOLD.this, "Dizzy Head",
						"Getting Your Friends - Please Hold...", true);
			}
		});
		mRequestButton.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);
		
	}

	private void updateList() {
		SharedPreferences sp = this.getSharedPreferences("friends", 0);
		myFriends = new String[sp.getAll().size()];
		int i = 0;
		for (Entry<String, ?> entry : sp.getAll().entrySet()) {
			myFriends[i] = entry.getKey();
			i++;
		}
		final ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, myFriends));
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SelectedFriend = lv.getItemAtPosition(position).toString();
				registerForContextMenu( view );
				view.setLongClickable(false);
				openContextMenu(view);
  

			}
		});
	}
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Friend Menu");
		menu.add(0, v.getId(), 0, "Invite Friend");
		menu.add(0, v.getId(), 0, "Post On Friend's Wall");
		menu.add(0, v.getId(), 0, "Delete Friend");
	}
    
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
        if(item.getTitle()=="Invite Friend To Use Dizzy Head"){function1(item.getItemId());}  
        else if(item.getTitle()=="Post On Friend's Wall"){postToWall();} 
        else if(item.getTitle()=="Delete Friend"){function3(item.getItemId());} 
        else {return false;}  
    return true;  
    }  
  
    public void function1(int id){  
        Toast.makeText(this, "function 1 called", Toast.LENGTH_SHORT).show();  
    }  
    protected void postToWall()
    {
    	SharedPreferences sp = this.getSharedPreferences("friends", 0);
    	String userId = sp.getString(SelectedFriend,"0");
        Bundle params = new Bundle();
        params.putString("message", "");
        params.putString("caption", "{*actor*} just posted a secret message.");
        params.putString("description", "A secret message is waiting for you.  Click the link to decode it.");
        params.putString("name", "A Secret Message For You");
        params.putString("picture", "http://www.kxminteractive.com/Content/images/app_logos/secretMessage.png");
        /*mFacebook.authorize(this, new String[] { "email", "publish_stream" },

        	      new DialogListener() {
        	           public void onComplete(Bundle values) {}

        	           public void onFacebookError(FacebookError error) {}

        	           public void onError(DialogError e) {}

        	           public void onCancel() {}
        	      }
        	);*/
        //mFacebook.dialog(this, "stream.publish", params, new WallPostDialogListener());
        mAsyncRunner.request(((userId == null) ? "me" : userId) + "/feed", params, "POST", new SampleRequestListener(),new Object());       
    }
    public void function3(int id){  
    	SharedPreferences sp = this.getSharedPreferences("friends", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.remove(SelectedFriend);
    	Toast.makeText(this, "Friend Deleted!", Toast.LENGTH_SHORT).show();  
    	editor.commit();
    	updateList();
    } 
    
    class WallPostDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
                    final String postId = values.getString("post_id");
                    if (postId != null) {
                    showToast("Message posted to your facebook wall!");
                } else {
                    showToast("Wall post cancelled!");
                }
                finish();
            }
        public void onFacebookError(FacebookError e) {
            showToast("Failed to post to wall!");
            e.printStackTrace();
            finish();
        }
        public void onError(DialogError e) {
            showToast("Failed to post to wall!");
            e.printStackTrace();
            finish();
        }
        public void onCancel() {
            showToast("Wall post cancelled!");
            finish();
        }
        }
    
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    
   
  

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data != null) {
			Bundle b = data.getExtras();
			myFriends = b.getStringArray("myfriends");
			SharedPreferences sp = this.getSharedPreferences("friends", 0);
			SharedPreferences.Editor editor = sp.edit();
			editor.clear();
			editor.commit();
			for (int i = 0; i < myFriends.length; i++) {
				editor.putString(nameArray[Integer.parseInt(myFriends[i])],
						idArray[Integer.parseInt(myFriends[i])]);
			}
			editor.commit();
			updateList();

		} else
			mFacebook.authorizeCallback(requestCode, resultCode, data);
	}

	public class SampleAuthListener implements AuthListener {

		public void onAuthSucceed() {
			mRequestButton.setVisibility(View.VISIBLE);
			mAsyncRunner.request("me", new SampleRequestListener2());
		}

		public void onAuthFail(String error) {
		}
	}

	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {

		}

		public void onLogoutFinish() {
			mRequestButton.setVisibility(View.INVISIBLE);
		}
	}

	public class SampleRequestListener extends BaseRequestListener {
		public void onComplete(final String response, final Object state) {
			try {
				// process the response here: executed in background thread
				Log.d("Facebook-Example", "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				JSONArray jArray = json.getJSONArray("data");
				JSONObject json_data = null;
				nameArray = new String[jArray.length()];
				idArray = new String[jArray.length()];
				for (int i = 0; i < jArray.length(); i++) {
					json_data = jArray.getJSONObject(i);
					nameArray[i] = json_data.getString("name");
					idArray[i] = json_data.getString("id");
				}
				java.util.Arrays.sort(nameArray);
				Intent i = new Intent(FriendsOLD.this, FriendsList.class);
				Bundle bundle = new Bundle();
				bundle.putStringArray("friends", nameArray);
				bundle.putStringArray("myfriends", myFriends);
				i.putExtras(bundle);
				dialog.dismiss();
				startActivityForResult(i, 0);
				/*
				 * FriendsOLD.this.runOnUiThread(new Runnable() { public void
				 * run() {
				 * 
				 * } });
				 */

				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."

			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}

	
	public class SampleRequestListener2 extends BaseRequestListener {
		public void onComplete(final String response, final Object state) {
			try {
				// process the response here: executed in background thread
				Log.d("Facebook-Example", "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				SaveToDB(json.getString("id"),json.getString("first_name"),json.getString("last_name"),json.getString("gender"),json.getString("birthday"));


			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}

	
	
	private void addTableRow(String friend) {
		final TableLayout table = (TableLayout) findViewById(R.id.my_table);
		final TableRow tr = (TableRow) getLayoutInflater().inflate(
				R.layout.friends_table_item, null);

		// rowCount++;
		// trA[rowCount] = tr;
		TextView tv;
		// Fill out our cells
		tv = (TextView) tr.findViewById(R.id.cell_1);
		String words[] = friend.split("\n");
		String text = null;
		if (!words[2].equalsIgnoreCase("0"))
			text = words[0] + "\nScore: " + words[2] + "\nLast Drink: "
					+ words[3] + "\nSober in " + words[4];
		else
			text = words[0];
		tv.setText(text);
		table.addView(tr);

		tr.setBackgroundColor(10802158);
		// tr.setTag(rowCount);
		// tr.setOnClickListener(myProgressBarShower);

		// Draw separator
		tv = new TextView(this);
		tv.setBackgroundColor(Color.parseColor("#80808080"));
		tv.setHeight(2);
		table.addView(tv);

		// If you use context menu it should be registered for each table row
		registerForContextMenu(tr);
	}


	public void onIOException(IOException e, Object state) {
		// TODO Auto-generated method stub
		
	}


	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		// TODO Auto-generated method stub
		
	}


	public void onMalformedURLException(MalformedURLException e, Object state) {
		// TODO Auto-generated method stub
		
	}


	public void onFacebookError(FacebookError e, Object state) {
		// TODO Auto-generated method stub
		
	}
	
	 public void SaveToDB(final String fbid, final String fname, final String lname, final String gender, final String bday){
	    	Thread t = new Thread() {
	            public void run() {
	            	String result = "";
	            	InputStream is = null;
	            	//http post
	            	try{
	            		SharedPreferences sp = getSharedPreferences("prefile", 0);
	            		SharedPreferences.Editor editor = sp.edit();
	            		editor.putString("fbid",fbid);
	            		editor.commit();
	            		nameValuePairs.add(new BasicNameValuePair("fbid",fbid));
	            		nameValuePairs.add(new BasicNameValuePair("fname",fname));
	            		nameValuePairs.add(new BasicNameValuePair("lname",lname));
	            		nameValuePairs.add(new BasicNameValuePair("email",sp.getString("email",null)));
	        	    	nameValuePairs.add(new BasicNameValuePair("country",sp.getString("country",null)));
	        	    	nameValuePairs.add(new BasicNameValuePair("gender",gender));
	        	    	nameValuePairs.add(new BasicNameValuePair("weight",Integer.toString((int)sp.getFloat("weight",0))));
	        	    	nameValuePairs.add(new BasicNameValuePair("birthday",bday));
	        	    	nameValuePairs.add(new BasicNameValuePair("figure",Integer.toString(sp.getInt("image",0))));
	        	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        	        Date date = new Date();
	        	    	nameValuePairs.add(new BasicNameValuePair("regdate",dateFormat.format(date)));
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
	            	try{
	            		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            	    StringBuilder sb = new StringBuilder();
	            		String line = null;
	            		while ((line = reader.readLine()) != null) {
	            	                sb.append(line + "n");
	            		}
	            	    is.close();
	            	    result=sb.toString();
	            	    System.out.println(result);
	            	}catch(Exception e){
	            		System.out.println("Error converting result "+e.toString());
	            	}
	            }
	        };
	        t.start();
	    }
	


}
