package com.recruitmentclient.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Job implements Serializable, Parcelable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String avgsala;
	String salary;
	String location;
	String skill;
	String position;
	String phone;
	String job_id;

	public void setJob_id(String job_id)
	{
		this.job_id = job_id;
	}

	public String getJob_id()
	{
		return job_id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAvgsala()
	{
		return avgsala;
	}

	public void setAvgsala(String avgsala)
	{
		this.avgsala = avgsala;
	}

	public String getSalary()
	{
		return salary;
	}

	public void setSalary(String salary)
	{
		this.salary = salary;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getSkill()
	{
		return skill;
	}

	public void setSkill(String skill)
	{
		this.skill = skill;
	}

	public String getPosition()
	{
		return position;
	}

	public void setPosition(String position)
	{
		this.position = position;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public String toString()
	{
		return "Job [name=" + name + ", avgsala=" + avgsala + ", salary="
				+ salary + ", location=" + location + ", skill=" + skill
				+ ", position=" + position + ", phone=" + phone + "]";
	}

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// TODO Auto-generated method stub

	}
}
