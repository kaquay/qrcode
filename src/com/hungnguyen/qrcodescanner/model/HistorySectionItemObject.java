package com.hungnguyen.qrcodescanner.model;

public class HistorySectionItemObject implements HistoryItemObject {
	private String title;

	public HistorySectionItemObject(String title) {
		setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public boolean isSection() {
		return true;
	}

}
