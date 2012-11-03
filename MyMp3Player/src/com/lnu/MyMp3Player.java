package com.lnu;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;

import com.lnu.Mp3PlayerService.LocalBinder;

public class MyMp3Player extends ListActivity implements OnClickListener,
		OnItemClickListener, SeekBar.OnSeekBarChangeListener {
	//
	private final Handler mHandler = new Handler();
	private Button nextButton;
	private Button playButton;
	private Button prevButton;
	private SeekBar songProgressBar;
	private static Mp3PlayerService mService;
	private static boolean mBound = false;

	private Button stopButton;

	private final Runnable mUpdateTimeTask = new Runnable() {
		@Override
		public void run() {
			songProgressBar.setProgress(mService.getCurrentProgress());
			mHandler.postDelayed(this, 100);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_mp3player);
		try {

			ListView lv = getListView();
			lv.setOnItemClickListener(this);
			playButton = (Button) findViewById(R.id.playButton);
			playButton.setOnClickListener(this);
			nextButton = (Button) findViewById(R.id.nextButton);
			nextButton.setOnClickListener(this);
			prevButton = (Button) findViewById(R.id.prevButton);
			prevButton.setOnClickListener(this);
			stopButton = (Button) findViewById(R.id.stopButton);
			stopButton.setOnClickListener(this);
			songProgressBar = (SeekBar) findViewById(R.id.seekBar);
			songProgressBar.setOnSeekBarChangeListener(this);

			if (!mBound) {
				Intent intent = new Intent(this, Mp3PlayerService.class);
				bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
			}

			if (mService != null && mService.isPlaying()) {
				playButton.setText("Pause");
				updateProgressBar();
			}

			String[] from = { "name" };
			int[] to = { android.R.id.text1 };
			ListAdapter adapter = new SimpleAdapter(MyMp3Player.this,
					SongsManager.getSongsList(),
					android.R.layout.simple_list_item_1, from, to);
			setListAdapter(adapter);

		} catch (Exception e) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setMessage(e.getMessage());
			alertDialog.show();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound && !mService.isPlaying()) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	private void loadSongs() {
		if (mService == null) {
			return;
		}
		mService.clear();
		for (HashMap<String, String> song : SongsManager.getSongsList()) {
			mService.addSong(song);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.playButton: {
				mService.playSong();
				break;
			}
			case R.id.nextButton: {
				mService.playNextSong();
				break;
			}
			case R.id.prevButton: {
				mService.playPrevSong();

				break;
			}
			case R.id.stopButton: {
				mService.stopSong();
				mHandler.removeCallbacks(mUpdateTimeTask);
				songProgressBar.setProgress(0);
				playButton.setText("Play");

				break;
			}
		}
		if (mService.isPlaying()) {
			playButton.setText("Pause");
			updateProgressBar();
		} else {
			playButton.setText("Play");
		}
	}

	private void updateProgressBar() {
		playButton.setText("Pause");
		songProgressBar.setProgress(0);
		songProgressBar.setMax(100);
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private final ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			try {
				LocalBinder binder = (LocalBinder) service;
				mService = binder.getService();
				mBound = true;
				loadSongs();

			} catch (Exception e) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						MyMp3Player.this).create();
				alertDialog.setMessage(e.getMessage());
				alertDialog.show();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_my_mp3player, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mService.playSong(position);
		playButton.setText("Pause");
		updateProgressBar();
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		mService.seekToCurrentPos(seekBar.getProgress());
		updateProgressBar();
	}

}
