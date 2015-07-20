package com.recruitmentclient.model;

public class Applicant extends Seeker
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String position;

	public void setPosition(String position)
	{
		this.position = position;
	}

	public String getPosition()
	{
		return position;
	}

	@Override
	public String toString()
	{
		return "Applicant [position=" + position + ", username=" + username
				+ ", name=" + name + ", gender=" + gender + ", school="
				+ school + ", major=" + major + ", skill=" + skill + ", phone="
				+ phone + "]";
	}
	
	

}
