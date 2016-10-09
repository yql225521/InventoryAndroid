/**
 * Project Name:baseadr
 * File Name:UploadBean.java
 * Package Name:com.gystudio.base.entity
 * Date:2016年7月10日下午7:42:46
 * Copyright (c) 2016, ybf_news@163.com All Rights Reserved.
 *
*/

package com.gystudio.base.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * ClassName:UploadBean <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年7月10日 下午7:42:46 <br/>
 * @author   yuanbaofu
 * @version   
 * @see 	 
 */
public class UploadBean implements Serializable {
	
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 */
	private static final long serialVersionUID = 1L;
	private String filepath;
	private Object obj; 
	private Map<String, String> param;
	
	public UploadBean() { 
		super();  
	}
	public UploadBean(String filepath, Map<String, String> param,Object obj) {
		super();
		this.setFilepath(filepath);
		this.setParam(param); 
		this.setObj(obj);
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public Map<String, String> getParam() {
		return param;
	}
	public void setParam(Map<String, String> param) {
		this.param = param;
	} 
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}  
}

