package com.hungnguyen.qrcodescanner.fragment;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.qrcodescanner.R;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.hungnguyen.qrcodescanner.activity.ResultActivity;
import com.hungnguyen.qrcodescanner.adapter.HistoryListAdapter;
import com.hungnguyen.qrcodescanner.database.Database;
import com.hungnguyen.qrcodescanner.model.HistoryItemObject;
import com.hungnguyen.qrcodescanner.model.HistoryItemEnity;
import com.hungnguyen.qrcodescanner.model.HistorySectionItemObject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HistoryFragment extends Fragment implements OnItemClickListener {
	SwipeListView mListHistory;
	ArrayList<HistoryItemEnity> mList;

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
		mListHistory = (SwipeListView) view.findViewById(R.id.history_list);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		// mList = new ArrayList<HistoryItemEnity>();
		// mList.add(new HistorySectionItemObject("section 1"));
		// mList.add(new HistoryItemObject("1", "entry 1"));
		// mList.add(new HistoryItemObject("2", "entry 2"));
		// mList.add(new HistoryItemObject("3", "entry 3"));
		// mList.add(new HistoryItemObject("4", "entry 4"));
		// mList.add(new HistorySectionItemObject("section 2"));
		// mList.add(new HistoryItemObject("1", "entry 1"));
		// mList.add(new HistoryItemObject("2", "entry 2"));
		// mList.add(new HistoryItemObject("3", "entry 3"));
		// mList.add(new HistoryItemObject("4", "entry 4"));
		// mList.add(new HistoryItemObject("5", "entry 5"));
		// mList.add(new HistoryItemObject("6", "entry 6"));
		// mList.add(new HistoryItemObject("7", "entry 7"));
		mList = new ArrayList<HistoryItemEnity>();
		Database db = new Database(getActivity());
		ArrayList<String> listSection = new ArrayList<String>();
		listSection = db.getAllDate();
		if (listSection != null) {
			for (String item : listSection) {
				try {
					Log.d("History", "" + item);
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat sdf = new SimpleDateFormat(
							"EEEE MMMM dd,yyyy");
					String newFt = sdf.format(format.parse(item));
					mList.add(new HistorySectionItemObject("" + newFt));
					ArrayList<HistoryItemObject> listEntry = new ArrayList<HistoryItemObject>();
					listEntry = db.getValueByDate(item);
					for (HistoryItemObject object : listEntry) {
						mList.add(object);
					}
				} catch (Exception e) {

				}
			}
		}
		if (mList != null) {
			HistoryListAdapter adapter = new HistoryListAdapter(getActivity(),
					mList);
			mListHistory.setAdapter(adapter);
		}
		mListHistory.setOnItemClickListener(this);
//		LinearLayout layoutShare = (LinearLayout) getActivity().findViewById(
//				R.id.historylistview_entry_back_ll_left);
//		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutShare
//				.getLayoutParams();
//		int widthLeft = params.width;
//		ImageButton ibDelete = (ImageButton) getActivity().findViewById(
//				R.id.historylistview_entry_ib_delete);
//		RelativeLayout.LayoutParams ibparams = (RelativeLayout.LayoutParams) ibDelete
//				.getLayoutParams();
//		int widthRight = ibparams.width;
//		int screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
		mListHistory.setOffsetLeft(140);
		mListHistory.setOffsetRight(140);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), ResultActivity.class);
		Bundle bundle = new Bundle();
		HistoryItemObject item = (HistoryItemObject) mList.get(position);
		bundle.putString("url", item.getTitle());
		intent.putExtras(bundle);
		getActivity().startActivity(intent);
		Toast.makeText(getActivity(), "zzzz", Toast.LENGTH_SHORT).show();
	}
}
