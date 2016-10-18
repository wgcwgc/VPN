package com.runcom.wgcwgc.business;

public class BonusList
{
	private String id , title , reason , type , value , bonusdate , hasgot ,
	        gotdate;

	public BonusList()
	{

	}

	public BonusList(String id , String title , String reason , String type , String value , String bonusdate , String hasgot , String gotdate)
	{
		this.id = id;
		this.title = title;
		this.reason = reason;
		this.type = type;
		this.value = value;
		this.bonusdate = bonusdate;
		this.hasgot = hasgot;
		this.gotdate = gotdate;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id )
	{
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title )
	{
		this.title = title;
	}

	/**
	 * @return the reason
	 */
	public String getReason()
	{
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(String reason )
	{
		this.reason = reason;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type )
	{
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value )
	{
		this.value = value;
	}

	/**
	 * @return the bonusdate
	 */
	public String getBonusdate()
	{
		return bonusdate;
	}

	/**
	 * @param bonusdate
	 *            the bonusdate to set
	 */
	public void setBonusdate(String bonusdate )
	{
		this.bonusdate = bonusdate;
	}

	/**
	 * @return the hasgot
	 */
	public String getHasgot()
	{
		return hasgot;
	}

	/**
	 * @param hasgot
	 *            the hasgot to set
	 */
	public void setHasgot(String hasgot )
	{
		this.hasgot = hasgot;
	}

	/**
	 * @return the gotdate
	 */
	public String getGotdate()
	{
		return gotdate;
	}

	/**
	 * @param gotdate
	 *            the gotdate to set
	 */
	public void setGotdate(String gotdate )
	{
		this.gotdate = gotdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "id=" + id + "\ntitle=" + title + "\nreason=" + reason + "\ntype=" + type + "\nvalue=" + value + "\nbonusdate=" + bonusdate + "\nhasgot=" + hasgot + "\ngotdate=" + gotdate + "\n";
	}

}
