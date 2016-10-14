package com.runcom.wgcwgc.business;

public class ProductsList
{
	private String id , name , desc , type , price , productcode , market ,
	        paymethod;

	public ProductsList()
	{
		// TODO Auto-generated constructor stub
	}

	public ProductsList(String id , String name , String desc , String type , String price , String productcode , String market , String paymethod)
	{
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.price = price;
		this.productcode = productcode;
		this.market = market;
		this.paymethod = paymethod;
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
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name )
	{
		this.name = name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc()
	{
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc )
	{
		this.desc = desc;
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
	 * @return the price
	 */
	public String getPrice()
	{
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(String price )
	{
		this.price = price;
	}

	/**
	 * @return the productcode
	 */
	public String getProductcode()
	{
		return productcode;
	}

	/**
	 * @param productcode
	 *            the productcode to set
	 */
	public void setProductcode(String productcode )
	{
		this.productcode = productcode;
	}

	/**
	 * @return the market
	 */
	public String getMarket()
	{
		return market;
	}

	/**
	 * @param market
	 *            the market to set
	 */
	public void setMarket(String market )
	{
		this.market = market;
	}

	/**
	 * @return the paymethod
	 */
	public String getPaymethod()
	{
		return paymethod;
	}

	/**
	 * @param paymethod
	 *            the paymethod to set
	 */
	public void setPaymethod(String paymethod )
	{
		this.paymethod = paymethod;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "id=" + id + "\nname=" + name + "\ndesc=" + desc + "\ntype=" + type + "\nprice=" + price + "\nproductcode=" + productcode + "\nmarket=" + market + "\npaymethod=" + paymethod + "\n";
	}
}
