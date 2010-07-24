package com.dragonsblaze.chatbot.plugin;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;

import com.dragonsblaze.chatbot.Bot;
import com.dragonsblaze.chatbot.Dictionary.DictionaryItem;
import com.dragonsblaze.chatbot.adapter.Adapter;

public class DictCommand extends Command
{
	private static final String REPLY_TAG = "=>";

	private static final String FORGET = "forget", INFO = "info", SIZE = "size";

	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		String[] argsArr = args.trim().split(" ", 2);

		if (argsArr[0].isEmpty())
		{
			adapter.send(getHelpText());
			return;
		}

		if (Bot.dictionary.has(argsArr[0]))
		{
			adapter.send(Bot.dictionary.get(argsArr[0]).getValue());
			return;
		}

		if (args.startsWith(SIZE))
		{
			int size = Bot.dictionary.getSize();
			if (size == -1)
				adapter.send(sender + ": I haven't a clue.");
			else
				adapter.send(sender + ": " + Bot.dictionary.getSize() + " entries");
			return;
		}
		
		if (argsArr.length < 2)
		{
			adapter.send("Not Found: " + argsArr[0]);
			return;
		}
		
		if (args.startsWith(INFO))
		{
			DictionaryItem item = Bot.dictionary.get(argsArr[1]);
			if (item == null)
				adapter.send("What is " + argsArr[1] + "?");
			else
				adapter.send("\"" + argsArr[1] + "\" was added by \"" + item.getAddedBy()
						+ "\" on " + new Date(item.getDate()));
			return;
		}

		if (args.startsWith(FORGET))
		{
			if (Bot.dictionary.has(argsArr[1]))
			{
				try
				{
					Bot.dictionary.remove(argsArr[1]);
					adapter.send("OK o.0");
				}
				catch (IOException e)
				{
					adapter.send("That I cannot forget! ;_;");
				}
				catch (ConfigurationException e)
				{
					adapter.send("That I cannot forget! ;_;");
					e.printStackTrace();
				}

			}
			else
				adapter.send("I never knew " + argsArr[1]);
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
		return "allows you to define something and retrieve it later";
	}
}