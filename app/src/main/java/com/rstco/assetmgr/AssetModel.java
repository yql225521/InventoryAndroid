/*
 * $RCSfile: AssetModel.java,v $$
 * $Revision: 1.1  $
 * $Date: 2014-05-04  $
 *
 */
package com.rstco.assetmgr;

import java.io.Serializable;
 
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.j256.ormlite.field.DatabaseField;

/**
 * <p>Title: AssetModel</p>
 * <p>Table: ASSET</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author CodeGenerator
 * @version 1.0
 */
 
public class AssetModel implements Serializable
{
	/**
	 * 版本号 
	 */
	private static final long serialVersionUID = 1L;

    @DatabaseField
	private String assetID;
	



	/**  */
    @DatabaseField
	private String assetName="";


	/**  */
    @DatabaseField
	private String assetCode="";


	/**  */
	private Integer seq;


	/**  */
	@DatabaseField
	private String spec="";


	/**  */
	private String unit="";


	/**  */
	@DatabaseField
	private String mgrOrganName="";


	/**  */
	@DatabaseField
	private String cateID="";


	/**  */
	private String mgrOrganID="";


	/**  */
	@DatabaseField
	private String assetType="";


	/**  */
	@DatabaseField
	private String status="";


	/**  */
	private Date buyDate;


	/**  */
	private String liablePerson="";


	/**  */
	@DatabaseField
	private String operator="";


	/**  */
	@DatabaseField
	private String organID="";


	/**  */
	@DatabaseField
	private String organName="";


	/**  */
	private String createdBy="";


	/**  */
	private Date creationDate;


	/**  */
	private String lastUpdateBy="";


	/**  */
	private Date lastUpdateDate;


	/**  */
	@DatabaseField
	private Double originalValue;


	/**  */
	private String delFlag;


	/**  */
	private String assetParentID="";


	/**  */
	private Date enableDate;


	/**  */
	private Date actDate;


	/**  */
	private String deprType="";


	/**  */
	private String storage="";


	/**  */
	@DatabaseField
	private String storageDescr;


	/**  */
	private String flags;


	/**  */
	@DatabaseField
	private String finCode;


	/**  */
	private String aCode;


	/**  */
	private String tagPrtFlag;


	/**  */
	private String batchID;


	/**  */ 
	private String note;
	
	@DatabaseField
	private Double starNum;
	
	@DatabaseField
	private String matterID;

	public AssetModel()
	{
		super();
	}

	/**
	 * ID(ASSET_ID)
	 * @return assetID
	 */
 
	public String getAssetID()
	{
		return this.assetID;
	}
    
	public void setAssetID(String assetID)
	{
		this.assetID = assetID;
	}

 
	public String getAssetPK()
	{
		return this.assetID;
	}
    
	public void setAssetPK(String pk)
	{
		this.assetID = pk;
	}

 
	public String getAssetName()
	{
		return this.assetName;
	}
    
    /**
	 * (ASSET_NAME)
	 * @param String assetName
	 */
	public void setAssetName(String assetName)
	{
		this.assetName = assetName;
	}
	
 
	public String getAssetCode()
	{
		if(StringUtils.isBlank(assetCode)){
			return "";
		}else
			return this.assetCode;
	}
    
    /**
	 * (ASSET_CODE)
	 * @param String assetCode
	 */
	public void setAssetCode(String assetCode)
	{
		this.assetCode = assetCode;
	}
	
 
	public Integer getSeq()
	{
		return this.seq;
	}
    
    /**
	 * (SEQ)
	 * @param Integer seq
	 */
	public void setSeq(Integer seq)
	{
		this.seq = seq;
	}
	
	/**
	 * (SPEC)
	 * @return String spec
	 */
 
	public String getSpec()
	{
		if(StringUtils.isBlank(spec)){
			return "";
		}else
		return this.spec;
	}
    
    /**
	 * (SPEC)
	 * @param String spec
	 */
	public void setSpec(String spec)
	{
		this.spec = spec;
	}
	
	/**
	 * (UNIT)
	 * @return String unit
	 */
 
	public String getUnit()
	{
		if(StringUtils.isBlank(unit)){
			return "";
		}else
		return this.unit;
	}
    
    /**
	 * (UNIT)
	 * @param String unit
	 */
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	
	/**
	 * (MGR_ORGAN_NAME)
	 * @return String mgrOrganName
	 */
 
	public String getMgrOrganName()
	{
		if(StringUtils.isBlank(mgrOrganName)){
			return "";
		}else
		return this.mgrOrganName;
	}
    
    /**
	 * (MGR_ORGAN_NAME)
	 * @param String mgrOrganName
	 */
	public void setMgrOrganName(String mgrOrganName)
	{
		this.mgrOrganName = mgrOrganName;
	}
	
	/**
	 * (CATE_ID)
	 * @return String cateID
	 */
 
	public String getCateID()
	{
		return this.cateID;
	}
    
    /**
	 * (CATE_ID)
	 * @param String cateID
	 */
	public void setCateID(String cateID)
	{
		this.cateID = cateID;
	}
	
	/**
	 * (MGR_ORGAN_ID)
	 * @return String mgrOrganID
	 */
 
	public String getMgrOrganID()
	{
		return this.mgrOrganID;
	}
    
    /**
	 * (MGR_ORGAN_ID)
	 * @param String mgrOrganID
	 */
	public void setMgrOrganID(String mgrOrganID)
	{
		this.mgrOrganID = mgrOrganID;
	}
	
	/**
	 * (ASSET_TYPE)
	 * @return String assetType
	 */
 
	public String getAssetType()
	{
		return this.assetType;
	}
    
    /**
	 * (ASSET_TYPE)
	 * @param String assetType
	 */
	public void setAssetType(String assetType)
	{
		this.assetType = assetType;
	}
	
	/**
	 * (STATUS)
	 * @return String status
	 */
 
	public String getStatus()
	{
		return this.status;
	}
    
    /**
	 * (STATUS)
	 * @param String status
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	/**
	 * (BUY_DATE)
	 * @return Date buyDate
	 */
 
	public Date getBuyDate()
	{
		return this.buyDate;
	}
    
    /**
	 * (BUY_DATE)
	 * @param Date buyDate
	 */
	public void setBuyDate(Date buyDate)
	{
		this.buyDate = buyDate;
	}
	
	/**
	 * (LIABLE_PERSON)
	 * @return String liablePerson
	 */
 
	public String getLiablePerson()
	{
		return this.liablePerson;
	}
    
    /**
	 * (LIABLE_PERSON)
	 * @param String liablePerson
	 */
	public void setLiablePerson(String liablePerson)
	{
		this.liablePerson = liablePerson;
	}
	
	/**
	 * (OPERATOR)
	 * @return String operator
	 */
 
	public String getOperator()
	{
		if("null".equals(operator)||null==operator)
			return "";
		return this.operator;
	}
    
    /**
	 * (OPERATOR)
	 * @param String operator
	 */
	public void setOperator(String operator)
	{
		this.operator = operator;
	}
	
	/**
	 * (ORGAN_ID)
	 * @return String organID
	 */
 
	public String getOrganID()
	{
		return this.organID;
	}
    
    /**
	 * (ORGAN_ID)
	 * @param String organID
	 */
	public void setOrganID(String organID)
	{
		this.organID = organID;
	}
	
	/**
	 * (ORGAN_NAME)
	 * @return String organName
	 */
 
	public String getOrganName()
	{
		if(StringUtils.isBlank(organName)){
			return "";
		}else
		return this.organName;
	}
    
    /**
	 * (ORGAN_NAME)
	 * @param String organName
	 */
	public void setOrganName(String organName)
	{
		
		this.organName = organName;
	}
	
	/**
	 * (CREATED_BY)
	 * @return String createdBy
	 */
 
	public String getCreatedBy()
	{
		return this.createdBy;
	}
    
    /**
	 * (CREATED_BY)
	 * @param String createdBy
	 */
	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}
	
	/**
	 * (CREATION_DATE)
	 * @return Date creationDate
	 */
 
	public Date getCreationDate()
	{
		return this.creationDate;
	}
    
    /**
	 * (CREATION_DATE)
	 * @param Date creationDate
	 */
	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}
	
	/**
	 * (LAST_UPDATE_BY)
	 * @return String lastUpdateBy
	 */
 
	public String getLastUpdateBy()
	{
		return this.lastUpdateBy;
	}
    
    /**
	 * (LAST_UPDATE_BY)
	 * @param String lastUpdateBy
	 */
	public void setLastUpdateBy(String lastUpdateBy)
	{
		this.lastUpdateBy = lastUpdateBy;
	}
	
	/**
	 * (LAST_UPDATE_DATE)
	 * @return Date lastUpdateDate
	 */
 
	public Date getLastUpdateDate()
	{
		return this.lastUpdateDate;
	}
    
    /**
	 * (LAST_UPDATE_DATE)
	 * @param Date lastUpdateDate
	 */
	public void setLastUpdateDate(Date lastUpdateDate)
	{
		this.lastUpdateDate = lastUpdateDate;
	}
	
	/**
	 * (ORIGINAL_VALUE)
	 * @return Double originalValue
	 */
 
	public Double getOriginalValue()
	{
		return this.originalValue;
	}
    
    /**
	 * (ORIGINAL_VALUE)
	 * @param Double originalValue
	 */
	public void setOriginalValue(Double originalValue)
	{
		this.originalValue = originalValue;
	}
	
	/**
	 * (DEL_FLAG)
	 * @return String delFlag
	 */
 
	public String getDelFlag()
	{
		return this.delFlag;
	}
    
    /**
	 * (DEL_FLAG)
	 * @param String delFlag
	 */
	public void setDelFlag(String delFlag)
	{
		this.delFlag = delFlag;
	}
	
	/**
	 * (ASSET_PARENT_ID)
	 * @return String assetParentID
	 */
 
	public String getAssetParentID()
	{
		return this.assetParentID;
	}
    
    /**
	 * (ASSET_PARENT_ID)
	 * @param String assetParentID
	 */
	public void setAssetParentID(String assetParentID)
	{
		this.assetParentID = assetParentID;
	}
	
	/**
	 * (ENABLE_DATE)
	 * @return Date enableDate
	 */
 
	public Date getEnableDate()
	{
		return this.enableDate;
	}
    
    /**
	 * (ENABLE_DATE)
	 * @param Date enableDate
	 */
	public void setEnableDate(Date enableDate)
	{
		this.enableDate = enableDate;
	}
	
	/**
	 * (ACT_DATE)
	 * @return Date actDate
	 */
	 
	public Date getActDate()
	{
		return this.actDate;
	}
    
    /**
	 * (ACT_DATE)
	 * @param Date actDate
	 */
	public void setActDate(Date actDate)
	{
		this.actDate = actDate;
	}
	
	/**
	 * (DEPR_TYPE)
	 * @return String deprType
	 */
	 
	public String getDeprType()
	{
		return this.deprType;
	}
    
    /**
	 * (DEPR_TYPE)
	 * @param String deprType
	 */
	public void setDeprType(String deprType)
	{
		this.deprType = deprType;
	}
	
	/**
	 * (STORAGE)
	 * @return String storage
	 */
	 
	public String getStorage()
	{
		return this.storage;
	}
    
    /**
	 * (STORAGE)
	 * @param String storage
	 */
	public void setStorage(String storage)
	{
		this.storage = storage;
	}
	
	/**
	 * (STORAGE_DESCR)
	 * @return String storageDescr
	 */
 
	public String getStorageDescr()
	{
		if(StringUtils.isBlank(storageDescr)){
			return "";
		}else
		return this.storageDescr;
	}
    
    /**
	 * (STORAGE_DESCR)
	 * @param String storageDescr
	 */
	public void setStorageDescr(String storageDescr)
	{
		this.storageDescr = storageDescr;
	}
	
	/**
	 * (FLAGS)
	 * @return String flags
	 */
	 
	public String getFlags()
	{
		return this.flags;
	}
    
    /**
	 * (FLAGS)
	 * @param String flags
	 */
	public void setFlags(String flags)
	{
		this.flags = flags;
	}
	
	/**
	 * (FIN_CODE)
	 * @return String finCode
	 */
 
	public String getFinCode()
	{
		if("null".equals(finCode)||null==finCode)
			return "";
		else
			return this.finCode;
	}
    
    /**
	 * (FIN_CODE)
	 * @param String finCode
	 */
	public void setFinCode(String finCode)
	{
		this.finCode = finCode;
	}
	
	/**
	 * (A_CODE)
	 * @return String aCode
	 */
 
	public String getACode()
	{
		return this.aCode;
	}
    
    /**
	 * (A_CODE)
	 * @param String aCode
	 */
	public void setACode(String aCode)
	{
		this.aCode = aCode;
	}
	
	/**
	 * (TAG_PRT_FLAG)
	 * @return String tagPrtFlag
	 */
 
	public String getTagPrtFlag()
	{
		return this.tagPrtFlag;
	}
    
    /**
	 * (TAG_PRT_FLAG)
	 * @param String tagPrtFlag
	 */
	public void setTagPrtFlag(String tagPrtFlag)
	{
		this.tagPrtFlag = tagPrtFlag;
	}
	
	/**
	 * (BATCH_ID)
	 * @return String batchID
	 */
 
	public String getBatchID()
	{
		return this.batchID;
	}
    
    /**
	 * (BATCH_ID)
	 * @param String batchID
	 */
	public void setBatchID(String batchID)
	{
		this.batchID = batchID;
	}
	
	/**
	 * (NOTE)
	 * @return String note
	 */
 
	public String getNote()
	{
		return this.note;
	}
    
    /**
	 * (NOTE)
	 * @param String note
	 */
	public void setNote(String note)
	{
		this.note = note;
	}
	

	@Override
	public int hashCode()
	{
		final int prime = 31;

		int result = 1;
		
		result = prime * result + ((this.assetID == null) ? 0 : assetID.hashCode());
			
		return result;
	}

	@Override
	public boolean equals(Object object)
	{
		if(this == object)
		{
			return true;
		}
	
		if(object == null)
		{
			return false;
		}
		
		if(this.getClass() != object.getClass())
		{
			return false;
		}
		
		final AssetModel other = (AssetModel)object;
		
		if(this.assetID == null)
		{
			if(other.assetID != null)
			{
				return false;
			}
		} 
		else if(!this.assetID.equals(other.assetID))
		{
			return false;
		}
		
		return true;
	}

	public Double getStarNum() {
		return starNum;
	}

	public void setStarNum(Double starNum) {
		this.starNum = starNum;
	}

	public String getMatterID() {
		return matterID;
	}

	public void setMatterID(String matterID) {
		this.matterID = matterID;
	}
}