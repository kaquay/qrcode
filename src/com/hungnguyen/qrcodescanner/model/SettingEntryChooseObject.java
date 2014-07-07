package com.hungnguyen.qrcodescanner.model;

import com.hungnguyen.qrcodescanner.ulty.Constants;

public class SettingEntryChooseObject extends SettingListViewItem implements  Constants {
	private String title;
	private String status;
	private boolean isShowStatus;
	private boolean isShowImage;

	public SettingEntryChooseObject(int id, String title, String status,
			boolean isshowstatus, boolean isshowimage) {
		super(id);
		setTitle(title);
		setStatus(status);
		setShowStatus(isshowstatus);
		setShowImage(isshowimage);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isShowStatus() {
		return isShowStatus;
	}

	public void setShowStatus(boolean isShowStatus) {
		this.isShowStatus = isShowStatus;
	}

	public boolean isShowImage() {
		return isShowImage;
	}

	public void setShowImage(boolean isShowImage) {
		this.isShowImage = isShowImage;
	}

	@Override
	public int chooseItem() {
		return SETTING_LISTVIEW_ENTRY_CHOOSE;
	}

}
