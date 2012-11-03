package com.lnu;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AlarmNotificationActivity extends Activity {
	
	SoundPool	soundPool	= null;
	int			soundID		= 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_notification);
		// Load the sound
		loadSoundPool();
		
		Button stopAlarm = (Button) findViewById(R.id.stopAlarmButton);
		stopAlarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				soundPool.stop(soundID);
				finish();
			}
		});
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		soundPool.release();
		soundPool = null;
	}
	
	private void loadSoundPool() {
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		soundID = soundPool.load(this, R.raw.cowbell, 1);
		
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				Toast.makeText(AlarmNotificationActivity.this, "Wake up....", Toast.LENGTH_LONG).show();
				soundPool.play(soundID, 1, 1, 0, -1, 1);
			}
		});
	}
	
}
