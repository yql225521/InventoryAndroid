package com.gystudio.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.gystudio.ksoap2.SoapEnvelope;
import org.gystudio.ksoap2.serialization.SoapObject;
import org.gystudio.ksoap2.serialization.SoapPrimitive;
import org.gystudio.ksoap2.serialization.SoapSerializationEnvelope;
import org.gystudio.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class SOAPWebServiceTask extends AsyncTask<Object, Void, Object> {
	
	
	protected ProgressDialog pdialog;
	private Context context = null;
	
	public SOAPWebServiceTask(Context ctx,String msg) { 
		this.context = ctx;
		if(msg!=null){
			pdialog = ProgressDialog.show(context, "正在加载...", msg==null?"系统正在处理您的请求":msg);
		}
	}
	
	/*
	 * 异步调用
	 */
	@SuppressWarnings( { "unchecked" })
	@Override
	protected Object doInBackground(Object... params) {
		// parameters
		String serviceUrl = params[0].toString();
		String nameSpace = params[1].toString();
		String methodName = params[2].toString();
		HashMap<String, String> hashMap = (HashMap<String, String>) params[3];
		SoapPrimitive soapObject = null;
		try {
			String SOAP_ACTION = nameSpace + methodName;
			// 第1步： 创建SoapObject对象，指定WebService的命名空间和调用方法名
			SoapObject request = new SoapObject(nameSpace, methodName);
			// 第2步： 创建WebService方法的参数
			Iterator<Entry<String, String>> iterator = hashMap.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) iterator
						.next();
				Object value = entry.getValue();
				Object key = entry.getKey();
				//System.out.println("  call para  "+key+":"+value);
				request.addProperty(key.toString(), value.toString());
			}
			// 第3步：创建SoapSerializationEnvelope对象，并指定WebService的版本
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// 设置bodyOut属性
			envelope.bodyOut = request;
			// 设置.net的WebService ，要不然会出错
			envelope.dotNet = false;
			envelope.setOutputSoapObject(request);
			// 第4步：创建HttpTransportSE对象,并指定WSDL文档的URL
			HttpTransportSE ht = new HttpTransportSE(serviceUrl,180000);
			ht.debug = true;
			Log.i(" call methodName  "+methodName, "start "+methodName);
			// 调用WebService
			
			ht.call(SOAP_ACTION, envelope);
			// 第6步： 使用getResponse方法获得WebService方法的返回结果
			soapObject = (SoapPrimitive) envelope.getResponse();
			Log.i(" call methodName  "+methodName, "end "+methodName);
			return onCallBack(soapObject);
		} catch (Exception e) {
			 Log.i("call  methodName",methodName);
			 e.printStackTrace();
     		// Log.e("WEB_exception", e.getMessage());
		}
		return soapObject;
	}
	 
	protected Object onCallBack(Object soapObject) {
		return soapObject;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result); 
		Message msg = new Message();
		if (result == null) {
			msg.what = 2;
		} else {
			msg.what = 1;
			msg.obj = result;
		}
		handleMessage(msg);
		
		if(pdialog!=null){
			pdialog.dismiss();
		}
	}

	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 同步调用
	 */
	public SoapObject getWebService(String serviceUrl, String nameSpace,
			String methodName, HashMap<String, String> hashMap) {
		try {
			String SOAP_ACTION = nameSpace + methodName;

			// 第1步： 创建SoapObject对象，指定WebService的命名空间和调用方法名
			SoapObject request = new SoapObject(nameSpace, methodName);
			// 第2步： 创建WebService方法的参数
			Iterator<Entry<String, String>> iterator = hashMap.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) iterator
						.next();
				Object value = entry.getValue();
				Object key = entry.getKey();
				request.addProperty(key.toString(), value.toString());
			}
			// 第3步：创建SoapSerializationEnvelope对象，并指定WebService的版本
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// 设置bodyOut属性
			envelope.bodyOut = request;
			// 设置.net的webservice ，要不然会出错
			envelope.dotNet = true;
			// envelope.setOutputSoapObject(request);
			// 第4步：创建HttpTransportSE对象,并指定WSDL文档的URL
			HttpTransportSE ht = new HttpTransportSE(serviceUrl);
			ht.debug = true;
			// 调用WebService
			ht.call(SOAP_ACTION, envelope);
			// 第6步： 使用getResponse方法获得WebService方法的返回结果
			SoapObject soapObject = (SoapObject) envelope.getResponse();
			return soapObject;

		} catch (Exception e) {
			//Log.e("WEB_exception", e.getMessage());
			 e.printStackTrace();
		}
		return null;
	}
}