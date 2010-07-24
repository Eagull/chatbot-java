package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.adapter.Adapter;

public abstract class Command
{
	public abstract void process(String sender, String args, Adapter adapter);
	
	public void process(String sender, String command, String args, Adapter adapter)
	{
		process(sender, args, adapter);
	}
	
	public String getHelpText()
	{
		return null;
	}
}