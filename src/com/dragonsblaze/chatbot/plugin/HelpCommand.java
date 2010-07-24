package com.dragonsblaze.chatbot.plugin;

import java.util.Map;

import com.dragonsblaze.chatbot.Bot.Authorization;
import com.dragonsblaze.chatbot.adapter.Adapter;
import com.dragonsblaze.chatbot.handler.CommandHandler;

public class HelpCommand extends Command
{
	CommandHandler handler;

	public HelpCommand(CommandHandler handler)
	{
		this.handler = handler;
	}

	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		Map<String, Authorization> commandAuth = handler.getCommandAuth();
		Authorization senderAuth = adapter.getAuthLevel(sender);
		
		if (!args.isEmpty())
		{
			String commandName = args.split(" ")[0];
			String helpText = handler.getCommands().get(commandName).getHelpText();
			if (helpText == null)
				adapter.send(sender + ": I don't really know, so maybe ask around. Someone might know. Maybe.");
			else if (senderAuth.compareTo(commandAuth.get(commandName)) >= 0)
				adapter.send(commandName + " - " + helpText);
			else
				adapter.send("You're so helpless!!");
			return;
		}

		Map<String, Command> commands = handler.getCommands();

		StringBuffer commandList = new StringBuffer();
		for (String commandName : commands.keySet())
			if (senderAuth.compareTo(commandAuth.get(commandName)) >= 0)
				commandList.append(", ").append(commandName);

		if (commandList.length() == 0)
			adapter.send("Bad, bad " + sender + "!! No donut for you!");
		else
			adapter.send("Available commands: " + commandList.substring(2));

	}
}