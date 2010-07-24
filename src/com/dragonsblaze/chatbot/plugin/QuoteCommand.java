package com.dragonsblaze.chatbot.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class QuoteCommand extends Command
{
	private static final String QUOTES_URI = "http://www.iheartquotes.com/api/v1/random?max_lines=1&show_source=false&show_permalink=false";
	
	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		
		try
		{
			URL quotes = new URL(QUOTES_URI);
			adapter.send(new BufferedReader(new InputStreamReader(quotes.openConnection()
					.getInputStream())).readLine());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}