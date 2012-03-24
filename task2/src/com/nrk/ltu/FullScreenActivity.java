package com.nrk.ltu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

public class FullScreenActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullscreenview);
		Intent intent = getIntent();
		String url = intent.getExtras().getString(GridViewActivity.IMAGE_KEY);
		int originalImageId = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1, url.length()));
		Bitmap b = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), originalImageId, MediaStore.Images.Thumbnails.MINI_KIND, null);

		ImageView iv = (ImageView) findViewById(R.id.fullscreenImageView);

		iv.setImageBitmap(b);

		// iv.setImageURI(uri);
		// TODO Auto-generated method stub
	}

}
