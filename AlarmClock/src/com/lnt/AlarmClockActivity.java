package com.lnt;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class AlarmClockActivity extends Activity implements OnClickListener, OnTimeChangedListener {
	
	private Runnable	mTicker;
	private boolean		mTickerStopped	= false;
	private Calendar	mCalendar;
	private Handler		mHandler;
	private TextView	currentTimeTextView;
	private TextView	alarmTimeTextView;
	private Button		addAlarmButton;
	private Button		resetAlarmButton;
	private Button		doneAlarmButton;
	private TimePicker	timePicker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_clock);
		currentTimeTextView = (TextView) findViewById(R.id.currentTimeTextView);
		alarmTimeTextView = (TextView) findViewById(R.id.alarmTimeTextView);
		setCurrentTime();
		
		addAlarmButton = (Button) findViewById(R.id.addAlarmButton);
		resetAlarmButton = (Button) findViewById(R.id.resetAlarmButton);
		doneAlarmButton = (Button) findViewById(R.id.doneAlarmButton);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		timePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
		timePicker.setIs24HourView(true);
		
		addAlarmButton.setOnClickListener(this);
		resetAlarmButton.setOnClickListener(this);
		doneAlarmButton.setOnClickListener(this);
		timePicker.setOnTimeChangedListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.addAlarmButton: {
				timePicker.setVisibility(View.VISIBLE);
				doneAlarmButton.setVisibility(View.VISIBLE);
				addAlarmButton.setVisibility(View.INVISIBLE);
				resetAlarmButton.setVisibility(View.INVISIBLE);
				onTimeChanged(timePicker, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
				break;
			}
			case R.id.resetAlarmButton: {
				addAlarmButton.setVisibility(View.VISIBLE);
				timePicker.setVisibility(View.INVISIBLE);
				doneAlarmButton.setVisibility(View.INVISIBLE);
				resetAlarmButton.setVisibility(View.INVISIBLE);
				alarmTimeTextView.setText(R.string.default_alarm);
				break;
			}
			case R.id.doneAlarmButton: {
				resetAlarmButton.setVisibility(View.VISIBLE);
				timePicker.setVisibility(View.INVISIBLE);
				doneAlarmButton.setVisibility(View.INVISIBLE);
				break;
			}
		}
	}
	
	private void setCurrentTime() {
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}
		mTickerStopped = false;
		mHandler = new Handler();
		mTicker = new Runnable() {
			@Override
			public void run() {
				if (mTickerStopped)
					return;
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
	
	@Override
	protected void onStop() {
		super.onStop();
		mTickerStopped = true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_alarm_clock, menu);
		return true;
	}
	
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		alarmTimeTextView.setText(new StringBuilder().append(pad(hourOfDay)).append(":").append(pad(minute)));
	}
	
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
}
