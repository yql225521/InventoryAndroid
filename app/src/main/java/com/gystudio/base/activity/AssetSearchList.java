package com.gystudio.base.activity;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.gystudio.base.R;
import com.gystudio.base.widget.AssetListViewAdapter;
import com.gystudio.db.DataHelper;
import com.gystudio.db.DataManager;
import com.gystudio.db.Page;
import com.gystudio.widget.BaseListActivity;
import com.gystudio.ws.DateSerializerUtils;
import com.rstco.assetmgr.AssetEntity;

public class AssetSearchList extends BaseListActivity<AssetEntity,DataHelper> {
	
	private AssetListViewAdapter assetListViewAdapter=null;
	private DataManager dataManager=new DataManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
 		requestWindowFeature(Window.FEATURE_PROGRESS);
 		this.setMethodName("searchAssetList");
 		this.Initialization();
		//setTitle("");
	}

	/* (non-Javadoc)
	 * @see com.gystudio.widget.BaseListActivity#Initialization()
	 */
	@Override
	protected void Initialization() {
		setContentView(R.layout.asset_search_list);
		assetListViewAdapter=new AssetListViewAdapter(this,new ArrayList<AssetEntity>());
		assetListViewAdapter.setQtybz(true);
 		super.Initialization();
		this.searchListData(this.getMethodName());
	}
	
	/* (non-Javadoc)
	 * @see com.gystudio.widget.BaseListActivity#getParamMap()
	 */
	@Override
	protected Map getParamMap() {
		Intent in = getIntent();
 
		Bundle bundle = in.getExtras();
		Map hashMap=new HashMap();
		hashMap.put("organCode",  bundle.getString("organCode").toString());
		hashMap.put("assetCode",  bundle.getString("assetCode").toString());
		hashMap.put("assetName",  bundle.getString("assetName").toString());
		hashMap.put("category",  bundle.getString("category").toString());
		hashMap.put("starNum1",  bundle.getString("starNum1").toString());
		hashMap.put("starNum2",  bundle.getString("starNum2").toString());
		return hashMap;
	}
	
	/* (non-Javadoc)
	 * @see com.gystudio.widget.BaseListActivity#buildData(com.google.gson.JsonObject)
	 */
	@Override
	protected void buildData(JsonObject jsonObject) {
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
		Type type = new com.google.gson.reflect.TypeToken<List<AssetEntity>>() {}.getType();
		String listString=jsonObject.get("list").getAsJsonArray().toString();
		List<AssetEntity> assetlst=g.fromJson(listString, type);
		this.assetListViewAdapter.addAll(assetlst);
	}
 

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see com.gystudio.widget.BaseListActivity#getListViewAdapter()
	 */
	@Override
	protected BaseAdapter getListViewAdapter() {
		// TODO Auto-generated method stub
		return this.assetListViewAdapter;
	}

	@Override
	protected Page findLocalData(Map fmap,int pageNo,int pageSize) {
 
		Page<AssetEntity> fpage = new Page();
		try{
			
			fpage.setPageNo(pageNo);
			fpage.setPageSize(pageSize);
			fpage =dataManager.findAssetPage(this.getHelper().getAssetsDao(),fpage,fmap);  
			this.assetListViewAdapter.addAll(fpage.getResult());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return fpage;
	}
 
}
