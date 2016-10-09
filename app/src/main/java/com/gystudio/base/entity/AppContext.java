package com.gystudio.base.entity;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gystudio.utils.ContextUtil;
import com.rstco.sjpt.entity.PubOrganEntity;
import com.rstco.sjpt.model.PubUser;

import android.content.Context;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class AppContext {

	public static PubUser currUser=null;
	public static PubOrganEntity currOrgan=null;
	private static LocationClient mLocationClient = null;
	private static double Latitude;
	private static double Longitude;

	/***屏幕显示材质**/
	private static final DisplayMetrics mDisplayMetrics = new DisplayMetrics();
	private static Context context=ContextUtil.getInstance();
	private static String address="none";
	public static String tel;
	public static String simId;
	public static Boolean offline=false;
	public static Boolean hasnet=false;
	
	/***屏幕宽度**/
	public static final int SCREEN_WIDTH = getDisplayMetrics().widthPixels;
	
	/***屏幕高度**/
	public static final int SCREEN_HEIGHT = getDisplayMetrics().heightPixels;
	
	
	private static GyLocationListenner myListener = new GyLocationListenner();
	
	public static String getSimId(){
		return  StringUtils.isNotBlank(AppContext.simId)?AppContext.simId:"no_sim_id";
	}
	public static String getAddr(){
		return StringUtils.isNotBlank(AppContext.getAddress())?AppContext.getAddress():"no_address";
	}
	/**获取系统显示材质***/
	public static DisplayMetrics getDisplayMetrics(){
		  System.out.println("context===>"+context);
		  WindowManager windowMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		  windowMgr.getDefaultDisplay().getMetrics(mDisplayMetrics);
		  return mDisplayMetrics;
	}
	
	/**获取摄像头支持的分辨率***/
	public static List<Camera.Size> getSupportedPreviewSizes(Camera camera){
        Camera.Parameters parameters = camera.getParameters(); 
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        return sizeList;
	}
	public static void setLocationOption(Context ct){
		context=ct;
		if(mLocationClient==null){
			mLocationClient = new LocationClient(context);
			mLocationClient.registerLocationListener( myListener );
			mLocationClient.start();
		}
		LocationClientOption option = new LocationClientOption();
		option.setServiceName("com.baidu.location.service_v2.6");

		// 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。  
		option.setAddrType("all");  
		// 设置是否返回POI的电话和地址等详细信息。默认值为false，即不返回POI的电话和地址信息。   
		option.setPoiExtraInfo(true);  

		// 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。   
		option.setProdName("通过GPS定位我当前的位置");  

		// 设置GPS，使用gps前提是用户硬件打开gps。默认是不打开gps的。   
		//option.setOpenGps(true);  

		// 定位的时间间隔，单位：ms  
		// 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。  
		option.setScanSpan(180000);  

		// 查询范围，默认值为500，即以当前定位位置为中心的半径大小。  
		option.setPoiDistance(500);  
		// 禁用启用缓存定位数据  
		option.disableCache(true);  
		// 坐标系类型，百度手机地图对外接口中的坐标系默认是bd09ll  
		option.setCoorType("bd09ll");  
		// 设置最多可返回的POI个数，默认值为3。由于POI查询比较耗费流量，设置最多返回的POI个数，以便节省流量。  
		option.setPoiNumber(3);  
		// 设置定位方式的优先级。  
		// 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。  
		option.setPriority(LocationClientOption.NetWorkFirst); 
		option.setPoiExtraInfo(true);
		mLocationClient.setLocOption(option);
	}

 
	/**
	 * 监听函数，更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	private static class GyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			if(location.getAddrStr()==null){
				mLocationClient.requestLocation();
			}

			Latitude=location.getLatitude();
			Longitude=location.getLongitude();
			String jw=getLatitude()+","+getLongitude();
			if (location.getLocType() == BDLocation.TypeGpsLocation){

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

				address=location.getAddrStr();
			}

			//Alert.DisplayProgressDialog(AssetInventory.this, "", " 请求当前位置："+jw+" 地址"+location.getAddrStr());
			// Toast.makeText(context, " 当前位置："+jw+" ,地址:"+location.getAddrStr(), Toast.LENGTH_LONG).show();
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ; 
			}
			if(poiLocation.hasAddr()){  
				// 获取反地理编码。 只有使用网络定位的情况下，才能获取当前位置的反地理编码描述。  
				address=poiLocation.getAddrStr();   
			}  
		}
	}
	public static double getLongitude() {
		return Longitude;
	}
	public static double getLatitude() {
		return Latitude;
	}
 
	public static String getAddress() {
		return address;
	}
	/**
	 *  权限信息
	 */
    //public List<>
}
