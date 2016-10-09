/*
 * $RCSfile: NoticeModel.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-11-27  $
 *
 */
package com.rstco.sjpt.model;

import java.io.Serializable;
 
import java.util.Date;
 
public class NoticeModel implements Serializable
{
	/**
	 * 版本号 
	 */
	private static final long serialVersionUID = 1L;

	/** ${column.remarks} */
 
	private String ID;
	



	/**  */
	private String title;


	/**  */
	private String context;


	/**  */
	private String creator;


	/**  */
	private Date createDate;


	/**  */
	private String status;

	public NoticeModel()
	{
		super();
	}

 
	public String getID()
	{
		return this.ID;
	}
    
	public void setID(String ID)
	{
		this.ID = ID;
	}

 
	public String getNoticePK()
	{
		return this.ID;
	}
    
	public void setNoticePK(String pk)
	{
		this.ID = pk;
	}

 
	public String getTitle()
	{
		return this.title;
	}
    
    /**
	 * (TITLE)
	 * @param String title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
 
	public String getContext()
	{
		return this.context;
	}
    
 
	public void setContext(String context)
	{
		this.context = context;
	}
	
 
	public String getCreator()
	{
		return this.creator;
	}
    
    /**
	 * (CREATOR)
	 * @param String creator
	 */
	public void setCreator(String creator)
	{
		this.creator = creator;
	}
	
 
	public Date getCreateDate()
	{
		return this.createDate;
	}
    
    /**
	 * (CREATE_DATE)
	 * @param Date createDate
	 */
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	
 
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
		
		final NoticeModel other = (NoticeModel)object;
		
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
}