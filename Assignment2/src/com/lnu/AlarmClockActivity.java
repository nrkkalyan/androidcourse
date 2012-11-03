package com.lnu;

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

public class AlarmClockActivity extends Activity implements OnClickListener,
		OnTimeChangedListener {

	private Runnable mTicker;
	private Calendar mCalendar;
	private Handler mHandler;
	private TextView currentTimeTextView;
	private TextView alarmTimeTextView;
	private Button addAlarmButton;
	private Button doneAlarmButton;
	private Button resetAlarmButton;
	private TimePicker timePicker;
	private ImageView bellImageView;
	private PendingIntent pendingIntent;
	private AlarmManager alarmManager;
	private Intent alarmIntent;
	private TextView alarmTextView;
	private static boolean isAlarmSet = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_clock);
		currentTimeTextView = (TextView) findViewById(R.id.currentTimeTextView);
		alarmTimeTextView = (TextView) findViewById(R.id.alarmTimeTextView);
		alarmTextView = (TextView) findViewById(R.id.alarmTextView);

		alarmIntent = new Intent(this, AlarmNotificationActivity.class);
		pendingIntent = PendingIntent.getActivity(this, 0, alarmIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
		setCurrentTime();

		addAlarmButton = (Button) findViewById(R.id.addAlarmButton);
		doneAlarmButton = (Button) findViewById(R.id.doneAlarmButton);
		resetAlarmButton = (Button) findViewById(R.id.resetAlarmButton);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		bellImageView = (ImageView) findViewById(R.id.bellImageView);
		timePicker.setIs24HourView(true);

		addAlarmButton.setOnClickListener(this);
		doneAlarmButton.setOnClickListener(this);
		resetAlarmButton.setOnClickListener(this);
		timePicker.setOnTimeChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		doneAlarmButton.setVisibility(View.INVISIBLE);
		addAlarmButton.setVisibility(View.INVISIBLE);
		resetAlarmButton.setVisibility(View.INVISIBLE);
		timePicker.setVisibility(View.INVISIBLE);
		bellImageView.setVisibility(View.INVISIBLE);
		alarmTextView.setVisibility(View.INVISIBLE);
		alarmTimeTextView.setVisibility(View.INVISIBLE);
		alarmTimeTextView.setText(R.string.default_alarm);
		isAlarmSet = false;
		v.setVisibility(View.INVISIBLE);
		switch (v.getId()) {
			case R.id.addAlarmButton: {
				timePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
				timePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
				timePicker.setVisibility(View.VISIBLE);
				doneAlarmButton.setVisibility(View.VISIBLE);
				onTimeChanged(timePicker, timePicker.getCurrentHour(),
						timePicker.getCurrentMinute());
				isAlarmSet = true;
				break;
			}
			case R.id.doneAlarmButton: {
				setAlarm();
				break;
			}
			case R.id.resetAlarmButton: {
				addAlarmButton.setVisibility(View.VISIBLE);
				alarmTimeTextView.setVisibility(View.VISIBLE);
				alarmManager.cancel(pendingIntent);
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
				currentTimeTextView.setText(DateFormat.format("k:mm:ss",
						mCalendar));
				currentTimeTextView.invalidate();
				long now = SystemClock.uptimeMillis();
				long next = now + 5000 - now % 1000;
				mHandler.postAtTime(mTicker, next);
			}
		};
		mTicker.run();
	}

	protected void setAlarm() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
		cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		cal.set(Calendar.SECOND, 0);

		if (cal.before(mCalendar)) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog
					.setMessage("You can set alarm only after the current time.");
			alertDialog.show();
			addAlarmButton.setVisibility(View.VISIBLE);
			return;
		}

		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);
		// Tell the user about what we did.
		String msg = "Alarm is set for " + cal.getTime();
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		bellImageView.setVisibility(View.VISIBLE);
		alarmTextView.setText("Alarm Set for:" + timePicker.getCurrentHour()
				+ ":" + timePicker.getCurrentMinute());
		resetAlarmButton.setVisibility(View.VISIBLE);
		alarmTextView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isAlarmSet) {
			alarmTextView.setText("Alarm Set for:"
					+ timePicker.getCurrentHour() + ":"
					+ timePicker.getCurrentMinute());
			resetAlarmButton.setVisibility(View.VISIBLE);
			alarmTextView.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_alarm_clock, menu);
		return true;
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		alarmTimeTextView.setText(new StringBuilder()
				.append(pad(view.getCurrentHour())).append(":")
				.append(pad(minute)));
	}

	private static String pad(int c) {
		if (c >= 10) {
			return String.valueOf(c);
		} else {
			return "0" + String.valueOf(c);
		}
	}
}
