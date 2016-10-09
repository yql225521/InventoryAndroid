/*
 * $RCSfile: VoteModel.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-11-27  $
 *
 */
package com.rstco.sjpt.model;

import java.io.Serializable;
 
import java.util.Date;

 
 
public class VoteModel implements Serializable
{
	/**
	 * 版本号 
	 */
	private static final long serialVersionUID = 1L;

 
	private String ID;
	



	/**  */
	private String title;


	/**  */
	private String type;


	/**  */
	private String item1;


	/**  */
	private String item2;


	/**  */
	private String item3;


	/**  */
	private String item4;


	/**  */
	private String item5;


	/**  */
	private String item6;


	/**  */
	private String item7;


	/**  */
	private String item8;


	/**  */
	private Date beginDate;


	/**  */
	private Date endDate;


	/**  */
	private String creator;


	/**  */
	private Integer itemNum;

	public VoteModel()
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

 
	public String getVotePK()
	{
		return this.ID;
	}
    
	public void setVotePK(String pk)
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
	
 
	public String getItem1()
	{
		return this.item1;
	}
    
    /**
	 * (ITEM_1)
	 * @param String item1
	 */
	public void setItem1(String item1)
	{
		this.item1 = item1;
	}
	
 
	public String getItem2()
	{
		return this.item2;
	}
    
    /**
	 * (ITEM_2)
	 * @param String item2
	 */
	public void setItem2(String item2)
	{
		this.item2 = item2;
	}
	
 
	public String getItem3()
	{
		return this.item3;
	}
    
    /**
	 * (ITEM_3)
	 * @param String item3
	 */
	public void setItem3(String item3)
	{
		this.item3 = item3;
	}
	
 
	public String getItem4()
	{
		return this.item4;
	}
    
    /**
	 * (ITEM_4)
	 * @param String item4
	 */
	public void setItem4(String item4)
	{
		this.item4 = item4;
	}
	
 
	public String getItem5()
	{
		return this.item5;
	}
    
    /**
	 * (ITEM_5)
	 * @param String item5
	 */
	public void setItem5(String item5)
	{
		this.item5 = item5;
	}
	
 
	public String getItem6()
	{
		return this.item6;
	}
    
    /**
	 * (ITEM_6)
	 * @param String item6
	 */
	public void setItem6(String item6)
	{
		this.item6 = item6;
	}
	
 
	public String getItem7()
	{
		return this.item7;
	}
    
    /**
	 * (ITEM_7)
	 * @param String item7
	 */
	public void setItem7(String item7)
	{
		this.item7 = item7;
	}
	
 
	public String getItem8()
	{
		return this.item8;
	}
    
    /**
	 * (ITEM_8)
	 * @param String item8
	 */
	public void setItem8(String item8)
	{
		this.item8 = item8;
	}
 
	public Date getBeginDate()
	{
		return this.beginDate;
	}
    
    /**
	 * (BEGIN_DATE)
	 * @param Date beginDate
	 */
	public void setBeginDate(Date beginDate)
	{
		this.beginDate = beginDate;
	}
	
 
	public Date getEndDate()
	{
		return this.endDate;
	}
    
    /**
	 * (END_DATE)
	 * @param Date endDate
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
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
 
	public Integer getItemNum()
	{
		return this.itemNum;
	}
    
    /**
	 * (ITEM_NUM)
	 * @param Integer itemNum
	 */
	public void setItemNum(Integer itemNum)
	{
		this.itemNum = itemNum;
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
		
		final VoteModel other = (VoteModel)object;
		
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