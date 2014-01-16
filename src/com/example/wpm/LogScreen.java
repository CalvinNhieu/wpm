package com.example.wpm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;

public class LogScreen extends Activity {
	
	private TextView logDisplay; // displays log data
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_screen);
		
		logDisplay = (TextView) findViewById(R.id.logDisplay); // init textview
		logDisplay.setText(MainActivity.getLogFile().getStringSet(MainActivity.getLogdatakey(), null).toString()); // display text
	}

// return to main menu method
	public void toMain () {
		Intent toMain = new Intent(this, MainActivity.class);
		startActivity(toMain);
		finish();
	}
	
	// Edit back button behaviour
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	toMain();
			finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_screen, menu);
		return true;
	}

	
	// GETTERS AND SETTERS
	public TextView getLogDisplay() {
		return logDisplay;
	}

	public void setLogDisplay(TextView logDisplay) {
		this.logDisplay = logDisplay;
	}

}
