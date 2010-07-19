package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class KickCommand extends Command
{
	public KickCommand(Adapter adapter)
	{
		super(adapter);
	}

	@Override
	public void process(String sender, String args)
	{
		System.out.println("* Kick requested:\t" + sender + " -> " + args);
		adapter.kickParticipant(args, "Requested by " + sender);
	}
}