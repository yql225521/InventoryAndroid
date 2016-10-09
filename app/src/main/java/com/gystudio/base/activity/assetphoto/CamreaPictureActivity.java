package com.gystudio.base.activity.assetphoto;

import java.io.File;

import com.gystudio.base.R;
import com.gystudio.utils.Alert;
import com.gystudio.utils.ToolDateTime;
import com.gystudio.utils.ToolFile;
import com.gystudio.utils.ToolPhone;
import com.gystudio.utils.ToolPicture;
import com.rstco.assetmgr.AssetEntity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 从相册中选择/拍照裁剪头 
 * 
 */
public class CamreaPictureActivity extends Activity implements View.OnClickListener {
	
	public final static int RESULT_CODE=5;

	/**
	 * 查看器图片默认选中位置
	 */
	final String PICTURE_VIEWER_DEFAULT_POSTION = "defaultPostion";

	/**
	 * 查看器数据源
	 */
	final String PICTURE_VIEWER_DATASOURCE = "pictureViewerDatasource";

	/**
	 * 图片裁剪A
	 */
	final String ACTION_CROP = "com.android.camera.action.CROP";

	/**
	 * [相册选择]选择请求码
	 */
	final int ALBUM_REQUEST_CODE = 128;

	/**
	 * [立即拍照]选择请求码
	 */
	final int CAMERA_REQUEST_CODE = 127;

	/**
	 * [裁剪图片]选择请求码
	 */
	final int CROPER_REQUEST_CODE = 126;



	/**
	 * 临时拍照存储的照片
	 */
	private String SOURCE_IMAGE_FILE = "";

	/**
	 * 图片文件源URI
	 */
	private Uri SOURCE_IMAGE_URI = null;

	/**
	 * 临时拍照存储的照片
	 */
	private String OUTPUT_IMAGE_FILE = "";

	/**
	 * 裁剪后输出文件URI
	 */
	private Uri OUTPUT_IMAGE_URI = null;
	
	private Intent in = null;
	private AssetEntity asset;
	
	/**
	 * 拍照/相册选择按钮
	 */
	private Button mAlbumBtn, mCameraBtn,okBtn,backBtn;

	/**
	 * 图片
	 */
	private ImageView mPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camrea_picture);
		mAlbumBtn = (Button) findViewById(R.id.btn_album);
		mCameraBtn = (Button) findViewById(R.id.btn_camera);
		mAlbumBtn.setOnClickListener(this);
		mCameraBtn.setOnClickListener(this); 
		
		backBtn = (Button) findViewById(R.id.scan_back_btn);
		backBtn.setOnClickListener(this);
		okBtn = (Button) findViewById(R.id.photo_sel_ok);
		okBtn.setOnClickListener(this);
		mPhoto = (ImageView) findViewById(R.id.iv_photo);
		mPhoto.setOnClickListener(this);
		initFilePath();
		Initialization();
	} 
	
	private void Initialization() {
		in = getIntent();
		Bundle bundle = in.getExtras();
		Object obj=bundle.getSerializable("asset");
		//Boolean pdztv=bundle.getBoolean("pdzt");
		if(null==obj){
			Alert.DisplayAlertQqDialog(CamreaPictureActivity.this, 1, "信息", "参数错误", "确定");
			this.finish();
		}
		//pdzt=pdztv==null?false:pdztv;
		asset=(AssetEntity)obj;
		getOutPutImageFile(asset);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_album:
			ToolPhone.toImagePickerActivity(this, ALBUM_REQUEST_CODE, 800, 800, SOURCE_IMAGE_URI,
					OUTPUT_IMAGE_URI);
			break;
		case R.id.btn_camera:
			ToolPhone.toCameraActivity(this, CAMERA_REQUEST_CODE, SOURCE_IMAGE_URI);
			break; 
		case R.id.photo_sel_ok:
			 Intent intent=new Intent();
		     intent.putExtra("path",  OUTPUT_IMAGE_URI.getPath());
		     intent.putExtra("asset", asset);
		     setResult(RESULT_CODE, intent);//RESULT_CODE是一个整型变量
			 finish();
			break; 
		case R.id.scan_back_btn:
			 finish();
			break; 
		default:
			break;
		}
	}

	/**
	 * 选择返回界面
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode)
			return;
		switch (requestCode) {
		// 立即拍照
		case CAMERA_REQUEST_CODE:
			ToolPhone.toCropImageActivity(this, SOURCE_IMAGE_URI, OUTPUT_IMAGE_URI, 800, 800, CROPER_REQUEST_CODE);

			break;
		// 相册选择
		case ALBUM_REQUEST_CODE:
			if (OUTPUT_IMAGE_URI != null) {
				Bitmap bitmap = ToolPicture.decodeUriAsBitmap(this, OUTPUT_IMAGE_URI);
				mPhoto.setImageBitmap(bitmap);
			}
			break;
		// 裁剪照片
		case CROPER_REQUEST_CODE:

			if (OUTPUT_IMAGE_URI != null) {
				Bitmap bitmap = ToolPicture.decodeUriAsBitmap(this, OUTPUT_IMAGE_URI);
				mPhoto.setImageBitmap(bitmap);
			}
			break;

		default:
			break;
		}
	} 

	private Uri getOutPutImageFile(AssetEntity asset){
		
		OUTPUT_IMAGE_FILE = ToolFile.gainSDCardPath() + "/ams/photo/"+asset.getAssetCode()+"/"
				+ ToolDateTime.gainCurrentDate("yyyyMMddhhmmss") + ".jpg";
		
		File outFile = new File(OUTPUT_IMAGE_FILE);  
		if (!outFile.exists()) {
			outFile.getParentFile().mkdirs();
		} 
		OUTPUT_IMAGE_URI = Uri.parse("file://" + OUTPUT_IMAGE_FILE); 
		
		return OUTPUT_IMAGE_URI;
	}
	/**
	 * 初始化文件
	 */
	private void initFilePath() {
		SOURCE_IMAGE_FILE = ToolFile.gainSDCardPath() + "/ams/temp.jpg";

		File srcFile = new File(SOURCE_IMAGE_FILE);
		if (!srcFile.exists()) {
			srcFile.getParentFile().mkdirs();
		}

		SOURCE_IMAGE_URI = Uri.parse("file://" + SOURCE_IMAGE_FILE); 
	}
}
