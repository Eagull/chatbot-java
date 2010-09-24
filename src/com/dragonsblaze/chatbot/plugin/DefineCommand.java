package com.dragonsblaze.chatbot.plugin;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;

import com.dragonsblaze.chatbot.Bot;
import com.dragonsblaze.chatbot.adapter.Adapter;

public class DefineCommand extends Command
{
	private static final String REPLY_TAG = "=>";

	public DefineCommand(String commandName)
	{
		super(commandName);
	}

	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		String[] argsArr = args.trim().split(" ", 2);

		if (argsArr.length < 2)
		{
			adapter.send(getHelpText());
			return;
		}

		if (Bot.dictionary.has(argsArr[0]))
		{
			adapter.send("But I already know " + argsArr[0] + "!!");
			return;
		}

		if (args.indexOf(REPLY_TAG) > 0)
		{
			int replyTagEndIndex = args.indexOf(REPLY_TAG) + REPLY_TAG.length();
			add(argsArr[0], args.substring(replyTagEndIndex).trim(), sender, adapter);
			return;
		}

		add(argsArr[0], args, sender, adapter);
	}

	private void add(String key, String value, String addedBy, Adapter adapter)
	{
		try
		{
			Bot.dictionary.add(key, value, addedBy);
			adapter.send(key + " => " + value);
		}
		catch (IOException e)
		{
			adapter.send("My mind has gone blank.");
			e.printStackTrace();
		}
		catch (ConfigurationException e)
		{
			adapter.send("My mind has gone blank.");
			e.printStackTrace();
		}
	}

	@Override
	public String getHelpText()
	{
		return "Allows you to define something. The term being defined must be exactly one word. Syntax: " + commandName + " term => definition";
	}
}