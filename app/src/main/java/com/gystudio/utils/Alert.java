package com.gystudio.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

public class Alert {
	static ProgressDialog progressDialog = null;

	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param Msg
	 */
	public static void DisplayToast(Context context, String Msg) {
		Toast.makeText(context, Msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param Msg
	 * @param Timer
	 */
	public static void DisplayToast(Context context, String Msg, int Timer) {
		Toast.makeText(context, Msg, Timer).show();
	}

	/**
	 * 显示进度窗口
	 * 
	 * @param context
	 * @param progressDialog
	 * @param Title
	 * @param Msg
	 */
	public static void DisplayProgressDialog(Context context, String Title,
			String Msg) {
		DisplayProgressDialog(context, Title, Msg, null, false);
	}

	/**
	 * 显示进度窗口
	 * 
	 * @param context
	 * @param progressDialog
	 * @param Title
	 * @param Msg
	 * @param IsShowCancel
	 */
	public static void DisplayProgressDialog(Context context, String Title,
			String Msg, Boolean IsShowCancel) {
		DisplayProgressDialog(context, Title, Msg, null, IsShowCancel);
	}

	/**
	 * 显示进度窗口
	 * 
	 * @param context
	 * @param progressDialog
	 * @param Title
	 * @param Msg
	 * @param onclick
	 */
	public static void DisplayProgressDialog(Context context, String Title,
			String Msg, DialogInterface.OnClickListener onclick) {
		DisplayProgressDialog(context, Title, Msg, onclick, false);
	}

	/**
	 * 显示进度窗口
	 * 
	 * @param context
	 * @param progressDialog
	 * @param Title
	 * @param Msg
	 * @param onclick
	 * @param IsShowCancel
	 */
	public static void DisplayProgressDialog(Context context, String Title,
			String Msg, DialogInterface.OnClickListener onclick,
			Boolean IsShowCancel) {
		progressDialog = new MyProgressDialog(context);
		progressDialog.setTitle(Title);
		progressDialog.setMessage(Msg);
		progressDialog.setIndeterminate(true);
		if (IsShowCancel) {
			if (onclick != null) {
				progressDialog.setButton("取消", onclick);
			} else {
				progressDialog.setButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int i) {
								progressDialog.cancel();
							}
						});
			}
		}
		progressDialog.show();
	}

	/**
	 * 自定义ProgressDialog，屏蔽back事件
	 * 
	 * @author Administrator
	 * 
	 */
	static class MyProgressDialog extends ProgressDialog {

		public MyProgressDialog(Context context) {
			super(context);
		}

		@Override
		public void onBackPressed() {
			return;
		}

	}

	/**
	 * 取消进度窗口
	 */
	public static void DisplayProgressDialogCancel() {
		progressDialog.dismiss();
	}

	public static void DisplayProgressDialogChgText(String msg) {
		progressDialog.setMessage(msg);
	}

	/**
	 * 显示弹出信息框
	 * 
	 * @param context
	 * @param Title
	 * @param Msg
	 */
	public static void DisplayAlertDialog(Context context, String Title,
			String Msg) {
		DisplayAlertDialog(context, Title, Msg, null);
	}

	/**
	 * 显示弹出信息框
	 * 
	 * @param context
	 * @param Title
	 * @param Msg
	 * @param onclick
	 */
	public static void DisplayAlertDialog(Context context, String Title,
			String Msg, DialogInterface.OnClickListener onclick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(Title);
		builder.setMessage(Msg);
		// builder.setIcon(android.R.drawable.ic_dialog_info);
		if (onclick != null) {
			builder.setPositiveButton(android.R.string.ok, onclick);
		} else {
			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
		}
		builder.show();
	}

	/**
	 * 显示用户确认信息框
	 * 
	 * @param context
	 * @param Title
	 * @param Msg
	 */
	public static void DesplayAlertDialogConfirm(Context context, String Title,
			String Msg, DialogInterface.OnClickListener onclick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(Title);
		builder.setMessage(Msg);
		builder.setPositiveButton(android.R.string.ok, onclick);
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.setCancelable(false);
		builder.create();
		builder.show();
	}

	/**
	 * 显示弹出信息框类似手机QQ
	 * 
	 * @param context
	 *            当前Activity
	 * @param Icon
	 *            Icon样式 0默认,1警示,2错误
	 * @param Title
	 *            标题内容
	 * @param Message
	 *            提示框内容
	 * @param Confirm
	 *            确定按钮文字
	 */
	public static void DisplayAlertQqDialog(Context context, int Icon,
			String Title, String Message, String Confirm) {
		DisplayAlertQqDialog(context, Icon, Title, Message, Confirm, null,
				null, null);
	}

	/**
	 * 显示弹出信息框类似手机QQ
	 * 
	 * @param context
	 *            当前Activity
	 * @param Icon
	 *            Icon样式 0默认,1警示,2错误
	 * @param Title
	 *            标题内容
	 * @param Message
	 *            提示框内容
	 * @param Confirm
	 *            确定按钮文字
	 * @param Cancel
	 *            取消按钮文字
	 */
	public static void DisplayAlertQqDialog(Context context, int Icon,
			String Title, String Message, String Confirm, String Cancel) {
		DisplayAlertQqDialog(context, Icon, Title, Message, Confirm, Cancel,
				null, null);
	}

	/**
	 * 显示弹出信息框类似手机QQ
	 * 
	 * @param context
	 *            当前Activity
	 * @param Icon
	 *            Icon样式 0默认,1警示,2错误
	 * @param Title
	 *            标题内容
	 * @param Message
	 *            提示框内容
	 * @param Confirm
	 *            确定按钮文字
	 * @param Cancel
	 *            取消按钮文字
	 * @param OKClick
	 *            确定事件
	 */
	public static void DisplayAlertQqDialog(Context context, int Icon,
			String Title, String Message, String Confirm, String Cancel,
			View.OnClickListener OKClick) {
		DisplayAlertQqDialog(context, Icon, Title, Message, Confirm, Cancel,
				OKClick, null);
	}

	/**
	 * 显示弹出信息框类似手机QQ
	 * 
	 * @param context
	 *            当前Activity
	 * @param Icon
	 *            Icon样式 0默认,1警示,2错误
	 * @param Title
	 *            标题内容
	 * @param Message
	 *            提示框内容
	 * @param Confirm
	 *            确定按钮文字
	 * @param Cancel
	 *            取消按钮文字
	 * @param OKClick
	 *            确定事件
	 * @param CancelClick
	 *            取消事件
	 */
	public static void DisplayAlertQqDialog(Context context, int Icon,
			String Title, String Message, String Confirm, String Cancel,
			View.OnClickListener OKClick, View.OnClickListener CancelClick) {
		QqDialog dialog = new QqDialog(context, Icon, Title, Message, Confirm,
				Cancel);
		if (OKClick != null) {
			dialog.setOKClickListener(OKClick);
		}
		if (CancelClick != null) {
			dialog.setCancelClickListener(CancelClick);
		}
		dialog.show();
	}
	
	public static void DisplayAlertAssetInfoDialog(Context context, int Icon,
			String Title, Float startnum,String Message, String Confirm, String Cancel,
			View.OnClickListener OKClick, View.OnClickListener CancelClick) {
		QqDialog dialog = new QqDialog(context, Icon, Title,startnum, Message, Confirm,
				Cancel);
		if (OKClick != null) {
			dialog.setOKClickListener(OKClick);
		}
		if (CancelClick != null) {
			dialog.setCancelClickListener(CancelClick);
		}
		dialog.show();
	}
	
	public static void DisplayAlertQqDialog(Context context, int Icon,
			String Title, String Message, String Confirm, String Cancel,
			View.OnClickListener OKClick, View.OnClickListener CancelClick,String custstr, View.OnClickListener custClick) {
		QqDialog dialog = new QqDialog(context, Icon, Title, Message, Confirm,
				Cancel);
		if (OKClick != null) {
			dialog.setOKClickListener(OKClick);
		}
		if (CancelClick != null) {
			dialog.setCancelClickListener(CancelClick);
		}
		if (custClick != null) {
			dialog.setCustClickListener(custClick);
		}
		dialog.show();
	}

	public static void DisplayAlertQqDialog(Context context, int Icon,
			String Title, String Message, String Confirm,
			View.OnClickListener OKClick) {
		QqDialog dialog = new QqDialog(context, Icon, Title, Message, Confirm,
				null);
		if (OKClick != null) {
			dialog.setOKClickListener(OKClick);
		}
		dialog.show();
	}

	public static void DisplayAlertInputQqDialog(Context context, int Icon,
			String Title, String Message, String Confirm,
			View.OnClickListener OKClick) {

		QqDialog dialog = new QqDialog(context, Icon, Title, Message, Confirm,
				null);
		if (OKClick != null) {
			dialog.setOKClickListener(OKClick);
		}
		dialog.show();
	}

	/**
	 * 显示应用程序关于信息对话框
	 */
	public static void DisplayAlertAppAboutDialog(Context context) {
		// // 从SharedPreferences中读取"地市名称"
		// PreferencesUtil pu = new PreferencesUtil(context);
		// final MyDaily myDaily = new MyDaily(context);
		// myDaily.setIcon(0);// 设置Icon
		// myDaily.setTitle(R.string.app_name);// 设置title
		// LayoutInflater mInflater = LayoutInflater.from(context);
		// RelativeLayout layout = (RelativeLayout) mInflater.inflate(
		// R.layout.app_about, null);
		// TextView tel = (TextView)
		// layout.findViewById(R.id.app_about_tel_value);
		// TextView url = (TextView)
		// layout.findViewById(R.id.app_about_url_value);
		// TextView version = (TextView) layout
		// .findViewById(R.id.app_about_version);// 版本号
		// TextView organ = (TextView) layout
		// .findViewById(R.id.app_about_organ_value);// 使用单位
		// TextView copyright = (TextView) layout
		// .findViewById(R.id.app_about_copyright);// 版权
		// TextView tecSup = (TextView) layout
		// .findViewById(R.id.app_about_tecSup_value);// 技术支持
		// String copyrightStr = pu.Read(Splash.APPABOUTGROUP,
		// Splash.APPABOUTCOPYRIGHT);
		// if (!"".equals(copyrightStr)) {
		// copyright.append(copyrightStr);
		// } else {
		// copyright.append("山西省工商管理局");
		// }
		// String tecSupStr =
		// StringUtil.ReplaceEnter(pu.Read(Splash.APPABOUTGROUP,
		// Splash.APPABOUTTECSUP));
		// if (!"".equals(tecSupStr)) {
		// tecSup.setText(tecSupStr);
		// } else {
		// tecSup.setText(context.getResources().getString(
		// R.string.app_about_tecSup_value));
		// }
		// String telStr = pu.Read(Splash.APPABOUTGROUP, Splash.APPABOUTTEL);//
		// 获得电话
		// if (!"".equals(telStr)) {
		// StringUtil.setSpanUrlString(telStr, 1, tel);
		// } else {
		// StringUtil.setSpanUrlString("03517027198", 1, tel);
		// }
		// String urlStr = pu.Read(Splash.APPABOUTGROUP, Splash.APPABOUTURL);//
		// 获得网址
		// if (!"".equals(urlStr)) {
		// StringUtil.setSpanUrlString(urlStr, 4, url);
		// } else {
		// StringUtil.setSpanUrlString("http://www.bettem.com", 4, url);
		// }
		// // 获得当前程序的版本号
		// try {
		// version.setText("版本 v"
		// + context.getPackageManager().getPackageInfo(
		// "com.bettem.mobenfolaw", 0).versionName);
		// } catch (NameNotFoundException e) {
		// e.printStackTrace();
		// }
		// String localAdm = pu.Read(AppSettings.APPSETTINGGROUPNAME,
		// AppSettings.USER_ORGAN_NAME);
		// if("".equals(localAdm)){
		// localAdm = "未设置";
		// }
		// organ.setText(localAdm);
		// myDaily.setView(layout);// 设置内容view
		// myDaily.setOkBtn(new OnClickListener() {// 设置确定按钮
		//
		// @Override
		// public void onClick(View arg0) {
		// myDaily.dismiss();
		// }
		// });
		// myDaily.show();
	}
}
