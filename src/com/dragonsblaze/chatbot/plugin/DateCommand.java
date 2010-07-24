package com.dragonsblaze.chatbot.plugin;

import java.util.Date;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class DateCommand extends Command
{
	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		adapter.send(sender + ": " + new Date().toString());
	}
	
	@Override
	public String getHelpText()
	{
		return "print the current date and time";
	}
}
