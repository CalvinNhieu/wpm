package com.example.wpm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public final static String logDataKey = "log_data_key";
	
	private final static String errorString = "Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] flg=0x10000000 cmp=com.example.wpm/.MainActivity }";	
	
	private static SharedPreferences logFile; // access to app's internal data storage
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        logFile = this.getPreferences(Context.MODE_PRIVATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    // to change to test screen
    public void startTest(View view) {
        Intent toTestScreen = new Intent(this, TestScreen.class);
        startActivity(toTestScreen);
        finish();
    }
    
    // to change to about screen
    public void startAbout (View view) {
    	Intent toAboutScreen = new Intent(this,AboutScreen.class);
    	startActivity(toAboutScreen);
    	finish();
    }
    
    // to change to log screen
    public void startLog (View view) {
    	Intent toLogScreen = new Intent(this,LogScreen.class);
    	startActivity(toLogScreen);
    	finish();
    }

    // GETTERS AND SETTERS
	public static String getExtraMessage() {
		return EXTRA_MESSAGE;
	}


	public static String getErrorstring() {
		return errorString;
	}


	public static SharedPreferences getLogFile() {
		return logFile;
	}


	public void setLogFile(SharedPreferences logFile) {
		MainActivity.logFile = logFile;
	}


	public static String getLogdatakey() {
		return logDataKey;
	}
    
    
    
}
