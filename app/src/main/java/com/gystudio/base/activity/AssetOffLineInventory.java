package com.gystudio.base.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gystudio.base.R;
import com.gystudio.base.entity.AppContext;
import com.gystudio.base.widget.AssetListViewAdapter;
import com.gystudio.db.DataHelper;
import com.gystudio.db.DataManager;
import com.gystudio.utils.Alert;
import com.gystudio.utils.ExtDate;
import com.gystudio.utils.ReflectionUtils;
import com.gystudio.utils.SysConfig;
import com.gystudio.widget.GyBaseActivity;
import com.gystudio.zxing.CaptureActivity;
import com.rstco.assetmgr.AssetEntity;
import com.rstco.sjpt.entity.PubOrganEntity;

/**
 */
public class AssetOffLineInventory extends  GyBaseActivity<DataHelper> {
	private final static int REQUEST_CODE=1;
	public static final String BARCODE = "barCode";
	public static final String BATCHNUM = "batchNum";
 
	private Spinner organSpinner = null; 
	private AssetListViewAdapter assetListViewAdapter=null;
	
	private DataManager dataManager=new DataManager();

	private ArrayAdapter<PubOrganEntity> organSpinnerArrayAdapter=null;
	private ListView assetListView = null;
	private Button btnEnd = null;
	private Button btnScan = null;
	private Button btnManual = null;
	private TextView datainfo =null;
	private Intent in = null;
 
	private String addr=AppContext.getAddress();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_inventory_main);
		setTitle("资产盘点");
		Initialization();

	}

	private void Initialization() {

		in = getIntent();
		organSpinner = (Spinner) findViewById(R.id.asset_pd_organs);
		datainfo=(TextView) findViewById(R.id.asset_pd_datainfo);
		// TODO Auto-generated method stub
		//定义适配器  

		organSpinnerArrayAdapter=new ArrayAdapter<PubOrganEntity>(this, android.R.layout.simple_spinner_item); 
		organSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		organSpinner.setPrompt("选择使用部门");
		organSpinner.setAdapter(organSpinnerArrayAdapter);  
		organSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {  
			@Override  
			public void onItemSelected(AdapterView<?> arg0, View arg1,  
					int arg2, long arg3) {  
				assetListViewAdapter.clear();
				try{
					List<AssetEntity> assetLst=
							dataManager.findPdAssets(AssetOffLineInventory.this.getHelper().getAssetsDao(), 
									AppContext.currUser.getAccounts(), 
									null!=organSpinner.getSelectedItem()?((PubOrganEntity)organSpinner.getSelectedItem()).getOrganCode():"");
					for (AssetEntity asset : assetLst) {
						assetListViewAdapter.add(asset);
						System.out.println("add=====>"+asset.toString());
						
					}
					datainfo.setText(" 列表有 "+assetLst.size()+"条数据");
				}catch(Exception ex){
					ex.printStackTrace();
					Toast.makeText(AssetOffLineInventory.this, "加载离线盘点数据出错！"+ex.getMessage(), Toast.LENGTH_LONG).show();
				}
			}  

			@Override  
			public void onNothingSelected(AdapterView<?> arg0) {  
				// TODO Auto-generated method stub  
				//organcode=obj[0];
				// organSpinner.setSelection(0);
				// arg0.setVisibility(View.VISIBLE);
			}  
		});  

		try{
			List<PubOrganEntity> orglst=dataManager.findOrgans(this.getHelper().getOrgDao(), AppContext.currUser.getAccounts(), null);
			if(orglst.isEmpty()){
				organSpinnerArrayAdapter.add(AppContext.currOrgan);
			}else{ 
				for (PubOrganEntity pubOrganEntity : orglst) {
					organSpinnerArrayAdapter.add(pubOrganEntity);
					System.out.println("add=====>"+pubOrganEntity.toString());
				}
			}
 
		}catch(Exception ex){
			Toast.makeText(AssetOffLineInventory.this, "查询数据出错！"+ex.getMessage(), Toast.LENGTH_LONG).show();
		}

		assetListView = (ListView) findViewById(R.id.asset_pd_list);

		assetListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
		assetListView.setLongClickable(true);
		assetListView.setClickable(true);
		assetListView.setFastScrollEnabled(true);
		assetListView.setSelected(true);
		assetListView.setLongClickable(true);

		assetListViewAdapter=new AssetListViewAdapter(this,new ArrayList<AssetEntity>());  
		assetListView.setAdapter(assetListViewAdapter);

		btnEnd = (Button) findViewById(R.id.fs_serter_search_main_btn_end);
		btnEnd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {      
				organSpinner.setEnabled(true);
			}
		});

		btnManual = (Button) findViewById(R.id.fs_serter_search_main_btn_manaul);
		btnManual.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {     
				Intent inIvt= new Intent(AssetOffLineInventory.this,
						AssetManual.class);
				AssetOffLineInventory.this.startActivityForResult(inIvt,REQUEST_CODE);
			}
		});

		btnScan = (Button) findViewById(R.id.fs_serter_search_main_btn_scan);
		btnScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {        
				if(organSpinnerArrayAdapter.getCount()>0){
					if(addr==null||addr.equals("")){
						Toast.makeText(AssetOffLineInventory.this, "未获得当前位置", Toast.LENGTH_LONG).show();
					}else{
						organSpinner.setEnabled(false);
						Intent inScaning = new Intent(AssetOffLineInventory.this,
								CaptureActivity.class);
						AssetOffLineInventory.this.startActivityForResult(inScaning, REQUEST_CODE);
					}
				}else{
					Toast.makeText(AssetOffLineInventory.this, "没有部门资产可盘点", Toast.LENGTH_LONG).show();
				}
			}
		});  
		if(organSpinnerArrayAdapter.getCount()>0){
			organSpinner.performClick();
		}
		//isInventory();
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 
		System.out.println("REQUEST_CODE===>"+requestCode);
		System.out.println("RESULT_CODE===>"+resultCode);
	 
	//	if (requestCode==REQUEST_CODE){
		//o  operator
		//s  spec
		//g  organName
		
			if (resultCode==CaptureActivity.RESULT_CODE){
				Bundle bundle=data.getExtras();
				String barcode=bundle.getString("barcode");
				if (!"".equals(barcode)) {
					String[] codes=SysConfig.getCodes(barcode);
					AssetEntity asset=new AssetEntity();			
					this.parseCodes(codes, asset);
					try {
						asset = dataManager.findAssetData(this.getHelper().getAssetsDao(),asset.getAssetCode());
					} catch (SQLException e) { 
						e.printStackTrace(); 
					}
					if(null==asset){
						asset=new AssetEntity();			
						this.parseCodes(codes, asset); 
					} 
					this.showInfo(asset);
				}
			}else if (resultCode==AssetManual.RESULT_CODE){
				Bundle bundle=data.getExtras();
				String code=bundle.getString("code");
				String name=bundle.getString("name");
				if (StringUtils.isNotBlank(code)) {
					AssetEntity asset = null;
				    try {
						asset = dataManager.findAssetData(this.getHelper().getAssetsDao(),code);
					} catch (SQLException e) { 
						e.printStackTrace(); 
					}
				    if(null==asset){
						asset=new  AssetEntity();
						asset.setAssetCode(code);
						if(StringUtils.isNotBlank(name)){
							asset.setAssetName(name);
						}else{
							asset.setAssetName("");
						}
					} 	
					asset.setPdfs("2");
					this.showInfo(asset);
				}
			}else if (resultCode==InventoryChgSel.RESULT_CODE){
				Bundle bundle=data.getExtras();
				AssetEntity asset=(AssetEntity)bundle.get("asset");
				Boolean pdzt=(Boolean)bundle.get("pdzt");
				pdzt=null==pdzt?false:pdzt;
				if (null!=asset) {
					doLocalInventoryBase(asset,pdzt);
					//不符项目 
					//assetListViewAdapter.notifyDataSetChanged();
				}
			}
		//} 
		 
	}

	public void doLocalInventory(final AssetEntity asset,final String msg,final boolean pdzt) { 
		 Alert.DisplayAlertQqDialog(this, 0, "信息", msg, "确认盘点",
					"不符项目", 
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							doLocalInventoryBase(asset,pdzt);
						}
					}, new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent inSeling = new Intent(AssetOffLineInventory.this,
									InventoryChgSel.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("asset", asset);
							bundle.putSerializable("pdzt", pdzt);
							inSeling.putExtras(bundle);
							AssetOffLineInventory.this.startActivityForResult(inSeling, REQUEST_CODE);
						}
					});
 
	}
	
	public void  showInfo(final AssetEntity asset) { 
		StringBuffer sb=new StringBuffer();
	
		sb.append("资产编码：").append(StringUtils.isNotBlank(asset.getFinCode())?asset.getFinCode():asset.getAssetCode()).append("\n");
		sb.append("资产名称：").append(asset.getAssetName()).append("\n");
		sb.append("资产类型：").append(asset.getAssetTypeName()).append("\n");
		sb.append("资产类别：").append(asset.getCateName()).append("\n");
		//sb.append("财务编码：").append(asset.getFinCode()).append("\n");
		sb.append("管理部门：").append(asset.getMgrOrganName()).append("\n");
		sb.append("使用部门：").append(asset.getOrganName()).append("\n");
		sb.append("存放地点：").append(asset.getStorageDescr()).append("\n");
		sb.append("使  用  人：").append(asset.getOperator()).append("\n");
		sb.append("原　　值：").append(asset.getOriginalValue()==null?"":asset.getOriginalValue()).append("\n");
		sb.append("使用日期：").append(asset.getEnableDateString()).append("\n");
		sb.append("使用年限：").append(asset.getUseAge()).append("\n");
		sb.append("当前状态：").append(asset.getStatus());
		Float starnum=asset.getStarNum()==null?null:asset.getStarNum().floatValue();
		ExtDate nowdate=new ExtDate();
		asset.setPdate(nowdate.format("yyyy-MM-dd HH:mm:ss SSS"));
		if(StringUtils.isBlank(asset.getPdfs())){
			asset.setPdfs("1");
		}
		asset.setAddr(addr);
		asset.setSimId(AppContext.getSimId());
		asset.setOrganCode(null!=organSpinner.getSelectedItem()?((PubOrganEntity)organSpinner.getSelectedItem()).getOrganCode():"");
		asset.setMgrOrganCode(AppContext.currOrgan.getOrganCode());
		asset.setDisCodes("");
		final String msg=sb.toString(); 
		
		/**
		 * 是否已盘点 已盘点 提示 是否覆盖 ; 是 盘点 ，否 继续下一个资产 未盘点 继续原来流程
		 */ 
		
		try {
			final boolean pdzt = dataManager.isExistPdAsset(
						this.getHelper().getAssetsDao(), 
						asset.getAssetCode(), 
						AppContext.currUser.getAccounts()
					);

			if (pdzt){
				Alert.DisplayAlertQqDialog(this, 0, "信息", "该资产已离线盘点  ！", 
						"覆盖","盘点其他资产", 
						 new OnClickListener() { 
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								doLocalInventory(asset,msg,pdzt);
							}
							
						}, 
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								Intent inScaning = new Intent(
										AssetOffLineInventory.this,
										CaptureActivity.class);
								AssetOffLineInventory.this
										.startActivityForResult(inScaning,
												REQUEST_CODE);
							}
						});
			}else{
				doLocalInventory(asset,msg,pdzt);
			}

		} catch (Exception ex) {
			ex.printStackTrace(); 
			Alert.DisplayAlertQqDialog(this, 0, "信息", "系统查离线数据出错！" + ex.getMessage(), "继续", "中止",
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent inScaning = new Intent(
									AssetOffLineInventory.this,
									CaptureActivity.class);
							AssetOffLineInventory.this.startActivityForResult(
									inScaning, REQUEST_CODE);
						}
					}, null);
		}
	}
	
	public void doLocalInventoryBase(final AssetEntity asset,boolean pdzt)  {
		String msg = "[" + asset.getAssetCode() + "] " + asset.getAssetName()
				+ " 盘点成功";
		try { 
			dataManager
					.savePdAsset(
							this.getHelper().getAssetsDao(),
							AppContext.currUser.getAccounts(),
							asset,
							AppContext.currOrgan.getOrganCode(),
							null != organSpinner.getSelectedItem() ? ((PubOrganEntity) organSpinner
									.getSelectedItem()).getOrganCode() : "",
							addr, AppContext.simId);
		} catch (Exception ex) {
			ex.printStackTrace();
			msg = "存储离线数据出错！" + ex.getMessage();
		}
		if(pdzt){
			assetListViewAdapter.replace(asset);
		}else{
			assetListViewAdapter.add(asset);
		}
		datainfo.setText(" 列表有 "+assetListViewAdapter.getCount()+"条数据");
		Alert.DisplayAlertQqDialog(this, 0, "信息", msg, "继续", "中止",
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent inScaning = new Intent(
								AssetOffLineInventory.this,
								CaptureActivity.class);
						AssetOffLineInventory.this.startActivityForResult(
								inScaning, REQUEST_CODE);
					}
				}, null);

	}
	
	public static AssetEntity parseCodes(String[] codes,AssetEntity asset){ 
		asset.setAssetCode(codes[0]);  
		for (int i = 0; i < codes.length; i++) {
			String string = codes[i]; 
			
			if(StringUtils.indexOf(string, "|")>-1){
				String[] va=StringUtils.split(string,"|");
				if("n".equalsIgnoreCase(va[0])){
					asset.setAssetName(va[1]);
				}else
				if("o".equalsIgnoreCase(va[0])){
					asset.setOperator(va[1]);
				}else
				if("s".equalsIgnoreCase(va[0])){
					asset.setSpec(va[1]);
				}else
				if("g".equalsIgnoreCase(va[0])){
					asset.setOrganName(va[1]);
				}else {
					ReflectionUtils.setFieldValue(asset, va[0], va[1]);
				}
			}else{
				if(i==1){
					System.out.println(i+"-->"+codes[i]);
					asset.setAssetName(codes[i]);
				} 
				if(i==2){
					System.out.println(i+"-->"+codes[i]);
					asset.setOperator(codes[i]);
				} 
				if(i==3){
					System.out.println(i+"-->"+codes[i]);
					asset.setOrganName(codes[i]);
				} 
			} 
		}  
		return asset;
	}
	
	@Override 
	protected void onDestroy() {  
 
		super.onDestroy();  
	}  
}
