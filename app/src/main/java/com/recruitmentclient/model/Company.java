package com.recruitmentclient.model;

import java.io.Serializable;

public class Company implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String location;
	String field;
	String phone;
	String avgsala;
	String id;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getAvgsala()
	{
		return avgsala;
	}

	public void setAvgsala(String avgsala)
	{
		this.avgsala = avgsala;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public String toString()
	{
		return "Company [name=" + name + ", location=" + location + ", field="
				+ field + ", phone=" + phone + ", avgsala=" + avgsala + ", id="
				+ id + "]";
	}

}
