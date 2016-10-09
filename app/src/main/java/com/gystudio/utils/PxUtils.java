/**
 * 文件名：PxUtils.java
 *
 * 版本信息：
 * 日期：2014-7-15
 * Copyright 高原工作室  Corporation 2014 
 * 版权所有
 *
 */
package com.gystudio.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 
 * 项目名称：baseadr
 * 类名称：PxUtils
 * 类描述：
 * 创建人：yuanbf
 * 创建时间：2014-7-15 下午4:08:26
 * @version 
 * 
 */
public class PxUtils {


	/**
	 * 获取当前分辨率下指定单位对应的像素大小（根据设备信息）
	 * px,dip,sp -> px
	 *
	 * Paint.setTextSize()单位为px
	 *
	 * 代码摘自：TextView.setTextSize()
	 *
	 * @param unit  TypedValue.COMPLEX_UNIT_*
	 * @param size
	 * @return
	 */
	public static float getRawSize(Context c,int unit, float size) {

		Resources r;

		if (c == null)
			r = Resources.getSystem();
		else
			r = c.getResources();

		return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
