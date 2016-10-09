package com.gystudio.base.entity;

import java.io.Serializable;
import java.util.ArrayList;
 
public class MainFunctionInfo implements Serializable {
 
	private static final long serialVersionUID = 1L;

	private int success;
	private int recordcount;
	private String title;
	private String message;
	private ArrayList<MainFunctionType> functionType;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getRecordcount() {
		return recordcount;
	}

	public void setRecordcount(int recordcount) {
		this.recordcount = recordcount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<MainFunctionType> getFunctionType() {
		return functionType;
	}

	public void setFunctionType(ArrayList<MainFunctionType> functionType) {
		this.functionType = functionType;
	}

}
