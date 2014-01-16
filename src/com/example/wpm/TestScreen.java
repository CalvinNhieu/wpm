package com.example.wpm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class TestScreen extends Activity {
		
	private EditText inputField; // user-single word input text field
	private TextView completedWords; // displays words that user has completed
	private Scanner tester; // manipulates scraped article
	private ArrayList<String> passageEntries; // holds article in individual strings
	
	private AlertDialog.Builder builder1; // popup builder
	private AlertDialog alert1; // notification window
	
	private int correct; // counts # of correct words typed
	private int mistakes; // counts # of wrong words typed
	private long startTime; // time test is started
	private long timeElapsed; // time test took to complete
	private double t; // double type of timeElapsed
	private double wpm; // user's wpm result
	private boolean finished; // whether or not test is complete
	
	private Set<String> logs; // set of strings to store in sharedpreferences
	private String logsDataKey; // key value to access log data within internal app storage
	
	// creates activity
	// switches to activity
	// instantiates textview and inputview
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_screen);

		inputField = (EditText) findViewById(R.id.user_input_word);
		inputField.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		completedWords = (TextView) findViewById(R.id.user_input_passage);
		
		// create global textview to display article scraped from web
		// AsyncTask attempts to connect to the web and pull a random
		// Wikipedia page in the form of a document.
		// A string is parsed from the document as the test passage
		// variable data is sent to onPostExecute to display text and
		// continues to execute the program "handleAsyncTaskEnd" 
		final TextView passageDisplay = (TextView) findViewById(R.id.system_passage);
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
	        	  passageDisplay.setText(title);
	        	  handleAsyncTaskEnd(passageDisplay);
	          }  
		}.execute();
	}
	
	// ...the rest of the program...
	// scanner individualizes the entire string pulled from web
	// into single strings stored in an Arraylist, "passageEntries"
	// other variables are instantiated
	private void handleAsyncTaskEnd(TextView passageDisplay) {
		
		tester = new Scanner(passageDisplay.getText().toString());
		tester.useDelimiter(" ");
		passageEntries = new ArrayList <String>();
		correct = 0;
		mistakes = 0;
		startTime = 0;
		timeElapsed = 0;
		finished = false;
		
		logsDataKey = "log_data_key";
		logs = new HashSet<String>();
		
		while(tester.hasNext()) {
			passageEntries.add(tester.next());
		}
		
		// logic for dynamically checking user input
		// addTextChangedListener implements 3 abstract methods...
		// using only "afterTextChanged" to listen for:
		
		// INITIAL INPUT
		// start timer
		
		// SPACEBAR INPUT
		// Clear input field, perform string comparison, check
		// for passage completion
		
		inputField.addTextChangedListener(new TextWatcher() {
			  public void afterTextChanged(Editable s){
				  if (s.length() < 1) {
					  return;
				  }
				  if(startTime==0 && correct == 0 && mistakes == 0) {
					  startTime = System.currentTimeMillis();
				  }
				  
				  if (s.charAt(s.length()-1) == ' ' && !finished && passageEntries.get(correct) != null) {
					  if ((inputField.getText().toString().substring(0,inputField.getText().toString().length()-1)).equals(passageEntries.get(correct))) {
						  completedWords.setText(completedWords.getText().toString() + s);
						  if (correct == passageEntries.size()-1) {
							  timeElapsed = System.currentTimeMillis() - startTime;
							  endTest(timeElapsed, correct);
							  return;
						  }
						  correct++;
					  }
					  else {
						  mistakes++;
					  }
					  inputField.setText("");
				  }
			  }
		      public void beforeTextChanged(CharSequence s, int start, int count,int after){}
		      public void onTextChanged(CharSequence s, int start, int before,int count){}
		});
	}
	
	// when test has been completed
	// appropriately parse time variable to double
	// calculate words per minute
	// display one of two dialogs:
	
	// COMPLETED
	// Displays words per minute result
	// Prompts to add test to log
	
	// FAILED
	// User did not complete test within 10minutes (600 000 ms)
	// User must retry test ...
	public void endTest (long time, int numOfWords) {
		finished = true;
		if (time < 600000) {
			t= (double) time;
			t /= 60000;
			wpm = (double) (numOfWords/t);
			wpm = Math.round(wpm);
			
			//dialog 1
			builder1 = new AlertDialog.Builder(this);
			builder1.setMessage("Results: " + wpm + " words/min");
			builder1.setCancelable(true);
			builder1.setPositiveButton("Send to Log",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					try {
						sendToLog(wpm,Math.round(t*60),correct,mistakes);
					} catch (IOException e) {
						e.printStackTrace();
					}
					toMain();
				}
			});
			builder1.setNegativeButton("Trash",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					toMain();
				}
			});
		}
		//dialog 2
		else {
			builder1 = new AlertDialog.Builder(this);
			builder1.setMessage("You have exceeded the maximum time limit to complete the test. Please try again.");
			builder1.setCancelable(true);
			builder1.setPositiveButton("Return",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					toMain();
				}
			});
		}
		//create dialog with appropriate settings
		alert1 = builder1.create();
        alert1.show();
	}
	
	public void sendToLog(double wpm, double timeInSeconds, int totalWords, int mistakes) throws IOException {
		
		if (MainActivity.getLogFile().getStringSet(logsDataKey,null) != null) {
			logs.addAll(MainActivity.getLogFile().getStringSet(logsDataKey, null));
		}
		logs.add("WPM: "  + wpm + " Time: " + timeInSeconds + " Typed: " + totalWords + " Mistakes: " +  mistakes + " ||| ");
		SharedPreferences.Editor editor = MainActivity.getLogFile().edit();
		editor.putStringSet(logsDataKey, logs);
		editor.commit();
	}
	
	// return to main menu method
	public void toMain() {
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
	
	public Context getTestScreen () {
		return this;
	}
	
	// abstract implementation
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	// GETTERS AND SETTERS
	public EditText getInputField() {
		return inputField;
	}

	public void setInputField(EditText inputField) {
		this.inputField = inputField;
	}

	public TextView getCompletedWords() {
		return completedWords;
	}

	public void setCompletedWords(TextView completedWords) {
		this.completedWords = completedWords;
	}

	public Scanner getTester() {
		return tester;
	}

	public void setTester(Scanner tester) {
		this.tester = tester;
	}

	public ArrayList<String> getPassageEntries() {
		return passageEntries;
	}

	public void setPassageEntries(ArrayList<String> passageEntries) {
		this.passageEntries = passageEntries;
	}

	public AlertDialog.Builder getBuilder1() {
		return builder1;
	}

	public void setBuilder1(AlertDialog.Builder builder1) {
		this.builder1 = builder1;
	}

	public AlertDialog getAlert1() {
		return alert1;
	}

	public void setAlert1(AlertDialog alert1) {
		this.alert1 = alert1;
	}

	public int getCorrect() {
		return correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public int getMistakes() {
		return mistakes;
	}

	public void setMistakes(int mistakes) {
		this.mistakes = mistakes;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(long timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public double getWpm() {
		return wpm;
	}

	public void setWpm(double wpm) {
		this.wpm = wpm;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Set<String> getLogs() {
		return logs;
	}

	public void setLogs(Set<String> logs) {
		this.logs = logs;
	}

	public String getLogsDataKey() {
		return logsDataKey;
	}

	public void setLogsDataKey(String logsDataKey) {
		this.logsDataKey = logsDataKey;
	}
	
	

}
