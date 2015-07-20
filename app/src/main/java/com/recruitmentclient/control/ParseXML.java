package com.recruitmentclient.control;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.recruitmentclient.model.Applicant;
import com.recruitmentclient.model.Company;
import com.recruitmentclient.model.Job;
import com.recruitmentclient.model.Seeker;

public class ParseXML
{
	public static List<Applicant> parseToAppliers(String xml)
	{
		List<Applicant> appliers = new ArrayList<Applicant>();
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader xmlReader = factory.newSAXParser().getXMLReader();
			ApplierParser applierParser = new ApplierParser(appliers);
			xmlReader.setContentHandler(applierParser);
			xmlReader.parse(new InputSource(new StringReader(xml)));
			appliers = applierParser.getAppliers();
		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return appliers;
	}

	public static Seeker parseToSeeker(String xml)
	{
		Seeker seeker = null;
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader xmlReader = factory.newSAXParser().getXMLReader();
			SeekerParser seekerParser = new SeekerParser(seeker);
			xmlReader.setContentHandler(seekerParser);
			xmlReader.parse(new InputSource(new StringReader(xml)));
			seeker = seekerParser.getSeeker();
		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return seeker;
	}

	public static Company parseToCompany(String xml)
	{
		Company company = null;
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader xmlReader = factory.newSAXParser().getXMLReader();
			ComParser comParser = new ComParser(company);
			xmlReader.setContentHandler(comParser);
			xmlReader.parse(new InputSource(new StringReader(xml)));
			company = comParser.getCompany();
		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return company;
	}

	public static List<Job> parseToJobs(String xml)
	{
		List<Job> jobs = new ArrayList<Job>();
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader xmlReader = factory.newSAXParser().getXMLReader();
			JobParser jobParser = new JobParser(jobs);
			xmlReader.setContentHandler(jobParser);
			xmlReader.parse(new InputSource(new StringReader(xml)));
			jobs = jobParser.getJobs();
		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jobs;
	}
}
