package com.hungnguyen.qrcodescanner.fragment;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
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
import com.hungnguyen.qrcodescanner.activity.ResultActivity;
import com.hungnguyen.qrcodescanner.adapter.HistoryListAdapter;
import com.hungnguyen.qrcodescanner.database.Database;
import com.hungnguyen.qrcodescanner.model.HistoryEntryItemObject;
import com.hungnguyen.qrcodescanner.model.HistoryItemObject;
import com.hungnguyen.qrcodescanner.model.HistorySectionItemObject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HistoryFragment extends Fragment implements OnItemClickListener {
	ListView mListHistory;
	ArrayList<HistoryItemObject> mList;

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
		View view = (View) inflater.inflate(R.layout.fragment_history,
				container, false);
		mListHistory = (ListView) view.findViewById(R.id.history_list);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		mList = new ArrayList<HistoryItemObject>();
		mList.add(new HistorySectionItemObject("section 1"));
		mList.add(new HistoryEntryItemObject("1", "entry 1"));
		mList.add(new HistoryEntryItemObject("2", "entry 2"));
		mList.add(new HistoryEntryItemObject("3", "entry 3"));
		mList.add(new HistoryEntryItemObject("4", "entry 4"));
		mList.add(new HistorySectionItemObject("section 2"));
		mList.add(new HistoryEntryItemObject("1", "entry 1"));
		mList.add(new HistoryEntryItemObject("2", "entry 2"));
		mList.add(new HistoryEntryItemObject("3", "entry 3"));
		mList.add(new HistoryEntryItemObject("4", "entry 4"));
		HistoryListAdapter adapter = new HistoryListAdapter(getActivity(),
				mList);
		mListHistory.setAdapter(adapter);
		mListHistory.setOnItemClickListener(this);
		Database db = new Database(getActivity());
		ArrayList<String> listSection = new ArrayList<String>();
		listSection = db.getAllDate();
		if (listSection != null) {
			for (String item : listSection) {
				mList.add(new HistorySectionItemObject(item));
				String[] values = item.split("-");
				ArrayList<HistoryEntryItemObject> listEntry = new ArrayList<HistoryEntryItemObject>();
				listEntry = db.getValueByDate(values[2], values[1], values[0]);
				for (HistoryEntryItemObject object : listEntry) {
					mList.add(object);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), ResultActivity.class);
		Bundle bundle = new Bundle();
		HistoryEntryItemObject item = (HistoryEntryItemObject) mList
				.get(position);
		bundle.putString("url", item.getTitle());
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
