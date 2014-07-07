package com.hungnguyen.qrcodescanner.adapter;

import java.util.ArrayList;

import android.app.Activity;
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
import com.hungnguyen.qrcodescanner.model.SettingEntryChooseObject;
import com.hungnguyen.qrcodescanner.model.SettingEntrySwitchObject;
import com.hungnguyen.qrcodescanner.model.SettingItemObject;
import com.hungnguyen.qrcodescanner.model.SettingSectionItemObject;
import com.hungnguyen.qrcodescanner.ulty.Constants;

public class SettingListAdapter extends ArrayAdapter<SettingItemObject> implements Constants, OnCheckedChangeListener{
	Activity mContext;
	ArrayList<SettingItemObject> mList;
	LayoutInflater mInflater;

	public SettingListAdapter(Activity context,
			ArrayList<SettingItemObject> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		TextView tvTitle;
		SettingItemObject item = mList.get(position);
		if (item != null) {
			switch(item.chooseItem()) {
			case SETTING_LISTVIEW_SECTION:
				SettingSectionItemObject sectionItem = (SettingSectionItemObject)item;
				view = mInflater.inflate(R.layout.item_settinglistview_section, null);
				tvTitle = (TextView)view.findViewById(R.id.setting_listview_section_text);
				tvTitle.setText(""+ sectionItem.getTitle());
				view.setOnClickListener(null);
				view.setOnLongClickListener(null);
				view.setLongClickable(false);
				break;
			case SETTING_LISTVIEW_ENTRY_SWITCH:
				SettingEntrySwitchObject switchItem = (SettingEntrySwitchObject)item;
				view= mInflater.inflate(R.layout.item_settinglistview_entry_switch, null);
				Switch sw = (Switch)view.findViewById(R.id.settinglistview_entry_sw_switch);
				sw.setChecked(switchItem.isSwitchOn());
				sw.setOnCheckedChangeListener(this);
				tvTitle = (TextView)view.findViewById(R.id.settinglistview_entry_tv_title);
				tvTitle.setText(""+ switchItem.getTitle());
				view.setOnClickListener(null);
				view.setOnLongClickListener(null);
				view.setLongClickable(false);
				break;
			case SETTING_LISTVIEW_ENTRY_CHOOSE:
				SettingEntryChooseObject chooseItem = (SettingEntryChooseObject)item;
				view = mInflater.inflate(R.layout.item_settinglistview_entry_choose, null);
				tvTitle = (TextView)view.findViewById(R.id.settinglistview_entry_tv_title);
				tvTitle.setText("" + chooseItem.getTitle());
				TextView tvStatus = (TextView)view.findViewById(R.id.settinglistview_entry_tv_status);
				tvStatus.setText("" + chooseItem.getStatus());
				if (!chooseItem.isShowStatus()) {
					tvStatus.setVisibility(View.INVISIBLE);
				} 
				ImageView iv = (ImageView)view.findViewById(R.id.settinglistview_entry_iv_choose);
				if (!chooseItem.isShowImage()) {
					iv.setVisibility(View.INVISIBLE);
				}
				break;
			}
		}
		return view;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		//TODO
	}

	
}
