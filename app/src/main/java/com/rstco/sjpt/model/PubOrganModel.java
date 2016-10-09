/*
 * $RCSfile: PubOrganModel.java,v $$
 * $Revision: 1.1  $
 * $Date: 2012-05-06  $
 *
 * Copyright (C) 2008 Bettem, Inc. All rights reserved.
 *
 * This software is the proprietary information of Bettem, Inc.
 * Use is subject to license terms.
 */
package com.rstco.sjpt.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

 

/**
 * <p>Title: PubOrganModel</p>
 * <p>Table: pub_organ</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author CodeGenerator
 * @version 1.0
 */
 
public class PubOrganModel implements Serializable
{
	/**
	 * 版本号 
	 */
	private static final long serialVersionUID = 1L;

	/**  */
	@DatabaseField
	private String organID;
	



	/**  */
	@DatabaseField
	private String organCode;


	/**  */
	@DatabaseField
	private String organName;


	/**  */
	@DatabaseField
	private String shortName;


	/**  */
	@DatabaseField
	private String organType;


	/**  */
	private String beginDate;


	/**  */
	private String endDate;


	/**  */
	@DatabaseField
	private String inUse;


	/**  */
	private String organLayer;


	/**  */
	private String organIDParent;


	/**  */
	private Integer serialNo;
	
	private String bussCode;


	/**  */
	private String isLeaf;


	/**  */
	private Integer organLevel;


	/**  */
	private String flag;

	public PubOrganModel()
	{
		super();
	}

	/**
	 * 变更ID(ORGAN_ID)
	 * @return organID
	 */
 
	public String getOrganID()
	{
		return this.organID;
	}
    
	public void setOrganID(String organID)
	{
		this.organID = organID;
	}

 
	public String getPubOrganPK()
	{
		return this.organID;
	}
    
	public void setPubOrganPK(String pk)
	{
		this.organID = pk;
	}

	/**
	 * (ORGAN_CODE)
	 * @return String organCode
	 */
 
	public String getOrganCode()
	{
		return this.organCode;
	}
    
    /**
	 * (ORGAN_CODE)
	 * @param String organCode
	 */
	public void setOrganCode(String organCode)
	{
		this.organCode = organCode;
	}
	
	/**
	 * (ORGAN_NAME)
	 * @return String organName
	 */
 
	public String getOrganName()
	{
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
	 * (SHORT_NAME)
	 * @return String shortName
	 */
 
	public String getShortName()
	{
		return this.shortName;
	}
    
    /**
	 * (SHORT_NAME)
	 * @param String shortName
	 */
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}
	
	/**
	 * (ORGAN_TYPE)
	 * @return String organType
	 */
 
	public String getOrganType()
	{
		return this.organType;
	}
    
    /**
	 * (ORGAN_TYPE)
	 * @param String organType
	 */
	public void setOrganType(String organType)
	{
		this.organType = organType;
	}
	
	/**
	 * (BEGIN_DATE)
	 * @return String beginDate
	 */
 
	public String getBeginDate()
	{
		return this.beginDate;
	}
    
    /**
	 * (BEGIN_DATE)
	 * @param String beginDate
	 */
	public void setBeginDate(String beginDate)
	{
		this.beginDate = beginDate;
	}
	
	/**
	 * (END_DATE)
	 * @return String endDate
	 */
 
	public String getEndDate()
	{
		return this.endDate;
	}
    
    /**
	 * (END_DATE)
	 * @param String endDate
	 */
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}
	
	/**
	 * (IN_USE)
	 * @return String inUse
	 */
 
	public String getInUse()
	{
		return this.inUse;
	}
    
    /**
	 * (IN_USE)
	 * @param String inUse
	 */
	public void setInUse(String inUse)
	{
		this.inUse = inUse;
	}
	
	/**
	 * (ORGAN_LAYER)
	 * @return String organLayer
	 */
 
	public String getOrganLayer()
	{
		return this.organLayer;
	}
    
    /**
	 * (ORGAN_LAYER)
	 * @param String organLayer
	 */
	public void setOrganLayer(String organLayer)
	{
		this.organLayer = organLayer;
	}
	
	/**
	 * (ORGAN_ID_parent)
	 * @return String organIDParent
	 */
 
	public String getOrganIDParent()
	{
		return this.organIDParent;
	}
    
    /**
	 * (ORGAN_ID_parent)
	 * @param String organIDParent
	 */
	public void setOrganIDParent(String organIDParent)
	{
		this.organIDParent = organIDParent;
	}
	
	/**
	 * (serial_no)
	 * @return Integer serialNo
	 */
 
	public Integer getSerialNo()
	{
		return this.serialNo;
	}
    
    /**
	 * (serial_no)
	 * @param Integer serialNo
	 */
	public void setSerialNo(Integer serialNo)
	{
		this.serialNo = serialNo;
	}
	
	/**
	 * (IS_LEAF)
	 * @return String isLeaf
	 */
 
	public String getIsLeaf()
	{
		return this.isLeaf;
	}
    
    /**
	 * (IS_LEAF)
	 * @param String isLeaf
	 */
	public void setIsLeaf(String isLeaf)
	{
		this.isLeaf = isLeaf;
	}
	
	/**
	 * (ORGAN_LEVEL)
	 * @return Integer organLevel
	 */
 
	public Integer getOrganLevel()
	{
		return this.organLevel;
	}
    
    /**
	 * (ORGAN_LEVEL)
	 * @param Integer organLevel
	 */
	public void setOrganLevel(Integer organLevel)
	{
		this.organLevel = organLevel;
	}
	
	/**
	 * (FLAG)
	 * @return String flag
	 */
 
	public String getFlag()
	{
		return this.flag;
	}
    
    /**
	 * (FLAG)
	 * @param String flag
	 */
	public void setFlag(String flag)
	{
		this.flag = flag;
	}
	

	@Override
	public int hashCode()
	{
		final int prime = 31;

		int result = 1;
		
		result = prime * result + ((this.organID == null) ? 0 : organID.hashCode());
			
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
		
		final PubOrganModel other = (PubOrganModel)object;
		
		if(this.organID == null)
		{
			if(other.organID != null)
			{
				return false;
			}
		} 
		else if(!this.organID.equals(other.organID))
		{
			return false;
		}
		
		return true;
	}
 
	public String getBussCode() {
		return bussCode;
	}

	public void setBussCode(String bussCode) {
		this.bussCode = bussCode;
	}
}