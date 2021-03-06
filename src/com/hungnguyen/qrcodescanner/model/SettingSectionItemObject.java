package com.hungnguyen.qrcodescanner.model;

import com.hungnguyen.qrcodescanner.utility.Constants;

public class SettingSectionItemObject implements SettingItemEntity, Constants {
	private String title;

	public SettingSectionItemObject(String title) {
		setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int chooseItem() {
		return SETTING_LISTVIEW_SECTION;
	}

}
