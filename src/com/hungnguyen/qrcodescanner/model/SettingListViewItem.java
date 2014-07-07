package com.hungnguyen.qrcodescanner.model;

public class SettingListViewItem implements SettingItemObject {
	protected int id;
	public SettingListViewItem(int id) {
		setId(id);
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int chooseItem() {
		return 0;
	}

}
