package com.gystudio.base.activity.assetphoto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.gystudio.ksoap2.serialization.SoapPrimitive;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gystudio.base.R;
import com.gystudio.base.activity.AssetManual;
import com.gystudio.base.activity.AssetOffLineInventory;
import com.gystudio.base.activity.OffLineDataManager;
import com.gystudio.base.entity.AppContext;
import com.gystudio.base.entity.UploadBean;
import com.gystudio.base.widget.CheckListViewAdapter;
import com.gystudio.db.DataHelper;
import com.gystudio.db.DataManager;
import com.gystudio.service.SOAPWebServiceTask;
import com.gystudio.utils.Alert;
import com.gystudio.utils.ContextUtil;
import com.gystudio.utils.SysConfig;
import com.gystudio.utils.TranUtils;
import com.gystudio.utils.UploadTask; 
import com.gystudio.widget.GyBaseActivity;
import com.gystudio.zxing.CaptureActivity;
import com.rstco.assetmgr.AssetEntity;
import com.rstco.sjpt.entity.PhotoEntity;
import com.rstco.sjpt.entity.PubOrganEntity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class AssetPhotoUploadActivity extends  GyBaseActivity<DataHelper> {
	private final static int REQUEST_CODE=6; 
	private Button btnScan = null;
	private Button btnEnd = null;
	private Button btnUpload = null;
	private Button btnAllCheck = null;
	private Button btnReverseCheck = null;
	private Button btnDel = null;
	private boolean pdstauts = false;
	private boolean upstatus = false;
	private String pdsmsg = "未获取到当前部门盘点状态";
	private String uploadMsg = "";
	private CheckListViewAdapter<PhotoEntity> assetListViewAdapter=null;
	private DataManager dataManager=new DataManager();
	private ListView assetListView = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assert_photo_main);
		setTitle("资产拍照上传");
		Initialization();
	}

	private void Initialization() {
		assetListView = (ListView) findViewById(R.id.scan_photo_list);

		assetListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
		assetListView.setLongClickable(true);
		assetListView.setClickable(true);
		assetListView.setFastScrollEnabled(true);
		assetListView.setSelected(true);
		assetListView.setLongClickable(true);
		
		List<PhotoEntity> photolst = null;
		try{
			photolst = dataManager.findPhotos(this.getHelper().getPhotoDao(),AppContext.currUser.getAccounts(),"01");
			//findOrgans(this.getHelper().getOrgDao(), AppContext.currUser.getAccounts(), null);
		}catch(Exception ex){
			Toast.makeText(AssetPhotoUploadActivity.this, "查询数据出错！"+ex.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		if(photolst == null){
			photolst = new ArrayList<PhotoEntity>();
		}
		assetListViewAdapter=new CheckListViewAdapter<PhotoEntity>(
				this,photolst,"id", new CheckListViewAdapter.CallBack<PhotoEntity>(){ 
					@Override
					public String onTitleRender(int position, View titleView,
							PhotoEntity item) {
						// TODO Auto-generated method stub
						return item.getAssetName();
					}

					@Override
					public String onInfoRender(int position, View infoView,
							PhotoEntity item) {
						StringBuffer sb=new StringBuffer(); 
						sb.append("资产编码：").append(item.getAssetCode()).append("\n");
						sb.append("资产名称：").append(item.getAssetName()).append("\n");  
						sb.append("使用部门：").append(item.getOrganName()).append("\n");
						return sb.toString();
					}

					@Override
					public String onClick(int position, View infoView, PhotoEntity item) {
						
						// TODO Auto-generated method stub
						//查看 删除图片 
						return null;
					}
					
				});  
		assetListView.setAdapter(assetListViewAdapter);
		
		btnEnd = (Button) findViewById(R.id.scan_back_btn);
		btnEnd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {      
				 finish();
			}
		});
		btnUpload = (Button) findViewById(R.id.scan_upload_btn);
		btnUpload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {      
				 //上传
			}
		});
		btnScan = (Button) findViewById(R.id.scan_photo_btn);
		btnScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 

				//selectAssetInfo("1114050101010000016660");
						Intent inScaning = new Intent(AssetPhotoUploadActivity.this,
								CaptureActivity.class);
						AssetPhotoUploadActivity.this.startActivityForResult(inScaning, REQUEST_CODE);
 
			}

		});
		btnAllCheck = (Button) findViewById(R.id.btn_allcheck);
		btnAllCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {     
				assetListViewAdapter.checkAll();
				assetListViewAdapter.notifyDataSetChanged();
			}
		});

		btnReverseCheck = (Button) findViewById(R.id.btn_reversecheck);
		btnReverseCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {     
				assetListViewAdapter.reverseCheck();
				assetListViewAdapter.notifyDataSetChanged();
			}
		});
		
		btnUpload = (Button) findViewById(R.id.scan_upload_btn);

		btnUpload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				if(upstatus){
					Alert.DisplayAlertQqDialog(AssetPhotoUploadActivity.this, 1, "信息", "正上传数据！不要重复点击", "确定");
				}else  if(AppContext.offline){
					Alert.DisplayAlertQqDialog(AssetPhotoUploadActivity.this, 1, "信息", "离线登录不能上传数据！", "确定");
				}else {
					final List<PhotoEntity> orgs=assetListViewAdapter.getCheckItem();
					if(!orgs.isEmpty()){
						upstatus = true;
						uploadMsg = "";
						uploadData(orgs);  
					}else{
						Alert.DisplayAlertQqDialog(AssetPhotoUploadActivity.this, 1, "信息", "请选择要上传的数据", "确定");
					}
				}
			}
		});

		btnDel = (Button) findViewById(R.id.scan_delete_btn);
		btnDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {     
				//  删除选定数据
				final List<PhotoEntity> orgs=assetListViewAdapter.getCheckItem();
				if(orgs.isEmpty()){
					Alert.DisplayAlertQqDialog(AssetPhotoUploadActivity.this, 1, "信息", "请选择要删除的数据", "确定");
				}else{
					Alert.DisplayAlertQqDialog(AssetPhotoUploadActivity.this, 0, "信息", "确认要删除这些数据吗?", "删除", "取消",
							new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							try {
								String testmsg="";
								for (PhotoEntity photo : orgs) {
									//testmsg+="\n"+pubOrganEntity.getOrganName();
									dataManager.deletePhoto(AssetPhotoUploadActivity.this.getHelper().getPhotoDao(),photo.getId());
								}
								//Alert.DisplayAlertDialog(OffLineDataManager.this, "测试信息", testmsg);

								List<PhotoEntity> orglst=dataManager.findPhotos(AssetPhotoUploadActivity.this.getHelper().getPhotoDao(),AppContext.currUser.getAccounts(),"01");
								assetListViewAdapter.clear();
								assetListViewAdapter.addAll(orglst);
								assetListViewAdapter.notifyDataSetChanged();
							} catch (SQLException ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
								Toast.makeText(AssetPhotoUploadActivity.this, "删除数据出错！"+ex.getMessage(), Toast.LENGTH_LONG).show();
							}

						}
					}, null);
				}

			}
		});
	}
	
	public void uploadData(final List<PhotoEntity> photos){    
		List<UploadBean> blist  = this.getUploadBean(photos); 
		try {
			isUploadSuccess(blist.iterator());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}  
	private void isUploadSuccess(final Iterator<UploadBean> iter) throws Exception{
		if(!iter.hasNext()){
			try {
				List<PhotoEntity> plist=dataManager.findPhotos(AssetPhotoUploadActivity.this.getHelper().getPhotoDao(),AppContext.currUser.getAccounts(),"01");
				assetListViewAdapter.clear();
				assetListViewAdapter.addAll(plist);
				assetListViewAdapter.notifyDataSetChanged();
			} catch (SQLException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
				Toast.makeText(AssetPhotoUploadActivity.this, "查询数据出错！"+ex.getMessage(), Toast.LENGTH_LONG).show();
			}  
			//完成 
			upstatus=false;
			return;
		}
		final UploadBean uploadBean=iter.next();
		try {  
			System.out.println("start upload photo" );
			String uploadUrl = SysConfig.getUploadImageUrl();//上传地址
			
			new UploadTask(this,"系统正在上传照片"){
				@Override
				public boolean handleMessage(Message msg) {
					UploadBean oldbean = (UploadBean)msg.obj;
					PhotoEntity pe =(PhotoEntity)oldbean.getObj();
					switch (msg.what) {
					case 1:  
						try {
							dataManager.deletePhoto(AssetPhotoUploadActivity.this.getHelper().getPhotoDao(), pe.getId()); 
							isUploadSuccess(iter);
						} catch (Exception e) {
							e.printStackTrace();
							upstatus=false;
							Toast.makeText(AssetPhotoUploadActivity.this, "删除本地数据出错！"+e.getMessage(), Toast.LENGTH_LONG).show();
						}
						break;
					case 2:
						Toast.makeText(AssetPhotoUploadActivity.this, "上传失败！", Toast.LENGTH_LONG).show(); 
						upstatus=false;
						break;
					}
					return false;
				}
			}.execute( uploadUrl, "uploadImages",uploadBean); 

		} catch (Exception e) {
			upstatus=false;
			e.printStackTrace();
		} 
	} 
	
	public List<UploadBean> getUploadBean(List<PhotoEntity> photos){
		 List<UploadBean> rlist =  new ArrayList<UploadBean>(); 
		 for (PhotoEntity pe : photos) { 
				Map param = new HashMap();
				param.put("note", pe.getDescr()==null?"":pe.getDescr());
				param.put("assetCode", pe.getAssetCode()==null?pe.getMatterID():pe.getAssetCode());  
			    rlist.add(new UploadBean(pe.getPath(),param,pe)); 
		}
		return rlist;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 
		//if (requestCode==REQUEST_CODE){
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
			}else if (resultCode==CamreaPictureActivity.RESULT_CODE){ 
				Bundle bundle=data.getExtras();
				AssetEntity robj = (AssetEntity)bundle.get("asset");
				String path = bundle.getString("path");
				String descr = bundle.getString("descr");
				PhotoEntity photoobj=buildPhoto(robj,descr);
				String pstr=photoobj.getPath()+","+path;
				if(pstr.startsWith(",")){
					pstr=pstr.substring(1);
				}
				photoobj.setPath(pstr); 
				assetListViewAdapter.add(photoobj); 
				 try {
					 dataManager.savePhoto(this.getHelper().getPhotoDao(),photoobj,AppContext.currUser.getAccounts());
				 } catch (SQLException e) { 
						e.printStackTrace(); 
				 }
				 System.out.println("photoobj==>"+photoobj);
			}
		//}
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
						Toast.makeText(AssetPhotoUploadActivity.this, "远程服务器无响应！", Toast.LENGTH_LONG).show();
						break;
					}
					return false;
				}
			}.execute(serviceUrl, nameSpace, methodName, hashMap);
		} catch (Exception e) {
			Toast.makeText(AssetPhotoUploadActivity.this, "请求出错！"+e.getMessage(), Toast.LENGTH_LONG).show();
			Alert.DisplayProgressDialogCancel();
		}
	}
	
	public void showInfo(final AssetEntity asset){
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
			sb.append("当前状态：").append(asset.getStatus()).append("\n");
		}else{
			sb.append("资产编码：").append(asset.getAssetCode()).append("\n");
			sb.append("资产名称：").append(asset.getAssetName()).append("\n"); 
			if(StringUtils.isNotBlank(asset.getSpec())){
				sb.append("规格型号：").append(asset.getSpec()).append("\n");
			}
			if(StringUtils.isNotBlank(asset.getOrganName())){
				sb.append("使用部门：").append(asset.getOrganName()).append("\n");
			}
			if(StringUtils.isNotBlank(asset.getOperator())){
				sb.append("使  用  人：").append(asset.getOperator()).append("\n");
			}
		}   
		
		Alert.DisplayAlertQqDialog(this, 0, "物品详情", sb.toString(), "拍照", "中止",
				new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent photoing = new Intent(AssetPhotoUploadActivity.this,
						CamreaPictureActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("asset", asset);
				//bundle.putSerializable("pdzt", pdzt);
				photoing.putExtras(bundle); 
				AssetPhotoUploadActivity.this.startActivityForResult(photoing, REQUEST_CODE);
			}
		}, null);
	}
	
	public PhotoEntity buildPhoto(AssetEntity asset,String descr){
		PhotoEntity obj=new PhotoEntity();
		obj.setAssetId(asset.getAssetID());
		obj.setAssetCode(asset.getAssetCode());
		obj.setAssetName(asset.getAssetName());
		obj.setOrganName(asset.getOrganName());
		obj.setPath("");
		obj.setDescr(descr);
		obj.setType("01");
		return obj;
		
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
				this.showInfo(asset);
			}else{
				Alert.DisplayAlertQqDialog(AssetPhotoUploadActivity.this, 1, "信息",message, "确定");
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
