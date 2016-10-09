/*
 * $RCSfile: PubDictModel.java,v $$
 * $Revision: 1.1  $
 * $Date: 2012-10-13  $
 *
 * Copyright (C) 2008 Bettem, Inc. All rights reserved.
 *
 * This software is the proprietary information of Bettem, Inc.
 * Use is subject to license terms.
 */
package com.rstco.sjpt.model;

import java.io.Serializable;
 
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

/**
 * <p>Title: PubDictModel</p>
 * <p>Table: PUB_DICT</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author CodeGenerator
 * @version 1.0
 */
 
public class PubDictModel implements Serializable
{
	/**
	 * 版本号 
	 */
	private static final long serialVersionUID = 1L;

	/** ${column.remarks} */
	@DatabaseField
	private String ID;
	



	/**  */
	@DatabaseField
	private String code;


	/**  */
	@DatabaseField
	private String type;


	/**  */
	@DatabaseField
	private String useFlag;


	/**  */
	@DatabaseField
	private String name;


	/**  */
	private String createdBy;


	/**  */
	private Date creationDate;


	/**  */
	private String lastUpdatedBy;


	/**  */
	private Date lastUpdateDate;


	/**  */
	@DatabaseField
	private String parentCode;


	/**  */
	@DatabaseField
	private String layer;


	private String cateFlags;
	/**  */
	private Integer levelNum;

	public PubDictModel()
	{
		super();
	}

	/**
	 * ID(ID)
	 * @return ID
	 */
 
	public String getID()
	{
		return this.ID;
	}
    
	public void setID(String ID)
	{
		this.ID = ID;
	}

 
	public String getPubDictPK()
	{
		return this.ID;
	}
    
	public void setPubDictPK(String pk)
	{
		this.ID = pk;
	}

	/**
	 * (CODE)
	 * @return String code
	 */
 
	public String getCode()
	{
		return this.code;
	}
    
    /**
	 * (CODE)
	 * @param String code
	 */
	public void setCode(String code)
	{
		this.code = code;
	}
	
	/**
	 * (TYPE)
	 * @return String type
	 */
 
	public String getType()
	{
		return this.type;
	}
    
    /**
	 * (TYPE)
	 * @param String type
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
	/**
	 * (USE_FLAG)
	 * @return String useFlag
	 */
 
	public String getUseFlag()
	{
		return this.useFlag;
	}
    
    /**
	 * (USE_FLAG)
	 * @param String useFlag
	 */
	public void setUseFlag(String useFlag)
	{
		this.useFlag = useFlag;
	}
	
	/**
	 * (NAME)
	 * @return String name
	 */
 
	public String getName()
	{
		return this.name;
	}
    
    /**
	 * (NAME)
	 * @param String name
	 */
	public void setName(String name)
	{
		this.name = name;
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
	 * (LAST_UPDATED_BY)
	 * @return String lastUpdatedBy
	 */
 
	public String getLastUpdatedBy()
	{
		return this.lastUpdatedBy;
	}
    
    /**
	 * (LAST_UPDATED_BY)
	 * @param String lastUpdatedBy
	 */
	public void setLastUpdatedBy(String lastUpdatedBy)
	{
		this.lastUpdatedBy = lastUpdatedBy;
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
	 * (PARENT_CODE)
	 * @return String parentCode
	 */
 
	public String getParentCode()
	{
		return this.parentCode;
	}
    
    /**
	 * (PARENT_CODE)
	 * @param String parentCode
	 */
	public void setParentCode(String parentCode)
	{
		this.parentCode = parentCode;
	}
	
	/**
	 * (LAYER)
	 * @return String layer
	 */
 
	public String getLayer()
	{
		return this.layer;
	}
    
    /**
	 * (LAYER)
	 * @param String layer
	 */
	public void setLayer(String layer)
	{
		this.layer = layer;
	}
	
	/**
	 * (LEVEL_NUM)
	 * @return Short levelNum
	 */
 
	public Integer getLevelNum()
	{
		return this.levelNum;
	}
    
    /**
	 * (LEVEL_NUM)
	 * @param Short levelNum
	 */
	public void setLevelNum(Integer levelNum)
	{
		this.levelNum = levelNum;
	}
	

	@Override
	public int hashCode()
	{
		final int prime = 31;

		int result = 1;
		
		result = prime * result + ((this.ID == null) ? 0 : ID.hashCode());
			
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
		
		final PubDictModel other = (PubDictModel)object;
		
		if(this.ID == null)
		{
			if(other.ID != null)
			{
				return false;
			}
		} 
		else if(!this.ID.equals(other.ID))
		{
			return false;
		}
		
		return true;
	}
 
	public String getCateFlags() {
		return cateFlags;
	}

	public void setCateFlags(String cateFlags) {
		this.cateFlags = cateFlags;
	}
}