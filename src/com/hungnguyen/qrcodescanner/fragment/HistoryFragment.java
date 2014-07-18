package com.hungnguyen.qrcodescanner.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.qrcodescanner.R;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.hungnguyen.qrcodescanner.activity.ResultActivity;
import com.hungnguyen.qrcodescanner.adapter.HistoryListAdapter;
import com.hungnguyen.qrcodescanner.database.Database;
import com.hungnguyen.qrcodescanner.model.HistoryItemEnity;
import com.hungnguyen.qrcodescanner.model.HistoryItemObject;
import com.hungnguyen.qrcodescanner.model.HistorySectionItemObject;
import com.hungnguyen.qrcodescanner.utility.DeleteItemHistoryListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HistoryFragment extends Fragment implements OnItemClickListener,
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
		BitmapFactory.Options dimensions = new BitmapFactory.Options();
		dimensions.inJustDecodeBounds = false;
		Bitmap mBitmap = BitmapFactory.decodeResource(getActivity()
				.getResources(), R.drawable.ic_btn_delete_fc, dimensions);
		int screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
		int imageWidth = dimensions.outWidth;
		int leftOffset = screenWidth - imageWidth;
		int rightOffset = screenWidth - (imageWidth * 4);
		Log.d("LOG", "RIGHT :" + rightOffset + ", LEFT " + leftOffset);
		mListHistory.setOffsetLeft(leftOffset);
		mListHistory.setOffsetRight(rightOffset);
		mListHistory.setSwipeListViewListener(new BaseSwipeListViewListener() {

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

	private void SetupList() {
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
		if (mList.isEmpty()) {
		} else {
			HistoryListAdapter adapter = new HistoryListAdapter(getActivity(),
					mList, this);
			mListHistory.setAdapter(adapter);
		}
	}

	@Override
	public void onDeleteItemHistoryComplete() {
		SetupList();
	}
}
