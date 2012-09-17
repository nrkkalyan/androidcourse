package com.lnu;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddCountry extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_country);
		final EditText countryEditText = (EditText) findViewById(R.id.countryEditText);
		final EditText yearEditText = (EditText) findViewById(R.id.yearEditText);
		final Button addCountryButton = (Button) findViewById(R.id.addCountryButton);
		addCountryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String year = yearEditText.getText().toString();
				String country = countryEditText.getText().toString();
				try {
					validate(year, country);
					Intent outData = new Intent();
					outData.putExtra(MyCountries.COUNTRY, year + " " + country);
					setResult(Activity.RESULT_OK, outData);
					finish();
				} catch (IllegalArgumentException e) {
					AlertDialog alertDialog = new AlertDialog.Builder(AddCountry.this).create();
					alertDialog.setMessage(e.getMessage());
					alertDialog.show();
				}
				
			}
		});
		
	}
	
	private void validate(String year, String country) {
		if (year.isEmpty() || country.trim().isEmpty()) {
			throw new IllegalArgumentException("Please enter both year and country.");
		}
		Integer y = Integer.valueOf(year);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		if (y < 1900 || y > currentYear) {
			throw new IllegalArgumentException("Year must be between (1900 and " + currentYear + ").");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_country, menu);
		return true;
	}
}
