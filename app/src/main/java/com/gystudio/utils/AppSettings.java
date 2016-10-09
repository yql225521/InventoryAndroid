package com.gystudio.utils;

import java.util.HashMap;

import org.gystudio.ksoap2.serialization.SoapPrimitive;

import com.gystudio.service.SOAPWebServiceTask;

import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class AppSettings extends PreferenceActivity {

	public static final String USER_ORGAN_KEY = "userOrganKey";
	public static final String USER_ORGAN_NAME = "userOrganName";
	public static final String APPSETTINGGROUPNAME = "org.gystudio.ams_preferences";
	public static final String USER_ORGAN_DEFAULT_VALUE = "无";
	private String userOrganType[] = null;
	private String userOrganValue[] = null;
	private String userOrganListPreferenceSummary;// 组织机构的初始值
	private PreferencesUtil pu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Initialization();
	}

	private void Initialization() {
		pu = new PreferencesUtil(AppSettings.this);
		SpinnerInitialication();// 下载组织机构ID
	}

	/**
	 * 从SharedPreferences中获得保存的组织机构
	 */
	private void getSaveUserOrganValue() {
		// 从SharedPreferences中读取“组织机构”
		String userOrgan = pu.Read(AppSettings.APPSETTINGGROUPNAME,
				AppSettings.USER_ORGAN_KEY);
		if ("".equals(userOrgan) || USER_ORGAN_DEFAULT_VALUE.equals(userOrgan)) {
			userOrganListPreferenceSummary = USER_ORGAN_DEFAULT_VALUE;
		} else {
			if(userOrganType != null && userOrganValue != null){
				for(int i = 0;i<userOrganValue.length;i++){
					if(userOrgan.equals(userOrganValue[i]) || userOrgan == userOrganValue[i]){
						userOrganListPreferenceSummary = userOrganType[i];
						break;
					}
				}
			}
		}
	}

	

	private void SpinnerInitialication() { 
		String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址
		String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
		String methodName = "GetUserOrgan"; // 需调用WebService名称
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {
			new SOAPWebServiceTask(this,"正在加载...") {
				@Override
				public boolean handleMessage(Message msg) {
					switch (msg.what) {
					case 1:
						buildData((SoapPrimitive) msg.obj);
						break;
					case 2: 
						Alert.DisplayAlertQqDialog(AppSettings.this, 0, "提示",
								"远程服务器无响应！", "确定",null,new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										finish();
									}
								});
						break;
					}
					return false;
				}
			}.execute(serviceUrl, nameSpace, methodName, hashMap);
		} catch (Exception e) {
			Alert.DisplayAlertQqDialog(this, 2, "提示", "网络出现异常,是否重新加载！",
					"确定","取消",oKClick,cancelClick); 
		}
	}
	
	public void buildData(SoapPrimitive soapObject) {
		
	}
	
	/**
	 * 响应对话框“确定”按钮监听器
	 */
	OnClickListener oKClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SpinnerInitialication();
		}
	};
	
	/**
	 * 响应对话框“取消”按钮监听器
	 */
	OnClickListener cancelClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};

}
