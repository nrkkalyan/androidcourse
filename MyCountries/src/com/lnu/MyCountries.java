package com.lnu;

import java.util.HashSet;
import java.util.Set;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class MyCountries extends ListActivity {
	
	public static final String		COUNTRY				= "COUNTRY";
	
	private ArrayAdapter<String>	adapter				= null;
	static final Set<String>		countryVisitedList	= new HashSet<String>();
	{
		countryVisitedList.add("2005 France");
		countryVisitedList.add("2006 UK");
		countryVisitedList.add("2007 USA");
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_countries);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		adapter.addAll(countryVisitedList);
		adapter.sort(String.CASE_INSENSITIVE_ORDER);
		setListAdapter(adapter);
		
		Button addNewCountryButton = (Button) findViewById(R.id.addNewCountryButton);
		addNewCountryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MyCountries.this, AddCountry.class);
				startActivityForResult(myIntent, 0);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 0:
				if (resultCode == RESULT_OK) {
					String country = data.getExtras().get(COUNTRY).toString();
					adapter.add(country);
				}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_my_countries, menu);
		return true;
	}
}
