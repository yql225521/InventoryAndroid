package com.gystudio.base.activity;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import com.gystudio.base.entity.MainFunctionInfo;
import com.gystudio.base.entity.MainFunctionItem;
import com.gystudio.base.entity.MainFunctionType;
import com.gystudio.db.DataHelper;
import com.gystudio.db.DataManager;
import com.gystudio.service.SOAPWebServiceTask;
import com.gystudio.utils.Encrypt;
import com.gystudio.utils.ScreenUtil;
import com.gystudio.utils.SysConfig;
import com.gystudio.utils.TranUtils;
import com.gystudio.widget.GyBaseActivity;
import com.gystudio.ws.DateSerializerUtils;
import com.j256.ormlite.dao.Dao;
import com.rstco.sjpt.entity.CfgEntity;
import com.rstco.sjpt.entity.PubDictEntity;
import com.rstco.sjpt.entity.PubOrganEntity;
import com.rstco.sjpt.model.PubUser;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Logining extends GyBaseActivity<DataHelper> {
	private ImageView LoginingImg = null;
	private TextView LoginingTip = null;
	private Intent in = null;
	private String Username = null;
	private String Password = null;
	private AnimationDrawable animation = null;
	private MainFunctionInfo mainFunction;
	private DataManager dataManager=new DataManager();

	public static final String MAINFUNCTIONINFO = "MainFunctionInfo";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenUtil.HideTitle(this);
		setContentView(R.layout.logining);
		Initialization();

	}

	private void Initialization() {
		LoginingImg = (ImageView) findViewById(R.id.logining_anim_imgview);
		LoginingTip = (TextView) findViewById(R.id.logining_tip_textview);
		animation = (AnimationDrawable) LoginingImg.getBackground();
		in = getIntent();
		Bundle bundle = in.getExtras();
		Username = bundle.get("Username").toString();
		
		String offlineLogin=bundle.getString("offlineLogin");
		if("1".equals(offlineLogin)){
			String off_organCode=bundle.getString("organInfo");
			String[] orgstrs=StringUtils.split(off_organCode, "@");
			String uname=bundle.getString("userName");
			AppContext.offline=true;
			PubUser user = new PubUser();
			user.setAccounts(Username);
			user.setUsername(uname);
			AppContext.currUser=user;
			PubOrganEntity  org = new PubOrganEntity();
			org.setOrganCode(orgstrs[0]);
			org.setOrganName(orgstrs[1]);
			AppContext.currOrgan=org;
			getMainFunction("");
		}else{
			AppContext.offline=false;
			Password = bundle.get("Password").toString();
			selectUserInfo(Username.trim(), Password.trim());
		}
	}

	private void selectUserInfo(String Username, String Password) {
		Encrypt md5 = new Encrypt();
	//	Password =  md5.getMD5ofStr(Password).toUpperCase();
		String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
		String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
		String methodName = "doLogin"; // 需调用WebService名称
		//String areaID = SysConfig.getAreaID();// 组织机构ID
		HashMap<String, String> hashMap = new LinkedHashMap<String, String>();
		//hashMap.put("areaID", areaID);
		hashMap.put("username", Username);
		hashMap.put("password", Password);
		//AppContext.getAddress()
		hashMap.put("addr",AppContext.getAddr());
		hashMap.put("simId",AppContext.getSimId());
		Log.i("", Username+"--"+Password);
		System.out.println("serviceUrl==>"+serviceUrl+"  methodName==>"+methodName);
		LoginingTip.setText("正在连接服务器验证...");
		try {
			new SOAPWebServiceTask(this,null) {
				@Override
				public boolean handleMessage(Message msg) {
					switch (msg.what) {
					case 1:
						onLoadData((SoapPrimitive) msg.obj);
						break;
					case 2:
						in.putExtra("ErrMessage", "远程服务器无响应！");
						Logining.this.setResult(RESULT_OK, in);
						Logining.this.finish();
						break;
					}
					return false;
				}
			}.execute(serviceUrl, nameSpace, methodName, hashMap);
		} catch (Exception e) {
			in.putExtra("ErrMessage", "出错了！" + e.getMessage());
			Logining.this.setResult(RESULT_OK, in);
			Logining.this.finish();
		}
	}

	public void onLoadData(SoapPrimitive soapObject) {
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
				PubUser user=gson.fromJson(jsonObject.get("user"), PubUser.class);
				PubOrganEntity org=gson.fromJson(jsonObject.get("org"), PubOrganEntity.class);
				Log.e("userinfo ",user.toString());           
				Dao<CfgEntity,String> cfgDao = this.getHelper().getCfgDao();
				this.selectBaseData(org.getOrganCode());
				AppContext.currUser=user;
				AppContext.currOrgan=org;
			
			} else {
				in.putExtra("ErrMessage", message);
				this.setResult(RESULT_OK, in);
				this.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
			in.putExtra("ErrMessage", "网络出现异常!");
			Logining.this.setResult(RESULT_OK, in);
			Logining.this.finish();
		} finally {
		}
	}

  
	private void selectBaseData(String organcode) {
		LoginingTip.setText("正在加载基础数据...");
		String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
		String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
		String methodName = "getAssetInventoryBase"; // 需调用WebService名称

		HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

		hashMap.put("organCode", organcode);
 
		System.out.println("serviceUrl==>"+serviceUrl+"  methodName==>"+methodName);

		try {
			new SOAPWebServiceTask(this,null) {
				@Override
				public boolean handleMessage(Message msg) {
					switch (msg.what) {
					case 1:
						onBaseDataLoadData((SoapPrimitive) msg.obj);
						break;
					case 2:
						in.putExtra("ErrMessage", "远程服务器无响应！");
						Logining.this.setResult(RESULT_OK, in);
						Logining.this.finish();
						break;
					}
					return false;
				}
			}.execute(serviceUrl, nameSpace, methodName, hashMap);
		} catch (Exception e) {
			in.putExtra("ErrMessage", "出错了！" + e.getMessage());
			Logining.this.setResult(RESULT_OK, in);
			Logining.this.finish();
		}
	}

	public void onBaseDataLoadData(SoapPrimitive soapObject) {
		try {
			String json=  TranUtils.decode(soapObject.toString());

			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(json);

			Log.e("JsonData", soapObject.toString());
			Log.e("success", jsonObject.get("success").getAsString());
			int success = jsonObject.get("success").getAsInt();
			String message = jsonObject.get("message").getAsString();
			String orglistString = jsonObject.get("organList").getAsJsonArray().toString();
			String dictlistString = jsonObject.get("typeList").getAsJsonArray().toString();
			String matchlistString = jsonObject.get("matchTypeList").getAsJsonArray().toString();
			String completelistString = jsonObject.get("completeTypeList").getAsJsonArray().toString();
			String starsTypesString = jsonObject.get("starsTypes").getAsJsonArray().toString();
			
			
			if (success == 1) {
 
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
				Type orgType = new com.google.gson.reflect.TypeToken<List<PubOrganEntity>>() {}.getType();
				Type dictType = new com.google.gson.reflect.TypeToken<List<PubDictEntity>>() {}.getType();
				List<PubOrganEntity> orglst=g.fromJson(orglistString, orgType);
				AppContext.currOrgan.setOrganType("1");
				orglst.add(AppContext.currOrgan);
				dataManager.saveOrganList(this.getHelper().getOrgDao(), AppContext.currUser.getAccounts(), orglst);
				
				List<PubDictEntity> dictlst=g.fromJson(dictlistString, dictType);
				List<PubDictEntity> matchlst=g.fromJson(matchlistString, dictType);
				dictlst.addAll(matchlst);
				List<PubDictEntity> completelst=g.fromJson(completelistString, dictType);
				dictlst.addAll(completelst);
				List<PubDictEntity> startypes=g.fromJson(starsTypesString, dictType);
				dictlst.addAll(startypes);
				dataManager.saveDictList(this.getHelper().getDictDao(), AppContext.currUser.getAccounts(), dictlst);
				dataManager.saveCfg(this.getHelper().getCfgDao(), AppContext.currUser.getAccounts(), "offline_flag", "1");
				dataManager.saveCfg(this.getHelper().getCfgDao(), AppContext.currUser.getAccounts(), "userName", AppContext.currUser.getUsername());
				dataManager.saveCfg(this.getHelper().getCfgDao(), AppContext.currUser.getAccounts(), "organInfo",AppContext.currOrgan.getOrganCode()+"@"+AppContext.currOrgan.getOrganName());
				getMainFunction("");// 下载主功能页面信息
			} 
		} catch (Exception e) {
			e.printStackTrace();
			in.putExtra("ErrMessage", "网络出现异常!");
			Logining.this.setResult(RESULT_OK, in);
			Logining.this.finish();
		} finally {
		}
	}
 
	
	
	/**
	 * 通过下载和解析获得主页面功能信息
	 * 
	 * @param EntName
	 * @param RegNo
	 * @param LeRep
	 * @param LocalAdm
	 */
	private void getMainFunction(String userName) {
		buildMainFunctionData(null);

		//		String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址
		//		String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
		//		String methodName = "GetFunction"; // 需调用WebService名称
		//		String areaID = SysConfig.getAreaID();//区域代码
		//		HashMap<String, String> hashMap = new HashMap<String, String>();
		//		hashMap.put("areaID", areaID);
		//		hashMap.put("UserName", userName);
		//		final String message = "网络出现异常!";
		//		try {
		//			new SOAPWebService() {
		//				@Override
		//				public boolean handleMessage(Message msg) {
		//					switch (msg.what) {
		//					case 1:
		//						buildMainFunctionData((SoapPrimitive) msg.obj);
		//						break;
		//					case 2:
		//						in.putExtra("ErrMessage", message);
		//						Logining.this.setResult(RESULT_OK, in);
		//						Logining.this.finish();
		//						break;
		//					}
		//					return false;
		//				}
		//			}.execute(serviceUrl, nameSpace, methodName, hashMap);
		//		} catch (Exception e) {
		//			in.putExtra("ErrMessage", message);
		//			Logining.this.setResult(RESULT_OK, in);
		//			Logining.this.finish();
		//		}
	}

	/**
	 * 解析 MainFunction Data
	 * 
	 * @param soapObject
	 */
	public void buildMainFunctionData(SoapPrimitive soapObject) {
		try {
			mainFunction = new MainFunctionInfo();// 主功能信息对象
			ArrayList<MainFunctionType> types = new ArrayList<MainFunctionType>();
			mainFunction.setSuccess(1);
			mainFunction.setRecordcount(1);
			mainFunction.setTitle("资产系统手机端");
			mainFunction.setMessage("");
			String verbz = SysConfig.getVerBz();
			 MainFunctionType functionType = new MainFunctionType();
			 ArrayList<MainFunctionItem> items = new ArrayList<MainFunctionItem>();
			 MainFunctionItem item = null;
			 
			 if(AppContext.offline){
				 item = new MainFunctionItem();
				 item.setIcon("7f020009");
				 item.setTitle("资产查询");
				 items.add(item);
				 item = new MainFunctionItem();
				 item.setIcon("7f020098");
				 item.setTitle("离线盘点");
				 items.add(item);
				 item = new MainFunctionItem();
				 item.setIcon("7f020029");
				 item.setTitle("离线扫描");
				 items.add(item);
				 item = new MainFunctionItem();
				 item.setIcon("7f020012");
				 item.setTitle("离线盘点管理");
				 items.add(item);
				 if(!"ty".equals(verbz)){
					 item = new MainFunctionItem();
					 item.setIcon("7f0200b8");
					 item.setTitle("离线数据管理");
					 items.add(item);
				 }
			 }else{

				 item = new MainFunctionItem();
				 item.setIcon("7f020009");
				 item.setTitle("资产查询");
				 items.add(item);
				 item = new MainFunctionItem();
				 item.setIcon("7f020059");
				 item.setTitle("资产盘点");
				 items.add(item);
				 item = new MainFunctionItem();
				 item.setIcon("7f020018");
				 item.setTitle("盘点情况");
				 items.add(item);
				 item = new MainFunctionItem();
				 item.setIcon("7f020029");
				 item.setTitle("资产扫描");
				 items.add(item);
				
				 item = new MainFunctionItem();
				 item.setIcon("7f020012");
				 item.setTitle("离线盘点管理");
				 items.add(item);
				 if(!"ty".equals(verbz)){
					 item = new MainFunctionItem();
					 item.setIcon("7f0200b8");
					 item.setTitle("离线数据管理");
					 items.add(item);
				 }
			 }
			 item = new MainFunctionItem();
			 item.setIcon("7f020000");
			 item.setTitle("拍照上传");
			 items.add(item);
			 item = new MainFunctionItem();;
			 item.setIcon("7f020008");
			 item.setTitle("帮助");
			 items.add(item);
			 functionType.setItems(items);
			 functionType.setRecordcount(1);
			 functionType.setTitle("001");
			 
			 types.add(functionType);
 
			mainFunction.setFunctionType(types);
			//Intent intentService = new Intent(this,UpdateMessageService.class);
			//this.startService(intentService);//启动服务
			Intent inMainPage = new Intent(this, Main.class);
			inMainPage.putExtra(MAINFUNCTIONINFO, mainFunction);
			this.startActivity(inMainPage);
			this.finish();

		} catch (Exception e) {
			e.printStackTrace();
			in.putExtra("ErrMessage", "网络出现异常!");
			Logining.this.setResult(RESULT_OK, in);
			Logining.this.finish();
		}
	}

	/**
	 * 响应对话框“确定”按钮监听器
	 */
	OnClickListener oKClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			selectUserInfo(Username.trim(), Password.trim());
		}
	};

	/**
	 * 响应对话框“取消”按钮监听器
	 */
	OnClickListener cancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		(new Timer(false)).schedule(new AnimationTimer(animation), 100);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		animation.stop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private static class AnimationTimer extends TimerTask {
		AnimationDrawable animation;

		public AnimationTimer(AnimationDrawable animation) {
			this.animation = animation;
		}

		@Override
		public void run() {
			animation.start();
			this.cancel();
		}
	}
}
