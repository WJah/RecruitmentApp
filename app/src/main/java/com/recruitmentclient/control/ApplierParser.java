package com.recruitmentclient.control;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.recruitmentclient.model.Applicant;
import com.recruitmentclient.model.Seeker;

public class ApplierParser extends DefaultHandler
{
	List<Applicant> appliers;
	private Applicant applier;
	private String tag;

	public ApplierParser(List<Applicant> appliers)
	{
		// TODO Auto-generated constructor stub
		this.appliers = appliers;
	}

	public List<Applicant> getAppliers()
	{
		return appliers;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		// TODO Auto-generated method stub
		String character = new String(ch, start, length);
		if (tag.equals("username"))
		{
			applier.setUsername(character);
		} else if (tag.equals("name"))
		{
			applier.setName(character);
		} else if (tag.equals("gender"))
		{
			applier.setGender(Integer.parseInt(character));
		} else if (tag.equals("school"))
		{
			applier.setSchool(character);
		} else if (tag.equals("major"))
		{
			applier.setMajor(character);
		} else if (tag.equals("skill"))
		{
			applier.setSkill(character);
		} else if (tag.equals("phone"))
		{
			applier.setPhone(character);
		} else if (tag.equals("position"))
		{
			applier.setPosition(character);
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
		if (tag.equals("applier"))
		{
			appliers.add(applier);
		}
		tag = "";
	}

	@Override
	public void startDocument() throws SAXException
	{
		// TODO Auto-generated method stub
		appliers = new ArrayList<Applicant>();

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		// TODO Auto-generated method stub
		tag = localName;
		if (tag.equals("applier"))
		{
			applier = new Applicant();
		}
	}

}
