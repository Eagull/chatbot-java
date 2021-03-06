package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.Bot.Authorization;
import com.dragonsblaze.chatbot.adapter.Adapter;

public class KickCommand extends Command
{
	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		String[] argsArr = args.split(" ", 2);
		String target = argsArr[0];
		if(target.isEmpty())
			return;
		
		String reason = (argsArr.length > 1) ? argsArr[1] : "Because " + sender + " said so.";
		
		System.out.println("* Kick:\t" + sender + " -> " + target + " (" + reason + ")");

		// if target is admin or above
		if(adapter.getAuthLevel(target).compareTo(Authorization.ADMIN) >= 0)
			adapter.send(sender + ": Are you nuts??");
		
		// if target is self
		else if (sender.equals(target))
			adapter.kickParticipant(target, "You get what you want.");

		// if target is superior or equal
		else if(adapter.isSuperior(target, sender, false))
			adapter.send("/me kicks " + target + "!!");
		
		else
			adapter.kickParticipant(target, reason);
	}
}