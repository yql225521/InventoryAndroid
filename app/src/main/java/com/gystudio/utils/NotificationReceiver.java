package com.gystudio.utils;
 

import java.util.List;

import com.gystudio.base.activity.Main;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

 

/**
 * @author Administrator 响应状态栏的接收器
 */
public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int taskId = intent.getIntExtra(Main.TASKID, 0);//获得MobEnfoLaw的TaskID
		
		ActivityManager mManager = (ActivityManager) context
				.getSystemService(Activity.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = mManager
				.getRunningTasks(20);
		for (ActivityManager.RunningTaskInfo amTask : list) {
			if (amTask.id == taskId) {
				ComponentName componentname = amTask.topActivity;
				String action = componentname.toString().split("/")[1].replace("}", "");
				Log.i("", "action=="+action);
				Intent mIntent = new Intent();
				mIntent.setAction(action);
				context.startActivity(mIntent);
			}
		}
	}

}
