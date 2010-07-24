package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.Bot.Authorization;
import com.dragonsblaze.chatbot.adapter.Adapter;

public class DevoiceCommand extends Command
{
	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		String target = args.split(" ", 2)[0];
		if(target.isEmpty())
			return;
		
		// if target is admin or above
		if(adapter.getAuthLevel(target).compareTo(Authorization.ADMIN) >= 0)
			adapter.send(sender + ": Are you nuts??");
		
		// if target is self
		else if (sender.equals(target))
		{
			adapter.send(target + ": you're so dumb!");
			adapter.revokeVoice(target);
		}

		// if target is superior or equal
		else if(adapter.isSuperior(target, sender, false))
			adapter.send("/me kicks " + sender + "!!");
		
		else
			adapter.revokeVoice(target);
		
		System.out.println("Voice:\t" + sender + " -> " + target);
		adapter.revokeVoice(target);
	}
}