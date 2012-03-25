package com.nrk.ltu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewActivity extends Activity {

	private Cursor cursor;
	private int columnIndex;
	private int count;
	public static final String IMAGE_KEY = "com.nrk.ltu.IMAGE";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gridview);

		GridView gridView = (GridView) findViewById(R.id.myGrid);
		String[] projection = { MediaStore.Images.Media._ID };
		// Create the cursor pointing to the SDCard
		cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Images.Media.DATA + " like ? ",
				new String[] { "%Camera%" }, null);

		count = cursor.getCount();
		// Get the column index of the image ID
		columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
		gridView.setAdapter(new ImageAdapter(this));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(position + 1));
				Intent intent = new Intent(GridViewActivity.this, FullScreenActivity.class);
				intent.putExtra(IMAGE_KEY, uri.toString());
				startActivity(intent);
			}
		});
	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;

		public ImageAdapter(Context c) {
			context = c;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(context);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(5, 5, 5, 5);
			} else {
				imageView = (ImageView) convertView;
			}
			cursor.moveToPosition(position);
			int imageID = cursor.getInt(columnIndex);
			Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imageID);
			String url = uri.toString();
			Bitmap b = CommonUtils.getBitmap(getContentResolver(), url);
			imageView.setImageBitmap(b);
			return imageView;
		}

	}

}
