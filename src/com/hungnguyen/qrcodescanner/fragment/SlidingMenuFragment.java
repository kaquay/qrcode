package com.hungnguyen.qrcodescanner.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.adapter.SlidingListAdapter;
import com.hungnguyen.qrcodescanner.model.SlidingListItemObject;
import com.hungnguyen.qrcodescanner.utility.ChangeFragmentListener;

public class SlidingMenuFragment extends Fragment implements
		OnItemClickListener {
	public SlidingMenuFragment() {
		super();
	}

	public SlidingMenuFragment(ChangeFragmentListener listener) {
		super();
		this.mListener = listener;
	}
	ChangeFragmentListener mListener;
	ListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = (View) inflater.inflate(R.layout.fragment_slidingmenu,
				container, false);
		mListView = (ListView) view.findViewById(R.id.slidinglist);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		ArrayList<SlidingListItemObject> list = new ArrayList<SlidingListItemObject>();
		list.add(new SlidingListItemObject(R.drawable.ic_btn_scan));
		list.add(new SlidingListItemObject(R.drawable.ic_btn_history));
		list.add(new SlidingListItemObject(R.drawable.ic_btn_setting));
		list.add(new SlidingListItemObject(R.drawable.ic_btn_info));
		mListView.setAdapter(new SlidingListAdapter(getActivity(), list));
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		ChangeFragmentListener listener = mListener;
		listener.onChangFragmentListener(index);
	}


}
