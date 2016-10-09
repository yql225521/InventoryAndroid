/*
 * $RCSfile: PubDictEntity.java,v $$
 * $Revision: 1.1  $
 * $Date: 2012-02-26  $
 *
 * 
 *
 * 
 * Use is subject to license terms.
 */
package com.rstco.sjpt.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rstco.sjpt.model.PubDictModel;

 


/**
 * <p>Title: PubDictEntity</p>
 * <p>Table: pub_dict</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @See com.rstco.platform.model.PubDictModel
 * @author CodeGenerator
 * @version V1.0
 */
@DatabaseTable(tableName = "pub_dict")
public class PubDictEntity extends PubDictModel
{
	/**
     *
	 */
	private static final long serialVersionUID = 1L;
	
	private String parentLayer;
	
	private String parentLevelNum;
	@DatabaseField
	private String userId;
	@DatabaseField(id = true)
	private String pid;

	@Override
	public String toString() {
		return this.getName();
	}
 
	public String getParentLayer() {
		return parentLayer;
	}

	public void setParentLayer(String parentLayer) {
		this.parentLayer = parentLayer;
	}
 
	public String getParentLevelNum() {
		return parentLevelNum;
	}

	public void setParentLevelNum(String parentLevelNum) {
		this.parentLevelNum = parentLevelNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	
}