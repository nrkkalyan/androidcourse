package com.nrk.ltu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class FullScreenActivity extends Activity {

	private String originalImageUrl = null;
	private DBAdapter db;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullscreenview);
		Intent intent = getIntent();
		originalImageUrl = intent.getExtras().getString(GridViewActivity.IMAGE_KEY);
		ImageView iv = (ImageView) findViewById(R.id.fullscreenImageView);
		Bitmap b = CommonUtils.getBitmap(getContentResolver(), originalImageUrl);
		iv.setImageBitmap(b);

		try {
			String destPath = "/data/data/" + getPackageName() + "/databases/MyDB";
			File f = new File(destPath);
			if (!f.exists()) {
				CopyDB(getBaseContext().getAssets().open("mydb"), new FileOutputStream(destPath));
			}
		} catch (Exception e) {
			Log.e("", "", e);
		}

	}

	public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
		// ---copy 1K bytes at a time---
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		inputStream.close();
		outputStream.close();
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
			// try {
			// db = new DBAdapter(this);
			// db.open();
			// Cursor c = db.getContacts(originalImageUrl);
			// if (c != null && c.getCount() > 1) {
			// String imageUrl = c.getString(1);
			// String contactUrl = c.getString(2);
			// Toast.makeText(this, "id: " + c.getString(0) + "\n" +
			// "ImageUrl: " + imageUrl + "\n" + "ContactName:  " +
			// contactUrl,
			// Toast.LENGTH_LONG).show();
			// Intent i = new Intent(android.content.Intent.ACTION_VIEW,
			// Uri.parse(contactUrl));
			// startActivity(i);
			Intent intent = new Intent(this, ContactsActivity.class);
			intent.putExtra(GridViewActivity.IMAGE_KEY, originalImageUrl);
			startActivity(intent);

			// } else {
			// Toast.makeText(this, "No contacts are tagged!",
			// Toast.LENGTH_LONG).show();
			// }
			// db.close();

			// } catch (Exception e) {
			// Log.e("", "", e);
			// }
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
			String name = CommonUtils.getContactName(FullScreenActivity.this,
					Integer.valueOf(contactId.substring(contactId.lastIndexOf("/") + 1, contactId.length())));
			try {
				db = new DBAdapter(this);
				db.open();
				long insertContact = db.insertContact(originalImageUrl, data.getDataString());
				if (insertContact == -1) {
					Toast.makeText(this, "Contact is already tagged!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, name += " tagged successfully!", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Log.e("", "", e);
			} finally {
				db.close();
			}
		}
	}
}
