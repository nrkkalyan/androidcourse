package com.lnt;

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
	private int	soundID;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_notification);
		// Load the sound
		final SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				playSound(soundPool);
			}
		});
		soundID = soundPool.load(this, R.raw.smokealarm, 1);
		
		Button stopAlarm = (Button) findViewById(R.id.stopAlarmButton);
		stopAlarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				soundPool.stop(soundID);
				soundPool.unload(soundID);
				finish();
			}
		});
		
	}
	
	private void playSound(SoundPool soundPool) {
		
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float actualVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		Toast.makeText(this, "Wake up....", Toast.LENGTH_LONG).show();
		soundPool.play(soundID, volume, volume, 1, -1, 1f);
	}
	
}
