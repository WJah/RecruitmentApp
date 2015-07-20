package com.recruitmentclient.model;

import java.io.Serializable;

public class Seeker implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String username;
	String name;
	int gender;
	String school;
	String major;
	String skill;
	String phone;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getGender()
	{
		return gender;
	}

	public void setGender(int gender)
	{
		this.gender = gender;
	}

	public String getSchool()
	{
		return school;
	}

	public void setSchool(String school)
	{
		this.school = school;
	}

	public String getMajor()
	{
		return major;
	}

	public void setMajor(String major)
	{
		this.major = major;
	}

	public String getSkill()
	{
		return skill;
	}

	public void setSkill(String skill)
	{
		this.skill = skill;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	@Override
	public String toString()
	{
		return "Seeker [username=" + username + ", name=" + name + ", gender="
				+ gender + ", school=" + school + ", major=" + major
				+ ", skill=" + skill + ", phone=" + phone + "]";
	}

}
