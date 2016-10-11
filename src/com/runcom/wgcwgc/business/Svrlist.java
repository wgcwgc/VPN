package com.runcom.wgcwgc.business;

public class Svrlist
{
	public String id , name , addr , type , protocol , area , prior;
	
	public Svrlist ()
	{
		
	}
	
	public Svrlist(String id , String name , String addr , String type , String protocol , String area , String prior)
	{
		this.id = id;
		this.name = name;
		this.addr = addr;
		this.type = type;
		this.protocol = protocol;
		this.area = area;
		this.prior = prior;
		
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id )
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name )
	{
		this.name = name;
	}

	public String getAddr()
	{
		return addr;
	}

	public void setAddr(String addr )
	{
		this.addr = addr;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type )
	{
		this.type = type;
	}

	public String getProtocol()
	{
		return protocol;
	}

	public void setProtocol(String protocol )
	{
		this.protocol = protocol;
	}

	public String getArea()
	{
		return area;
	}

	public void setArea(String area )
	{
		this.area = area;
	}

	public String getPrior()
	{
		return prior;
	}

	public void setPrior(String prior )
	{
		this.prior = prior;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString()
    {
	    return "Svrlist [id=" + id + ", name=" + name + ", addr=" + addr + ", type=" + type + ", protocol=" + protocol + ", area=" + area + ", prior=" + prior + "]";
    }
	

}
