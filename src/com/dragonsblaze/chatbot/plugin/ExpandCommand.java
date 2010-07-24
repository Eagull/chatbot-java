package com.dragonsblaze.chatbot.plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class ExpandCommand extends Command
{
	private static String URL_PREFIX = "http://api.longurl.org/v2/expand?format=json&url=";

	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		try
		{
			URL url = new URL(URL_PREFIX + URLEncoder.encode(args, "UTF-8"));
			String httpResponse = IOUtils.toString(url.openConnection().getInputStream());
			JSONObject jsonObject = new JSONObject(httpResponse);
			adapter.send(sender + ": " + jsonObject.getString("long-url"));
		}
		catch (FileNotFoundException e)
		{
			System.err.println("404 Not Found");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}