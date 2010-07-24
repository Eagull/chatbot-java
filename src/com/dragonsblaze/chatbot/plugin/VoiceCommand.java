package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class VoiceCommand extends Command
{
	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		String target = args.split(" ", 2)[0];
		if(target.isEmpty())
			return;
		
		System.out.println("Voice:\t" + sender + " -> " + target);
		adapter.grantVoice(target);
	}
	
	@Override
	public String getHelpText()
	{
		return "grant voice to someone";
	}
}