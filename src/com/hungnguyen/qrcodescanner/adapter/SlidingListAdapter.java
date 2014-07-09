package com.hungnguyen.qrcodescanner.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.model.SlidingListItemObject;

@SuppressLint("InflateParams")
public class SlidingListAdapter extends ArrayAdapter<SlidingListItemObject> {
	Context mContext;
	ArrayList<SlidingListItemObject> mList;
	LayoutInflater mInflater;

	public SlidingListAdapter(Context context,
			ArrayList<SlidingListItemObject> list) {
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
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int height = display.getHeight();
		height -= 40;
		int margin = height / 22;
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		ll.setMargins(0, margin, 0, margin);
		convertView.setLayoutParams(ll);
		return convertView;
	}

	private class Holder {
		ImageView iv;
	}
}
