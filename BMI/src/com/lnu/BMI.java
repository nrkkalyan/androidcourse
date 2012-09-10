package com.lnu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BMI extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bmi);

		final EditText lengthEditText = (EditText) findViewById(R.id.lengthEditText);
		final EditText weightEditText = (EditText) findViewById(R.id.weightEditText);
		final TextView resultTextView = (TextView) findViewById(R.id.resultTextView);

		Button computeButton = (Button) findViewById(R.id.computeButton);
		computeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				double length = getLongValue(lengthEditText.getText().toString());
				double weight = getLongValue(weightEditText.getText().toString());
				double result = getBMI(length, weight);
				resultTextView.setText(result + "");
//				Toast.makeText(BMI.this, resultTextView.getText(), Toast.LENGTH_SHORT).show();

			}
		});
		Button resetButton = (Button) findViewById(R.id.resetButton);
		resetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lengthEditText.setText("");
				weightEditText.setText("");
				resultTextView.setText("");
			}
		});
	}

	protected double getLongValue(String value) {
		if (value == null || value.trim().isEmpty()) {
			return 0;
		}

		return Math.abs(Double.parseDouble(value));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_bmi, menu);
		return true;
	}

	public double getBMI(double height,double weight) {
		//To make sure both values are valid.
		if(height+weight <= 0){
			return 0;
		}
		double bmi = weight * Math.pow(100, 2) 
				/ Math.pow(height, 2);
		return Math.round(bmi * 100) / 100.0;
	}
}
