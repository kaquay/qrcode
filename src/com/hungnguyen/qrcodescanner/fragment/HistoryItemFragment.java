package com.hungnguyen.qrcodescanner.fragment;

import com.example.qrcodescanner.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HistoryItemFragment extends Fragment {
	public HistoryItemFragment() {
		super();
	}

	public static HistoryItemFragment newInstance(Bundle bundle) {
		HistoryItemFragment fragment = new HistoryItemFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = (View) inflater.inflate(
				R.layout.fragment_historyitem_entry, container, false);
		
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

}
