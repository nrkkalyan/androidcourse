package com.nrk.ltu;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class GameViewActivity extends Activity implements SensorEventListener {

	private GameView mGameView;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGameView = new GameView(this);
		setContentView(mGameView);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		float R[] = new float[9];
		float orientation[] = new float[3];
		SensorManager.getOrientation(R, orientation);
		mGameView.processOrientationEvent(orientation);
	}

	public void finish(String status) {
		Intent intent = new Intent(this, GameStatusActivity.class);
		intent.putExtra(GameStatusActivity.STATUS, status);
		startActivity(intent);
		super.finish();
	}

}
