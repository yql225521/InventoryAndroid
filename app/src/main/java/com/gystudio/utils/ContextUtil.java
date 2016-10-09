package com.gystudio.utils;

import android.app.Application;

/**
 * 整个应用程序Applicaiton 
 * @version 1.0
 * 
 */
public class ContextUtil extends Application {

	private static ContextUtil instance;

	public static ContextUtil getInstance() {
		return instance;
	} 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
	}
}