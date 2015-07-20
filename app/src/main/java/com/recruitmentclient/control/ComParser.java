package com.recruitmentclient.control;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.recruitmentclient.model.Company;

public class ComParser extends DefaultHandler
{
	Company company;
	String tag;

	public ComParser(Company company)
	{
		this.company = company;
	}

	public Company getCompany()
	{
		return company;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		// TODO Auto-generated method stub
		String character = new String(ch, start, length);
		if (tag.equals("name"))
		{
			company.setName(character);
		} else if (tag.equals("field"))
		{
			company.setField(character);
		} else if (tag.equals("avgsala"))
		{
			company.setAvgsala(character);
		} else if (tag.equals("location"))
		{
			company.setLocation(character);
		} else if (tag.equals("id"))
		{
			company.setId(character);
		} else if (tag.equals("phone"))
		{
			company.setPhone(character);
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
		super.endElement(uri, localName, qName);
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
			company = new Company();
		}
	}

}
