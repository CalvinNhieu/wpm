package com.example.wpm;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;

public class AboutScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_screen);
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
		
	// return to main menu method
	public void toMain () {
		Intent toMain = new Intent(this, MainActivity.class);
		startActivity(toMain);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
