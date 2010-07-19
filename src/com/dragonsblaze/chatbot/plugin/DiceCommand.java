package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class DiceCommand extends Command
{
	public DiceCommand(Adapter adapter)
	{
		super(adapter);
	}

	@Override
	public void process(String sender, String args)
	{
		adapter.send(args);
	}
}