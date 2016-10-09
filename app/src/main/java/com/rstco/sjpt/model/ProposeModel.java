/*
 * $RCSfile: ProposeModel.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-11-27  $
 *
 */
package com.rstco.sjpt.model;

import java.io.Serializable;
 
import java.util.Date;

 
public class ProposeModel implements Serializable
{
	/**
	 * 版本号 
	 */
	private static final long serialVersionUID = 1L;

	/** ${column.remarks} */
 
	private String ID;
	



	/**  */
	private String creator;


	/**  */
	private String title;


	/**  */
	private String context;


	/**  */
	private Date pdate;


	/**  */
	private String reply;


	/**  */
	private Date rdate;


	/**  */
	private String rUser;

	public ProposeModel()
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

 
	public String getProposePK()
	{
		return this.ID;
	}
    
	public void setProposePK(String pk)
	{
		this.ID = pk;
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
    
    /**
	 * (CONTEXT)
	 * @param String context
	 */
	public void setContext(String context)
	{
		this.context = context;
	}
	
 
	public Date getPdate()
	{
		return this.pdate;
	}
    
    /**
	 * (PDATE)
	 * @param Date pdate
	 */
	public void setPdate(Date pdate)
	{
		this.pdate = pdate;
	}
	
 
	public String getReply()
	{
		return this.reply;
	}
    
    /**
	 * (REPLY)
	 * @param String reply
	 */
	public void setReply(String reply)
	{
		this.reply = reply;
	}
	
 
	public Date getRdate()
	{
		return this.rdate;
	}
    
    /**
	 * (RDATE)
	 * @param Date rdate
	 */
	public void setRdate(Date rdate)
	{
		this.rdate = rdate;
	}
	
 
	public String getRUser()
	{
		return this.rUser;
	}
    
    /**
	 * (R_USER)
	 * @param String rUser
	 */
	public void setRUser(String rUser)
	{
		this.rUser = rUser;
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
		
		final ProposeModel other = (ProposeModel)object;
		
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