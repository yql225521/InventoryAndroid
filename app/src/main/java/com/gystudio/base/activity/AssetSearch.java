package com.gystudio.base.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gystudio.base.R;
import com.gystudio.base.entity.AppContext;
import com.gystudio.db.DataHelper;
import com.gystudio.db.DataManager;
import com.gystudio.widget.GyBaseActivity;
import com.rstco.sjpt.entity.PubDictEntity;
import com.rstco.sjpt.entity.PubOrganEntity;


public class AssetSearch extends GyBaseActivity<DataHelper> {
	private EditText assetCodeEdt = null; 
	private EditText assetNameEdt = null; 
	private Spinner organSpinner = null; 
	private Spinner categorySpinner = null; 
	
	private Spinner starnum1Spinner = null; 
	private Spinner starnum2Spinner = null; 
	private Button btnSearch = null;
	private DataManager dataManager=new DataManager();
	
	private ArrayAdapter<PubDictEntity>  typeSpinnerArrayAdapter=null;
	private ArrayAdapter<PubDictEntity>  starSpinnerArrayAdapter=null;
	private ArrayAdapter<PubOrganEntity> organSpinnerArrayAdapter=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asset_search_main);
		setTitle("资产查询");
		Initialization();
	}

	private void Initialization() {
		
		assetNameEdt = (EditText)findViewById(R.id.asset_search_main_edit_assetname);
		assetCodeEdt = (EditText)findViewById(R.id.asset_search_main_edit_assetcode);
		
		typeSpinnerArrayAdapter=new ArrayAdapter<PubDictEntity>(this, android.R.layout.simple_spinner_item);  
		typeSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		
		starSpinnerArrayAdapter=new ArrayAdapter<PubDictEntity>(this, android.R.layout.simple_spinner_item);  
		starSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		
		organSpinnerArrayAdapter=new ArrayAdapter<PubOrganEntity>(this, android.R.layout.simple_spinner_item); 
		organSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		
		categorySpinner = (Spinner) findViewById(R.id.asset_search_main_spinner_category);
		categorySpinner.setAdapter(typeSpinnerArrayAdapter);
		
		
		starnum1Spinner = (Spinner) findViewById(R.id.asset_search_main_spinner_starnum1);
		starnum1Spinner.setAdapter(starSpinnerArrayAdapter); 
		starnum2Spinner = (Spinner) findViewById(R.id.asset_search_main_spinner_starnum2);
		starnum2Spinner.setAdapter(starSpinnerArrayAdapter);
		
		
		organSpinner = (Spinner) findViewById(R.id.asset_search_main_spinner_organs);
		organSpinner.setAdapter(organSpinnerArrayAdapter); 
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
			List<PubDictEntity> dictlst=dataManager.findDicts(this.getHelper().getDictDao(), AppContext.currUser.getAccounts(), "001");
			for (PubDictEntity pubDictEntity : dictlst) {
				typeSpinnerArrayAdapter.add(pubDictEntity);
				System.out.println("add=====>"+pubDictEntity.toString());
			}
			List<PubDictEntity> dictlst1=dataManager.findDicts(this.getHelper().getDictDao(), AppContext.currUser.getAccounts(), "V03");
			for (PubDictEntity pubDictEntity : dictlst1) {
				starSpinnerArrayAdapter.add(pubDictEntity);
				System.out.println("add=====>"+pubDictEntity.toString());
			}
		}catch(Exception ex){
			ex.printStackTrace();
			Toast.makeText(AssetSearch.this, "查询数据出错！"+ex.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		organSpinnerArrayAdapter.notifyDataSetChanged();
		typeSpinnerArrayAdapter.notifyDataSetChanged();	
		starSpinnerArrayAdapter.notifyDataSetChanged();
		
		
		
		
		btnSearch = (Button) findViewById(R.id.asset_search_main_searchbtn);
		
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {    
				Intent inEntList = new Intent(AssetSearch.this,
						AssetSearchList.class);
				// 把用户名传给下一个activity
				Bundle bundle = new Bundle();
				bundle.putString("assetCode", assetCodeEdt.getText().toString().trim());
				bundle.putString("assetName", assetNameEdt.getText().toString().trim());
				bundle.putString("organCode", null!=organSpinner.getSelectedItem()?((PubOrganEntity)organSpinner.getSelectedItem()).getOrganCode():"");
				bundle.putString("category",null!=categorySpinner.getSelectedItem()?((PubDictEntity)categorySpinner.getSelectedItem()).getCode():"" );
				bundle.putString("starNum1",null!=starnum1Spinner.getSelectedItem()?((PubDictEntity)starnum1Spinner.getSelectedItem()).getCode():"" );
				bundle.putString("starNum2",null!=starnum2Spinner.getSelectedItem()?((PubDictEntity)starnum2Spinner.getSelectedItem()).getCode():"" );

				inEntList.putExtras(bundle);
				AssetSearch.this.startActivity(inEntList);
			}
		});
		 
	}

 
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
