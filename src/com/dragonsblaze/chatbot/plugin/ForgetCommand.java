package com.dragonsblaze.chatbot.plugin;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;

import com.dragonsblaze.chatbot.Bot;
import com.dragonsblaze.chatbot.adapter.Adapter;

public class ForgetCommand extends Command
{
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
			try
			{
				Bot.dictionary.remove(argsArr[0]);
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
		else
			adapter.send("I never knew " + argsArr[1]);
		return;
	}

	@Override
	public String getHelpText()
	{
		return "Forgets a definition. Syntax: forget term";
	}
}