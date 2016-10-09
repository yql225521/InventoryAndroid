package com.gystudio.base.service;
 

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.gystudio.ksoap2.serialization.SoapPrimitive;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gystudio.base.activity.Logining;
import com.gystudio.base.entity.AppContext;
import com.gystudio.service.SOAPWebServiceTask;
import com.gystudio.utils.StringUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * 服务用于检测服务器是否有更新
 * @author Administrator
 *
 */
public class UpdateMessageService extends Service {
	public static int ID = 1;
	public static final String SERVICEURL = "serviceUrl";
	public static final String NAMESPACE = "nameSpace";
	public static final String NOTIFICATIONID = "notificationId";
	private String serviceUrl;
	private String nameSpace;
	private String areaID;

	@Override
	public void onCreate() {
		super.onCreate();
		/* 获得WebSerivce地址 和 空间名,可修改*/
		SharedPreferences preferences = this.getSharedPreferences("SysConfig",
				Context.MODE_PRIVATE);
		serviceUrl = preferences.getString("ServiceUrl", "");
		nameSpace = preferences.getString("NameSpace", "");
 
		mThread.start();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
//			SelectEntList("140000");
//			 startNotification("dddddddddd"); 
			mHandler.postDelayed(mThread, 100000);
		}
	};

	Thread mThread = new Thread() {

		@Override
		public void run() {
			super.run();
			Message msg = mHandler.obtainMessage();
			msg.sendToTarget();
		}
	};

	private void SelectEntList(String EntName) {
		String methodName = "GetEntStatisticsIndustryType"; // 需调用WebService名称
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("username", AppContext.currUser.getAccounts());
 
		try {
			new SOAPWebServiceTask(null,null) {
				@Override
				public boolean handleMessage(Message msg) {
					switch (msg.what) {
					case 1:
						buildData((SoapPrimitive) msg.obj);
						break;
					case 2:
						break;
					}
					return false;
				}
			}.execute(serviceUrl, nameSpace, methodName, hashMap);
		} catch (Exception e) {
		}
	}

	public void buildData(SoapPrimitive soapObject) {
		try {
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(StringUtil
					.FilterJsonChar(soapObject.toString()));
			Log.e("JsonData", soapObject.toString());
			int success = jsonObject.get("success").getAsInt();
			int total = jsonObject.get("total").getAsInt();
			String message = jsonObject.get("message").getAsString();
			if (success == 1) {
				for (int i = 0; i <= total - 1; i++) {
					try {
						String name = new String(jsonObject.get("list")
								.getAsJsonArray().get(i).getAsJsonObject().get(
										"name").getAsString().toString()
								.getBytes(), "utf-8");

					} catch (UnsupportedEncodingException e) {
					}
				}
				startNotification(message);
			} else {
			}
		} catch (Exception e) {
		} finally {
		}
	}

	/**
	 * 发送Notification
	 */
	private void startNotification(String message) {
		// 获得NotificationManager对象
		NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// 图标资源
		int icon = android.R.drawable.stat_notify_chat;
		CharSequence tickerText = "手机执法";
		// 系统时间
		long when = System.currentTimeMillis();
		// 创建Notification对象
//		Notification notification = new Notification(icon, tickerText, when);
		// 当用户点击后自动销毁
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
		//将此通知放到通知栏的"Ongoing"即"正在运行"组中
//		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// 默认的声音
//		notification.defaults |= Notification.DEFAULT_SOUND;
		// 添加震动
//		notification.defaults |= Notification.DEFAULT_VIBRATE;
		/* 设置扩展信息 */
		// 获得当前上下问环境
		Context context = this.getApplicationContext();
		// 设置标题
		CharSequence title = "你有信消息";
		// 设置内容
		CharSequence content = "详细信息详细信息写";
		//
		Intent intent = new Intent(this, Logining.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

//		notification.setLatestEventInfo(context, title, content, pendingIntent);

		// 广播Notification
//		notiManager.notify(ID, notification);

        Notification noti = new Notification.Builder(this)
                .setContentTitle(tickerText)
                .setContentText(title)
                .setSmallIcon(icon)
                .setContentIntent(pendingIntent)
                .build();
//        noti.notify();
        notiManager.notify(ID,noti);

		ID++;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(this.getClass().getName(), "onDestroy");
	}

}
