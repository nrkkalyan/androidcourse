package com.lnu;

import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.lnu.mycountries.db.Country;
import com.lnu.mycountries.db.CountryDAO;

public class MyCountries extends ListActivity {
	
	private static boolean				isSortByYear	= true;
	private static Comparator<Country>	comparator		= Country.COUNTRY_YEAR_COMPARATOR;
	public static final String			COUNTRY			= "COUNTRY";
	public static final String			OLD_COUNTRY		= "OLD_COUNTRY";
	
	private ArrayAdapter<Country>		adapter			= null;
	private CountryDAO					countryDAO;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_countries);
		countryDAO = new CountryDAO(this);
		List<Country> allCountries = countryDAO.findAll();
		adapter = new ArrayAdapter<Country>(this, android.R.layout.simple_list_item_1);
		adapter.addAll(allCountries);
		adapter.sort(comparator);
		setListAdapter(adapter);
		
		// Add context menu to the list
		registerForContextMenu(getListView());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 0:
				if (resultCode == RESULT_OK) {
					Bundle extras = data.getExtras();
					if (extras.containsKey(OLD_COUNTRY)) {
						Country oldCountry = (Country) extras.get(OLD_COUNTRY);
						adapter.remove(oldCountry);
					}
					
					Country country = (Country) extras.get(COUNTRY);
					adapter.add(country);
					adapter.sort(comparator);
				}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_my_countries, menu);
		return true;
	}
	
	public static final int	DELETE	= 0;
	public static final int	UPDATE	= 1;
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Select");
		menu.add(0, DELETE, 0, "Delete");
		menu.add(0, UPDATE, 1, "Update");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Country country = adapter.getItem(menuInfo.position);
		switch (item.getItemId()) {
			case DELETE:
				countryDAO.delete(country);
				adapter.remove(country);
				return true;
			case UPDATE:
				Intent intent = new Intent(MyCountries.this, AddCountry.class);
				intent.putExtra(MyCountries.OLD_COUNTRY, country);
				startActivityForResult(intent, 0);
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.sort: {
				if (isSortByYear) {
					isSortByYear = false;
					comparator = Country.COUNTRY_NAME_COMPARATOR;
					item.setTitle(R.string.sortByName);
				} else {
					isSortByYear = true;
					comparator = Country.COUNTRY_YEAR_COMPARATOR;
					item.setTitle(R.string.sortByYear);
				}
				adapter.sort(comparator);
				return true;
			}
			case R.id.add_country: {
				Intent myIntent = new Intent(MyCountries.this, AddCountry.class);
				startActivityForResult(myIntent, 0);
				return true;
			}
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
}
