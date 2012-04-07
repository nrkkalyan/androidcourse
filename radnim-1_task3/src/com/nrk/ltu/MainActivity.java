package com.nrk.ltu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.main);
	// Button button = (Button) findViewById(R.id.buttonPlay);
	// button.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// Intent intent = new Intent(MainActivity.this, GameViewActivity.class);
	// startActivity(intent);
	// }
	// });
	//
	// }

	private GameView mGameView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGameView = new GameView(this);
		setContentView(mGameView);

	}

	public void finish(String status) {
		Intent intent = new Intent(this, GameStatusActivity.class);
		intent.putExtra(GameStatusActivity.STATUS, status);
		startActivity(intent);
		super.finish();
	}

}
