package com.gystudio.base.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gystudio.base.R;
import com.gystudio.base.widget.CheckListViewAdapter;
import com.gystudio.base.widget.CheckListViewAdapter.CallBack;
import com.gystudio.utils.Alert;
import com.rstco.assetmgr.AssetEntity;
import com.rstco.sjpt.entity.PubDictEntity;

/**
 */
public class InventoryChgSel extends Activity {
	public final static int RESULT_CODE=4;
	
	public static final Map<String, String> DIS_MAP = new HashMap<String, String>();
	 
    static {
    	DIS_MAP.put("organCode", "使用部门");
    	DIS_MAP.put("operator", "使用人");
    	DIS_MAP.put("storage", "存放地点");
    	DIS_MAP.put("status", "资产状态");
    }

	private CheckListViewAdapter<PubDictEntity> checkListViewAdapter=null;
	
	private ListView assetListView = null;
 
	private Button btnOK = null;
	private Button btnCancel = null;
	private TextView assetCodeEdt = null; 
	private TextView assetNameEdt = null; 
	private TextView intNotEdt = null; 
	private RatingBar starBar=null;
	private Intent in = null;
	private AssetEntity asset;
	private boolean pdzt;
	
	private InputFilter[] infarr= new InputFilter[] { new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start,     
				int end, Spanned dest, int dstart, int dend) {
			// TODO Auto-generated method stub
			return source.length() < 1 ? dest.subSequence(dstart, dend): "";     
		}     

	} };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory_chgsel);
		setTitle("选择不符项目");
		Initialization();

	}

	private void Initialization() {
		in = getIntent();
		assetNameEdt = (TextView)findViewById(R.id.asset_manual_edit_assetname);
		assetCodeEdt = (TextView)findViewById(R.id.asset_manual_edit_assetcode);
		intNotEdt = (TextView)findViewById(R.id.inv_note);
		starBar = (RatingBar)findViewById(R.id.room_ratingbar);
		assetListView = (ListView) findViewById(R.id.data_manager_list);
		assetListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
		assetListView.setLongClickable(true);
		assetListView.setClickable(true);
		assetListView.setFastScrollEnabled(true);
		assetListView.setSelected(true);
		assetListView.setLongClickable(true);
		Bundle bundle = in.getExtras();
		Object obj=bundle.getSerializable("asset");
		Boolean pdztv=bundle.getBoolean("pdzt");
		if(null==obj){
			Alert.DisplayAlertQqDialog(InventoryChgSel.this, 1, "信息", "参数错误", "确定");
			this.finish();
		}
		pdzt=pdztv==null?false:pdztv;
		asset=(AssetEntity)obj;
		Double sn=asset.getStarNum()==null?0.0:asset.getStarNum();
		starBar.setRating(sn.floatValue());
		List<PubDictEntity> itemlst=this.getDictList(asset);
	
 
		checkListViewAdapter=new CheckListViewAdapter<PubDictEntity>(this,itemlst,"code",new CallBack<PubDictEntity>(){

			@Override
			public String onTitleRender(int position, View titleView,
					PubDictEntity item) {
				// TODO Auto-generated method stub
				return item.getType();
			}

			@Override
			public String onInfoRender(int position, View infoView,
					PubDictEntity item) {
				// TODO Auto-generated method stub
				return item.getName();
			}

			@Override
			public String onClick(int position, View infoView, PubDictEntity item) {
				
				// TODO Auto-generated method stub
				return null;
			}
 
			
		});  
		assetListView.setAdapter(checkListViewAdapter);
 
		btnOK = (Button) findViewById(R.id.btn_ok);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {     
				 Intent intent=new Intent();
				 List<PubDictEntity> chklst=checkListViewAdapter.getCheckItem();
				 Double snum = Double.parseDouble(String.valueOf(starBar.getRating()));
				 if(chklst.isEmpty()&&snum.equals(asset.getStarNum())&&
						 StringUtils.isBlank(intNotEdt.getText().toString())){
					 Alert.DisplayAlertQqDialog(InventoryChgSel.this, 1, "信息", "请选择项目或者输入备注或者选择星级", "确定");
				 }else{
					 String rstr="";
					 for (PubDictEntity pubDictEntity : chklst) {
						 rstr+=","+pubDictEntity.getCode();
					 }
					 if(StringUtils.isNotBlank(rstr)){
						 rstr=rstr.substring(1);
					 }
					 asset.setInvNote(intNotEdt.getText().toString().trim());
					 asset.setDisCodes(rstr);
					 asset.setStarNum(snum);
					 intent.putExtra("asset",asset);
					 intent.putExtra("pdzt",pdzt);
					 setResult(RESULT_CODE, intent);//RESULT_CODE是一个整型变量
					 finish();
				 }
			}
		});

		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {     
				 finish();
			}
		});
 
	}
	
	private List<PubDictEntity> getDictList(AssetEntity asset){

		List<PubDictEntity> dictlst=new ArrayList<PubDictEntity>();
		assetNameEdt.setText(asset.getAssetName());
		assetNameEdt.setEnabled(false);
		assetCodeEdt.setText(asset.getAssetCode());
		assetCodeEdt.setEnabled(false);
		PubDictEntity obj1=new PubDictEntity();
		
		obj1.setCode("organCode");
		obj1.setName(asset.getOrganName());
		obj1.setType("使用部门");
		dictlst.add(obj1);
		
		obj1=new PubDictEntity();
		
		obj1.setCode("operator");
		obj1.setName(asset.getOperator());
		obj1.setType("使用人");
		dictlst.add(obj1);
		
		obj1=new PubDictEntity();
		
		obj1.setCode("storage");
		obj1.setName(asset.getStorageDescr());
		obj1.setType("存放地点");
		dictlst.add(obj1);
		
		obj1=new PubDictEntity();
		
		obj1.setCode("status");
		obj1.setName(asset.getStatus());
		obj1.setType("资产状态");
		dictlst.add(obj1);
		
		return dictlst;
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
