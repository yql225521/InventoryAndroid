package com.gystudio.base.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gystudio.base.R;
import com.gystudio.base.activity.assetphoto.AssetPhotoUploadActivity;
import com.gystudio.base.activity.assetphoto.CamreaPictureActivity;
import com.gystudio.base.entity.AppContext;
import com.gystudio.base.entity.MainFunctionInfo;
import com.gystudio.base.entity.MainFunctionItem;
import com.gystudio.base.entity.MainFunctionType;
import com.gystudio.utils.Alert;
import com.gystudio.utils.MainGalleryAdapter;
import com.gystudio.utils.NotificationReceiver;
import com.gystudio.utils.ScreenUtil;
import com.gystudio.widget.SlipViewGroup;

/**
 * 主功能页面
 * 
 * @author Administrator
 * 
 */
public class Main extends Activity {

	private MainFunctionInfo functionInfo;
	private SlipViewGroup slipViewGroup;
	private LinearLayout screenPositionLayout;
	private TextView noData;
	public static ArrayList<ImageView> screenPositionList = new ArrayList<ImageView>();
	public int gridViewWitch;
	public int gridViewHeight;
	private Intent intent;
	private static final int NOTIFICATIONID = 102;
	private NotificationReceiver mReceiver;
	private static final String NOTIFICATION_ACTION = "Intent.ACTION_MOBENFOLAW_NOTIFICATION";
	public static final String TASKID = "DTJX_taskID";
	public static final String ICON = "icon";
	public static final String TITLE = "title";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenUtil.HideTitle(this);
		setContentView(R.layout.main);
		setParameters();
		Initialization();
	}

	private void Initialization() {
		slipViewGroup = (SlipViewGroup) findViewById(R.id.slip_viewGroup);
		screenPositionLayout = (LinearLayout) findViewById(R.id.screen_position_layout);
		noData = (TextView) findViewById(R.id.main_no_date);
		intent = this.getIntent();
		// 获得Intent中携带的MainFunctionInfo
		functionInfo = (MainFunctionInfo) intent.getExtras().getSerializable(
				Logining.MAINFUNCTIONINFO);
		// 获得功能类型的数量
		int recordcount = functionInfo.getRecordcount();
		noData(recordcount);// 如果当用户没有功能是显示提示信息
		if (recordcount > 0) {
			for (int i = 0; i < recordcount; i++) {
				slipViewGroup.addView(this.getMainWindow(i, functionInfo
						.getFunctionType().get(i)));
				screenPositionLayout.addView(this.getScreenPositionItem(i));
			}
		}
		registerReceiver();// 注册广播接收器
		startNotification();// 向状态栏“正在运行组”发送通知
	}

	/**
	 * 如果当用户没有功能是显示提示信息
	 */
	private void noData(int recordcount) {
		if (recordcount > 0) {
			slipViewGroup.setVisibility(ViewGroup.VISIBLE);
			screenPositionLayout.setVisibility(LinearLayout.VISIBLE);
		} else {
			noData.setText(functionInfo.getMessage());
			noData.setVisibility(TextView.VISIBLE);
		}
	}

	/**
	 * 注册广播接收器
	 */
	private void registerReceiver() {
		mReceiver = new NotificationReceiver();
		// 生成一个IntentFilter对象
		IntentFilter filter = new IntentFilter();
		// 为IntentFilter添加一个Action
		filter.addAction(NOTIFICATION_ACTION);
		// 将BroadcastReceiver对象注册到系统当中
		Main.this.registerReceiver(mReceiver, filter);
	}

	/**
	 * 设置Gallery 点击监听其
	 */
	OnItemClickListener clickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			HashMap<String, Object> map = (HashMap<String, Object>) arg0
					.getItemAtPosition(arg2);
			int iconRes = Integer.parseInt(map.get(Main.ICON).toString());

            String title = map.get(Main.TITLE).toString();
			String WarningMessage = "由于系统数据库正在优化，此功能目前无法正常使用,给您带来的不便尽请谅解!";
			String NewsClassID = null;

			switch (iconRes) {

			case R.drawable.a_aj_icon_market_search:
				Intent inAssetSearch = new Intent(Main.this, AssetSearch.class);
				Main.this.startActivity(inAssetSearch);
				break;
			case R.drawable.inv_asset:
				Intent inAssetInventory = new Intent(Main.this, AssetInventory.class);
				Main.this.startActivity(inAssetInventory);
				break;	
			case R.drawable.a_as_icon_oa_inform:
				Intent inOfflineManager = new Intent(Main.this, OffLineDataManager.class);
				Main.this.startActivity(inOfflineManager);
				break;
			case R.drawable.a_ay_icon_nolicence_enter:
				Intent inInventory  = new Intent(Main.this, InventorySearch.class);
				Main.this.startActivity(inInventory);
				break;	
			case R.drawable.offline_inv:
				Intent inOffInventory  = new Intent(Main.this, AssetOffLineInventory.class);
				Main.this.startActivity(inOffInventory);
				break;
			case R.drawable.asset_qrcode:
				Intent inScanInventory = new Intent(Main.this, AssetQRShow.class);
				Main.this.startActivity(inScanInventory);
				break;
			case R.drawable.qtydata_syn:
				Intent inQtyMgr = new Intent(Main.this, OffLineQtyDataManager.class);
				Main.this.startActivity(inQtyMgr);
				break;		
			case R.drawable.a_aa_icon_policy:
				Intent cpa = new Intent(Main.this, AssetPhotoUploadActivity.class);
				Main.this.startActivity(cpa);
				break;	 
			case R.drawable.a_ai_icon_help:// 帮助
				Intent inHelpPage = new Intent(Main.this, Help.class);
				Main.this.startActivity(inHelpPage);
				break;
			}
		}
	};


	/**
	 * 获得主功能页面View
	 * 
	 * @param gridViewId
	 * @param types
	 * @return
	 */
	private LinearLayout getMainWindow(int gridViewId, MainFunctionType types) {
		LinearLayout mainWindowLayout = new LinearLayout(this);
		mainWindowLayout.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mainWindowLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		mainWindowLayout.setHapticFeedbackEnabled(false);

		GridView gridView = new GridView(this);
		gridView.setId(gridViewId);
		gridView.setLayoutParams(new LayoutParams(this.gridViewWitch,
				this.gridViewHeight));
		gridView.setVerticalSpacing(10);
		gridView.setHorizontalSpacing(10);
		gridView.setNumColumns(3);
		gridView.setColumnWidth(60);
		gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridView.setBackgroundResource(R.drawable.main_back);
		gridView.setOnItemClickListener(clickListener);// 绑定监听器
		ArrayList<Integer> icons = new ArrayList<Integer>();
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<MainFunctionItem> items = types.getItems();
		for (int i = 0; i < items.size(); i++) {
			MainFunctionItem item = items.get(i);
			String icon = item.getIcon();
			String title = item.getTitle();
			try {
				icons.add(Integer.valueOf(icon, 16));
			} catch (Exception e) {
				icons.add(0x7f020019);
			}
			titles.add(title);
		}
		MainGalleryAdapter mAdapter = new MainGalleryAdapter(this, icons,
				titles);
		gridView.setAdapter(mAdapter);

		mainWindowLayout.addView(gridView);
		return mainWindowLayout;
	}

	/**
	 * 获得标记当前屏幕索引的View
	 * 
	 * @param index
	 * @return
	 */
	private ImageView getScreenPositionItem(int index) {
		ImageView mImageView = new ImageView(this);
		mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mImageView.setPadding(2, 2, 2, 2);
		if (index == 0) {
			mImageView.setImageResource(R.drawable.screen_position_focused);
		} else {
			mImageView.setImageResource(R.drawable.screen_position_unfocused);

		}
		screenPositionList.add(mImageView);
		return mImageView;
	}

	private void setParameters() {
		DisplayMetrics dm = ScreenUtil.GetScreenPixels(this);
		int height = dm.heightPixels;
		int width = dm.widthPixels;
		switch (height) {
		case 320:
			gridViewWitch = 220;
			gridViewHeight = 235;
			break;
		case 480:
			gridViewWitch = 310;
			gridViewHeight = 375;
			break;
		case 800:
			gridViewWitch = 460;
			gridViewHeight = 640;
			break;
		case 854:
			gridViewWitch = 460;
			gridViewHeight = 680;
		case 1920:
			gridViewWitch = 980;
			gridViewHeight = 1560;	
			break;
		case 1280:
			gridViewWitch = 660;
			gridViewHeight = 1080;	
			break;	
		default:
			gridViewWitch = width - 20;
			gridViewHeight = height - 160;
		}
	}

	/**
	 * 为GridView绑定适配器
     * @param mGridView
     * @param titles
     * @param icons
     */
	public void setAdapterForGridView(GridView mGridView,
			ArrayList<String> titles, ArrayList<Integer> icons) {

		MainGalleryAdapter mAdapter = new MainGalleryAdapter(this, icons,
				titles);
		mGridView.setAdapter(mAdapter);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitApp();// 退出程序
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	/**
	 * 退出程序
	 */
	private void exitApp() {
		Alert.DisplayAlertQqDialog(this, 1, "退出", "你确定要退出系统吗？", "确定", "取消",
				OKClick, null);
	}

	// 按下键盘上返回按钮
	View.OnClickListener OKClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// 获得NotificationManager对象
			NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notiManager.cancel(NOTIFICATIONID);
			// 解除BroadcastReceiver对象的注册
			Main.this.unregisterReceiver(mReceiver);
			Intent in = new Intent();
			Main.this.setResult(0, in);
			Main.this.finish();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main_option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 获得NotificationManager对象
		NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		switch (item.getItemId()) {
		case R.id.main_option_about:
			//显示关于对话框
			Alert.DisplayAlertAppAboutDialog(Main.this);
			break;
		case R.id.main_option_exit:
			exitApp();// 退出程序
			break;
		case R.id.main_option_logout:
			screenPositionList.clear();
			notiManager.cancel(NOTIFICATIONID);
			// 解除BroadcastReceiver对象的注册
			Main.this.unregisterReceiver(mReceiver);
			Intent intent = new Intent(Main.this, Login.class);
			Main.this.startActivity(intent);
			Main.this.finish();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	/**
	 * 发送Notification
	 */
	private void startNotification() {
		// 获得NotificationManager对象
		NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// 图标资源
		int icon = R.drawable.d_ico_asset;
		CharSequence tickerText = "资产系统手机端";
		// 系统时间
		long when = System.currentTimeMillis();

        // 创建Notification对象
//        Notification notification= new Notification(icon,tickerText,when);
		// 当用户点击后自动销毁
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
//		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// 默认的声音
		// notification.defaults |= Notification.DEFAULT_SOUND;
		// 添加震动
		// notification.defaults |= Notification.DEFAULT_VIBRATE;
		/* 设置扩展信息 */
		// 获得当前上下问环境
		Context context = this.getApplicationContext();
		// 设置标题
		CharSequence title = tickerText+" 正在运行...";
		// 设置内容
		CharSequence content = "当前登陆用户: " + AppContext.currUser.getUsername();
		//Intent intent = new Intent(context,Main.class);
		//intent.putExtra(TASKID, Main.this.getTaskId());
		//intent.setAction(NOTIFICATION_ACTION);

		PendingIntent pendingIntent =  PendingIntent.getActivity(Main.this,0,new Intent(Main.this,Main.class),0);
				//PendingIntent.getBroadcast(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

//		notification.setLatestEventInfo(context, title, content, pendingIntent);
//
//		// 广播Notification
//		notiManager.notify(NOTIFICATIONID, notification);


        /*
        Notification 新版本用法
         */
        Notification noti = new Notification.Builder(this)
                .setContentTitle(tickerText)
                .setContentText(title)
                .setSmallIcon(icon)
                .setContentIntent(pendingIntent)
                .build();
//        noti.notify();
        notiManager.notify(NOTIFICATIONID,noti);
	}
}
