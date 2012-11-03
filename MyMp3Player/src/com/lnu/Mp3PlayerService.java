package com.lnu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class Mp3PlayerService extends Service implements OnCompletionListener {

	private static int lastSelectedIndex = -1;
	private MediaPlayer mp;
	private final List<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	private final IBinder mBinder = new LocalBinder();
	private NotificationManager mNM;
	private final int NOTIFICATION = 1;// R.string.local_service_started;

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		Mp3PlayerService getService() {
			return Mp3PlayerService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void addSong(HashMap<String, String> map) {
		songsList.add(map);
	}

	public void clear() {
		songsList.clear();
	}

	public List<HashMap<String, String>> getSongs() {
		return songsList;
	}

	public void playPrevSong() {
		playSong(lastSelectedIndex - 1);
	}

	public void playNextSong() {
		playSong(lastSelectedIndex + 1);
	}

	public void stopSong() {
		if (mp == null) {
			return;
		}
		mp.stop();
		mp = null;
	}

	public String playSong() {

		if (lastSelectedIndex == -1) {
			playSong(0);
		} else if (mp == null) {
			playSong(lastSelectedIndex);
		} else if (mp.isPlaying()) {
			mp.pause();
		} else {
			mp.start();
			return "Pause";
		}
		return "Play";
	}

	public boolean isPlaying() {

		if (mp == null) {
			return false;
		}
		return mp.isPlaying();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		lastSelectedIndex += 1;
		if (lastSelectedIndex >= songsList.size()) {
			lastSelectedIndex = 0;
		}
		playSong(lastSelectedIndex);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Display a notification about us starting. We put an icon in the
		// status bar.
		showNotification();
		if (mp == null) {
			mp = new MediaPlayer();
			mp.setOnCompletionListener(this);
		}
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = "Mp3Player Started";

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.ic_launcher,
				text, System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MyMp3Player.class), 0);

		// Set the info for the views that show in the notification panel.
		notification
				.setLatestEventInfo(this, "Mp3Service", text, contentIntent);

		// Send the notification.
		mNM.notify(NOTIFICATION, notification);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	// @Override
	public void stopService() {
		// Cancel the persistent notification.
		mNM.cancel(NOTIFICATION);

		// Tell the user we stopped.
		Toast.makeText(this, "Mp3Player Stopped", Toast.LENGTH_SHORT).show();
	}

	public void playSong(int position) {
		try {
			if (songsList.isEmpty()) {
				throw new RuntimeException("No Mp3 Songs found.");
			}
			if (position < 0) {
				position = 0;
			} else if (position >= songsList.size()) {
				position = songsList.size() - 1;
			}
			lastSelectedIndex = position;
			if (mp == null) {
				mp = new MediaPlayer();
				mp.setOnCompletionListener(this);
			}
			mp.reset();
			mp.setDataSource(songsList.get(position).get("songPath"));
			mp.prepare();
			mp.start();
		} catch (Exception e) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setMessage(e.getMessage());
			alertDialog.show();
		}
	}

	public void seekToCurrentPos(int progress) {

		int totalDuration = mp.getDuration() / 1000;
		int curPos = (int) ((double) progress / 100 * totalDuration) * 1000;
		mp.seekTo(curPos);
	}

	public int getCurrentProgress() {
		if (mp == null) {
			return 0;
		}
		long totalDuration = mp.getDuration();
		long currentDuration = mp.getCurrentPosition();
		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);
		return Double.valueOf((double) currentSeconds / totalSeconds * 100)
				.intValue();
	}
}
