package com.hungnguyen.qrcodescanner.fragment;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.adapter.HistoryListAdapter;
import com.hungnguyen.qrcodescanner.model.HistoryEntryItemObject;
import com.hungnguyen.qrcodescanner.model.HistoryItemObject;
import com.hungnguyen.qrcodescanner.model.HistorySectionItemObject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HistoryFragment extends Fragment implements OnItemClickListener{
	ListView mListHistory;
	public HistoryFragment() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = (View) inflater.inflate(R.layout.fragment_history, container, false);
		mListHistory = (ListView)view.findViewById(R.id.history_list);
		
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		ArrayList<HistoryItemObject> list = new ArrayList<HistoryItemObject>();
		list.add(new HistorySectionItemObject("section 1"));
		list.add(new HistoryEntryItemObject("1", "entry 1"));
		list.add(new HistoryEntryItemObject("2", "entry 2"));
		list.add(new HistoryEntryItemObject("3", "entry 3"));
		list.add(new HistoryEntryItemObject("4", "entry 4"));
		list.add(new HistorySectionItemObject("section 2"));
		list.add(new HistoryEntryItemObject("1", "entry 1"));
		list.add(new HistoryEntryItemObject("2", "entry 2"));
		list.add(new HistoryEntryItemObject("3", "entry 3"));
		list.add(new HistoryEntryItemObject("4", "entry 4"));
		HistoryListAdapter adapter = new HistoryListAdapter(getActivity(), list);
		mListHistory.setAdapter(adapter);
		mListHistory.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}
	
}
