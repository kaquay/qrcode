package com.hungnguyen.qrcodescanner.fragment;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.SharedPreferences;
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
import com.hungnguyen.qrcodescanner.adapter.SettingListAdapter;
import com.hungnguyen.qrcodescanner.model.SettingEntryChooseObject;
import com.hungnguyen.qrcodescanner.model.SettingEntrySwitchObject;
import com.hungnguyen.qrcodescanner.model.SettingItemObject;
import com.hungnguyen.qrcodescanner.model.SettingListViewItem;
import com.hungnguyen.qrcodescanner.model.SettingSectionItemObject;
import com.hungnguyen.qrcodescanner.ulty.Constants;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends Fragment implements OnItemClickListener,
		Constants {
	ListView mListView;
	ArrayList<SettingItemObject> mList;

	public SettingsFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container,
				false);
		mListView = (ListView) view.findViewById(R.id.setting_list);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		mList = new ArrayList<SettingItemObject>();
		setupList();
		SettingListAdapter adapter = new SettingListAdapter(getActivity(), mList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);

	}

	private void setupList() {
		mList.add(new SettingSectionItemObject("SCANNER"));
		SharedPreferences sp = getActivity()
				.getSharedPreferences(SHARE_NAME, 0);
		boolean swSound = sp.getBoolean(SHARE_SW_SOUND, true);
		mList.add(new SettingEntrySwitchObject(SETTING_ITEM_SOUND, "Sound",
				swSound));
		boolean swAutoOpen = sp.getBoolean(SHARE_SW_AUTO_OPEN, true);
		mList.add(new SettingEntrySwitchObject(SETTING_ITEM_AUTO_OPEN_URL,
				"Auto Open URL", swAutoOpen));
		String urlprofile = sp.getString(SHARE_URL_PROFILE, "");
		mList.add(new SettingEntryChooseObject(SETTING_ITEM_URL_PROFILE,
				"URL Profile", urlprofile, true, false));
		String autoClose = sp.getString(SHARE_AUTO_CLOSE_URL, "");
		mList.add(new SettingEntryChooseObject(SETTING_ITEM_AUTO_CLOSE_URL,
				"Auto close URL view after", autoClose, true, true));
		mList.add(new SettingSectionItemObject("SHARE"));
		mList.add(new SettingEntryChooseObject(SETTING_ITEM_MESSAGE, "Message",
				"", false, true));
		mList.add(new SettingEntryChooseObject(SETTING_ITEM_MAIL, "Mail", "",
				false, true));
		mList.add(new SettingEntryChooseObject(SETTING_ITEM_TWITTER, "Twitter",
				"", false, true));
		mList.add(new SettingEntryChooseObject(SETTING_ITEM_FACEBOOK,
				"Facebook", "", false, true));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SettingListViewItem listviewItem = (SettingListViewItem) mList
				.get(position);
		switch (listviewItem.getId()) {
		case SETTING_ITEM_URL_PROFILE:
			break;
		case SETTING_ITEM_AUTO_CLOSE_URL:
			break;
		case SETTING_ITEM_MESSAGE:
			break;
		case SETTING_ITEM_MAIL:
			break;
		case SETTING_ITEM_TWITTER:
			break;
		case SETTING_ITEM_FACEBOOK:
			break;

		}
	}

}
