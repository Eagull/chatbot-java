package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class SayCommand extends Command
{
	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		adapter.send(args);
	}
}