package com.example.wpm;

import android.app.Activity;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class TestScreen extends Activity {
	
	EditText editText;
	TextView textView;
	KeyboardView keyView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_screen);
		editText = (EditText) findViewById(R.id.user_input_word);
		textView = (TextView) findViewById(R.id.user_input_passage);
		editText.addTextChangedListener(new TextWatcher() {
			  public void afterTextChanged(Editable s){
				  if (s.length() < 1) {
					  return;
				  }
				  if (s.charAt(s.length()-1) == ' ') {
					  editText.setText("");
					  textView.setText(textView.getText().toString() + s);
				  }
			  }
		      public void beforeTextChanged(CharSequence s, int start, int count,int after){}
		      public void onTextChanged(CharSequence s, int start, int before,int count){}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_screen, menu);
		return true;
	}
	

}
