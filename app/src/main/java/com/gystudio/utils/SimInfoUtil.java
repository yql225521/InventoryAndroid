package com.gystudio.utils;
 
import java.util.HashMap;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;

public class SimInfoUtil {
	public static final String CALLSTATE = "callState";// 电话状态
	public static final String CALLLOCATION = "callLocation";// 终端的位置;
	public static final String DATAACTIVITY = "dataActicity";// 数据活动状态;
	public static final String DATASTATE = "dataState";// 数据连接状态;
	public static final String DEVICEID = "deviceId";// 终端的唯一标识;
	public static final String DEVICESOFTWAREVERSION = "deviceSoftWareVersion";// 终端的软件版本;
	public static final String LINE1NUMBER = "line1Number";// 手机号码;
	public static final String NETWORKCOUNTRYISO = "networkCountryIso";// 国际长途区号;
	public static final String NETWORKOPERATOR = "entworkOperator";// 营商国家代码和运营商网络代码;
	public static final String NETWORKOPERATORNAME = "entworkOperatorName";// 运营商的名字;
	public static final String NETWORKTYPE = "networkType";// 网路类型;
	public static final String PHONETYPE = "phoneType";// 移动终端的类型;
	public static final String SIMCOUNTRYISO = "SimCountryIso";// SIM卡区域
	public static final String SIMOPERATOR = "SimOperator";// SIM卡供货商代码
	public static final String SIMOPERATORNAME = "SimOperatorName";// SIM卡供货商名称
	public static final String SIMSERIALNUMBER = "SimSerialNumber";// SIM卡的序列号
	public static final String SIMSTATE = "SimState";// SIM卡状态
	public static final String SUBSCRIBERID = "SubscriberId";// 用户唯一标识
	public static final String VOICEMAILALPHATAG = "VoiceMailAlphaTag";// 语音信箱号码关联的字母标识
	public static final String VOICEMAILNUMBER = "VoiceMailNumber";// 语音邮件号码
	public static final String ICCCARD = "IccCard";// ICC卡是否存在
	public static final String NETWORKROAMING = "NetworkRoaming";// ICC卡是否存在

	public static HashMap<String, String> getSimInfo(Context mContext) {
		// 获得TelephonyManager对象
		TelephonyManager telMgr = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		HashMap<String, String> map = new HashMap<String, String>();

		/**
		 * 获得电话状态 CALL_STATE_IDLE = 0 闲置状态时 CALL_STATE_OFFHOOK = 1 接起电话时
		 * CALL_STATE_RINGING = 2 电话进来时
		 */
		int callState = telMgr.getCallState();
		switch (callState) {
		case TelephonyManager.CALL_STATE_IDLE:
			map.put(CALLSTATE, "闲置何状态");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			map.put(CALLSTATE, "接起电话");
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			map.put(CALLSTATE, "电话进来");
		}

		// 获得当前移动终端的位置
		// 需要添加permission为ACCESS_COARSE_LOCATION。
		CellLocation location = telMgr.getCellLocation();
		if (location != null) {
			// 如果location不为空，location.toString()的为[0,0]到[0xffff ,0xffff]之间的一组。
			// 第一个值是cell id（小区识别码 ）
			map.put(CALLLOCATION, location.toString());
		} else {
			map.put(CALLLOCATION, "无法取得");
		}
		// 请求位置更新，如果更新将产生广播，接收对象为注册LISTEN_CELL_LOCATION的对象
		CellLocation.requestLocationUpdate();

		/**
		 * 获取数据活动状态 DATA_ACTIVITY_IN 数据连接状态：活动，正在接受数据 DATA_ACTIVITY_OUT
		 * 数据连接状态：活动，正在发送数据 DATA_ACTIVITY_INOUT 数据连接状态：活动，正在接受和发送数据
		 * DATA_ACTIVITY_NONE 数据连接状态：活动，但无数据发送和接受 DATA_ACTIVITY_DORMANT
		 * 数据连接状态：不活动
		 */
		int dataActivity = telMgr.getDataActivity();
		switch (dataActivity) {
		case TelephonyManager.DATA_ACTIVITY_IN:
			map.put(DATAACTIVITY, "活动，正在接受数据");
			break;
		case TelephonyManager.DATA_ACTIVITY_OUT:
			map.put(DATAACTIVITY, "活动，正在发送数据");
			break;
		case TelephonyManager.DATA_ACTIVITY_INOUT:
			map.put(DATAACTIVITY, "活动，正在接受和发送数据");
			break;
		case TelephonyManager.DATA_ACTIVITY_NONE:
			map.put(DATAACTIVITY, "活动，但无数据发送和接受");
			break;
		case TelephonyManager.DATA_ACTIVITY_DORMANT:
			map.put(DATAACTIVITY, "不活动");
		}

		/**
		 * 获取数据连接状态 DATA_CONNECTED 数据连接状态：已连接 DATA_CONNECTING 数据连接状态：正在连接
		 * DATA_DISCONNECTED 数据连接状态：断开 DATA_SUSPENDED 数据连接状态：暂停
		 */
		int dataState = telMgr.getDataState();
		switch (dataState) {
		case TelephonyManager.DATA_CONNECTED:
			map.put(DATASTATE, "已连接");
			break;
		case TelephonyManager.DATA_CONNECTING:
			map.put(DATASTATE, "正在连接");
			break;
		case TelephonyManager.DATA_DISCONNECTED:
			map.put(DATASTATE, "断开");
			break;
		case TelephonyManager.DATA_SUSPENDED:
			map.put(DATASTATE, "暂停");
		}

		/**
		 * 获取当前移动终端的唯一标识
		 * 如果是GSM网络，返回IMEI(国际移动设备身份码)；如果是CDMA网络，返回MEID(全球唯一的56bit移动终端标识号)
		 */
		if (telMgr.getDeviceId() != null
				&& !telMgr.getDeviceId().trim().equals("")) {
			map.put(DEVICEID, telMgr.getDeviceId());
		} else {
			map.put(DEVICEID, "无法取得");
		}

		/*
		 * 返回移动终端的软件版本，例如：GSM手机的IMEI/SV码。 移动网络码，共2位 中国移动TD系统使用00 中国联通GSM系统使用01
		 * 中国移动GSM系统使用02 中国电信CDMA系统使用03
		 */
		if (telMgr.getDeviceSoftwareVersion() != null
				&& !telMgr.getDeviceSoftwareVersion().trim().equals("")) {
			map.put(DEVICESOFTWAREVERSION, telMgr.getDeviceSoftwareVersion());
		} else {
			map.put(DEVICESOFTWAREVERSION, "无法取得");
		}

		// 返回手机号码，对于GSM网络来说即MSISDN(移动用户国际号码)
		if (telMgr.getLine1Number() != null
				&& !telMgr.getLine1Number().trim().equals("")) {
			map.put(LINE1NUMBER, telMgr.getLine1Number());
		} else {
			map.put(LINE1NUMBER, "无法获得");
		}

		// 返回当前移动终端附近移动终端的信息
		// 需要权限：android.Manifest.permission#ACCESS_COARSE_UPDATES
		/*
		 * List<NeighboringCellInfo> infos=telMgr.getNeighboringCellInfo();
		 * for(NeighboringCellInfo info:infos){ //获取邻居小区号
		 * item.add(getResources().getText(R.string.str_list7).toString());
		 * value.add(String.valueOf(info.getCid())); //获取邻居小区LAC，LAC:
		 * 位置区域码。为了确定移动台的位置， //每个GSM/PLMN的覆盖区都被划分成许多位置区，LAC则用于标识不同的位置区。
		 * item.add(getResources().getText(R.string.str_list8).toString());
		 * value.add(String.valueOf(info.getLac())); //获得网络类型
		 * item.add(getResources().getText(R.string.str_list9).toString());
		 * value.add(String.valueOf(info.getNetworkType())); //获得Psc
		 * item.add(getResources().getText(R.string.str_list10).toString());
		 * value.add(String.valueOf(info.getPsc())); //获取邻居小区信号强度
		 * item.add(getResources().getText(R.string.str_list11).toString());
		 * value.add(String.valueOf(info.getRssi())); }
		 */

		/**
		 * 获取ISO标准的国家码，即国际长途区号。 注意：仅当用户已在网络注册后有效。 在CDMA网络中结果也许不可靠。
		 */
		if (telMgr.getNetworkCountryIso() != null
				&& !telMgr.getNetworkCountryIso().trim().equals("")) {
			map.put(NETWORKCOUNTRYISO, telMgr.getNetworkCountryIso());
		} else {
			map.put(NETWORKCOUNTRYISO, "无法取得");
		}

		// 获取MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
		if (telMgr.getNetworkOperator() != null
				&& !telMgr.getNetworkOperator().trim().equals("")) {
			map.put(NETWORKOPERATOR, telMgr.getNetworkOperator());
		} else {
			map.put(NETWORKOPERATOR, "无法取得");
		}

		// 获取移动网络运营商的名字(SPN)
		if (telMgr.getNetworkOperatorName() != null
				&& !telMgr.getNetworkOperatorName().trim().equals("")) {
			map.put(NETWORKOPERATORNAME, telMgr.getNetworkOperatorName());
		} else {
			map.put(NETWORKOPERATORNAME, "无法取得");
		}

		/**
		 * 获取网络类型 NETWORK_TYPE_CDMA 网络类型为CDMA NETWORK_TYPE_EDGE 网络类型为EDGE
		 * NETWORK_TYPE_EVDO_0 网络类型为EVDO0 NETWORK_TYPE_EVDO_A 网络类型为EVDOA
		 * NETWORK_TYPE_GPRS 网络类型为GPRS NETWORK_TYPE_HSDPA 网络类型为HSDPA
		 * NETWORK_TYPE_HSPA 网络类型为HSPA NETWORK_TYPE_HSUPA 网络类型为HSUPA
		 * NETWORK_TYPE_UMTS 网络类型为UMTS
		 * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
		 */
		int networkType = telMgr.getNetworkType();
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_CDMA:
			map.put(NETWORKTYPE, "CDMA");
			break;
		case TelephonyManager.NETWORK_TYPE_EDGE:
			map.put(NETWORKTYPE, "EDGE");
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			map.put(NETWORKTYPE, "EVDO0");
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			map.put(NETWORKTYPE, "EVDOA");
			break;
		case TelephonyManager.NETWORK_TYPE_GPRS:
			map.put(NETWORKTYPE, "GPRS");
			break;
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			map.put(NETWORKTYPE, "HSDPA");
			break;
		case TelephonyManager.NETWORK_TYPE_HSPA:
			map.put(NETWORKTYPE, "HSPA");
			break;
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			map.put(NETWORKTYPE, "HSUPA");
			break;
		case TelephonyManager.NETWORK_TYPE_UMTS:
			map.put(NETWORKTYPE, "UMTS");
			break;
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			map.put(NETWORKTYPE, "未知");
		}

		/**
		 * 返回移动终端的类型 PHONE_TYPE_CDMA 手机制式为CDMA，电信 PHONE_TYPE_GSM 手机制式为GSM，移动和联通
		 * PHONE_TYPE_NONE 手机制式未知
		 */
		int phoneType = telMgr.getPhoneType();
		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			map.put(PHONETYPE, "电信");
			break;
		case TelephonyManager.PHONE_TYPE_GSM:
			map.put(PHONETYPE, "移动/联通");
			break;
		case TelephonyManager.PHONE_TYPE_NONE:
			map.put(PHONETYPE, "手机制式未知");
		}

		/* 取得SIM卡区域 */
		if (telMgr.getSimCountryIso() != null
				&& !telMgr.getSimCountryIso().trim().equals("")) {
			map.put(SIMCOUNTRYISO, telMgr.getSimCountryIso());
		} else {
			map.put(SIMCOUNTRYISO, "无法取得");
		}

		/* 取得SIM卡供货商代码 */
		if (telMgr.getSimOperator() != null
				&& !telMgr.getSimOperator().trim().equals("")) {
			map.put(SIMOPERATOR, telMgr.getSimOperator());
		} else {
			map.put(SIMOPERATOR, "无法取得");
		}

		/**
		 * 取得SIM卡供货商名称： 例如：中国移动、联通 SIM卡的状态必须是
		 * SIM_STATE_READY(使用getSimState()判断).
		 */
		if (telMgr.getSimState() == TelephonyManager.SIM_STATE_READY) {
			if (telMgr.getSimOperatorName() != null
					&& !telMgr.getSimOperatorName().trim().equals("")) {
				map.put(SIMOPERATORNAME, telMgr.getSimOperatorName());
			} else {
				map.put(SIMOPERATORNAME, "无法取得ss");
			}
		} else {
			map.put(SIMOPERATORNAME, "无法取得");
		}

		/**
		 * 返回SIM卡的序列号(IMEI)
		 */
		if (telMgr.getSimSerialNumber() != null
				&& !telMgr.getSimSerialNumber().trim().equals("")) {
			map.put(SIMSERIALNUMBER, telMgr.getSimSerialNumber());
		} else {
			map.put(SIMSERIALNUMBER, "无法取得");
		}

		/**
		 * 返回SIM卡状态 SIM_STATE_ABSENT SIM卡未找到 SIM_STATE_NETWORK_LOCKED
		 * SIM卡网络被锁定，需要Network PIN解锁 SIM_STATE_PIN_REQUIRED SIM卡PIN被锁定，需要User
		 * PIN解锁 SIM_STATE_PUK_REQUIRED SIM卡PUK被锁定，需要User PUK解锁 SIM_STATE_READY
		 * SIM卡可用 SIM_STATE_UNKNOWN SIM卡未知
		 */
		if (telMgr.getSimState() == TelephonyManager.SIM_STATE_READY) {
			map.put(SIMSTATE, "良好");
		} else if (telMgr.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
			map.put(SIMSTATE, "无SIM卡");
		} else {
			map.put(SIMSTATE, "SIM卡被锁定或未知的状态");
		}

		// 获取用户唯一标识，比如GSM网络的IMSI编号
		if (telMgr.getSubscriberId() != null
				&& !telMgr.getSubscriberId().trim().equals("")) {
			map.put(SUBSCRIBERID, telMgr.getSubscriberId());
		} else {
			map.put(SUBSCRIBERID, "无法取得");
		}

		// 获取语音信箱号码关联的字母标识。
		if (telMgr.getVoiceMailAlphaTag() != null
				&& !telMgr.getVoiceMailAlphaTag().trim().equals("")) {
			map.put(VOICEMAILALPHATAG, telMgr.getVoiceMailAlphaTag());
		} else {
			map.put(VOICEMAILALPHATAG, "无法取得");
		}

		// 获取语音邮件号码
		if (telMgr.getVoiceMailNumber() != null
				&& !telMgr.getVoiceMailNumber().trim().equals("")) {
			map.put(VOICEMAILNUMBER, telMgr.getVoiceMailNumber());
		} else {
			map.put(VOICEMAILNUMBER, "无法取得");
		}

		// ICC卡是否存在
		if (telMgr.hasIccCard()) {
			map.put(ICCCARD, "是");
		} else {
			map.put(ICCCARD, "否");
		}

		// 返回手机是否处于漫游状态
		if (telMgr.isNetworkRoaming()) {
			map.put(NETWORKROAMING, "是");
		} else {
			map.put(NETWORKROAMING, "否");
		}
		return null;
	}

}
