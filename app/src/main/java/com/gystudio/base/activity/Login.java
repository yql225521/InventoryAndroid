package com.gystudio.base.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.util.ByteArrayBuffer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gystudio.base.R;
import com.gystudio.base.entity.AppContext;
import com.gystudio.db.DataHelper;
import com.gystudio.db.DataManager;
import com.gystudio.utils.Alert;
import com.gystudio.utils.AppSettings;
import com.gystudio.utils.ContextUtil;
import com.gystudio.utils.NetUtil;
import com.gystudio.utils.PreferencesUtil;
import com.gystudio.utils.PxUtils;
import com.gystudio.utils.SDcardUtil;
import com.gystudio.utils.ScreenUtil;
import com.gystudio.utils.SysConfig;
import com.gystudio.widget.GyBaseActivity;
import com.rstco.sjpt.entity.CfgEntity;

public class Login extends GyBaseActivity<DataHelper> {
	private EditText Username = null;
	private EditText Password = null;
	private Button btnLogin = null;
	private Button btnOffLogin = null;
	private TextView chgUser =null;
	private Handler mHandler;
	private Boolean IsAuto = true;
	private String newVerFileName = null;
	public ProgressDialog pBar = null;
	private int fileLength = 0;
	PreferencesUtil pu = new PreferencesUtil(Login.this);
	private boolean offline_flag=false;
	private DataManager dataManager=new DataManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenUtil.HideTitle(this);

		String acc=pu.Read("Login", "Username");

		if(StringUtils.isNotBlank(acc)){
			CfgEntity cfg=null;

			try{
				cfg=dataManager.getCfg(this.getHelper().getCfgDao(), acc, "offline_flag");

			}catch(Exception ex){
				ex.printStackTrace();
			}
			if(cfg!=null&&"1".equals(cfg.getVal())){
				setContentView(R.layout.offline_login);
				offline_flag=true;
			}else{
				offline_flag=false;
				setContentView(R.layout.login);
			}
		}else{
			setContentView(R.layout.login);
		}

		Initialization();

		mHandler = new Handler();
		/* Get Last Update Time from Preferences */
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceid = tm.getDeviceId();   //取出用户手机号码，
		// String imei =tm.getSimSerialNumber();  //取出IMEI，
		AppContext.simId=tm.getSubscriberId(); //取出IMSI
		AppContext.tel=tm.getLine1Number(); 
		System.out.println("tel=======>"+ AppContext.tel);
		System.out.println("deviceid=======>"+deviceid);
		System.out.println("simId=======>"+ AppContext.simId);
		if (NetUtil.hasInternet(Login.this)) {// 判断网络是否可用
			AppContext.hasnet=true;
			AppContext.setLocationOption(this.getApplicationContext());
//			Alert.DisplayAlertQqDialog(Login.this, 1, "提示", "不能连接到网络！", "确定", new OnClickListener() {
//
//				@Override
//				public void onClick(View paramView) {
//					// TODO Auto-generated method stub
//					finish();
//				}
//
//			});
			//Toast.makeText(Login.this, "获得当前位置....", Toast.LENGTH_LONG).show();
		}
		ExecCheckUpdate();
	}


	private void Initialization() {
		DisplayMetrics dm = ScreenUtil.GetScreenPixels(this);
		int screenWidth = dm.widthPixels;
		int screenHeigth = dm.heightPixels;

		Username = (EditText) findViewById(R.id.login_edit_account);
		Password = (EditText) findViewById(R.id.login_edit_pwd);
		btnLogin = (Button) findViewById(R.id.login_btn_login);
		if(offline_flag){
			chgUser =  (TextView) findViewById(R.id.ohter_login_textView);
			chgUser.setOnClickListener(ChgUserClick);
		}
		Log.i("screenWidth", String.valueOf(screenWidth));
		Log.i("screenHeigth", String.valueOf(screenHeigth));
		if (screenWidth == 240 && screenHeigth == 320) {
			btnLogin.setHeight(22);
			btnLogin.setWidth(100);
		}
		
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//Toast.makeText(Login.this, "当前手机号"+AppContext.tel, Toast.LENGTH_LONG).show();
				if (StringUtils.isBlank(AppContext.getAddress())) {
					Alert.DisplayToast(Login.this, "未获得当前位置！");
					return;
				}
				if ("".equals(Username.getText().toString().trim())) {
					Alert.DisplayToast(Login.this, "请输入用户名！");
					return;
				}
				if ("".equals(Username.getText().toString().trim())) {
					Alert.DisplayToast(Login.this, "请输入密码！");
					return;
				}


				pu.Write("Login", "Username", Username.getText().toString()
						.trim());


				Intent inLogining = new Intent(Login.this, Logining.class);
				// 把用户名传给下一个activity
				Bundle bundle = new Bundle();
				bundle.putString("Username", Username.getText().toString()
						.trim());
				bundle.putString("Password", Password.getText().toString()
						.trim());
				//bundle.putString("UserOrgan", userOrgan);
				inLogining.putExtras(bundle);
				startActivityForResult(inLogining, 0);

				// finish();
			}
		});
		// 获取上次保存的用户名和密码
		PreferencesUtil pu = new PreferencesUtil(Login.this);
		Username.setText(pu.Read("Login", "Username"));

		if(offline_flag){
			lock();
			CfgEntity cfgOrgn=null;
			CfgEntity cfgUser=null;
			try{
				cfgOrgn=dataManager.getCfg(this.getHelper().getCfgDao(),Username.getText().toString(), "organInfo");
				cfgUser=dataManager.getCfg(this.getHelper().getCfgDao(),Username.getText().toString(), "userName");
			}catch(Exception ex){
				ex.printStackTrace();
				Alert.DisplayToast(Login.this, "离线数据出错不能离线登陆！");
			}
			final CfgEntity cfgOrgnf=cfgOrgn;
			final CfgEntity cfgUserf=cfgUser;
			btnOffLogin = (Button) findViewById(R.id.login_btn_offlogin);
			btnOffLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(null==cfgOrgnf){
						Alert.DisplayToast(Login.this, "离线数据出错不能离线登陆！");
					}else{
						Bundle bundle = new Bundle();
						bundle.putString("Username", Username.getText().toString()
								.trim());
						bundle.putString("offlineLogin", "1");
						bundle.putString("organInfo", cfgOrgnf.getVal());
						bundle.putString("userName", cfgUserf.getVal());
						
						//bundle.putString("UserOrgan", userOrgan);
						Intent inLogining = new Intent(Login.this, Logining.class);
						inLogining.putExtras(bundle);
						startActivityForResult(inLogining, 0);
					}
				}

			});
		}


	}



	private void ExecCheckUpdate() {
		SharedPreferences prefs = getPreferences(0);
		long lastUpdateTime = prefs.getLong("lastUpdateTime",
				System.currentTimeMillis());
		double curVersion = getVersion();
		Log.i("version", String.valueOf(curVersion));
		/* 检查上次更新时间到现在是否满足一天 */
		if ((lastUpdateTime + (24 * 60 * 60 * 1000)) < System
				.currentTimeMillis()
				|| !IsAuto) {
			Alert.DisplayProgressDialog(Login.this, "", "检查是否有新版本...");
			/* 保存当前时间给下次更新使用 */
			lastUpdateTime = System.currentTimeMillis();
			SharedPreferences.Editor editor = getPreferences(0).edit();
			editor.putLong("lastUpdateTime", lastUpdateTime);
			editor.commit();
			/* 开始检查更新 */
			checkUpdate();
		}
	}

	/**
	 * 检查更新
	 * 
	 * @paramurl
	 */
	void checkUpdate() {
		// 此线程负责在后台检查是否有新版本
		new Thread() {
			public void run() {
				Message msg = myHandler.obtainMessage();
				try {
					if (NetUtil.hasInternet(Login.this)) {// 判断网络是否可用
						URL updateURL = new URL(SysConfig.getAutoUpdateUrl()+ "&func=checkVersion");
						URLConnection conn = updateURL.openConnection();
						conn.setConnectTimeout(10000);
						InputStream is = conn.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(is);

						ByteArrayBuffer baf = new ByteArrayBuffer(1024);
						int current = 0;
						while ((current = bis.read()) != -1) {
							baf.append((byte) current);
						}
						/* 转换字节数组为字符串 */
						String s = new String(baf.toByteArray());
						msg.obj = s;
						msg.what = 1;
					} else {
						msg.obj = "不能连接到网络！";
						msg.what = 2;
					}
				} catch (SocketTimeoutException e) {
					stop();
					msg.obj = "连接更新服务器出错！";
					msg.what = 2;
				} catch (Exception e) {
					stop();
					msg.obj = "不能连接到网络！";
					msg.what = 2;
				}
				msg.sendToTarget();
			}
		}.start();
	}

	/**
	 * 下载完成之后更新UI
	 */
	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Alert.DisplayProgressDialogCancel();
			String msgStr = msg.obj.toString();
			switch (msg.what) {
			case 1:
				buildData(msgStr);
				break;
			case 2:
				Alert.DisplayAlertQqDialog(Login.this, 2, "提示", msgStr, "确定");
				break;
			}
		}

	};

	/**
	 * 处理检查更新
	 * 
	 * @param jsonStr
	 */
	private void buildData(String jsonStr) {
		String Msg = "您现在使用的是最新版本,不需要更新!";
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonStr
				.toString());
		int success = jsonObject.get("success").getAsInt();
		if (success == 1) {
			JsonObject json = jsonObject.get("apk").getAsJsonObject();
			Log.e("version", json.get("version").getAsString().toString());

			newVerFileName = json.get("fileName").getAsString();
			/* 得到当前系统的版本 */
			double curVersion = getVersion();
			double newVersion = json.get("version").getAsDouble();
			fileLength = json.get("fileSize").getAsInt();
			/* 判段服务器上是否有新版本 */
			if (newVersion > curVersion) {
				/* 提示用户有新版本让用户选择是否开始下载 */
				mHandler.post(showUpdate);
			} else {
				if (!IsAuto) {
					Alert.DisplayAlertQqDialog(Login.this, 0, "提示", Msg, "确定");
				}else{
					Alert.DisplayToast(Login.this, Msg);
				}
			}
		} else {
			Alert.DisplayAlertQqDialog(Login.this, 2, "提示", "不能连接到网络！", "确定");
		}
	}

	private Runnable showUpdate = new Runnable() {
		public void run() {
			Alert.DisplayAlertQqDialog(Login.this, 0, "系统更新", "发现新版本"
					+ newVerFileName + "，是否更新？", "是", "否",
					new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (SDcardUtil.isExistsSdcard()) {// 判断sdcard是否存在
						pBar = new ProgressDialog(Login.this);
						pBar.setTitle("提示");
						pBar.setMessage("正在下载中...");
						pBar.setIndeterminate(false);
						pBar
						.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						pBar.setProgress(100);
						pBar.setMax(fileLength);
						pBar.setIndeterminate(false);
						downFile(SysConfig.getAutoUpdateUrl()
								+ "&func=download");
					} else {
						Alert.DisplayAlertQqDialog(Login.this, 0, "提示", "请插入sdcard！",
								"确定");
					}
				}
			});
		}
	};

	/**
	 * 下载新版本并存到sdcard中
	 * @param url
	 */
	void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(Environment
								.getExternalStorageDirectory(), newVerFileName);
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							pBar.setProgress(count);
							try {
								Thread.sleep(5);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (length > 0) {
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	void down() {
		mHandler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});

	}

	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri
				.fromFile(new File("/sdcard/" + newVerFileName)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:// 系统登录过程中的返回值
			Bundle bundle = data.getExtras();
			String ErrMessage = bundle.get("ErrMessage").toString();
			Alert.DisplayAlertQqDialog(Login.this, 0, "提示", ErrMessage, "确定");
			break;
		case 0:// 退出系统
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 *获得当前程序的版本号 
	 * @return
	 */
	private double getVersion() {
		double curVersion = 0; 
		curVersion =  Login.getLocalVersion(ContextUtil.getInstance()); 
		return curVersion;
	}
	
	
	/**
	 * 获取应用程序版本（versionName）
	 * @return 当前应用的版本号
	 */
	private static double getLocalVersion(Context context) {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			Log.e(Login.class.getSimpleName(), "获取应用程序版本失败，原因："+e.getMessage());
			return 0.0;
		}
		
		return Double.valueOf(info.versionCode);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login_option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login_option_about:
			//显示关于对话框
			Alert.DisplayAlertAppAboutDialog(Login.this);
			break;
		case R.id.login_option_checkupdate:
			IsAuto = false;
			ExecCheckUpdate();
			break;
		case R.id.login_option_exit:
			Intent in = new Intent();
			Login.this.setResult(RESULT_OK, in);
			Login.this.finish();
			break;
		case R.id.login_option_settings:
			Intent intent = new Intent(Login.this,this.getClass());
			Login.this.startActivity(intent);
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	OnClickListener ChgUserClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Username.setText("");
			Unlock();
			btnOffLogin.setVisibility(View.GONE);
			chgUser.setVisibility(View.GONE);
			btnLogin.setWidth(PxUtils.dip2px(Login.this, 130));
		}
	};




	/**
	 * 设置提示用户选择组织机构的对话框中OKClick按钮监听器
	 */
	OnClickListener OKClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Login.this, AppSettings.class);
			Login.this.startActivity(intent);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Login.this.moveTaskToBack(true); // 将此activity移到栈底。
			finish(); // 将移到栈底的activity结束掉，此activity栈以上的activity全部都结束了。
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
		// 或者下面这种方式
		// android.os.Process.killProcess(android.os.Process.myPid());
	}
	//让EditText变成可编辑状态  
	private void Unlock() {     

		Username.setFilters(new InputFilter[] { new InputFilter() {     
			@Override    
			public CharSequence filter(CharSequence source, int start,     
					int end, Spanned dest, int dstart, int dend) {     

				return null;  
			}     
		} }); 


	}  
	//让EditText变成不可编辑状态  
	private void lock(){     

		Username.setFilters(new InputFilter[] { new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start,     
					int end, Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				return source.length() < 1 ? dest.subSequence(dstart, dend): "";     
			}     
 
		} });    
	} 

}