package com.nrk.ltu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
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
			try {
				// FileInputStream fIn = openFileInput("textfile.txt");
				// InputStreamReader is = new InputStreamReader(fIn);
				// BufferedReader br = new BufferedReader(is);
				// String str = br.readLine();
				// // while ((str = br.readLine()) != null) {
				// // Toast.makeText(getBaseContext(), str,
				// // Toast.LENGTH_SHORT).show();
				// // }
				// is.close();
				// br.close();
				//
				// String[] split = str.split("=");
				db = new DBAdapter(this);
				db.open();
				Cursor c = db.getContact(originalImageUrl);
				if (c != null) {
					String imageUrl = c.getString(1);
					String contactUrl = c.getString(2);
					Toast.makeText(this, "id: " + c.getString(0) + "\n" + "ImageUrl: " + imageUrl + "\n" + "ContactName:  " + contactUrl,
							Toast.LENGTH_LONG).show();
					Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(contactUrl));
					startActivity(i);
				} else {
					Toast.makeText(this, "No contact found", Toast.LENGTH_LONG).show();
				}
				db.close();

			} catch (Exception e) {
				Log.e("", "", e);
			}
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
				db = new DBAdapter(this);
				db.open();
				db.insertContact(originalImageUrl, data.getDataString());
				db.close();
				String name = CommonUtils.getContactName(FullScreenActivity.this,
						Integer.valueOf(contactId.substring(contactId.lastIndexOf("/") + 1, contactId.length())));
				Toast.makeText(this, name + " tagged successfully!", Toast.LENGTH_SHORT).show();

				// Saving in File
				// FileOutputStream fOut = openFileOutput("textfile.txt",
				// Context.MODE_PRIVATE);
				// OutputStreamWriter osw = new OutputStreamWriter(fOut);
				// String str = originalImageUrl + "=" + data.getDataString();
				// osw.write(str);
				// osw.flush();
				// osw.close();
			} catch (Exception e) {
				Log.e("", "", e);
			}
		}
	}

}
