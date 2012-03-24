package com.nrk.ltu;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class FullScreenActivity extends Activity {

	private Integer originalImageId = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullscreenview);
		Intent intent = getIntent();
		originalImageId = intent.getExtras().getInt(GridViewActivity.IMAGE_KEY);
		Bitmap b = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), originalImageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
		ImageView iv = (ImageView) findViewById(R.id.fullscreenImageView);
		iv.setImageBitmap(b);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fullscreenmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.tag: {

			Intent intent = new Intent(android.content.Intent.ACTION_PICK);
			intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			startActivityForResult(intent, 1);
			return true;
		}
		case R.id.taggedimage: {
			Toast.makeText(this, "Taggedimage!", Toast.LENGTH_SHORT).show();
			return true;
		}
		default:
			return false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String contactId = data.getDataString();
			try {
				FileOutputStream fOut = openFileOutput("textfile.txt", Context.MODE_PRIVATE);
				OutputStreamWriter osw = new OutputStreamWriter(fOut);
				String str = originalImageId + "=" + contactId;
				osw.write(str);
				osw.flush();
				osw.close();
				Toast.makeText(this, "Tagged successfully!", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
