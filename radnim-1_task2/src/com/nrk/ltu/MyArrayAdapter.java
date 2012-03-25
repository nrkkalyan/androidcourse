package com.nrk.ltu;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class MyArrayAdapter extends ArrayAdapter<TagedImage> {

	/**
	 * 
	 */
	private final Context context;
	private final List<TagedImage> tagedImageList;

	public MyArrayAdapter(Context context, int resource, int textViewResourceId, List<TagedImage> tagedImageList) {
		super(context, resource, textViewResourceId, tagedImageList);
		this.context = context;
		this.tagedImageList = tagedImageList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.list_view, parent, false);

		ImageView iv = (ImageView) row.findViewById(R.id.imageView1);
		TextView tv = (TextView) row.findViewById(R.id.textView1);

		Bitmap b = CommonUtils.getBitmap(context.getContentResolver(), tagedImageList.get(position).getImageUrl());
		iv.setImageBitmap(b);
		tv.setText(CommonUtils.getContactName(row.getContext(), tagedImageList.get(position).getContactUrl()));
		return row;
	}
}