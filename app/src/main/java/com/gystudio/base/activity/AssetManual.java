package com.gystudio.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.gystudio.base.R;
import com.gystudio.utils.Alert;


public class AssetManual extends Activity {
	public final static int RESULT_CODE=3;
	private EditText assetCodeEdt = null; 
	private EditText assetNameEdt = null; 
 
	private Button btnOK = null;
	private Button btnCancel = null;
 
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asset_manual_main);
		setTitle("手工录入");
		Initialization();
	}

	private void Initialization() {
		assetNameEdt = (EditText)findViewById(R.id.asset_manual_edit_assetname);
		assetCodeEdt = (EditText)findViewById(R.id.asset_manual_edit_assetcode);
		btnOK = (Button) findViewById(R.id.asset_enter_okbtn);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {    
 
				// 把用户名传给下一个activity
				if ("".equals(assetCodeEdt.getText().toString().trim())) {
					Alert.DisplayToast(AssetManual.this, "请输入资产编码！");
					return;
				}
//				if ("".equals(assetNameEdt.getText().toString().trim())) {
//					Alert.DisplayToast(AssetManual.this, "请输入资产名称！");
//					return;
//				}
				 Intent intent=new Intent();
			     intent.putExtra("code",assetCodeEdt.getText().toString().trim());
			     intent.putExtra("name",assetNameEdt.getText().toString().trim());
			     setResult(RESULT_CODE, intent);//RESULT_CODE是一个整型变量
				 finish();
			}
		});
		btnCancel = (Button) findViewById(R.id.asset_enter_cancelbtn);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {    
				 finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
