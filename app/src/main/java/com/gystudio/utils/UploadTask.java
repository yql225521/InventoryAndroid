/**
 * Project Name:baseadr
 * File Name:ddd.java
 * Package Name:com.gystudio.utils
 * Date:2016年5月13日上午9:25:06
 * Copyright (c) 2016, ybf_news@163.com All Rights Reserved.
 *
*/

package com.gystudio.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.gystudio.base.entity.UploadBean;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

/**
 * 
 * 上传工具类 
 */
public class UploadTask  extends AsyncTask<Object, Void, Object> { 
	private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
																			// 随机生成
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
	
	protected ProgressDialog pdialog;
	private Context context = null;

	public UploadTask(Context ctx,String msg) {
		this.context = ctx;
		if(msg!=null){
			pdialog = ProgressDialog.show(context, "正在上传...", msg==null?"系统正在处理您的请求":msg);
		}
	} 

 

	private static final String TAG = "UploadUtil";
	private int readTimeOut = 10 * 1000; // 读取超时
	private int connectTimeout = 10 * 1000; // 超时时间
	/***
	 * 请求使用多长时间
	 */
	private static int requestTime = 0;

	private static final String CHARSET = "utf-8"; // 设置编码

	/***
	 * 上传成功
	 */
	public static final int UPLOAD_SUCCESS_CODE = 1;
	/**
	 * 文件不存在
	 */
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	/**
	 * 服务器出错
	 */
	public static final int UPLOAD_SERVER_ERROR_CODE = 3; 
	
	/**
	 * 上传完成
	 */
	public static final int UPLOAD_END_CODE = 4; 
	
	
	protected static final int WHAT_TO_UPLOAD = 1;
	protected static final int WHAT_UPLOAD_DONE = 2;

	/**
	 * android上传文件到服务器
	 * 
	 * @param filePath
	 *            需要上传的文件的路径
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public File getFile(UploadBean upbean) {
		if (upbean.getFilepath() == null) { 
			return null;
		}
		try {
			File file = new File(upbean.getFilepath());
			return file;
		} catch (Exception e) { 
			e.printStackTrace();
			return null;
		}
		
	}
 

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public UploadResult uploadFile(final UploadBean upbean, final String fileKey, final String RequestURL) {
		final File file = this.getFile(upbean);
		if (file == null || (!file.exists())) { 
			return new UploadResult(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在",null);
		}

		Log.i(TAG, "请求的URL=" + RequestURL);
		Log.i(TAG, "请求的fileName=" + file.getName());
		Log.i(TAG, "请求的fileKey=" + fileKey); 
		return toUploadFile(file, fileKey, RequestURL, upbean.getParam(),upbean); 

	}

	private UploadResult toUploadFile(File file, String fileKey, String RequestURL, Map<String, String> param,final UploadBean upbean) {
		String result = null;
		requestTime = 0;

		long requestTime = System.currentTimeMillis();
		long responseTime = 0;

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeOut);
			conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
			// conn.setRequestProperty("Content-Type",
			// "application/x-www-form-urlencoded");

			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = "";

			/***
			 * 以下是用于上传参数
			 */
			if (param != null && param.size() > 0) {
				Iterator<String> it = param.keySet().iterator();
				while (it.hasNext()) {
					sb = null;
					sb = new StringBuffer();
					String key = it.next();
					String value = param.get(key);
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END)
							.append(LINE_END);
					sb.append(value).append(LINE_END);
					params = sb.toString();
					Log.i(TAG, key + "=" + params + "##");
					dos.write(params.getBytes());
					// dos.flush();
				}
			}

			sb = null;
			params = null;
			sb = new StringBuffer();
			/**
			 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
			 * filename是文件的名字，包含后缀名的 比如:abc.png
			 */
			sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
			sb.append("Content-Disposition:form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + "\""
					+ LINE_END);
			sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的
																// ，用于服务器端辨别文件的类型的
			sb.append(LINE_END);
			params = sb.toString();
			sb = null;

			Log.i(TAG, file.getName() + "=" + params + "##");
			dos.write(params.getBytes());
			/** 上传文件 */
			InputStream is = new FileInputStream(file); 
			byte[] bytes = new byte[1024];
			int len = 0;
			int curLen = 0;
			while ((len = is.read(bytes)) != -1) {
				curLen += len;
				dos.write(bytes, 0, len); 
			}
			is.close();

			dos.write(LINE_END.getBytes());
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
			dos.write(end_data);
			dos.flush();
			//
			// dos.write(tempOutputStream.toByteArray());
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			responseTime = System.currentTimeMillis();
			this.requestTime = (int) ((responseTime - requestTime) / 1000);
			Log.e(TAG, "response code:" + res);
			if (res == 200) {
				Log.e(TAG, "request success");
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				Log.e(TAG, "result : " + result); 
				return new UploadResult(UPLOAD_SUCCESS_CODE,"上传结果:"+result,upbean);
			} else {
				Log.e(TAG, "request error"); 
				return new UploadResult(UPLOAD_SUCCESS_CODE,"上传失败：code="+res,upbean); 
			}
		} catch (MalformedURLException e) {  
			e.printStackTrace();
			return new UploadResult(UPLOAD_SUCCESS_CODE,"上传失败：error="+e.getMessage(),upbean); 
		} catch (IOException e) { 
			e.printStackTrace();
			return new UploadResult(UPLOAD_SUCCESS_CODE,"上传失败：error="+e.getMessage(),upbean); 
		}
	}  
	
	protected Object onCallBack(Object soapObject) {
		return soapObject;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if(pdialog!=null){
			pdialog.dismiss();
		}
		UploadResult uploadResult=(UploadResult)result;
		Message msg = new Message(); 
		 msg.what = uploadResult.code;
		 msg.obj = uploadResult.obj; 
		 handleMessage(msg);
	}

	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * 异步调用
	 */
	@SuppressWarnings( { "unchecked" })
	@Override
	protected Object doInBackground(Object... params) {
		// parameters
		String RequestURL = params[0].toString();
		String fileKey = params[1].toString();
	    UploadBean uploadbean= (UploadBean)params[2]; 
	    return this.uploadFile(uploadbean, fileKey, RequestURL);  
	}
	
	public static class UploadResult {
		public int code;
		public String msg;
		public Object obj;
		
		public UploadResult(int code, String msg, Object obj) {
			super();
			this.code = code;
			this.msg = msg;
			this.obj = obj;
		} 
	}
	 
	 
	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	} 

}