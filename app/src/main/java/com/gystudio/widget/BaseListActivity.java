/**
 * 文件名：BaseListActivity.java
 *
 * 版本信息：
 * 日期：2014-6-13
 * Copyright 高原工作室  Corporation 2014 
 * 版权所有
 *
 */
package com.gystudio.widget;

import java.util.Map;

import org.gystudio.ksoap2.serialization.SoapPrimitive;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gystudio.base.R;
import com.gystudio.base.entity.AppContext;
import com.gystudio.db.Page;
import com.gystudio.service.SOAPWebServiceTask;
import com.gystudio.utils.Alert;
import com.gystudio.utils.SysConfig;
import com.gystudio.utils.TranUtils;
import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;


/**
 * 
 * 项目名称：baseadr
 * 类名称：BaseListActivity
 * 类描述：
 * 创建人：yuanbf
 * 创建时间：2014-6-13 下午5:18:25
 * @version 
 * 
 */
public abstract class BaseListActivity<T,H extends OrmLiteSqliteOpenHelper> extends OrmLiteBaseListActivity<H> {

	private int pageSize = 15;
	private int currPageIndex = 1;
	private int recordCount = 0;
	private ListView lstView;
	private int lastItem;
	private int totalCount;// 列表中的item的数量
	private boolean isLoading = false;// 标记是否正在加载
	private Boolean isFirstLoad = false;// 标记是否是第一次下载
	private int currentSelectItemIndex;// 标记每次下载之前被选中Item的下标
	private LinearLayout mProgressBarLayout;
	private TextView noDataText;// 提示无查询结果
	private LayoutInflater mInflater;
	private View footerView = null;
	private boolean isAddFooterView = false;// 标记是否已添加了FooterView
	private String methodName;
	private int contentViewTop = 0;// 内容距顶部距离
 
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	private void viewInited() {
		Rect rect = new Rect();
		Window window = getWindow();
		lstView.getWindowVisibleDisplayFrame(rect);
		// int statusBarHeight= rect.top;
		contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT)
				.getTop();
		// int titleBarHeight= contentViewTop - statusBarHeight;
	}
	protected void Initialization() {
		mInflater = getLayoutInflater();
		noDataText = (TextView) findViewById(R.id.asset_search_list_no_data);
		mProgressBarLayout = (LinearLayout) findViewById(R.id.asset_search_list_progress_layout);
		lstView = (ListView) getListView();
		footerView=getFooterView();
		lstView.addFooterView(footerView);
		lstView.setAdapter(this.getListViewAdapter());
		lstView.removeFooterView(footerView);
		lstView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
		lstView.setLongClickable(true);
		lstView.setClickable(true);
		lstView.setFastScrollEnabled(true);
		lstView.setSelected(true);
		lstView.setLongClickable(true);


		lstView.setOnScrollListener(new OnScrollListener() {
			public void onScroll(AbsListView v, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem + visibleItemCount;
				totalCount = totalItemCount;
				System.out.println("lastItem==>"+lastItem);
				System.out.println("totalCount==>"+totalCount);
				System.out.println("isLoading==>"+isLoading);
				if (lastItem - 1 == totalCount - 1 && !isLoading) {
					isLoading = !isLoading;
					getPage(totalCount - 2);
				}
			}
			public void onScrollStateChanged(AbsListView v, int state) {
			}
		});
		// 获得焦点
		lstView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == getListViewAdapter().getCount() - 1 && !isLoading) {
					isLoading = !isLoading;
					getPage(arg2);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		lstView.post(new Runnable() {
			public void run() {
				viewInited();
			}
		});
	}
 
	protected void getPage(int index) {
		currentSelectItemIndex = index;
		int newCurrPageIndex = (int) Math.ceil((double) recordCount/(double) pageSize);
		int tempCurrPageIndex = currPageIndex + 1;
		Log.e("CurrPageIndex", String.valueOf(currPageIndex));
		Log.e("newCurrPageIndex", String.valueOf(newCurrPageIndex));
		if (tempCurrPageIndex <= newCurrPageIndex) {
			isFirstLoad = true;
			currPageIndex++;
			searchListData(getMethodName()); 
		}
	}

	protected void searchListData(String methodName){
		//Alert.DisplayProgressDialog(this, "", "正在加载...");
		String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
		String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改

		Map hashMap=this.getParamMap(); 
		 if(AppContext.offline){
			Page page=this.findLocalData(hashMap,currPageIndex,pageSize);
			recordCount = Long.valueOf(page.getTotalCount()).intValue();
			totalCount=this.getListViewAdapter().getCount();
			this.setTitle("列表数据-已经加载"+totalCount+"条-共" + String.valueOf(recordCount) + "条");
			this.afterBuild();
			mProgressBarLayout.setVisibility(LinearLayout.GONE);// mProgressBar消失
		 }else{ 
			System.out.println("serviceUrl==>"+serviceUrl);
			hashMap.put("pageSize", pageSize);
			hashMap.put("pageNo", currPageIndex);
			try{
				new SOAPWebServiceTask(this,null) {
					@Override
					public boolean handleMessage(Message msg) {
					//	Alert.DisplayProgressDialogCancel();
						switch (msg.what) {
						case 1:
							onLoadData((SoapPrimitive) msg.obj);
							break;
						case 2:
							mProgressBarLayout.setVisibility(LinearLayout.GONE);// mProgressBar消失
							Toast.makeText(BaseListActivity.this, "远程服务器无响应！", Toast.LENGTH_LONG).show();
							break;
						}
						return false;
					}
				}.execute(serviceUrl, nameSpace, methodName, hashMap);
			} catch (Exception e) {
				Toast.makeText(BaseListActivity.this, "请求出错！"+e.getMessage(), Toast.LENGTH_LONG).show();
				mProgressBarLayout.setVisibility(LinearLayout.GONE);// mProgressBar消失
			}
		}
	}

	private void onLoadData(SoapPrimitive soapObject){

		try {
			String json=  TranUtils.decode(soapObject.toString());
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(json);
			Log.e("JsonData", json);
			int success = jsonObject.get("success").getAsInt();
			int total = jsonObject.get("total").getAsInt();
			if (recordCount == 0) {
				recordCount = jsonObject.get("recordcount").getAsInt();
			}
			String message = jsonObject.get("message").getAsString();

			
			if (success == 1) {
				buildData(jsonObject);
				this.afterBuild();
			} else {
				if (getListViewAdapter().getCount() == 0) {
					noDataText.setVisibility(TextView.VISIBLE);
					noDataText.setText(message);
				}
			}
			totalCount=this.getListViewAdapter().getCount();
			this.setTitle("列表数据-已经加载"+totalCount+"条-共" + String.valueOf(recordCount) + "条");
		} catch (Exception e) {
			e.printStackTrace();
			Alert.DisplayAlertQqDialog(this, 2, "提示", "网络出现异常,是否重新加载！"+e.getMessage(), "确定",
					"取消", oKClick, cancelClick);
			mProgressBarLayout.setVisibility(LinearLayout.GONE);// mProgressBar消失

		} finally {
			mProgressBarLayout.setVisibility(LinearLayout.GONE);// mProgressBar消失
		}
	}

	private void  afterBuild(){
		/* 如果当前列表中的数量小于总数量,则将footerView添加到列表的尾部,负责将footerView从列表中移除 */
		if (currPageIndex * pageSize < recordCount && !isAddFooterView) {
			lstView.addFooterView(footerView);
			isAddFooterView = true;
		} else if (currPageIndex * pageSize >= recordCount && isAddFooterView) {
			lstView.removeFooterView(footerView);
			isAddFooterView = false;
		}
		if(!getListViewAdapter().isEmpty()){
			lstView.setSelection(currentSelectItemIndex);
		}
		System.out.println("isLoading===>false");
		isLoading = false;
	}

	public ListView getLstView() {
		return lstView;
	}

	public void setLstView(ListView lstView) {
		this.lstView = lstView;
	}

	/**
	 * 响应对话框“确定”按钮监听器
	 */
	OnClickListener oKClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mProgressBarLayout.setVisibility(LinearLayout.VISIBLE);// mProgressBar显示
			searchListData(getMethodName());
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
	/**
	 * 获得ListView底部固定显示的View
	 * 
	 * @return
	 */
	public View getFooterView() {
		View footer = mInflater.inflate(R.layout.list_item_footer, null);
		System.out.println("footer====>"+footer);
		return footer;
	}
	
 
	protected abstract Map getParamMap();

	protected abstract void buildData(JsonObject jsonObject);
	
	protected abstract Page findLocalData(Map fmap,int pageNo,int pageSize);

	protected abstract BaseAdapter getListViewAdapter();

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrPageIndex() {
		return currPageIndex;
	}
	public void setCurrPageIndex(int currPageIndex) {
		this.currPageIndex = currPageIndex;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public int getLastItem() {
		return lastItem;
	}
	public void setLastItem(int lastItem) {
		this.lastItem = lastItem;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public boolean isLoading() {
		return isLoading;
	}
	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}
	public Boolean getIsFirstLoad() {
		return isFirstLoad;
	}
	public void setIsFirstLoad(Boolean isFirstLoad) {
		this.isFirstLoad = isFirstLoad;
	}
	public int getCurrentSelectItemIndex() {
		return currentSelectItemIndex;
	}
	public void setCurrentSelectItemIndex(int currentSelectItemIndex) {
		this.currentSelectItemIndex = currentSelectItemIndex;
	}
}
