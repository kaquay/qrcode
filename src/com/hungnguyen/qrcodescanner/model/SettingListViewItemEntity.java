package com.hungnguyen.qrcodescanner.model;

public class SettingListViewItemEntity implements SettingItemEntity {
	protected int id;
	public SettingListViewItemEntity(int id) {
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
