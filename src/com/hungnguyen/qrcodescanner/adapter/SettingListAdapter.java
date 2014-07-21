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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
		SettingItemEntity item = mList.get(position);
		final Holder holder;
		if (convertView == null) {
			if (item != null) {
				switch (item.chooseItem()) {
				case SETTING_LISTVIEW_SECTION:
					SettingSectionItemObject sectionItem = (SettingSectionItemObject) item;
					convertView = mInflater.inflate(
							R.layout.item_settinglistview_section, null);
					holder = new Holder();
					holder.tvTitle = (TextView) convertView
							.findViewById(R.id.setting_listview_section_text);
					holder.tvTitle.setText("" + sectionItem.getTitle());
					convertView.setOnClickListener(null);
					convertView.setOnLongClickListener(null);
					convertView.setLongClickable(false);
					convertView.setTag(holder);
					break;
				case SETTING_LISTVIEW_ENTRY_SWITCH:
					final SettingSwitchObject switchItem = (SettingSwitchObject) item;
					convertView = mInflater.inflate(
							R.layout.item_settinglistview_entry_switch, null);
					holder = new Holder();
					holder.sw = (Switch) convertView
							.findViewById(R.id.settinglistview_entry_sw_switch);
					holder.relativelayout = (RelativeLayout) convertView
							.findViewById(R.id.settinglistview_entry_layout_switch);
					holder.sw.setChecked(switchItem.isSwitchOn());
					holder.sw
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {
									if (switchItem.getId() == SETTING_ITEM_SOUND) {
										switchItem.setSwitchOn(isChecked);
										SharedPreferences sp = mContext
												.getSharedPreferences(
														SHARE_NAME, 0);
										Editor editor = sp.edit();
										editor.putBoolean(SHARE_SW_SOUND,
												isChecked);
										editor.commit();
									}
									if (switchItem.getId() == SETTING_ITEM_AUTO_OPEN_URL) {
										switchItem.setSwitchOn(isChecked);
										SharedPreferences sp = mContext
												.getSharedPreferences(
														SHARE_NAME, 0);
										Editor editor = sp.edit();
										editor.putBoolean(SHARE_SW_AUTO_OPEN,
												isChecked);
										editor.commit();
									}
								}
							});
					holder.tvTitle = (TextView) convertView
							.findViewById(R.id.settinglistview_entry_tv_title);
					holder.tvTitle.setText("" + switchItem.getTitle());
					convertView.setOnClickListener(null);
					convertView.setOnLongClickListener(null);
					convertView.setLongClickable(false);

					FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.relativelayout
							.getLayoutParams();
					params.height = 60;
					holder.relativelayout.setLayoutParams(params);
					convertView.setTag(holder);

					convertView.setTag(holder);
					break;
				case SETTING_LISTVIEW_ENTRY_CHOOSE:
					SettingChooseObject chooseItem = (SettingChooseObject) item;
					convertView = mInflater.inflate(
							R.layout.item_settinglistview_entry_choose, null);
					holder = new Holder();
					holder.relativelayout = (RelativeLayout) convertView
							.findViewById(R.id.settinglistview_entry_layout_choose);
					holder.tvTitle = (TextView) convertView
							.findViewById(R.id.settinglistview_entry_tv_title);
					holder.tvTitle.setText("" + chooseItem.getTitle());
					holder.tvStatus = (TextView) convertView
							.findViewById(R.id.settinglistview_entry_tv_status);
					holder.tvStatus.setText("" + chooseItem.getStatus());
					holder.ivLeft = (ImageView) convertView
							.findViewById(R.id.settinglistview_entry_iv_left);
					if (!chooseItem.isShowStatus()) {
						holder.tvStatus.setVisibility(View.INVISIBLE);
					}
					holder.iv = (ImageView) convertView
							.findViewById(R.id.settinglistview_entry_iv_choose);
					if (!chooseItem.isShowImage()) {
						holder.iv.setVisibility(View.INVISIBLE);
					}
					if (chooseItem.getImage() == 0) {
						holder.ivLeft.setVisibility(View.INVISIBLE);
					} else {
						holder.ivLeft.setImageResource(chooseItem.getImage());
					}

					FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) holder.relativelayout
							.getLayoutParams();
					params1.height = 60;
					holder.relativelayout.setLayoutParams(params1);

					convertView.setTag(holder);
					break;
				}
			}
		} else {
			holder = (Holder) convertView.getTag();
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {

		return getCount();
	}

	@Override
	public int getItemViewType(int position) {

		return position;
	}

	private class Holder {
		TextView tvTitle;
		Switch sw;
		TextView tvStatus;
		ImageView ivLeft;
		ImageView iv;
		RelativeLayout relativelayout;
	}

}
