package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class SayCommand extends Command
{
	public SayCommand(Adapter adapter)
	{
		super(adapter);
	}

	@Override
	public void process(String sender, String args)
	{
		adapter.send(args);
	}
}