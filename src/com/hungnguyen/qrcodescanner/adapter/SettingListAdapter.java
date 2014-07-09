package com.hungnguyen.qrcodescanner.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.model.SettingChooseObject;
import com.hungnguyen.qrcodescanner.model.SettingItemEntity;
import com.hungnguyen.qrcodescanner.model.SettingSectionItemObject;
import com.hungnguyen.qrcodescanner.model.SettingSwitchObject;
import com.hungnguyen.qrcodescanner.utility.Constants;

public class SettingListAdapter extends ArrayAdapter<SettingItemEntity>
		implements Constants {
	Activity mContext;
	ArrayList<SettingItemEntity> mList;
	LayoutInflater mInflater;

	public SettingListAdapter(Activity context,
			ArrayList<SettingItemEntity> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		TextView tvTitle;
		SettingItemEntity item = mList.get(position);
		if (item != null) {
			switch (item.chooseItem()) {
			case SETTING_LISTVIEW_SECTION:
				SettingSectionItemObject sectionItem = (SettingSectionItemObject) item;
				view = mInflater.inflate(R.layout.item_settinglistview_section,
						null);
				tvTitle = (TextView) view
						.findViewById(R.id.setting_listview_section_text);
				tvTitle.setText("" + sectionItem.getTitle());
				view.setOnClickListener(null);
				view.setOnLongClickListener(null);
				view.setLongClickable(false);
				break;
			case SETTING_LISTVIEW_ENTRY_SWITCH:
				final SettingSwitchObject switchItem = (SettingSwitchObject) item;
				view = mInflater.inflate(
						R.layout.item_settinglistview_entry_switch, null);
				Switch sw = (Switch) view
						.findViewById(R.id.settinglistview_entry_sw_switch);
				sw.setChecked(switchItem.isSwitchOn());
				sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (switchItem.getId() == SETTING_ITEM_SOUND) {
							switchItem.setSwitchOn(isChecked);
							SharedPreferences sp = mContext
									.getSharedPreferences(SHARE_NAME, 0);
							Editor editor = sp.edit();
							editor.putBoolean(SHARE_SW_SOUND, isChecked);
							editor.commit();
						}
						if (switchItem.getId() == SETTING_ITEM_AUTO_OPEN_URL) {
							switchItem.setSwitchOn(isChecked);
							SharedPreferences sp = mContext
									.getSharedPreferences(SHARE_NAME, 0);
							Editor editor = sp.edit();
							editor.putBoolean(SHARE_SW_AUTO_OPEN, isChecked);
							editor.commit();
						}
					}
				});
				tvTitle = (TextView) view
						.findViewById(R.id.settinglistview_entry_tv_title);
				tvTitle.setText("" + switchItem.getTitle());
				view.setOnClickListener(null);
				view.setOnLongClickListener(null);
				view.setLongClickable(false);
				break;
			case SETTING_LISTVIEW_ENTRY_CHOOSE:
				SettingChooseObject chooseItem = (SettingChooseObject) item;
				view = mInflater.inflate(
						R.layout.item_settinglistview_entry_choose, null);
				tvTitle = (TextView) view
						.findViewById(R.id.settinglistview_entry_tv_title);
				tvTitle.setText("" + chooseItem.getTitle());
				TextView tvStatus = (TextView) view
						.findViewById(R.id.settinglistview_entry_tv_status);
				tvStatus.setText("" + chooseItem.getStatus());
				ImageView ivLeft = (ImageView) view
						.findViewById(R.id.settinglistview_entry_iv_left);
				if (!chooseItem.isShowStatus()) {
					tvStatus.setVisibility(View.INVISIBLE);
				}
				ImageView iv = (ImageView) view
						.findViewById(R.id.settinglistview_entry_iv_choose);
				if (!chooseItem.isShowImage()) {
					iv.setVisibility(View.INVISIBLE);
				}
				if (chooseItem.getImage() == 0) {
					ivLeft.setVisibility(View.INVISIBLE);
				} else {
					ivLeft.setImageResource(chooseItem.getImage());
				}
				break;
			}
		}
		return view;
	}

	@Override
	public int getViewTypeCount() {

		return getCount();
	}

	@Override
	public int getItemViewType(int position) {

		return position;
	}
}
