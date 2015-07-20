package com.recruitmentclient.control;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.recruitmentclient.model.Job;

public class JobParser extends DefaultHandler
{
	private List<Job> jobs;
	private Job job;
	private String tag;

	public JobParser(List<Job> jobs)
	{
		this.jobs = jobs;
		// TODO Auto-generated constructor stub
	}

	public List<Job> getJobs()
	{
		return jobs;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		// String name;
		// String avgsala;
		// String salary;
		// String location;
		// String skill;
		// String position;
		// String phone;
		// TODO Auto-generated method stub
		String character = new String(ch, start, length);
		if (tag.equals("name"))
		{
			job.setName(character);
		} else if (tag.equals("avgsala"))
		{
			job.setAvgsala(character);
		} else if (tag.equals("salary"))
		{
			job.setSalary(character);
		} else if (tag.equals("location"))
		{
			job.setLocation(character);
		} else if (tag.equals("skill"))
		{
			job.setSkill(character);
		} else if (tag.equals("position"))
		{
			job.setPosition(character);
		} else if (tag.equals("phone"))
		{
			job.setPhone(character);
		} else if (tag.equals("job_id"))
		{
			job.setJob_id(character);
		}
	}

	@Override
	public void endDocument() throws SAXException
	{
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		// TODO Auto-generated method stub
		tag = localName;
		if (tag.equals("job"))
		{
			jobs.add(job);
		}
		tag = "";
	}

	@Override
	public void startDocument() throws SAXException
	{
		// TODO Auto-generated method stub
		jobs = new ArrayList<Job>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		// TODO Auto-generated method stub
		tag = localName;
		if (tag.equals("job"))
		{
			job = new Job();
		}
	}

}
