/*
 * $RCSfile: VoteEntity.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-11-27  $
 *
 */
package com.rstco.sjpt.entity;
 
import com.j256.ormlite.table.DatabaseTable;
import com.rstco.sjpt.model.PhotoModel;

@DatabaseTable(tableName = "photo")
public class PhotoEntity extends PhotoModel
{
 
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "PhotoEntity [getId()=" + getId() + ", getAssetId()=" + getAssetId() + ", getXh()=" + getXh()
				+ ", getPath()=" + getPath() + ", getType()=" + getType() + ", getAssetCode()=" + getAssetCode()
				+ ", getAssetName()=" + getAssetName() + ", getOrganName()=" + getOrganName() + ", getUserId()="
				+ getUserId() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}