package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.adapter.Adapter;

public abstract class Command
{
	protected Adapter adapter;

	public Command(Adapter adapter)
	{
		this.adapter = adapter;
	}
	
	public abstract void process(String sender, String args);
}