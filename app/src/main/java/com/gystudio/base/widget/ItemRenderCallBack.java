/**
 * 文件名：ItemRenderCallBack.java
 *
 * 版本信息：
 * 日期：2014-6-15
 * Copyright 高原工作室  Corporation 2014 
 * 版权所有
 *
 */
package com.gystudio.base.widget;

/**
 * 
 * 项目名称：baseadr
 * 类名称：ItemRenderCallBack
 * 类描述：
 * 创建人：yuanbf
 * 创建时间：2014-6-15 下午6:28:00
 * @version 
 * 
 */
public interface ItemRenderCallBack<T> {
	
	public String onTitleRender(T item);
	
	public String onInfoRender(T item);
	
	public String onAction(T item);

}
