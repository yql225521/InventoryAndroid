
package com.rstco.sjpt.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rstco.sjpt.model.PubOrganModel;
 
/**
 * <p>Title: PubOrganEntity</p>
 * <p>Table: pub_organ</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @See com.rstco.platform.model.PubOrganModel
 * @author CodeGenerator
 * @version V1.0
 */
@DatabaseTable(tableName = "pub_organ")
public class PubOrganEntity extends PubOrganModel
{
	/**
     *
	 */
	private static final long serialVersionUID = 1L;
	
	private String organIDParentName;
	@DatabaseField
	private String userId;
	@DatabaseField(id = true)
	private String pid;
	
	private Double num;
 
	public PubOrganEntity() {
		super();
	}
	
	

	public PubOrganEntity(String organID,String organCode,String organName,Double num) {
		super();
		this.num = num;
		this.setOrganCode(organCode);
		this.setOrganID(organID);
		this.setOrganName(organName);
	}



	public String getOrganIDParentName() {
		return organIDParentName;
	}

	public void setOrganIDParentName(String organIDParentName) {
		this.organIDParentName = organIDParentName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getOrganName();
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

	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}
}