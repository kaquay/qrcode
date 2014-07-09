package com.hungnguyen.qrcodescanner.model;

public class HistoryItemObject implements HistoryItemEnity {

	private String id;
	private String title;
	private boolean isShowLeft;
	private boolean isShowRight;

	public HistoryItemObject(String id, String title) {
		setId(id);
		setTitle(title);
		setShowLeft(false);
		setShowRight(false);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isShowLeft() {
		return isShowLeft;
	}

	public void setShowLeft(boolean isShowLeft) {
		this.isShowLeft = isShowLeft;
	}

	public boolean isShowRight() {
		return isShowRight;
	}

	public void setShowRight(boolean isShowRight) {
		this.isShowRight = isShowRight;
	}

	@Override
	public boolean isSection() {
		return false;
	}

}
