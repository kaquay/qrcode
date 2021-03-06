package com.hungnguyen.qrcodescanner.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.qrcodescanner.R;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.hungnguyen.qrcodescanner.activity.ResultActivity;
import com.hungnguyen.qrcodescanner.adapter.HistoryListAdapter;
import com.hungnguyen.qrcodescanner.adapter.HistoryListAdapter.Holder;
import com.hungnguyen.qrcodescanner.database.Database;
import com.hungnguyen.qrcodescanner.model.HistoryItemEnity;
import com.hungnguyen.qrcodescanner.model.HistoryItemObject;
import com.hungnguyen.qrcodescanner.model.HistorySectionItemObject;
import com.hungnguyen.qrcodescanner.utility.DeleteItemHistoryListener;
import com.hungnguyen.qrcodescanner.utility.Util;
import com.hungnguyen.qrcodescanner.utility.Util.isSwipe;

@SuppressLint("SimpleDateFormat")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HistoryFragment extends Fragment implements
		DeleteItemHistoryListener {
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
		SetupList();
		int screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
		int imageWidth = (screenWidth * 10 / 100) + 5; // 10%
		int leftOffset = screenWidth - imageWidth;
		int rightOffset = screenWidth - (imageWidth * 4);
		mListHistory.setOffsetLeft(leftOffset);
		mListHistory.setOffsetRight(rightOffset);
		mListHistory.setSwipeListViewListener(new BaseSwipeListViewListener() {

			@Override
			public void onOpened(int position, boolean toRight) {
				if (toRight) {
					Util.listSwipe.get(position).isRight = false;
					Util.listSwipe.get(position).isLeft = true;
				} else {
					Util.listSwipe.get(position).isRight = true;
					Util.listSwipe.get(position).isLeft = false;
				}
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
				Util.listSwipe.get(position).isRight = false;
				Util.listSwipe.get(position).isLeft = false;
			}

			@Override
			public void onClickFrontView(int position) {
				Intent intent = new Intent(getActivity(), ResultActivity.class);
				Bundle bundle = new Bundle();
				HistoryItemObject item = (HistoryItemObject) mList
						.get(position);
				bundle.putString("url", item.getTitle());
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
			}

		});
	}

	private void SetupList() {
		mList = new ArrayList<HistoryItemEnity>();
		Database db = new Database(getActivity());
		ArrayList<String> listSection = new ArrayList<String>();
		listSection = db.getAllDate();
		ArrayList<isSwipe> list = new ArrayList<isSwipe>();
		if (listSection != null) {
			for (String item : listSection) {
				try {
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

			int listLength = mList.size();
			for (int i = 0; i < listLength; i++) {
				list.add(new isSwipe());
			}
			Util.listSwipe = list;
		}
		// if (mList.isEmpty()) {
		// mListHistory.invalidate();
		// } else {
		try {
			HistoryListAdapter adapter = new HistoryListAdapter(getActivity(),
					mList, this);
			mListHistory.setAdapter(adapter);
		} finally {

		}
		// }
	}

	@Override
	public void onDeleteItemHistoryComplete() {
		SetupList();
		Toast.makeText(getActivity(), "Deleted !", Toast.LENGTH_SHORT).show();
	}

}
