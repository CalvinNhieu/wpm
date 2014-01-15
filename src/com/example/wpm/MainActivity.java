package com.example.wpm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
