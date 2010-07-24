package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.adapter.Adapter;
import com.dragonsblaze.chatbot.adapter.Adapter.Action;

public class ModeCommand extends Command
{
	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		String[] argsArr = args.split(" ");

		if (argsArr[0].isEmpty())
		{
			adapter.send(sender, getHelpText());
			return;
		}
		if (argsArr.length < 2)
		{
			String target = adapter.getRoomName() + "/" + argsArr[0];
			adapter.send(argsArr[0] + " => " + adapter.getAuthLevel(target).name().toLowerCase());
		}

		String target = argsArr[1];
		boolean grant = argsArr[0].charAt(0) == '+';
		char mode = argsArr[0].charAt(1);

		Action action;
		switch (mode)
		{
			case 'm':
				action = Action.membership;
				break;
			case 'v':
				action = Action.voice;
				break;
			default:
				adapter.send(sender, getHelpText());
				return;
		}

		try
		{
			adapter.take(action, target, grant ? "1" : "0");
			adapter.send(action + (grant ? " granted" : " revoked") + " - " + target);
		}
		catch (Exception e)
		{
			adapter.send(sender, "Something went wrong, sorry!");
			e.printStackTrace();
		}
	}

	@Override
	public String getHelpText()
	{
		return "update user mode";
	}
}