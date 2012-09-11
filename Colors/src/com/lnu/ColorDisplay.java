package com.lnu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ColorDisplay extends Activity {

	private final InputFilter[] inputFilters = new InputFilter[] { new InputFilterMinMax("0", "255") };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_display);

		final EditText redEditText = (EditText) findViewById(R.id.redEditText);
		final EditText greenEditText = (EditText) findViewById(R.id.greenEditText);
		final EditText blueEditText = (EditText) findViewById(R.id.blueEditText);
		
		redEditText.setFilters(inputFilters);
		greenEditText.setFilters(inputFilters);
		blueEditText.setFilters(inputFilters);

		Button displayColorButton = (Button) findViewById(R.id.displayColorButton);
		displayColorButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int r = getIntValue(redEditText.getText().toString());
				int g = getIntValue(greenEditText.getText().toString());
				int b = getIntValue(blueEditText.getText().toString());
				TextView colorTextView = (TextView) findViewById(R.id.colorTextView);
				colorTextView.setBackgroundColor(Color.rgb(r, g, b));
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_color_display, menu);
		return true;
	}

	private int getIntValue(String value) {
		if (value == null || value.trim().isEmpty()) {
			return 0;
		}

		return Math.abs(Integer.parseInt(value));
	}
}

class InputFilterMinMax implements InputFilter {

	private int min, max;

	public InputFilterMinMax(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public InputFilterMinMax(String min, String max) {
		this.min = Integer.parseInt(min);
		this.max = Integer.parseInt(max);
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend) {
		try {
			String startString = dest.toString().substring(0, dstart);
			String insert = source.toString();
			String endString = dest.toString().substring(dend);
			String parseThis = startString + insert + endString;
			int input = Integer.parseInt(parseThis);
			if (isInRange(min, max, input)) {
				return null;
			}
		} catch (NumberFormatException nfe) {
		}
		return "";
	}

	private boolean isInRange(int a, int b, int c) {
		return b > a ? c >= a && c <= b : c >= b && c <= a;
	}
}
