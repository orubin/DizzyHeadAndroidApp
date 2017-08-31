package com.example.DizzyHead;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

public class About extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setText("Dizzy Head, the safest drinking game, combining a funny drinking game with an alcohol meter.\n"
			+"Dizzy head will track your drinking habits, score your alcohol capabilities, and in the same time will help you track your alcohol blood level.\n\n"
			+"For more information:");
        tv = (TextView) findViewById(R.id.textView3);
        tv.setText(Html.fromHtml("<a href='http://www.dizzy-head.com'>www.dizzy-head.com</a>\n"));
        tv.setAutoLinkMask(Linkify.WEB_URLS);
        tv = (TextView) findViewById(R.id.textView4);
        tv.setText("\nDesigned and created by Or Rubin and Yossi Levi.\n\n"
    			+"Warning! Our company accepts no liability for the content and information"
    			+" presented in this application, or for the consequences of any actions taken on the basis of the information provided.");
        
   }
}
