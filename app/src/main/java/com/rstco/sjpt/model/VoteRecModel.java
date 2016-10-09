/*
 * $RCSfile: VoteRecModel.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-11-27  $
 *
 */
package com.rstco.sjpt.model;

import java.io.Serializable;
 
import java.util.Date;

 
public class VoteRecModel implements Serializable
{
	/**
	 * 版本号 
	 */
	private static final long serialVersionUID = 1L;

 
	private String ID;
	



	/**  */
	private String voteID;


	/**  */
	private String voter;


	/**  */
	private Date voteDate;


	/**  */
	private String voteValue;

	public VoteRecModel()
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

 
	public String getVoteRecPK()
	{
		return this.ID;
	}
    
	public void setVoteRecPK(String pk)
	{
		this.ID = pk;
	}

 
	public String getVoteID()
	{
		return this.voteID;
	}
    
    /**
	 * (VOTE_ID)
	 * @param String voteID
	 */
	public void setVoteID(String voteID)
	{
		this.voteID = voteID;
	}
	
 
	public String getVoter()
	{
		return this.voter;
	}
    
    /**
	 * (VOTER)
	 * @param String voter
	 */
	public void setVoter(String voter)
	{
		this.voter = voter;
	}
	
 
	public Date getVoteDate()
	{
		return this.voteDate;
	}
    
    /**
	 * (VOTE_DATE)
	 * @param Date voteDate
	 */
	public void setVoteDate(Date voteDate)
	{
		this.voteDate = voteDate;
	}
	
 
	public String getVoteValue()
	{
		return this.voteValue;
	}
    
    /**
	 * (VOTE_VALUE)
	 * @param String voteValue
	 */
	public void setVoteValue(String voteValue)
	{
		this.voteValue = voteValue;
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
		
		final VoteRecModel other = (VoteRecModel)object;
		
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