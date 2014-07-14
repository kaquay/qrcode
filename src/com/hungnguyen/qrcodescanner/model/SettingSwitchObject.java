package com.hungnguyen.qrcodescanner.model;

import com.hungnguyen.qrcodescanner.utility.Constants;

public class SettingSwitchObject extends SettingItemFactory implements  Constants {
	private String title;
	private boolean isSwitchOn;

	public SettingSwitchObject(int id, String title, boolean is) {
		super(id);
		setTitle(title);
		setSwitchOn(is);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public boolean isSwitchOn() {
		return isSwitchOn;
	}

	public void setSwitchOn(boolean isSwitchOn) {
		this.isSwitchOn = isSwitchOn;
	}

	@Override
	public int chooseItem() {
		return SETTING_LISTVIEW_ENTRY_SWITCH;
	}

}
