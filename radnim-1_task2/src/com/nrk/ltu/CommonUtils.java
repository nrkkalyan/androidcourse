package com.nrk.ltu;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.provider.MediaStore;

public class CommonUtils {

	public static Bitmap getBitmap(ContentResolver cr, String originalImageUrl) {
		int originalImageId = Integer.valueOf(originalImageUrl.substring(originalImageUrl.lastIndexOf("/") + 1, originalImageUrl.length()));
		return MediaStore.Images.Thumbnails.getThumbnail(cr, originalImageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
	}

	public static String getContactName(Context context, long contactId) {
		String contactName = null;

		String[] whereArgs = new String[] { String.valueOf(contactId) };

		Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", whereArgs, null);

		int nameIndex = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

		if (cursor != null) {
			try {
				if (cursor.moveToNext()) {
					contactName = cursor.getString(nameIndex);
				}
			} finally {
				cursor.close();
			}
		}

		return contactName;
	}

	public static String getContactName(Context baseContext, String contactUrl) {
		int contactId = Integer.valueOf(contactUrl.substring(contactUrl.lastIndexOf("/") + 1, contactUrl.length()));
		return getContactName(baseContext, contactId);
	}

}
