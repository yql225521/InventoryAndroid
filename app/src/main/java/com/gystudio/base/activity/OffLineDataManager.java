package com.gystudio.base.activity;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.gystudio.ksoap2.serialization.SoapPrimitive;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.gystudio.base.R;
import com.gystudio.base.entity.AppContext;
import com.gystudio.base.widget.CheckListViewAdapter;
import com.gystudio.db.DataHelper;
import com.gystudio.db.DataManager;
import com.gystudio.service.SOAPWebServiceTask;
import com.gystudio.utils.Alert;
import com.gystudio.utils.SysConfig;
import com.gystudio.utils.TranUtils;
import com.gystudio.widget.GyBaseActivity;
import com.gystudio.ws.DateSerializerUtils;
import com.rstco.assetmgr.AssetEntity;
import com.rstco.sjpt.entity.PubOrganEntity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 */
public class OffLineDataManager extends  GyBaseActivity<DataHelper> {
	private final static int REQUEST_CODE = 1;

	private CheckListViewAdapter<PubOrganEntity> checkListViewAdapter = null;

	private DataManager dataManager = new DataManager();

	private ListView assetListView = null;
	private Button btnUpload = null;
	private Button btnDel = null;
	private Button btnAllCheck = null;
	private Button btnReverseCheck = null;
	private Button btnCancelCheck = null;
	private Intent in = null;
	private boolean pdstauts = false;
	private boolean upstatus = false;
	private String pdsmsg = "未获取到当前部门盘点状态";
	private String uploadMsg = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_inventory_manager);
		setTitle("离线数据管理");
		Initialization();

	}

	private void Initialization() {
		in = getIntent();
		assetListView = (ListView) findViewById(R.id.data_manager_list);
		assetListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
		assetListView.setLongClickable(true);
		assetListView.setClickable(true);
		assetListView.setFastScrollEnabled(true);
		assetListView.setSelected(true);
		assetListView.setLongClickable(true);
		List<PubOrganEntity> orglst = null;
		try{
			orglst = dataManager.findOrgansWithAsset(this.getHelper().getOrgDao(),this.getHelper().getAssetsDao(),AppContext.currUser.getAccounts());
			//findOrgans(this.getHelper().getOrgDao(), AppContext.currUser.getAccounts(), null);
		}catch(Exception ex){
			Toast.makeText(OffLineDataManager.this, "查询数据出错！"+ex.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		if(orglst == null){
			orglst = new ArrayList<PubOrganEntity>();
		}
		
		final DecimalFormat df2 = new DecimalFormat("####");
		checkListViewAdapter = new CheckListViewAdapter<PubOrganEntity>(
				this,orglst,"organID", new CheckListViewAdapter.CallBack<PubOrganEntity>(){

					@Override
					public String onTitleRender(int position, View titleView,
							PubOrganEntity item) {
						// TODO Auto-generated method stub
						return item.getOrganName();
					}

					@Override
					public String onInfoRender(int position, View infoView,
							PubOrganEntity item) {
						// TODO Auto-generated method stub
						return "有离线盘点数据"+df2.format(item.getNum())+"条";
					}

					@Override
					public String onClick(int position, View infoView, PubOrganEntity item) {
						
						// TODO Auto-generated method stub
						return null;
					}
					
				});  
		assetListView.setAdapter(checkListViewAdapter);

		btnUpload = (Button) findViewById(R.id.offline_manager_btn_upload);

		btnUpload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				if(upstatus){
					Alert.DisplayAlertQqDialog(OffLineDataManager.this, 1, "信息", "正上传数据！不要重复点击", "确定");
				}else  if(AppContext.offline){
					Alert.DisplayAlertQqDialog(OffLineDataManager.this, 1, "信息", "离线登录不能上传数据！", "确定");
				}else if(!pdstauts){
					Alert.DisplayAlertQqDialog(OffLineDataManager.this, 1, "信息", pdsmsg+",不能上传数据", "确定");
				}else {
					final List<PubOrganEntity> orgs=checkListViewAdapter.getCheckItem();
					if(!orgs.isEmpty()){
						upstatus = true;
						uploadMsg = "";
						uploadData(orgs);
					}else{
						Alert.DisplayAlertQqDialog(OffLineDataManager.this, 1, "信息", "请选择要上传数据的部门", "确定");
					}
				}
			}
		});

		btnDel = (Button) findViewById(R.id.offline_manager_btn_delete);
		btnDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {     
				//  删除选定数据
				final List<PubOrganEntity> orgs=checkListViewAdapter.getCheckItem();
				if(orgs.isEmpty()){
					Alert.DisplayAlertQqDialog(OffLineDataManager.this, 1, "信息", "请选择要删除数据的部门", "确定");
				}else{
					Alert.DisplayAlertQqDialog(OffLineDataManager.this, 0, "信息", "确认要删除这些部门的离线数据吗?", "删除", "取消",
							new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							try {
								String testmsg="";
								for (PubOrganEntity pubOrganEntity : orgs) {
									//testmsg+="\n"+pubOrganEntity.getOrganName();
									dataManager.deleteAssetsWithOrgan(OffLineDataManager.this.getHelper().getAssetsDao(), AppContext.currUser.getAccounts(), pubOrganEntity.getOrganCode());
								}
								//Alert.DisplayAlertDialog(OffLineDataManager.this, "测试信息", testmsg);

								List<PubOrganEntity> orglst=dataManager.findOrgansWithAsset(OffLineDataManager.this.getHelper().getOrgDao(),OffLineDataManager.this.getHelper().getAssetsDao(),AppContext.currUser.getAccounts());
								checkListViewAdapter.clear();
								checkListViewAdapter.addAll(orglst);
								checkListViewAdapter.notifyDataSetChanged();
							} catch (SQLException ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
								Toast.makeText(OffLineDataManager.this, "删除数据出错！"+ex.getMessage(), Toast.LENGTH_LONG).show();
							}

						}
					}, null);
				}

			}
		});

		btnAllCheck = (Button) findViewById(R.id.btn_allcheck);
		btnAllCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {     
				checkListViewAdapter.checkAll();
				checkListViewAdapter.notifyDataSetChanged();
			}
		});

		btnReverseCheck = (Button) findViewById(R.id.btn_reversecheck);
		btnReverseCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {     
				checkListViewAdapter.reverseCheck();
				checkListViewAdapter.notifyDataSetChanged();
			}
		});

		btnCancelCheck = (Button) findViewById(R.id.btn_cancelcheck);
		btnCancelCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {     
				checkListViewAdapter.cancelCheck();
				checkListViewAdapter.notifyDataSetChanged();
			}
		});
		 if(!AppContext.offline){
			 isInventory();
		 }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		super.onActivityResult(requestCode, resultCode, data);
	}

	public String getUploadData(PubOrganEntity org) throws SQLException {
		String json="";
		GsonBuilder gsonBuilder = new GsonBuilder();
		
		gsonBuilder.registerTypeAdapter(java.util.Date.class, 
				new DateSerializerUtils()).setDateFormat(DateFormat.LONG);
		
		gsonBuilder.registerTypeAdapter(Double.class,
				new JsonDeserializer<Double>(){
					public Double deserialize(JsonElement json, Type typeOfT,
							JsonDeserializationContext context)
							throws JsonParseException {
						try {
							return Double.valueOf(json.getAsString());
						} catch (Exception e) {
							return null;
						}
					}
				});
		Gson g=gsonBuilder.create();
		//步骤        查 有否upid 有（查询服务端 upid 是否成功   成功 删除 按 upid 删除数据,  不成功   转a ）, 无 upid 转 a
		// a 生成更新upid ->  生成 json -> 上传 --> 结果  -> 按upid 删除数据 
		List<AssetEntity> datlst=dataManager.findPdAssets(this.getHelper().getAssetsDao(), AppContext.currUser.getAccounts(), org.getOrganCode());
		json=g.toJson(datlst);
		return json;

	}
	
	
	private void isInventory() { 
		String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
		String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
		String methodName = "isInventory"; // 需调用WebService名称

		HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

		hashMap.put("organCode",AppContext.currOrgan.getOrganCode() );
  
		System.out.println("serviceUrl==>"+serviceUrl);
		try {
			new SOAPWebServiceTask(this, "正在获取当部门盘点状态...") {
				@Override
				public boolean handleMessage(Message msg) { 
					switch (msg.what) {
					case 1:
						onIsInventoryData((SoapPrimitive) msg.obj);
						break;
					case 2:
						Toast.makeText(OffLineDataManager.this, "获取盘点状态无响应！", Toast.LENGTH_LONG).show(); 
						break;
					}
					return false;
				}
			}.execute(serviceUrl, nameSpace, methodName, hashMap);
		} catch (Exception e) {
			Toast.makeText(OffLineDataManager.this, "请求出错！"+e.getMessage(), Toast.LENGTH_LONG).show();
			Alert.DisplayProgressDialogCancel();
		}
	}
	
	public void onIsInventoryData(SoapPrimitive soapObject) {

		try {
			String json=  TranUtils.decode(soapObject.toString());

			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(json);

			Log.e("JsonData", soapObject.toString());
			Log.e("success", jsonObject.get("success").getAsString());
			int success = jsonObject.get("success").getAsInt();
			pdsmsg = jsonObject.get("message").getAsString();

			if (success == 1) {
				this.pdstauts=true;
  			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	
	private void uploadData(List<PubOrganEntity> orgs){ 
		try {
			isUploadSuccess(orgs.iterator());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private void isUploadSuccess(final Iterator<PubOrganEntity> iter) throws Exception{
		
		String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
		String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
		String methodName = "getUpidStatus"; // 需调用WebService名称
	
		if(!iter.hasNext()){
			Alert.DisplayAlertQqDialog(OffLineDataManager.this, 1, "信息",uploadMsg, "确定");
			List<PubOrganEntity> orglst=dataManager.findOrgansWithAsset(OffLineDataManager.this.getHelper().getOrgDao(),OffLineDataManager.this.getHelper().getAssetsDao(),AppContext.currUser.getAccounts());
			checkListViewAdapter.clear();
			checkListViewAdapter.addAll(orglst);
			checkListViewAdapter.notifyDataSetChanged();
			upstatus=false;
			return ;
		}  
		final PubOrganEntity org=iter.next();
		final String upid=dataManager.findUpid(getHelper().getAssetsDao(),AppContext.currUser.getAccounts(), org.getOrganCode());
 
		if(StringUtils.isBlank(upid)){
			//无上次遗留数据
			doUploadInventory(iter,org);
		}else{
			//有则请求服务器 返回信息  如果是成功 删除本地upid 数据 
			//不成功 则 全部上传
			try {
				HashMap<String, String> hashMap = new LinkedHashMap<String, String>();
				hashMap.put("organCode",AppContext.currOrgan.getOrganCode() );
				hashMap.put("username", AppContext.currUser.getAccounts());
				hashMap.put("upid",upid);
				new SOAPWebServiceTask(this,"上传"+org.getOrganName()+"部门数据") {
					@Override
					public boolean handleMessage(Message msg) {
						switch (msg.what) {
						case 1:
							if("1".equals(msg.obj)){//成功
								try {
									dataManager.deleteAssetsWithUpid(getHelper().getAssetsDao(), AppContext.currUser.getAccounts(), upid);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									upstatus=false;
									Toast.makeText(OffLineDataManager.this, "删除本地数据出错！"+e.getMessage(), Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
							}
							doUploadInventory(iter,org);
							break;
						case 2: 
							upstatus=false;
							break;
						}
						return false;
					}
				}.execute(serviceUrl, nameSpace, methodName, hashMap);
			} catch (Exception e) {
				Alert.DisplayProgressDialogCancel();
				upstatus=false;
				Toast.makeText(OffLineDataManager.this, "请求出错！"+e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	private void doUploadInventory(final Iterator<PubOrganEntity> iter,final PubOrganEntity org) {
		String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
		String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
		String methodName = "doUpLoadInventory"; // 需调用WebService名称
		
		try {
			final String tupid=this.dataManager.updateUpid(this.getHelper().getAssetsDao(), AppContext.currUser.getAccounts(), org.getOrganCode());
			HashMap<String, String> hashMap = new LinkedHashMap<String, String>();
			
			hashMap.put("organCode",AppContext.currOrgan.getOrganCode() );
			hashMap.put("username", AppContext.currUser.getAccounts());
			System.out.println("  encode start");
			hashMap.put("assetJson", TranUtils.encode(this.getUploadData(org)));
			System.out.println("  encode end");
			System.out.println("serviceUrl==>"+serviceUrl);
			new SOAPWebServiceTask(this,null) {
				@Override
				public boolean handleMessage(Message msg) { 
					switch (msg.what) {
					case 1:
						//成功后  删除upid; 
						JsonObject jsonObject = TranUtils.getJsonObject((SoapPrimitive)msg.obj);
						String rmsg= jsonObject.get("message").getAsString();
						String success= jsonObject.get("success").getAsString();
						uploadMsg+=(null!=rmsg?rmsg:"")+"\n";
						try {
							if("1".equals(success)){
								dataManager.deleteAssetsWithUpid(getHelper().getAssetsDao(), AppContext.currUser.getAccounts(), tupid);
							}
							//开始下一个
							isUploadSuccess(iter);
						} catch (Exception e) {
							//
							e.printStackTrace();
							upstatus=false;
							Toast.makeText(OffLineDataManager.this, "删除本地数据出错！"+e.getMessage(), Toast.LENGTH_LONG).show();
						}

						break;
					case 2: 
						upstatus=false;
						Toast.makeText(OffLineDataManager.this, "远程服务器无响应！", Toast.LENGTH_LONG).show();
						break;
					}
					return false;
				}
			}.execute(serviceUrl, nameSpace, methodName, hashMap);
		} catch (Exception e) {
			upstatus=false; 
			Alert.DisplayProgressDialogCancel(); 
			Toast.makeText(OffLineDataManager.this, "上传数据出错！"+e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
 
 
	@Override 
	protected void onDestroy() {  
		super.onDestroy();  
	}  
}
