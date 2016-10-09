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


public class InventorySearch extends GyBaseActivity<DataHelper> {
 
	private EditText storageEdt = null; 
	private Spinner organSpinner = null; 
	private Spinner matchSpinner = null; 
	private Spinner categorySpinner = null; 
	private Spinner completeSpinner = null; 
	private Button btnSearch = null;
	private DataManager dataManager=new DataManager();
	
	private ArrayAdapter<PubDictEntity>  matchSpinnerArrayAdapter=null;
	private ArrayAdapter<PubDictEntity>  typeSpinnerArrayAdapter=null;
	private ArrayAdapter<PubOrganEntity> organSpinnerArrayAdapter=null;
	private ArrayAdapter<PubDictEntity> completeSpinnerArrayAdapter=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asset_inventory_search_main);
		setTitle("盘点情况查看");
		Initialization();
	}

	private void Initialization() {
		
		storageEdt= (EditText)findViewById(R.id.asset_inventory_search_main_edit_storage);
		
		matchSpinnerArrayAdapter=new ArrayAdapter<PubDictEntity>(this, android.R.layout.simple_spinner_item);  
		matchSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
 
		typeSpinnerArrayAdapter=new ArrayAdapter<PubDictEntity>(this, android.R.layout.simple_spinner_item);  
		typeSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		
		organSpinnerArrayAdapter=new ArrayAdapter<PubOrganEntity>(this, android.R.layout.simple_spinner_item); 
		organSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		
		completeSpinnerArrayAdapter=new ArrayAdapter<PubDictEntity>(this, android.R.layout.simple_spinner_item); 
		completeSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		
		categorySpinner = (Spinner) findViewById(R.id.asset_inventory_search_main_spinner_category);
		categorySpinner.setAdapter(typeSpinnerArrayAdapter);
		organSpinner = (Spinner) findViewById(R.id.asset_inventory_search_main_spinner_organs);
		organSpinner.setAdapter(organSpinnerArrayAdapter);
		completeSpinner = (Spinner) findViewById(R.id.asset_inventory_search_main_spinner_complete);
		completeSpinner.setAdapter(completeSpinnerArrayAdapter); 
		matchSpinner = (Spinner) findViewById(R.id.asset_inventory_search_main_spinner_matchtype);
		matchSpinner.setAdapter(matchSpinnerArrayAdapter); 

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
			List<PubDictEntity> matchlst=dataManager.findDicts(this.getHelper().getDictDao(), AppContext.currUser.getAccounts(), "V02");
			for (PubDictEntity pubDictEntity : matchlst) {
				matchSpinnerArrayAdapter.add(pubDictEntity);
				System.out.println("add=====>"+pubDictEntity.toString());
			}
			List<PubDictEntity> completelst=dataManager.findDicts(this.getHelper().getDictDao(), AppContext.currUser.getAccounts(), "V01");
			for (PubDictEntity pubDictEntity : completelst) {
				completeSpinnerArrayAdapter.add(pubDictEntity);
				System.out.println("add=====>"+pubDictEntity.toString());
			}
		}catch(Exception ex){
			Toast.makeText(InventorySearch.this, "查询数据出错！"+ex.getMessage(), Toast.LENGTH_LONG).show();
		}
		organSpinnerArrayAdapter.notifyDataSetChanged();
		typeSpinnerArrayAdapter.notifyDataSetChanged();
		matchSpinnerArrayAdapter.notifyDataSetChanged();
		completeSpinnerArrayAdapter.notifyDataSetChanged();
		
		
		btnSearch = (Button) findViewById(R.id.asset_inventory_search_main_searchbtn);
		
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {    
				Intent inEntList = new Intent(InventorySearch.this,
						InventorySearchList.class);
				// 把用户名传给下一个activity
				Bundle bundle = new Bundle();
 
				bundle.putString("storage", storageEdt.getText().toString().trim());
				bundle.putString("organCode", null!=organSpinner.getSelectedItem()?((PubOrganEntity)organSpinner.getSelectedItem()).getOrganCode():"");
				bundle.putString("storageMatchType",  null!=matchSpinner.getSelectedItem()?((PubDictEntity)matchSpinner.getSelectedItem()).getCode():"");
				bundle.putString("category",null!=categorySpinner.getSelectedItem()?((PubDictEntity)categorySpinner.getSelectedItem()).getCode():"" );
				bundle.putString("complete",null!=completeSpinner.getSelectedItem()?((PubDictEntity)completeSpinner.getSelectedItem()).getCode():"" );
				inEntList.putExtras(bundle);
				InventorySearch.this.startActivity(inEntList);
			}
		});
 
	}
 
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
