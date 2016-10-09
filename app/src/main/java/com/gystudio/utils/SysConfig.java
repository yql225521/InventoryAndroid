package com.gystudio.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.gystudio.base.R;

import android.content.Context;


public class SysConfig {
	private static SysConfig m_intance = null;
	private static String ServiceUrl = null;
	private static String imageBaseUrl=null;
	private static String imgServiceUrl=null;
	private static String verbz = null;
	private static String LocalAdm = null;
	private static String NameSpace = null;
	private static String AutoUpdateUrl = null;
	private static String ModelID = null;
	private static Context _context = null;
	private static String areaID = null;

	private SysConfig() {
	}

	/**
	 * 回车换行
	 */
	public static String LFCR = "\n";

	synchronized public static SysConfig getInstance(Context c) {
		if (m_intance == null) {
			m_intance = new SysConfig();
			_context = c;
		}
		return m_intance;
	}

	/**
	 * 设置服务地址
	 * 
	 * @param serviceUrl
	 */
	public static void setServiceUrl(String serviceUrl) {
		ServiceUrl = serviceUrl;
		PreferencesUtil pu = new PreferencesUtil(_context);
		pu.Write("SysConfig", "ServiceUrl", serviceUrl);
	}
	
	
	public static String[] getCodes(String barcode) { 
		if (!"".equals(barcode)) {
			if(StringUtils.indexOf(barcode, "\n")>-1){//晋中标签问题
				 String[]  tas=barcode.split("\n"); 
				 if(tas.length>=2&&"晋中市烟草公司".equals(tas[0])) {
					 List<String> tlst=new ArrayList(); 
					 tlst.add(tas[2]);//0
					 if(tas.length>3){
						 tlst.add(tas[3]);//1
					 }else{
						 tlst.add("");//1
					 }
					 if(tas.length>4){
						 tlst.add(tas[4]);//1
					 }else{
						 tlst.add("");//1
					 } 
					 tlst.add(tas[1]); 
					 System.out.println("asset:"+StringUtils.join(tlst,","));
					 return tlst.toArray(new String[tlst.size()]);
				 }
				 return tas;
			}else{
				return barcode.split("\t"); 
			} 
		} 
		
		return null;
	}

	/**
	 * 获取服务地址
	 * 
	 * @return
	 */
	public static String getServiceUrl() {
		//PreferencesUtil pu = new PreferencesUtil(_context);
		//ServiceUrl = pu.Read("SysConfig", "ServiceUrl");
		//if (ServiceUrl.equals("")) {
			PropertieUtil p = new PropertieUtil(_context.getResources()
					.openRawResource(R.raw.config));
			ServiceUrl = p.Read("ServiceUrl");
		//	setServiceUrl(ServiceUrl);
		//}
		return ServiceUrl;
	}
	
	/**
	 * 获取服务地址
	 * 
	 * @return
	 */
	public static String getImageBaseUrl() {
		//PreferencesUtil pu = new PreferencesUtil(_context);
		//ServiceUrl = pu.Read("SysConfig", "ServiceUrl");
		//if (ServiceUrl.equals("")) {
			PropertieUtil p = new PropertieUtil(_context.getResources()
					.openRawResource(R.raw.config));
			imageBaseUrl = p.Read("ImgBaseUrl");
		//	setServiceUrl(ServiceUrl);
		//}
		return imageBaseUrl;
	}
	
	/**
	 * 获取服务地址
	 * 
	 * @return
	 */
	public static String getUploadImageUrl() {
		//PreferencesUtil pu = new PreferencesUtil(_context);
		//ServiceUrl = pu.Read("SysConfig", "ServiceUrl");
		//if (ServiceUrl.equals("")) {
			PropertieUtil p = new PropertieUtil(_context.getResources()
					.openRawResource(R.raw.config));
			imgServiceUrl = p.Read("ImgServiceUrl");
		//	setServiceUrl(ServiceUrl);
		//}
		return imgServiceUrl;
	}
	
	public static String getVerBz() {
		PropertieUtil p = new PropertieUtil(_context.getResources()
				.openRawResource(R.raw.config));
		verbz = p.Read("verbz");
	//	setServiceUrl(ServiceUrl);
	//}
	return verbz;
	}
	

	/**
	 * 设置机构代码
	 * 
	 * @param orginID
	 */
	public static void setLocalAdm(String localAdm) {
		LocalAdm = localAdm;
		PreferencesUtil pu = new PreferencesUtil(_context);
		pu.Write("SysConfig", "LocalAdm", localAdm);
	}

	/**
	 * 获取机构代码
	 * 
	 * @return
	 */
	public static String getLocalAdm() {
		PreferencesUtil pu = new PreferencesUtil(_context);
		LocalAdm = pu.Read("SysConfig", "LocalAdm");
		if (LocalAdm.equals("")) {
			PropertieUtil p = new PropertieUtil(_context.getResources()
					.openRawResource(R.raw.config));
			LocalAdm = p.Read("LocalAdm");
			setLocalAdm(LocalAdm);
		}
		return LocalAdm;
	}

	/**
	 * 设置服务命名空间
	 * 
	 * @param nameSpace
	 */
	public static void setNameSpace(String nameSpace) {
		NameSpace = nameSpace;
		PreferencesUtil pu = new PreferencesUtil(_context);
		pu.Write("SysConfig", "NameSpace", nameSpace);
	}

	/**
	 * 获取命名空间
	 * 
	 * @return
	 */
	public static String getNameSpace() {
		PreferencesUtil pu = new PreferencesUtil(_context);
		NameSpace = pu.Read("SysConfig", "NameSpace");
		if (NameSpace.equals("")) {
			PropertieUtil p = new PropertieUtil(_context.getResources()
					.openRawResource(R.raw.config));
			NameSpace = p.Read("NameSpace");
			setNameSpace(NameSpace);
		}
		return NameSpace;
	}

	/**
	 * @param autoUpdateUrl
	 *            the autoUpdateUrl to set
	 */
	public static void setAutoUpdateUrl(String autoUpdateUrl) {
		AutoUpdateUrl = autoUpdateUrl;
		PreferencesUtil pu = new PreferencesUtil(_context);
		pu.Write("SysConfig", "AutoUpdateUrl", autoUpdateUrl);
	}

	/**
	 * @return the autoUpdateUrl
	 */
	public static String getAutoUpdateUrl() {
		//PreferencesUtil pu = new PreferencesUtil(_context);
		//AutoUpdateUrl = pu.Read("SysConfig", "AutoUpdateUrl");
		//if (AutoUpdateUrl.equals("")) {
			PropertieUtil p = new PropertieUtil(_context.getResources()
					.openRawResource(R.raw.config));
			AutoUpdateUrl = p.Read("AutoUpdateUrl");
		//	setAutoUpdateUrl(AutoUpdateUrl);
		//}
		return AutoUpdateUrl;
	}

	/**
	 * @param modelID
	 *            the modelID to set
	 */
	public static void setModelID(String modelID) {
		ModelID = modelID;
		PreferencesUtil pu = new PreferencesUtil(_context);
		pu.Write("SysConfig", "ModelID", modelID);
	}

	/**
	 * @return the modelID
	 */
	public static String getModelID() {
		PreferencesUtil pu = new PreferencesUtil(_context);
		ModelID = pu.Read("SysConfig", "ModelID");
		if (ModelID.equals("")) {
			PropertieUtil p = new PropertieUtil(_context.getResources()
					.openRawResource(R.raw.config));
			ModelID = p.Read("ModelID");
			setModelID(ModelID);
		}
		return ModelID;
	}
	
	/**
	 * 获得组织机构ID
	 * @return
	 */
	public static String getAreaID(){
		PreferencesUtil pu = new PreferencesUtil(_context);
		areaID= "";
				//pu.Read(AppSettings.APPSETTINGGROUPNAME, AppSettings.USER_ORGAN_KEY);
		return areaID;
	}
}
