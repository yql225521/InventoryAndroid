package com.gystudio.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	/**
	 * 检查是否可以连接到网络
	 * 
	 * @param activity
	 * @return boolean return true if the application can access the internet
	 */
	public static boolean hasInternet(Activity activity) {
		ConnectivityManager manager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}

	/**
	 * 检测链接是否可用
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static boolean checkUriExist(Context context, String path) {
		boolean uriExist = true;
		try {
			URL url = new URL(path);
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection.getResponseCode();
		} catch (IOException e) {
			uriExist = false;
			e.printStackTrace();
		}
		return uriExist;
	}
}
