package com.runcom.wgcwgc.configure;

public class Configure
{
	public int term;
	public String os;
	public String dev;
	public String app;
	public String ver;
	public String build;
	public int isbreak;
	public String lang;
	public int market;

	public Configure()
	{

	}

	public Configure(int term , int market , int isbreak , String os , String dev , String app , String ver , String build , String lang)
	{
		this.term = term;
		this.os = os;
		this.market = market;
		this.isbreak = isbreak;
		this.dev = dev;
		this.app = app;
		this.ver = ver;
		this.lang = lang;
		this.build = build;
	}

	public int getTerm()
	{
		return term;
	}

	public void setTerm(int term )
	{
		this.term = term;
	}

	public String getOs()
	{
		return os;
	}

	public void setOs(String os )
	{
		this.os = os;
	}

	public String getDev()
	{
		return dev;
	}

	public void setDev(String dev )
	{
		this.dev = dev;
	}

	public String getApp()
	{
		return app;
	}

	public void setApp(String app )
	{
		this.app = app;
		// Log.d("LOG" ,"set ÷¥––¡À" + app);
	}

	public String getVer()
	{
		return ver;
	}

	public void setVer(String ver )
	{
		this.ver = ver;
	}

	public String getBuild()
	{
		return build;
	}

	public void setBuild(String build )
	{
		this.build = build;
	}

	public int getIsbreak()
	{
		return isbreak;
	}

	public void setIsbreak(int isbreak )
	{
		this.isbreak = isbreak;
	}

	public String getLang()
	{
		return lang;
	}

	public void setLang(String lang )
	{
		this.lang = lang;
	}

	public int getMarket()
	{
		return market;
	}

	public void setMarket(int market )
	{
		this.market = market;
	}

}
