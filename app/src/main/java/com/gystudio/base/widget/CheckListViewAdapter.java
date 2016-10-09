/**
 * 文件名：AssetListViewAdapter.java
 *
 * 版本信息：
 * 日期：2014-6-13
 * Copyright 高原工作室  Corporation 2014 
 * 版权所有
 *
 */
package com.gystudio.base.widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gystudio.base.R;
import com.gystudio.utils.ReflectionUtils;
import com.rstco.assetmgr.AssetEntity;

/**
 * 
 * 项目名称：baseadr
 * 类名称：AssetListViewAdapter
 * 类描述：
 * 创建人：yuanbf
 * 创建时间：2014-6-13 上午11:08:55
 * @version 
 * 
 */
public class CheckListViewAdapter<T>  extends BaseAdapter {   
	private Context context;                        //运行上下文   
	private List<T> listItems;    //商品信息集合   
	private LayoutInflater listContainer; 
    private Map<String,T> hasCheckMap=new LinkedHashMap<String,T>();
    private String keyName;
    private CallBack<T> callback;

	//视图容器   
	public final class ListItemView{                //自定义控件集合        
		public TextView title;     
		public TextView info;   
		public CheckBox check;          
	}     


	public CheckListViewAdapter(Context context, List<T> listItems,String key ) {   
		this.context = context;            
		listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
		this.keyName=key;
		this.listItems = listItems;  
	}   

	public CheckListViewAdapter(Context context, List<T> listItems,String key,CallBack<T> callback) {   
		this.context = context;            
		listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
		this.keyName=key;
		this.listItems = listItems;  
		this.callback=callback;
	}   

	public int getCount() {   
		// TODO Auto-generated method stub   
		return listItems.size();   
	}   

	public void add(T item){
		listItems.add(item);
		this.notifyDataSetChanged();
	}
	
	public void addOrUpdate(T item){  
		int i = 0;
		boolean bz = false;
		for (T vitem : listItems) {
			if (vitem.equals(item)) {
				listItems.set(i, item);
				bz = true;
				break;
			}
			i++;
		}
		if (!bz) {
			this.add(item);
		} 
		this.notifyDataSetChanged();
	}

	public void addAll(List<T> items){
		listItems.addAll(items);
		this.notifyDataSetChanged();
	}

	public void clear(){
		listItems.clear();
		this.hasCheckMap.clear();
		this.notifyDataSetChanged();
	}


	
	public Object getItem(int arg0) {   
		// TODO Auto-generated method stub   
		return null;   
	}   

	public long getItemId(int arg0) {   
		// TODO Auto-generated method stub   
		return 0;   
	}   
	
	public List<T> getCheckItem(){
		ArrayList<T> rlst=new ArrayList<T>();
		for (Map.Entry<String,T> entry : hasCheckMap.entrySet()) {
			rlst.add(entry.getValue());
		}
		return rlst;
	}

	public void checkAll(){
		this.checkAction("all");
	}
	
	public void cancelCheck(){
		this.checkAction("cancel");
	}
	
	public void reverseCheck(){
		this.checkAction("reverse");
	}
	
	private void checkAction(String type){
		for (T item : listItems) {
			if("all".equals(type)){
				this.checkedChange(item, true);
			}else if("reverse".equals(type)){
				if(this.isChecked(item)){
					this.checkedChange(item, false);
				}else{
					this.checkedChange(item, true);
				}
			}else if("cancel".equals(type)){
				this.hasCheckMap.clear();
			} 
		}
	}
	
	public boolean isChecked(T item){
		String keyid;
		try {
			keyid = (String)ReflectionUtils.getFieldValue(item, this.keyName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		T chk=this.hasCheckMap.get(keyid);
		if(null==chk){
			return false;
		}else{
			return true;
		}
	}
	
	private boolean checkedChange(T item,Boolean isChecked) {   
		String keyid;
		try {
			keyid = (String)ReflectionUtils.getFieldValue(item, this.keyName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		T chk=this.hasCheckMap.get(keyid);
		if(isChecked){
			if(null==chk){
				hasCheckMap.put(keyid,item);
			}
		}else{
			hasCheckMap.remove(keyid);
		}
		return true;
		
	}   
	
	/**  
	 * ListView Item设置  
	 */  
	public View getView(final int position,View convertView, ViewGroup parent) {   
		// TODO Auto-generated method stub   
		Log.e("method", "getView");   
		final int selectID = position;   
		final T item=listItems.get(selectID) ;
		//自定义视图   
		ListItemView  listItemView = null;   
		if (convertView == null) {   
			listItemView = new ListItemView();    
			//获取list_item布局文件的视图   
			convertView = listContainer.inflate(R.layout.list_view_item_type2, null);   
			//获取控件对象   
			listItemView.title = (TextView)convertView.findViewById(R.id.titleItem);   
			listItemView.info = (TextView)convertView.findViewById(R.id.infoItem);   
			listItemView.check= (CheckBox )convertView.findViewById(R.id.checkItem);     
			//设置控件集到convertView   
			convertView.setTag(listItemView);   
		}else {   
			listItemView = (ListItemView)convertView.getTag();   
		}   
		listItemView.title.setOnClickListener(new  View.OnClickListener() { 
			@Override
			public void onClick(View v) { 
				if(null!=callback){
					callback.onClick(position, v, item);
				}
			}
		} );
		listItemView.info.setOnClickListener(new  View.OnClickListener() { 
			@Override
			public void onClick(View v) { 
				if(null!=callback){
					callback.onClick(position, v, item);
				}
			}
		} );
		listItemView.check   
		.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {   
			@Override  
			public void onCheckedChanged(CompoundButton buttonView,   
					boolean isChecked) {   
				//记录物品选中状态   
				if(!checkedChange(item,isChecked)){
					//如果不成功   改变
				}
			}   
		});  
		
		listItemView.check.setChecked(isChecked(item));
		//设置文字和图片   
		if(null==callback){
			listItemView.title.setText(item.toString());
			listItemView.info.setText("");
		}else{
			listItemView.info.setText(callback.onInfoRender(position,listItemView.info,item));
			listItemView.title.setText(callback.onTitleRender(position,listItemView.title,item));
		}
		

		return convertView;   
	}


	public String getKeyName() {
		return keyName;
	}


	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
 
  

	public static interface CallBack<T> {

		public String onTitleRender(final int position, View titleView, T item);
		public String onInfoRender(final int position, View infoView, T item);
		public String onClick(final int position, View infoView, T item);

	}
 

}  
