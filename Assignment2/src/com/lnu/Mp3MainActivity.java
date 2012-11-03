/*   
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lnu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lnu.musicplayer.MusicService;

/**
 * Main activity: shows media player buttons. This activity shows the media
 * player buttons and lets the user click them. No media handling is done here
 * -- everything is done by passing Intents to our {@link MusicService}.
 * */
public class Mp3MainActivity extends Activity implements OnClickListener {
	/**
	 * The URL we suggest as default when adding by URL. This is just so that
	 * the user doesn't have to find an URL to test this sample.
	 */
	final String SUGGESTED_URL = "http://www.vorbis.com/music/Epoq-Lepidoptera.ogg";

	Button mPlayButton;
	Button mPauseButton;
	Button mNextButton;
	Button mPrevButton;
	Button mStopButton;

	/**
	 * Called when the activity is first created. Here, we simply set the event
	 * listeners and start the background service ({@link MusicService}) that
	 * will handle the actual media playback.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_player);

		mPlayButton = (Button) findViewById(R.id.playbutton);
		mPauseButton = (Button) findViewById(R.id.pausebutton);
		mNextButton = (Button) findViewById(R.id.nextbutton);
		mPrevButton = (Button) findViewById(R.id.prevbutton);
		mStopButton = (Button) findViewById(R.id.stopbutton);

		mPlayButton.setOnClickListener(this);
		mPauseButton.setOnClickListener(this);
		mNextButton.setOnClickListener(this);
		mPrevButton.setOnClickListener(this);
		mStopButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View target) {
		// Send the correct intent to the MusicService, according to the button
		// that was clicked
		if (target == mPlayButton) {
			startService(new Intent(MusicService.ACTION_PLAY));
		} else if (target == mPauseButton) {
			startService(new Intent(MusicService.ACTION_PAUSE));
		} else if (target == mNextButton) {
			startService(new Intent(MusicService.ACTION_NEXT));
		} else if (target == mPrevButton) {
			startService(new Intent(MusicService.ACTION_PREV));
		} else if (target == mStopButton) {
			startService(new Intent(MusicService.ACTION_STOP));
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			case KeyEvent.KEYCODE_HEADSETHOOK:
				startService(new Intent(MusicService.ACTION_TOGGLE_PLAYBACK));
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
