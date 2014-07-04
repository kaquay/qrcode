package com.hungnguyen.qrcodescanner.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.model.SlidingListItemObject;

@SuppressLint("InflateParams")
public class SlidingListAdapter extends ArrayAdapter<SlidingListItemObject> {
	Context mContext;
	ArrayList<SlidingListItemObject> mList;
	LayoutInflater mInflater;

	public SlidingListAdapter(Context context, ArrayList<SlidingListItemObject> list) {
		super(context, R.layout.item_slidinglist, list);
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_slidinglist, null);
			holder = new Holder();
			holder.iv = (ImageView) convertView
					.findViewById(R.id.item_slidinglist_image);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.iv.setImageResource(getItem(position).getImage());
		return convertView;
	}

	private class Holder {
		ImageView iv;
	}
}
