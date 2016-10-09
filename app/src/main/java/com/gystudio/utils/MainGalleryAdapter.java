package com.gystudio.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.gystudio.base.R;
import com.gystudio.base.activity.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

 

public class MainGalleryAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Integer> icons = null;
	private ArrayList<String> titles = null;

	public MainGalleryAdapter(Context context, ArrayList<Integer> icons,
			ArrayList<String> titles) {
		super();
		this.context = context;
		this.icons = icons;
		this.titles = titles;
	}

	@Override
	public int getCount() {
		return icons.size();
	}

	@Override
	public Object getItem(int position) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Main.ICON, icons.get(position));
		map.put(Main.TITLE, titles.get(position));
		return map;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewHandler handler;
		if (convertView == null) {
			handler = new ViewHandler();
			convertView = inflater.inflate(R.layout.main_app_icon, null);
			handler.mImageView = (ImageView) convertView
					.findViewById(R.id.main_app_icon);
			handler.mTextView = (TextView) convertView
					.findViewById(R.id.main_appp_icon_title);
			convertView.setTag(handler);
		} else {
			handler = (ViewHandler) convertView.getTag();
		}
		handler.mImageView.setImageResource(icons.get(position));
		handler.mTextView.setText(titles.get(position));
		return convertView;
	}

	class ViewHandler {
		public ImageView mImageView;
		public TextView mTextView;
	}

}
