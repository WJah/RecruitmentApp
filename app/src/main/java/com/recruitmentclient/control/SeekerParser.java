package com.recruitmentclient.control;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.recruitmentclient.model.Seeker;

public class SeekerParser extends DefaultHandler
{
	private Seeker seeker;
	private String tag;

	public SeekerParser(Seeker seeker)
	{
		// TODO Auto-generated constructor stub
		this.seeker = seeker;
	}

	public Seeker getSeeker()
	{
		return seeker;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		// TODO Auto-generated method stub
		String character = new String(ch, start, length);
		if (tag.equals("username"))
		{
			seeker.setUsername(character);
		} else if (tag.equals("name"))
		{
			seeker.setName(character);
		} else if (tag.equals("gender"))
		{
			seeker.setGender(Integer.parseInt(character));
		} else if (tag.equals("school"))
		{
			seeker.setSchool(character);
		} else if (tag.equals("major"))
		{
			seeker.setMajor(character);
		} else if (tag.equals("skill"))
		{
			seeker.setSkill(character);
		} else if (tag.equals("phone"))
		{
			seeker.setPhone(character);
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
	}

	@Override
	public void startDocument() throws SAXException
	{
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		// TODO Auto-generated method stub
		tag = localName;
		if (tag.equals("user"))
		{
			seeker = new Seeker();
		}
	}

}
