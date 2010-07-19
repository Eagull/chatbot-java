package com.dragonsblaze.chatbot.plugin;

import java.util.Date;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class DateCommand extends Command
{
	public DateCommand(Adapter adapter)
	{
		super(adapter);
	}

	@Override
	public void process(String sender, String args)
	{
		String text = new Date().toString();
		adapter.send(sender + ": " + text);
		System.out.println("* Sent:\t" + text);
	}
}