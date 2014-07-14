package com.hungnguyen.qrcodescanner.model;

public class SettingItemFactory implements SettingItemEntity {
	protected int id;
	public SettingItemFactory(int id) {
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
