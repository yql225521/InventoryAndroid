package com.gystudio.base.activity;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.gystudio.db.DataHelper;
import com.gystudio.db.DataManager;
import com.gystudio.service.SOAPWebServiceTask;
import com.gystudio.utils.Alert;
import com.gystudio.utils.SysConfig;
import com.gystudio.utils.TranUtils;
import com.gystudio.widget.GyBaseActivity;
import com.gystudio.ws.DateSerializerUtils;
import com.rstco.assetmgr.AssetEntity;
import com.rstco.sjpt.entity.CfgEntity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 */
public class OffLineQtyDataManager extends  GyBaseActivity<DataHelper> {
	private final static int REQUEST_CODE = 1;
  
	private DataManager dataManager = new DataManager();
 
	private Button btnDowload = null; 
	private TextView organInfo = null; 
	private TextView dateInfo = null; 
	
	private Intent in = null; 
	private boolean dlstatus = false; 
	private String dlMsg = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_data_manager);
		setTitle("离线资产数据管理");
		Initialization();

	}

	private void Initialization() {
		in = getIntent();
 
		final DecimalFormat df2 = new DecimalFormat("####"); 
		organInfo = (TextView)findViewById(R.id.data_organ_info);
		dateInfo = (TextView)findViewById(R.id.data_date_info);
		
		btnDowload = (Button) findViewById(R.id.offline_qty_btn_download);
		
		if(AppContext.offline){
			btnDowload.setVisibility(View.GONE);
		}

		btnDowload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getAssetDatas();
			}
		});
		CfgEntity cfg = null;
		try{
			cfg=dataManager.getCfg(this.getHelper().getCfgDao(), "*", "qty_data_flag");
			
			if(null!=cfg){
				System.out.println(" syninfo==>"+cfg.getVal());
				String[] infos = StringUtils.split(cfg.getVal(), "@");
				organInfo.setText(infos[0]);
				dateInfo.setText(infos[1]);
 			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getAssetDatas() {
		btnDowload.setEnabled(false);
		String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
		String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
		String methodName = "getAssetDatas"; // 需调用WebService名称

		HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

		hashMap.put("mgrOrganCode",AppContext.currOrgan.getOrganCode());
  
		System.out.println("serviceUrl==>"+serviceUrl);
		try {
			new SOAPWebServiceTask(this,"正在下载资产数据...") {

				@Override
				protected Object onCallBack(Object soapObject) {
					if(null!=soapObject){  
						System.out.println("SOAPWebServiceTask onCallBack");
						//this.pdialog.setMessage("正在保存数据..."); 
						//this.publishProgress();
						//Toast.makeText(OffLineQtyDataManager.this, "正在保存数据！", Toast.LENGTH_LONG).show();
						Object obj=onDownloadData((SoapPrimitive) soapObject);
						System.out.println("SOAPWebServiceTask onCallBack end");
						return obj;
					}
					return null;
				};
				public boolean handleMessage(Message msg) {  
					 if(msg.what==2){
						 Toast.makeText(OffLineQtyDataManager.this, "获取资产数据无响应！", Toast.LENGTH_LONG).show(); 
					 }else{
						 Map rmap = (Map)msg.obj;
						 organInfo.setText(""+rmap.get("organInfo"));
						 dateInfo.setText(""+rmap.get("dateInfo")); 
						 String rmsg =""+rmap.get("msg");
						 if(StringUtils.isBlank(rmsg)){
							 rmsg="数据下载成功！";
						 }
						 Toast.makeText(OffLineQtyDataManager.this, rmsg, Toast.LENGTH_LONG).show(); 
						 btnDowload.setEnabled(true);
					 } 
					return false;
				}
			}.execute(serviceUrl, nameSpace, methodName, hashMap);
		} catch (Exception e) {
			Toast.makeText(OffLineQtyDataManager.this, "请求出错！"+e.getMessage(), Toast.LENGTH_LONG).show();
			Alert.DisplayProgressDialogCancel();
		} finally { 
		}
	}
	
	public Object onDownloadData(SoapPrimitive soapObject) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Map rmap = new HashMap();
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
		try {
			String json=  TranUtils.decode(soapObject.toString());

			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(json);

			Log.e("JsonData", soapObject.toString());
			Log.e("success", jsonObject.get("success").getAsString());
			int success = jsonObject.get("success").getAsInt();
			String msg = jsonObject.get("message").getAsString();
			rmap.put("msg", msg);
			String syn_info = jsonObject.get("synInfo").getAsString(); 
			if (success == 1) {
				this.dataManager.deleteQtyAssets(this.getHelper().getAssetsDao());
				Type type = new com.google.gson.reflect.TypeToken<List<AssetEntity>>() {}.getType();
				String listString=jsonObject.get("list").getAsJsonArray().toString();
				List<AssetEntity> assetlst=g.fromJson(listString, type);  
				this.dataManager.saveQueryAsset(this.getHelper().getAssetsDao(), assetlst, AppContext.currUser.getAccounts());
				dataManager.saveCfg(this.getHelper().getCfgDao(), "*", "qty_data_flag",syn_info); 
				if(StringUtils.isNotBlank(syn_info)){
					String[] infos = StringUtils.split(syn_info, "@");
					rmap.put("organInfo", infos[0]);
					rmap.put("dateInfo", infos[1]);
				}
				rmap.put("success", 1);   
			}else{
				rmap.put("success", 0);  
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return rmap;
		}
		
	} 

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		super.onActivityResult(requestCode, resultCode, data);
	}
 
	@Override 
	protected void onDestroy() {  
		super.onDestroy();  
	}  
}
