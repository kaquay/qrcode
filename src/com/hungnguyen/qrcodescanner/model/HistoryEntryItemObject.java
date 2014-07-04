package com.hungnguyen.qrcodescanner.model;

public class HistoryEntryItemObject implements HistoryItemObject{

	private String id;
	private String title;
	
	public HistoryEntryItemObject(String id, String title){
		setId(id);
		setTitle(title);
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


	@Override
	public boolean isSection() {
		return false;
	}

}
