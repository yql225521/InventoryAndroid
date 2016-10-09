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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gystudio.base.R;
import com.gystudio.base.activity.AssetOffLineInventory;
import com.gystudio.base.activity.InventoryChgSel;
import com.gystudio.base.activity.assetphoto.PhotoViewerActivity;
import com.gystudio.base.activity.assetphoto.PictureBean;
import com.gystudio.base.entity.AppContext;
import com.gystudio.utils.Alert;
import com.gystudio.utils.SysConfig;
import com.gystudio.utils.ToolString;
import com.gystudio.zxing.CaptureActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rstco.assetmgr.AssetEntity; 

/**
 * 
 * 项目名称：baseadr 类名称：AssetListViewAdapter 类描述： 创建人：yuanbf 创建时间：2014-6-13
 * 上午11:08:55
 * 
 * @version
 * 
 */
public class AssetListViewAdapter extends BaseAdapter {
	private Context context; // 运行上下文
	private List<AssetEntity> listItems; // 商品信息集合
	private LayoutInflater listContainer;
	private boolean qtybz = false;

	private CallBack callback;

	// 视图容器
	public final class ListItemView { // 自定义控件集合
		public TextView title;
		public TextView info;
		public Button detail;
		public Button images;
	}

	public AssetListViewAdapter(Context context, List<AssetEntity> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		 ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
		 ImageLoader.getInstance().init(config);
		this.listItems = listItems;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	public void add(AssetEntity item) {
		listItems.add(item);
		this.notifyDataSetChanged();
	}

	public void replace(AssetEntity item) {
		int i = 0;
		boolean bz = false;
		for (AssetEntity vitem : listItems) {
			if (vitem.getAssetCode().equals(item.getAssetCode())) {
				Log.i("", "  " + vitem.getAssetCode() + "----" + item.getAssetCode());
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

	public void addAll(List<AssetEntity> items) {
		listItems.addAll(items);
		this.notifyDataSetChanged();
	}

	public void clear() {
		listItems.clear();
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

	/**
	 * 显示物品详情
	 * 
	 * @param clickID
	 */
	public void showDetailInfo(int clickID) {
		AssetEntity asset = listItems.get(clickID);

		StringBuffer sb = new StringBuffer();
		// if(AppContext.offline==false||isQtybz()||true){
		sb.append("资产编码：")
				.append(StringUtils.isNotBlank(asset.getFinCode()) ? asset.getFinCode() : asset.getAssetCode())
				.append("\n");
		sb.append("资产名称：").append(asset.getAssetName()).append("\n");
		sb.append("规格型号：").append(asset.getSpec()).append("\n");
		sb.append("资产类型：").append(asset.getAssetTypeName()).append("\n");
		sb.append("资产类别：").append(asset.getCateName()).append("\n");
		// sb.append("财务编码：").append(asset.getFinCode()).append("\n");
		sb.append("管理部门：").append(asset.getMgrOrganName()).append("\n");
		sb.append("使用部门：").append(asset.getOrganName()).append("\n");
		sb.append("存放地点：").append(asset.getStorageDescr()).append("\n");
		sb.append("使  用  人：").append(asset.getOperator()).append("\n");
		sb.append("原　　值：").append(asset.getOriginalValue() == null ? "" : asset.getOriginalValue()).append("\n");
		sb.append("使用日期：").append(asset.getEnableDateString()).append("\n");
		sb.append("使用年限：").append(asset.getUseAge()).append("\n");
		sb.append("当前状态：").append(asset.getStatus());
		// }else{
		// sb.append("资产编码：").append(StringUtils.isNotBlank(asset.getFinCode())?asset.getFinCode():asset.getAssetCode()).append("\n");
		// sb.append("资产名称：").append(asset.getAssetName()).append("\n");
		// if(StringUtils.isNotBlank(asset.getSpec())){
		// sb.append("规格型号：").append(asset.getSpec()).append("\n");
		// }
		// if(StringUtils.isNotBlank(asset.getOrganName())){
		// sb.append("使用部门：").append(asset.getOrganName()).append("\n");
		// }
		// if(StringUtils.isNotBlank(asset.getOperator())){
		// sb.append("使 用 人：").append(asset.getOperator()).append("\n");
		// }
		// }

		if (StringUtils.isNotBlank(asset.getDisCodes())) {
			sb.append("不符项目：");
			String[] bfarr = StringUtils.split(asset.getDisCodes(), ",");
			String ff = "";
			for (String string : bfarr) {
				ff += "," + InventoryChgSel.DIS_MAP.get(string);
			}
			if (StringUtils.isNotBlank(ff)) {
				sb.append(ff.substring(1));
			}
			sb.append("\n");
		}
		if (StringUtils.isNotBlank(asset.getInvNote())) {
			sb.append("\n").append("盘点备注：").append(asset.getInvNote());
		}
		Float starnum=asset.getStarNum()==null?null:asset.getStarNum().floatValue();
		if (ToolString.isNoBlankAndNoNull(asset.getImgUrls())&&AppContext.offline == false) { 
 
			final String imgurls = asset.getImgUrls();
			Alert.DisplayAlertAssetInfoDialog(context, 0,
					"物品详情：【" + (StringUtils.isNotBlank(asset.getFinCode()) ? asset.getFinCode() : asset.getAssetCode())
							+ "】" + asset.getAssetName(),starnum,
					sb.toString(), "查看图片", "关闭", 
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							imageBrower(0, imgurls, "");
						}
					},
					null);
		} else {
			Alert.DisplayAlertAssetInfoDialog(context, 0,
					"物品详情：【" + (StringUtils.isNotBlank(asset.getFinCode()) ? asset.getFinCode() : asset.getAssetCode())
							+ "】" + asset.getAssetName(),
					starnum,sb.toString(), "确定",null,null,null);
		}
	}

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.e("method", "getView");
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.list_view_item_type1, null);
			// 获取控件对象
			listItemView.title = (TextView) convertView.findViewById(R.id.titleItem);
			listItemView.info = (TextView) convertView.findViewById(R.id.infoItem);
			listItemView.detail = (Button) convertView.findViewById(R.id.detailItem);
			listItemView.images = (Button) convertView.findViewById(R.id.imagesItem); 
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		AssetEntity asset = listItems.get(position);
		if (null == callback) {
			if (AppContext.offline == false || isQtybz()) {
				listItemView.info.setText("资产名称：" + asset.getAssetName() + "\n资产类别：" + asset.getCateName() + "\n存放地点："
						+ asset.getStorageDescr());
				
				
				final String imgurls = asset.getImgUrls();
				if(ToolString.isNoBlankAndNoNull(asset.getImgUrls())){
					listItemView.images.setVisibility(View.VISIBLE);
					listItemView.images.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 显示物品详情
							imageBrower(0, imgurls, "");
						}
					});
				}else{
					listItemView.images.setVisibility(View.GONE);
				}
			} else {
				listItemView.images.setVisibility(View.GONE);
				listItemView.info.setText("资产名称：" + asset.getAssetName());
			}
			listItemView.title.setText("序号:" + (position + 1) + " 编码:【"
					+ (StringUtils.isNotBlank(asset.getFinCode()) ? asset.getFinCode() : asset.getAssetCode()) + "】");
			// 注册按钮点击
			listItemView.detail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 显示物品详情
					showDetailInfo(selectID);
				}
			});
		} else {
			callback.onInfoRender(position, listItemView.info, asset);
			callback.onTitleRender(position, listItemView.title, asset);
			callback.onAction(position, listItemView.detail, asset);
		}
		return convertView;
	}

	public void setCallback(CallBack callback) {
		this.callback = callback;
	}

	public boolean isQtybz() {
		return qtybz;
	}

	public void setQtybz(boolean qtybz) {
		this.qtybz = qtybz;
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls
	 */
	protected void imageBrower(int position, String urlstr, String filenames) {
		Intent mIntent = new Intent(context, PhotoViewerActivity.class);
		ArrayList<PictureBean> dataSource = new ArrayList<PictureBean>();
		String[] urls = urlstr.split(",");
		String[] fnames = filenames.split(",");
		for (String imageURL : urls) {
			PictureBean image = new PictureBean(PictureBean.FILE_TYPE_NETWORK, "",
					SysConfig.getImageBaseUrl() + imageURL);
			dataSource.add(image);
		}
		mIntent.putExtra(PhotoViewerActivity.PICTURE_VIEWER_DEFAULT_POSTION, position);
		mIntent.putExtra(PhotoViewerActivity.PICTURE_VIEWER_DATASOURCE, dataSource);
		context.startActivity(mIntent);
	}

	public static interface CallBack {

		public String onTitleRender(final int position, View titleView, AssetEntity item);

		public String onInfoRender(final int position, View infoView, AssetEntity item);

		public String onAction(final int position, View btnView, AssetEntity item);

	}
}
