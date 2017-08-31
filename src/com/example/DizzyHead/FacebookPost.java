package com.example.DizzyHead;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.BaseKeyListener;
import android.widget.Toast;



public class FacebookPost extends Activity{

    private static final String APP_ID = "163122417088005";
    private static final String[] PERMISSIONS = new String[] {"publish_stream"};

    
    private Facebook facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebook = new Facebook(APP_ID);
        Bundle extras = getIntent().getExtras();
        Bundle parameters = new Bundle();/*
        parameters.putString("message", extras.getString("text")+ " Check out Dizzy Head at http://www.dizzy-head.com");
        facebook.dialog(this, "stream.publish", parameters, new WallPostDialogListener());*/
        loginAndPostToWall();
        postToWall(extras.getString("text")+ " Check out Dizzy Head at http://www.dizzy-head.com");
    }

    public void share(){
        if (! facebook.isSessionValid()) {
            loginAndPostToWall();
        }
    }

    public void loginAndPostToWall(){
         facebook.authorize(this, PERMISSIONS, new LoginDialogListener());
    }

    public void postToWall(String message){
        Bundle parameters = new Bundle();
            parameters.putString("message", message);
            facebook.dialog(this, "stream.publish", parameters, new WallPostDialogListener());
            
    }
    

    class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
           
        }
        public void onFacebookError(FacebookError error) {
            showToast("Authentication with Facebook failed!");
            finish();
        }
        public void onError(DialogError error) {
            showToast("Authentication with Facebook failed!");
            finish();
        }
        public void onCancel() {
            showToast("Authentication with Facebook cancelled!");
            finish();
        }
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
    
    public class FriendListRequestListener extends BaseKeyListener {

        public void onComplete(final String response) {
            
        }

		public int getInputType() {
			// TODO Auto-generated method stub
			return 0;
		}
    }

}
