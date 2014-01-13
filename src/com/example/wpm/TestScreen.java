package com.example.wpm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.inputmethodservice.KeyboardView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class TestScreen extends Activity {
	
	EditText editText;
	TextView textView;
	KeyboardView keyView;
	Scanner tester;
	ArrayList<String> passageEntries;
	int correct;
	int wrong;
	String passage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_screen);

		editText = (EditText) findViewById(R.id.user_input_word);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		textView = (TextView) findViewById(R.id.user_input_passage);
				
		final TextView myTextView = (TextView) findViewById(R.id.system_passage);
		new AsyncTask<Void, Void, String>() {
	          @Override
	          protected String doInBackground(Void... unused) {
	              Document doc;
	            try {
	            	Connection conn = Jsoup.connect("http://en.wikipedia.org/wiki/Special:Random");
	        		doc = conn.get();
	           		String data = doc.select("p").first().text();
	        		  data = data.replaceAll("\\(.*\\)", "");
	        		  data = data.replaceAll("\\[.*\\]", "");
	        		  data = data.replaceAll("  ", " ");
	        		  return data;
	              } catch (IOException e) {
	                  e.printStackTrace();
	                  return null;
	              }
	          }

	          @Override
	          protected void onPostExecute(String title) {        
	        	  myTextView.setText(title);
	        	  handleAsyncTaskEnd(myTextView);
	          }  
		}.execute();
	}
	
	private void handleAsyncTaskEnd(TextView myTextView) {
		
		tester = new Scanner(myTextView.getText().toString());
		tester.useDelimiter(" ");
		passageEntries = new ArrayList <String>();
		correct = 0;
		wrong = 0;
		int counter=0;
		System.out.println(myTextView.getText().toString());
		while(tester.hasNext()) {
			passageEntries.add(tester.next());
			counter++;
		}
		System.out.println(counter);
		editText.addTextChangedListener(new TextWatcher() {
			  public void afterTextChanged(Editable s){
				  if (s.length() < 1) {
					  return;
				  }
				  
				  if (s.charAt(s.length()-1) == ' ') {
					  System.out.println(editText.getText().toString().substring(0,editText.getText().toString().length()-1));
					  if ((editText.getText().toString().substring(0,editText.getText().toString().length()-1)).equals(passageEntries.get(correct))) {
						  correct++;
						  textView.setText(textView.getText().toString() + s);
					  }
					  else {
						  wrong++;
					  }
					  editText.setText("");
					  System.out.println("C:"+correct+" W:"+wrong);
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
