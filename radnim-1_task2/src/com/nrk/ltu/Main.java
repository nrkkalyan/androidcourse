package com.nrk.ltu;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Main extends ListActivity {

	private List<TagedImage> tagedImageList = new ArrayList<TagedImage>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllContacts();
		if (c != null && c.getCount() > 0) {
			if (c.moveToFirst()) {
				do {
					tagedImageList.add(new TagedImage(c.getString(1), c.getString(2)));
				} while (c.moveToNext());
			}
			db.close();
			// setListAdapter(new MyArrayAdapter(this,
			// android.R.layout.simple_gallery_item, R.id.textView1,
			// tagedImageList));
			setListAdapter(new MyArrayAdapter(this, android.R.layout.simple_gallery_item, R.id.textView1, tagedImageList));
		} else {
			Toast.makeText(this, "No images are tagged!", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, GridViewActivity.class);
			startActivity(intent);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.galary: {
			Intent intent = new Intent(Main.this, GridViewActivity.class);
			startActivity(intent);
			return true;
		}
		default:
			return false;
		}
	}

	// private class MyArrayAdapter extends ArrayAdapter<TagedImage> {
	//
	// public MyArrayAdapter(Context context, int resource, int
	// textViewResourceId, List<TagedImage> objects) {
	// super(context, resource, textViewResourceId, objects);
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	//
	// LayoutInflater inflater = (LayoutInflater)
	// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View row = inflater.inflate(R.layout.list_view, parent, false);
	//
	// ImageView iv = (ImageView) row.findViewById(R.id.imageView1);
	// TextView tv = (TextView) row.findViewById(R.id.textView1);
	//
	// Bitmap b = CommonUtils.getBitmap(getContentResolver(),
	// tagedImageList.get(position).imageUrl);
	// iv.setImageBitmap(b);
	// tv.setText(CommonUtils.getContactName(row.getContext(),
	// tagedImageList.get(position).contactUrl));
	// return row;
	// }
	//
	// }
	//
	// private static class TagedImage implements Serializable {
	// private static final long serialVersionUID = 1L;
	// private final String imageUrl;
	// private final String contactUrl;
	//
	// public TagedImage(String imageUrl, String contactUrl) {
	// this.imageUrl = imageUrl;
	// this.contactUrl = contactUrl;
	// }
	//
	// }

}