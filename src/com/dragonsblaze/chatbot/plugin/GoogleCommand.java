package com.dragonsblaze.chatbot.plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class GoogleCommand extends Command
{
	private static String URL_PREFIX = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&rsz=1&q=";

	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		try
		{
			URL url = new URL(URL_PREFIX + URLEncoder.encode(args, "UTF-8"));
			String httpResponse = IOUtils.toString(url.openConnection().getInputStream());

			JSONObject jsonResponse = new JSONObject(httpResponse);
			JSONObject result = jsonResponse.getJSONObject("responseData").getJSONArray("results").getJSONObject(0);
			
			String response = result.getString("titleNoFormatting") + ": " + result.getString("content") + "\n" + result.getString("unescapedUrl");
			response = response.replaceAll("</?b> ?", "");
			
			adapter.send(response);
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
			adapter.send("Error: invalid server response");
			e.printStackTrace();
		}
	}
	
	@Override
	public String getHelpText()
	{
		return "googles your query";
	}
}