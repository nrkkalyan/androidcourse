package com.lnu;

import java.util.Comparator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lnu.mycountries.db.Country;
import com.lnu.mycountries.db.CountryDAO;

public class MyCountries extends ListActivity {
	
	private enum RequestCode {
		ADD_COUNTRY(0), SET_PREFS(1);
		private final int	requestCode;
		
		private RequestCode(int code) {
			requestCode = code;
		}
		
	}
	
	private boolean					isSortByYear;
	private Comparator<Country>		comparator;
	public static final String		COUNTRY			= "COUNTRY";
	public static final String		OLD_COUNTRY		= "OLD_COUNTRY";
	
	private ArrayAdapter<Country>	adapter			= null;
	private CountryDAO				countryDAO;
	private final String			SORT_KEY		= "SORT_BY";
	private ListView				listView;
	
	private final String			PREF_BG_COLOR	= "pref_bg_color";
	private final String			PREF_ROTATION	= "pref_rotation";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_my_countries);
			countryDAO = new CountryDAO(this);
			adapter = new ArrayAdapter<Country>(this, android.R.layout.simple_list_item_1);
			adapter.addAll(countryDAO.findAll());
			setListAdapter(adapter);
			
			isSortByYear = getPreferences(0).getBoolean(SORT_KEY, true);
			if (isSortByYear) {
				comparator = Country.COUNTRY_YEAR_COMPARATOR;
			} else {
				comparator = Country.COUNTRY_NAME_COMPARATOR;
			}
			adapter.sort(comparator);
			
			// Add context menu to the list
			listView = getListView();
			registerForContextMenu(listView);
			updateAccordingToPreferences();
			
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialog alertDialog = new AlertDialog.Builder(MyCountries.this).create();
			alertDialog.setMessage(e.getMessage());
			alertDialog.show();
		}
	}
	
	@Override
	protected void onStop() {
		SharedPreferences sp = getPreferences(0);
		Editor edit = sp.edit();
		edit.putBoolean(SORT_KEY, isSortByYear);
		edit.commit();
		super.onStop();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch (requestCode) {
			case 0: {// RequestCode.ADD_COUNTRY
				if (resultCode == RESULT_OK) {
					Bundle extras = intent.getExtras();
					if (extras.containsKey(OLD_COUNTRY)) {
						Country oldCountry = (Country) extras.get(OLD_COUNTRY);
						adapter.remove(oldCountry);
					}
					
					Country country = (Country) extras.get(COUNTRY);
					adapter.add(country);
					adapter.sort(comparator);
				}
				break;
			}
			case 1: {// RequestCode.SET_PREFS
				updateAccordingToPreferences();
				break;
			}
		}
	}
	
	private void updateAccordingToPreferences() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String bg_color = sp.getString(PREF_BG_COLOR, "White");
		int color = Color.WHITE;
		if (bg_color.equalsIgnoreCase("Red")) {
			color = Color.RED;
		} else if (bg_color.equalsIgnoreCase("Blue")) {
			color = Color.BLUE;
		}
		listView.setBackgroundColor(color);
		
		//
		float rotation_angle = Float.valueOf(sp.getString(PREF_ROTATION, "0"));
		listView.setRotation(rotation_angle);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_my_countries, menu);
		SubMenu subMenu = menu.getItem(0).getSubMenu();
		MenuItem item = subMenu.getItem(0);
		if (isSortByYear) {
			item.setTitle(R.string.sortByName);
		} else {
			item.setTitle(R.string.sortByYear);
		}
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
		try {
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
					startActivityForResult(intent, RequestCode.ADD_COUNTRY.requestCode);
					return true;
				default:
					return super.onContextItemSelected(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialog alertDialog = new AlertDialog.Builder(MyCountries.this).create();
			alertDialog.setMessage(e.getMessage());
			alertDialog.show();
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.sort_menu: {
				isSortByYear = item.getTitle().toString().toLowerCase().contains("year");
				if (isSortByYear) {
					comparator = Country.COUNTRY_YEAR_COMPARATOR;
					item.setTitle(R.string.sortByName);
				} else {
					comparator = Country.COUNTRY_NAME_COMPARATOR;
					item.setTitle(R.string.sortByYear);
				}
				adapter.sort(comparator);
				return true;
			}
			case R.id.add_country: {
				Intent myIntent = new Intent(MyCountries.this, AddCountry.class);
				startActivityForResult(myIntent, RequestCode.ADD_COUNTRY.requestCode);
				return true;
			}
			case R.id.prefs: {
				Intent myIntent = new Intent(MyCountries.this, MyPreferences.class);
				startActivityForResult(myIntent, RequestCode.SET_PREFS.requestCode);
				return true;
			}
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
}
