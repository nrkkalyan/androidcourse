package com.glowingpigs.savepreferences;


import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

/* This archive file contains the source code for examples discussed in Tutorials 10-11 of developerglowingpigs YouTube channel.
 *  The source code is for your convenience purposes only. The source code is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/


public class SavePreferencesActivity extends Activity {
    

	private static final String TAG = "SavePreferences: ";
	private CheckBox checkBox;
	private RadioButton radioButton1;
	private RadioButton radioButton2;
	private Button buttonPlayStop;
	private Boolean boolMusicPlaying = false;
	private static final String MUSIC_PLAYING = "Music_Playing";
	
	private SharedPreferences prefs;
	private String prefName = "MyPref";
	private boolean radioButton1_isChecked;
	private boolean radioButton2_isChecked;
	private boolean checkBox1_isChecked;
	private static final String CHECKBOX1_STATE = "checkBox1_State";
	private static final String RADIOBUTTON1_STATE = "radioButton1_State";
	private static final String RADIOBUTTON2_STATE = "radioButton2_State";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        checkBox = (CheckBox) findViewById(R.id.checkBox1);
      
       
        buttonPlayStop = (Button) findViewById(R.id.ButtonPlayStop);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        
        setButtonPlayStop();
        
        //--- Listener for ButtonPlayStop ---
        buttonPlayStop.setOnClickListener(new View.OnClickListener() {
		//If the music is not playing and the button is clicked, display pause button, else...
			public void onClick(View v) {
				if (! boolMusicPlaying) {
					buttonPlayStop.setBackgroundResource(R.drawable.pausebuttonsm);
					boolMusicPlaying = true;
				} else {
					buttonPlayStop.setBackgroundResource(R.drawable.playbuttonsm);
					boolMusicPlaying = false;
				}
			}
		});       
    }

   //---Initialize ButtonPlayStop...---
    protected void setButtonPlayStop(){
    	 // If the music is not playing, display play button, else...
    	if (! boolMusicPlaying) {
			buttonPlayStop.setBackgroundResource(R.drawable.playbuttonsm);
		} else {
			buttonPlayStop.setBackgroundResource(R.drawable.pausebuttonsm);
		}
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(TAG, "onDestroy()");
	}

	//--- On pause, save screen data ---
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v(TAG, "onPause()");
		
		if (checkBox.isChecked()) {
        	checkBox1_isChecked = true;
        } else {
        	checkBox1_isChecked = false;
        }
		
		 if (radioButton1.isChecked()) {
	            radioButton1_isChecked = true;
	        } else {
	        	radioButton1_isChecked = false;
	    	}
		 
		 if (radioButton2.isChecked()) {
	            radioButton2_isChecked = true;
	        } else {
	        	radioButton2_isChecked = false;
	    	}
		 
		prefs = getSharedPreferences(prefName, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(CHECKBOX1_STATE, checkBox1_isChecked);
		editor.putBoolean(RADIOBUTTON1_STATE, radioButton1_isChecked);
		editor.putBoolean(RADIOBUTTON2_STATE, radioButton2_isChecked);
		
		editor.putBoolean(MUSIC_PLAYING, boolMusicPlaying);
		editor.commit();


		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.v(TAG, "onRestart()");
	}

	//--- On Resume, retrieve screen data---
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "onResume()");	
		
		prefs = getSharedPreferences(prefName, MODE_PRIVATE);
		checkBox.setChecked(prefs.getBoolean(CHECKBOX1_STATE, false));
		radioButton1.setChecked(prefs.getBoolean(RADIOBUTTON1_STATE, false));
		radioButton2.setChecked(prefs.getBoolean(RADIOBUTTON2_STATE, false));
		
		boolMusicPlaying = prefs.getBoolean(MUSIC_PLAYING, false);
		setButtonPlayStop();
	
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v(TAG, "onStart()");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v(TAG, "onStop()");
	}

	// --- Code can go here for saving instance state, but this is not always called ---
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		Log.v(TAG, "onSaveInstanceState()");
		
	}
	
	// --- Code can go here for restoring instance state, but this is not always called ---
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		Log.v(TAG, "onRestoreInstanceState()");
		
		
		
	}
    
}

