/*
 * $RCSfile: VoteModel.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-11-27  $
 *
 */
package com.rstco.sjpt.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

 

public class CfgModel implements Serializable
{
	/**
	 * 版本号 
	 */
	private static final long serialVersionUID = 1L;
	@DatabaseField(id = true)
	private String cfgId;
	@DatabaseField
	private String userId;
	@DatabaseField
	private String cfgName;
	@DatabaseField
	private String val;
	public String getCfgId() {
		return cfgId;
	}
	public void setCfgId(String cfgId) {
		this.cfgId = cfgId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCfgName() {
		return cfgName;
	}
	public void setCfgName(String cfgName) {
		this.cfgName = cfgName;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}

 
}