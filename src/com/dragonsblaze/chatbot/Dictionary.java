package com.dragonsblaze.chatbot;

/**
 * @author aaditya
 */
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Dictionary
{
	PropertiesConfiguration dictionary = null;

	File dictionaryFile;

	public Dictionary(String dictionaryFilename) throws IOException, ConfigurationException
	{
		dictionaryFile = new File(dictionaryFilename);
		FileUtils.touch(dictionaryFile);

		dictionary = new PropertiesConfiguration(dictionaryFilename);
		dictionary.setDelimiterParsingDisabled(true);
	}

	public void add(String key, String value, String addedBy) throws IOException,
			ConfigurationException
	{
		DictionaryItem item = new DictionaryItem(value, addedBy, new Date().getTime());
		dictionary.addProperty(key, new JSONObject(item).toString().replaceAll(",", ";"));
		dictionary.save();
	}

	public boolean has(String key)
	{
		return dictionary.containsKey(key);
	}

	public DictionaryItem get(String key)
	{
		String jsonText = dictionary.getString(key);

		if (jsonText == null)
			return null;

		try
		{
			JSONObject j = new JSONObject(jsonText.replaceAll(";", ","));
			return new DictionaryItem(j.getString("value"), j.getString("addedBy"),
					j.getLong("date"));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public int getSize()
	{
		// TODO: implement this
		return -1;
	}

	public void remove(String key) throws IOException, ConfigurationException
	{
		dictionary.clearProperty(key);
		dictionary.save();
	}

	public class DictionaryItem
	{
		String value, addedBy;

		long date;

		public DictionaryItem(String value, String addedBy, Date date)
		{
			this.value = value;
			this.addedBy = addedBy;
			this.date = date.getTime();
		}

		public DictionaryItem(String value, String addedBy, long date)
		{
			this.value = value;
			this.addedBy = addedBy;
			this.date = date;
		}

		public String getValue()
		{
			return value;
		}

		public String getAddedBy()
		{
			return addedBy;
		}

		public long getDate()
		{
			return date;
		}
	}
}