package com.nrk.ltu;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

public class ContactsActivity extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		Intent intent = getIntent();
		String originalImageUrl = intent.getExtras().getString(GridViewActivity.IMAGE_KEY);
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getContacts(originalImageUrl);
		List<TagedImage> tagedImageList = new ArrayList<TagedImage>();
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				do {
					tagedImageList.add(new TagedImage(c.getString(1), c.getString(2)));
				} while (c.moveToNext());
			}
			db.close();
			setListAdapter(new MyArrayAdapter(this, android.R.layout.simple_gallery_item, R.id.textView1, tagedImageList));
		} else {
			Toast.makeText(this, "No contacts are taged to this image!", Toast.LENGTH_LONG).show();
			intent = new Intent(this, GridViewActivity.class);
			startActivity(intent);
			finish();
		}

	}

}