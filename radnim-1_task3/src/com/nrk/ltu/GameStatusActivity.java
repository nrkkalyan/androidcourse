package com.nrk.ltu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameStatusActivity extends Activity {
	public static final String STATUS = "Status";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.loosegame);
		Intent intent = getIntent();
		String status = intent.getExtras().getString(STATUS);

		TextView textView = (TextView) findViewById(R.id.textView1);
		textView.setText(status);
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GameStatusActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}
}
