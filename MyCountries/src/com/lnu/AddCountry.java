package com.lnu;

import java.util.Calendar;

import com.lnu.mycountries.db.Country;

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
		final Button addOrUpdateCountryButton = (Button) findViewById(R.id.addOrUpdateCountryButton);
		final Intent intent = getIntent();
		
		Object oldCountry = intent.getExtras().get(MyCountries.OLD_COUNTRY);
		if (oldCountry != null) {
			addOrUpdateCountryButton.setText(R.string.updateCountry);
			yearEditText.setText(((Country) oldCountry).getYear() + "");
			countryEditText.setText(((Country) oldCountry).getName());
		}
		
		addOrUpdateCountryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Integer year = Integer.parseInt(yearEditText.getText().toString());
				String country = countryEditText.getText().toString();
				try {
					validate(year, country);
					intent.putExtra(MyCountries.COUNTRY, new Country(year, country));
					setResult(Activity.RESULT_OK, intent);
					finish();
				} catch (IllegalArgumentException e) {
					AlertDialog alertDialog = new AlertDialog.Builder(AddCountry.this).create();
					alertDialog.setMessage(e.getMessage());
					alertDialog.show();
				}
				
			}
		});
		
	}
	
	private void validate(Integer year, String country) {
		if (year == null || country.trim().isEmpty()) {
			throw new IllegalArgumentException("Please enter both year and country.");
		}
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		if (year < 1900 || year > currentYear) {
			throw new IllegalArgumentException("Year must be between (1900 and " + currentYear + ").");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_country, menu);
		return true;
	}
}
