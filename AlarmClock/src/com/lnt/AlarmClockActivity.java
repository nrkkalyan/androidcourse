package com.lnt;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class AlarmClockActivity extends Activity implements OnClickListener, OnTimeChangedListener {
	
	private Runnable	mTicker;
	private Calendar	mCalendar;
	private Handler		mHandler;
	private TextView	currentTimeTextView;
	private TextView	alarmTimeTextView;
	private Button		addAlarmButton;
	private Button		doneAlarmButton;
	private TimePicker	timePicker;
	private ImageView	imageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_clock);
		currentTimeTextView = (TextView) findViewById(R.id.currentTimeTextView);
		alarmTimeTextView = (TextView) findViewById(R.id.alarmTimeTextView);
		setCurrentTime();
		
		addAlarmButton = (Button) findViewById(R.id.addAlarmButton);
		doneAlarmButton = (Button) findViewById(R.id.doneAlarmButton);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		imageView = (ImageView) findViewById(R.id.bellImageView);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
		
		addAlarmButton.setOnClickListener(this);
		doneAlarmButton.setOnClickListener(this);
		timePicker.setOnTimeChangedListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		doneAlarmButton.setVisibility(View.INVISIBLE);
		addAlarmButton.setVisibility(View.INVISIBLE);
		timePicker.setVisibility(View.INVISIBLE);
		v.setVisibility(View.INVISIBLE);
		switch (v.getId()) {
			case R.id.addAlarmButton: {
				timePicker.setVisibility(View.VISIBLE);
				doneAlarmButton.setVisibility(View.VISIBLE);
				onTimeChanged(timePicker, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
				break;
			}
			case R.id.doneAlarmButton: {
				setAlarm();
				addAlarmButton.setVisibility(View.VISIBLE);
				break;
			}
		}
		
	}
	
	private void setCurrentTime() {
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}
		mHandler = new Handler();
		mTicker = new Runnable() {
			@Override
			public void run() {
				mCalendar.setTimeInMillis(System.currentTimeMillis());
				currentTimeTextView.setText(DateFormat.format("k:mm:ss", mCalendar));
				currentTimeTextView.invalidate();
				long now = SystemClock.uptimeMillis();
				long next = now + (5000 - now % 1000);
				mHandler.postAtTime(mTicker, next);
			}
		};
		mTicker.run();
	}
	
	protected void setAlarm() {
		Integer alarmHour = timePicker.getCurrentHour();
		Integer alarmMinute = timePicker.getCurrentMinute();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, alarmHour);
		cal.set(Calendar.MINUTE, alarmMinute);
		cal.set(Calendar.SECOND, 0);
		
		if (cal.before(mCalendar)) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setMessage("You can set alarm only after the current time.");
			alertDialog.show();
			return;
		}
		
		// Create a new PendingIntent and add it to the AlarmManager
		Intent intent = new Intent(this, AlarmNotificationActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
		// Tell the user about what we did.
		String msg = "Alarm is set for " + cal.getTime();
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		imageView.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		imageView.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_alarm_clock, menu);
		return true;
	}
	
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		alarmTimeTextView.setText(new StringBuilder().append(pad(view.getCurrentHour())).append(":")
				.append(pad(minute)));
	}
	
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
}
