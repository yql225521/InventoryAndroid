package com.gystudio.base.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class MainFunctionType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private int recordcount;
	private ArrayList<MainFunctionItem> items;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRecordcount() {
		return recordcount;
	}

	public void setRecordcount(int recordcount) {
		this.recordcount = recordcount;
	}

	public ArrayList<MainFunctionItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<MainFunctionItem> items) {
		this.items = items;
	}

}
