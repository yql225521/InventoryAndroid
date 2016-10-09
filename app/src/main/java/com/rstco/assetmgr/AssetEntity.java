/*
 * $RCSfile: AssetEntity.java,v $$
 * $Revision: 1.1  $
 * $Date: 2014-04-07  $
 *
 */
package com.rstco.assetmgr;

import org.apache.commons.lang.StringUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
 
/**
 * <p>Title: AssetEntity</p>
 * <p>Table: ASSET</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @See com.rstco.ams.model.AssetModel
 * @author CodeGenerator
 * @version V1.0
 */
@DatabaseTable(tableName = "asset")
public class AssetEntity extends AssetModel 
{
	/**
     *
	 */
	private static final long serialVersionUID = 1L;
	@DatabaseField
	private String assetTypeName="";
	@DatabaseField
	private String cateName="";
	@DatabaseField
	private String useAge="";
	@DatabaseField
	private String organCode="";
	@DatabaseField
	private String mgrOrganCode="";
	@DatabaseField
	private String inventoryInfo="";
	@DatabaseField
	private String enableDateString="";
	@DatabaseField
	private String userId="";
	@DatabaseField
	private String simId="";
	@DatabaseField
	private String pdate="";
	@DatabaseField
	private String addr="";
	@DatabaseField
	private String disCodes="";
	@DatabaseField
	private String dt="";
	@DatabaseField(id = true)
	private String pid="";
	@DatabaseField
	private String invNote;
	/**
	 * 用做上传标记id
	 */
	@DatabaseField
	private String upid;
	/**
	 * pdfs 1 二维码盘点 2 手工盘点
	 */
	@DatabaseField
	private String pdfs;
	
	private String imgUrls;
	
	
	public String getAssetTypeName() {
		if(StringUtils.isBlank(assetTypeName)){
			return "";
		}else
		return assetTypeName;
	}

	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}

	public String getCateName() {
		if(StringUtils.isBlank(cateName)){
			return "";
		}else
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getUseAge() {
		if(StringUtils.isBlank(useAge)){
			return "";
		}else
		return useAge;
	}

	public void setUseAge(String useAge) {
		this.useAge = useAge;
	}

	public String getInventoryInfo() {
		return inventoryInfo;
	}

	public void setInventoryInfo(String inventoryInfo) {
		this.inventoryInfo = inventoryInfo;
	}

	public String getEnableDateString() {
		return enableDateString;
	}

	public void setEnableDateString(String enableDateString) {
		this.enableDateString = enableDateString;
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

	public String getOrganCode() {
		return organCode;
	}

	public void setOrganCode(String organCode) {
		this.organCode = organCode;
	}

	public String getMgrOrganCode() {
		return mgrOrganCode;
	}

	public void setMgrOrganCode(String mgrOrganCode) {
		this.mgrOrganCode = mgrOrganCode;
	}

	public String getSimId() {
		return simId;
	}

	public void setSimId(String simId) {
		this.simId = simId;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPdate() {
		return pdate;
	}

	public void setPdate(String pdate) {
		this.pdate = pdate;
	}

	public String getUpid() {
		return upid;
	}

	public void setUpid(String upid) {
		this.upid = upid;
	}

	public String getPdfs() {
		return pdfs;
	}

	public void setPdfs(String pdfs) {
		this.pdfs = pdfs;
	}

	public String getDisCodes() {
		return disCodes;
	}

	public void setDisCodes(String disCodes) {
		this.disCodes = disCodes;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	 

	public String getInvNote() {
		return invNote;
	}

	public void setInvNote(String invNote) {
		this.invNote = invNote;
	}

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	@Override
	public String toString() {
		return "AssetEntity [assetTypeName=" + assetTypeName + ", cateName=" + cateName + ", useAge=" + useAge
				+ ", organCode=" + organCode + ", mgrOrganCode=" + mgrOrganCode + ", inventoryInfo=" + inventoryInfo
				+ ", enableDateString=" + enableDateString + ", userId=" + userId + ", simId=" + simId + ", pdate="
				+ pdate + ", addr=" + addr + ", disCodes=" + disCodes + ", dt=" + dt + ", pid=" + pid + ", invNote="
				+ invNote + ", upid=" + upid + ", pdfs=" + pdfs + ", imgUrls=" + imgUrls + ", getAssetID()="
				+ getAssetID() + ", getAssetName()=" + getAssetName() + ", getAssetCode()=" + getAssetCode()
				+ ", getSeq()=" + getSeq() + ", getSpec()=" + getSpec() + ", getUnit()=" + getUnit()
				+ ", getMgrOrganName()=" + getMgrOrganName() + ", getCateID()=" + getCateID() + ", getMgrOrganID()="
				+ getMgrOrganID() + ", getAssetType()=" + getAssetType() + ", getStatus()=" + getStatus()
				+ ", getBuyDate()=" + getBuyDate() + ", getLiablePerson()=" + getLiablePerson() + ", getOperator()="
				+ getOperator() + ", getOrganID()=" + getOrganID() + ", getOrganName()=" + getOrganName()
				+ ", getCreatedBy()=" + getCreatedBy() + ", getCreationDate()=" + getCreationDate()
				+ ", getLastUpdateBy()=" + getLastUpdateBy() + ", getLastUpdateDate()=" + getLastUpdateDate()
				+ ", getOriginalValue()=" + getOriginalValue() + ", getDelFlag()=" + getDelFlag()
				+ ", getAssetParentID()=" + getAssetParentID() + ", getEnableDate()=" + getEnableDate()
				+ ", getActDate()=" + getActDate() + ", getDeprType()=" + getDeprType() + ", getStorage()="
				+ getStorage() + ", getStorageDescr()=" + getStorageDescr() + ", getFlags()=" + getFlags()
				+ ", getFinCode()=" + getFinCode() + ", getACode()=" + getACode() + ", getTagPrtFlag()="
				+ getTagPrtFlag() + ", getBatchID()=" + getBatchID() + ", getNote()=" + getNote() + ", hashCode()="
				+ hashCode() + ", getStarNum()=" + getStarNum() + ", getMatterID()=" + getMatterID() + "]";
	}

 
 
	
}