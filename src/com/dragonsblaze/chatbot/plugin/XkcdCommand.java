package com.dragonsblaze.chatbot.plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class XkcdCommand extends Command
{
	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		String index = args.split(" ")[0];
		
		try
		{
			URL quotes = new URL("http://xkcd.com/" + index + "/info.0.json");
			String response = IOUtils.toString(quotes.openConnection().getInputStream());

			JSONObject xkcdObject = new JSONObject(response);
			adapter.send(xkcdObject.getString("img"));
		}
		catch(FileNotFoundException e)
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
}