package com.lnu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	private final List<String> activities = new ArrayList<String>();
	private final Map<String, Class<? extends Activity>> name2class = new HashMap<String, Class<? extends Activity>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setup_activities();
		// setContentView(R.layout.activity_main);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main,
				activities));

		/* Attach list item listener */
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClick());
	}

	/* Private Help Entities */
	private class OnItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			/* Find selected activity */
			String activity_name = activities.get(position);
			Class<? extends Activity> activity_class = name2class
					.get(activity_name);

			/* Start new Activity */
			Intent intent = new Intent(MainActivity.this, activity_class);
			MainActivity.this.startActivity(intent);
		}
	}

	private void setup_activities() {
		addActivity(MyCountries.class);
		addActivity(Mp3MainActivity.class);
		addActivity(AlarmClockActivity.class);
	}

	private void addActivity(Class<? extends Activity> activity) {
		activities.add(activity.getSimpleName());
		name2class.put(activity.getSimpleName(), activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
