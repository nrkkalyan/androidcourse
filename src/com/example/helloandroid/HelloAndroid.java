package com.example.helloandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class HelloAndroid extends Activity implements View.OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button firstButton = (Button) findViewById(R.id.button1);
		final Button secondButton = (Button) findViewById(R.id.button2);

		firstButton.setOnClickListener(this);
		secondButton.setOnClickListener(this);

	}

	public void onClick(View view) {
		CharSequence text = "Hello toast!";
		ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
		ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
		if (view.getId() == R.id.button1) {
			imageView1.setVisibility(View.VISIBLE);
			imageView1.setBackgroundResource(R.drawable.staron);
			imageView2.setVisibility(View.GONE);
			text = "Shining Star!";
		} else if (view.getId() == R.id.button2) {
			imageView1.setVisibility(View.GONE);
			imageView2.setVisibility(View.VISIBLE);
			imageView2.setBackgroundResource(R.drawable.staroff);
			text = "Black Star!";
		}

		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

}