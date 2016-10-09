package com.gystudio.utils;
/*
 使用方法
 QqDialog dialog = new QqDialog(Login.this,R.string.strCaption,2,"用户名密码错误！","确定","取消");
 dialog.setOKClickListener(new View.OnClickListener(){
 @Override
 public void onClick(View v) {
 // TODO Auto-generated method stub
 Alert.DisplayToast(Login.this, "请输入用户名！");
 }
 });
 dialog.show();*/
import com.gystudio.base.activity.Login;
import com.gystudio.base.activity.Logining;
import com.gystudio.base.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


public class QqDialog extends Dialog implements View.OnClickListener {
	private Button btnOK;
	private Button btnCust;
	private Button btnCancel;
	private RatingBar starBar=null;
	private TextView starBarLabel = null; 
	private View.OnClickListener btnOKClickListener;
	private View.OnClickListener btnCustClickListener;
	private View.OnClickListener btnCancelClickListener;

	public QqDialog(Context context, int paramInt) {
		super(context, paramInt);
	}

	/**
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
	public QqDialog(Context context, int Icon, String Title, String Message,
			String Confirm, String Cancel) {
		this(context, R.style.MyDialogStyle);
		setContentView(R.layout.qq_dialog);
		this.QqDialogBase(context, Icon, Title, Message, Confirm, Cancel, null);
	}
	
	public QqDialog(Context context, int Icon, String Title,Float starnum, String Message,
			String Confirm, String Cancel) {
		this(context, R.style.MyDialogStyle);
		setContentView(R.layout.asset_info_dialog); 
		if(starnum!=null){
			starBar = (RatingBar)findViewById(R.id.room_ratingbar); 
			starBarLabel = (TextView)findViewById(R.id.star_label); 
			starBar.setVisibility(View.VISIBLE);
			starBarLabel.setVisibility(View.VISIBLE);
			starBar.setRating(starnum);
		}
		this.QqDialogBase(context, Icon, Title, Message, Confirm, Cancel, null);
	}
	public QqDialog(Context context, int Icon, String Title, String Message,
			String Confirm, String Cancel,String custStr) {
		this(context, R.style.MyDialogStyle);
		setContentView(R.layout.qq_dialog);
		this.QqDialogBase(context, Icon, Title, Message, Confirm, Cancel, custStr);
	}
	
	public void QqDialogBase(Context context, int Icon, String Title, String Message,
			String Confirm, String Cancel,String custStr) {
		int windowWidth = ((WindowManager) context.getSystemService("window"))
				.getDefaultDisplay().getWidth();
		// int windowHeight =
		// ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getHeight();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		// ViewGroup localObject2 =
		// (ViewGroup)getLayoutInflater().inflate(R.layout.qq_dialog, null);
		lp.y = 0;
		lp.width = windowWidth - 30;
		getWindow().setAttributes(lp);
		((TextView) this.findViewById(R.id.qq_dialog_msg_id)).setText(Message);
		this.btnOK = (Button) this.findViewById(R.id.qq_dialog_ok_btn_id);
		this.btnOK.setOnClickListener(this);
		this.btnOK.setText(Confirm);
		this.btnCancel = (Button) this
				.findViewById(R.id.qq_dialog_cancel_btn_id);
		if (Cancel != null) {
			this.btnCancel.setText(Cancel);
			this.btnCancel.setOnClickListener(this);
			this.btnCancel.setVisibility(View.VISIBLE);
		}
		this.btnCust = (Button) this
				.findViewById(R.id.qq_dialog_cust_btn_id);
		if (custStr != null) {
			this.btnCust.setText(custStr);
			this.btnCust.setOnClickListener(this);
			this.btnCust.setVisibility(View.VISIBLE);
		}
		setTitle(Title);
		setIcon(Icon);
	}
	

	public void setOKClickListener(View.OnClickListener paramOnClickListener) {
		this.btnOKClickListener = paramOnClickListener;
	}

	public void setCancelClickListener(View.OnClickListener paramOnClickListener) {
		this.btnCancelClickListener = paramOnClickListener;
	}

	@Override
	public void onClick(View paramView) {
		Button localButton1 = this.btnOK;
		if (paramView == localButton1) {
			dismiss();
			if (this.btnOKClickListener != null)
				this.btnOKClickListener.onClick(paramView);
		}
		Button localButton2 = this.btnCancel;
		if (paramView == localButton2) {
			dismiss();
			if (this.btnCancelClickListener != null)
				this.btnCancelClickListener.onClick(paramView);
		}
		Button localButton3 = this.btnCust;
		if (paramView == localButton3) {
			dismiss();
			if (this.btnCustClickListener != null)
				this.btnCustClickListener.onClick(paramView);
		}
	}

	public void setTitle(CharSequence paramCharSequence) {
		TextView title = (TextView) this.findViewById(R.id.qq_dialog_title);
		if (title == null)
			return;
		title.setText(paramCharSequence);
		super.setTitle(null);
	}

	public void setIcon(int paramInt) {
		ImageView icon = (ImageView) findViewById(R.id.qq_dialog_icon);
		if (icon != null) {
			if (paramInt == 0)
				icon.setImageResource(R.drawable.qq_dialog_default_icon);
			if (paramInt == 1)
				icon.setImageResource(R.drawable.qq_dialog_alert_icon);
			if (paramInt == 2)
				icon.setImageResource(R.drawable.qq_dialog_err_icon);
		}
	}

	public void show() {
		try {
			super.show();
		} catch (Throwable localThrowable) {
			Context localContext = getContext();
			Intent localIntent = new Intent(localContext, Login.class);
			localIntent.setFlags(67108864);
			getContext().startActivity(localIntent);
		}
	}

	public void dismiss() {
		super.dismiss();
	}

	public void setCustClickListener(View.OnClickListener btnCustClickListener) {
		this.btnCustClickListener = btnCustClickListener;
	}
}
