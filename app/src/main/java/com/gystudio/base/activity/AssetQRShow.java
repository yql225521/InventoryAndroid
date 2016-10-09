package com.gystudio.base.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.gystudio.ksoap2.serialization.SoapPrimitive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gystudio.base.R;
import com.gystudio.base.activity.assetphoto.PhotoViewerActivity;
import com.gystudio.base.activity.assetphoto.PictureBean;
import com.gystudio.base.entity.AppContext;
import com.gystudio.base.widget.AssetListViewAdapter;
import com.gystudio.db.DataHelper;
import com.gystudio.db.DataManager; 
import com.gystudio.service.SOAPWebServiceTask;
import com.gystudio.utils.Alert;
import com.gystudio.utils.SysConfig;
import com.gystudio.utils.ToolString;
import com.gystudio.utils.TranUtils;
import com.gystudio.widget.GyBaseActivity;
import com.gystudio.zxing.CaptureActivity;
import com.rstco.assetmgr.AssetEntity;


public class AssetQRShow extends  GyBaseActivity<DataHelper> {
	private final static int REQUEST_CODE=1;
	private Button btnScan = null;
	private Button btnEnd = null;
	private AssetListViewAdapter assetListViewAdapter=null;
	private DataManager dataManager=new DataManager();
	private ListView assetListView = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assert_show_main);
		setTitle("资产二维码查询");
		Initialization();
	}

	private void Initialization() {
		assetListView = (ListView) findViewById(R.id.asset_show_list);

		assetListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
		assetListView.setLongClickable(true);
		assetListView.setClickable(true);
		assetListView.setFastScrollEnabled(true);
		assetListView.setSelected(true);
		assetListView.setLongClickable(true);
		assetListViewAdapter=new AssetListViewAdapter(this,new ArrayList<AssetEntity>());  
		assetListView.setAdapter(assetListViewAdapter);
		
		btnEnd = (Button) findViewById(R.id.assset_show_main_btn_end);
		btnEnd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {      
				 finish();
			}
		});
		btnScan = (Button) findViewById(R.id.assset_show_main_btn_scan);
		btnScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {        
						Intent inScaning = new Intent(AssetQRShow.this,
								CaptureActivity.class);
						AssetQRShow.this.startActivityForResult(inScaning, REQUEST_CODE);
 
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 
		if (requestCode==REQUEST_CODE){
			if (resultCode==CaptureActivity.RESULT_CODE){
				Bundle bundle=data.getExtras();
				String barcode=bundle.getString("barcode");
				if (!"".equals(barcode)) {
					String[] codes=SysConfig.getCodes(barcode);
					if(AppContext.offline){
						   AssetEntity asset=new AssetEntity(); 
						   try {
								asset = dataManager.findAssetData(this.getHelper().getAssetsDao(),codes[0]);
							} catch (SQLException e) { 
								e.printStackTrace(); 
							}
						   if(null==asset){
								asset=new AssetEntity();			
								AssetOffLineInventory.parseCodes(codes, asset); 
							} 
						
						this.showInfo(asset);
					}else{
						selectAssetInfo(codes[0]);
					}
				}
			}
		}
	}
	
	private void selectAssetInfo(String assetCode) { 
		String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
		String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
		String methodName = "getAssetInfo"; // 需调用WebService名称

		HashMap<String, String> hashMap = new LinkedHashMap<String, String>();
 		hashMap.put("assetCode", assetCode);
 
		System.out.println("serviceUrl==>"+serviceUrl);

		try {
			new SOAPWebServiceTask(this,"正在加载...") {
				@Override
				public boolean handleMessage(Message msg) { 
					switch (msg.what) {
					case 1:
						onAssetInfoLoadData((SoapPrimitive) msg.obj);
						break;
					case 2:
						Toast.makeText(AssetQRShow.this, "远程服务器无响应！", Toast.LENGTH_LONG).show();
						break;
					}
					return false;
				}
			}.execute(serviceUrl, nameSpace, methodName, hashMap);
		} catch (Exception e) {
			Toast.makeText(AssetQRShow.this, "请求出错！"+e.getMessage(), Toast.LENGTH_LONG).show();
			Alert.DisplayProgressDialogCancel();
		}
	}
	
	public void showInfo(AssetEntity asset){
		StringBuffer sb=new StringBuffer(); 
		
		if(StringUtils.isNotBlank(asset.getFinCode())){
			sb.append("资产编码：").append(asset.getAssetCode()).append("\n");
			sb.append("资产名称：").append(asset.getAssetName()).append("\n");
			sb.append("规格型号：").append(asset.getSpec()).append("\n");
			sb.append("资产类型：").append(asset.getAssetTypeName()).append("\n");
			sb.append("资产类别：").append(asset.getCateName()).append("\n");
			sb.append("财务编码：").append(asset.getFinCode()).append("\n");
			sb.append("管理部门：").append(asset.getMgrOrganName()).append("\n");
			sb.append("使用部门：").append(asset.getOrganName()).append("\n");
			sb.append("存放地点：").append(asset.getStorageDescr()).append("\n");
			sb.append("使  用  人：").append(asset.getOperator()).append("\n");
			sb.append("原　　值：").append(asset.getOriginalValue()).append("\n");
			sb.append("使用日期：").append(asset.getEnableDateString()).append("\n");
			sb.append("使用年限：").append(asset.getUseAge()).append("\n");
			sb.append("当前状态：").append(asset.getStatus());
		}else{
			sb.append("资产编码：").append(asset.getAssetCode()).append("\n");
			sb.append("资产名称：").append(asset.getAssetName()); 
			if(StringUtils.isNotBlank(asset.getSpec())){
				sb.append("\n").append("规格型号：").append(asset.getSpec());
			}
			if(StringUtils.isNotBlank(asset.getOrganName())){
				sb.append("\n").append("使用部门：").append(asset.getOrganName());
			}
			if(StringUtils.isNotBlank(asset.getOperator())){
				sb.append("\n").append("使  用  人：").append(asset.getOperator());
			}
		}  
		Float starnum=asset.getStarNum()==null?null:asset.getStarNum().floatValue();
		assetListViewAdapter.add(asset);
		Alert.DisplayAlertAssetInfoDialog(this, 0, "物品详情",starnum, sb.toString(), "继续", "中止",
				new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent inScaning = new Intent(AssetQRShow.this,
						CaptureActivity.class);
				AssetQRShow.this.startActivityForResult(inScaning, REQUEST_CODE);
			}
		}, null);
	}

	public void onAssetInfoLoadData(SoapPrimitive soapObject) {

		try {
			String json=  TranUtils.decode(soapObject.toString());

			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(json);

			Log.e("JsonData", soapObject.toString());
			Log.e("success", jsonObject.get("success").getAsString());
			int success = jsonObject.get("success").getAsInt();
			String message = jsonObject.get("message").getAsString();

			if (success == 1) {
				//String ustr = jsonObject.get("user")
				Gson gson=new Gson();
				AssetEntity asset=gson.fromJson(jsonObject.get("asset"), AssetEntity.class);
				Log.e("asset ",asset.toString()); 
			
				StringBuffer sb=new StringBuffer();
				sb.append("资产编码：").append(asset.getAssetCode()).append("\n");
				sb.append("资产名称：").append(asset.getAssetName()).append("\n");
				sb.append("资产类型：").append(asset.getAssetTypeName()).append("\n");
				sb.append("资产类别：").append(asset.getCateName()).append("\n");
				sb.append("财务编码：").append(asset.getFinCode()).append("\n");
				sb.append("管理部门：").append(asset.getMgrOrganName()).append("\n");
				sb.append("使用部门：").append(asset.getOrganName()).append("\n");
				sb.append("存放地点：").append(asset.getStorageDescr()).append("\n");
				sb.append("使  用  人：").append(asset.getOperator()).append("\n");
				sb.append("原　　值：").append(asset.getOriginalValue()).append("\n");
				sb.append("使用日期：").append(asset.getEnableDateString()).append("\n");
				sb.append("使用年限：").append(asset.getUseAge()).append("\n");
				sb.append("当前状态：").append(asset.getStatus());
				message=sb.toString();
				assetListViewAdapter.add(asset);
				Float starnum=asset.getStarNum()==null?null:asset.getStarNum().floatValue();
				if (ToolString.isNoBlankAndNoNull(asset.getImgUrls())) { 
					final String imgurls = asset.getImgUrls();
					Alert.DisplayAlertAssetInfoDialog(this, 0, "物品详情",starnum, message,  "查看图片", "关闭", new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							imageBrower(0, imgurls, "");
						}
					}, null);
				} else{
					Alert.DisplayAlertAssetInfoDialog(this, 0, "物品详情",starnum, message, "确定",null,null,null);
				}
			}else{
				Toast.makeText(AssetQRShow.this, "获取信息失败 ！", Toast.LENGTH_LONG).show();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls
	 */
	protected void imageBrower(int position, String urlstr, String filenames) {
		Intent mIntent = new Intent(AssetQRShow.this, PhotoViewerActivity.class);
		ArrayList<PictureBean> dataSource = new ArrayList<PictureBean>();
		String[] urls = urlstr.split(",");
		String[] fnames = filenames.split(",");
		for (String imageURL : urls) {
			PictureBean image = new PictureBean(PictureBean.FILE_TYPE_NETWORK, "",
					SysConfig.getImageBaseUrl() + imageURL);
			dataSource.add(image);
		}
		mIntent.putExtra(PhotoViewerActivity.PICTURE_VIEWER_DEFAULT_POSTION, position);
		mIntent.putExtra(PhotoViewerActivity.PICTURE_VIEWER_DATASOURCE, dataSource);
		AssetQRShow.this.startActivity(mIntent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
