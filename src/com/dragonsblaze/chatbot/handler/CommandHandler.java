package com.dragonsblaze.chatbot.handler;

import java.util.HashMap;
import java.util.Map;

import com.dragonsblaze.chatbot.Bot.Authorization;
import com.dragonsblaze.chatbot.adapter.Adapter;
import com.dragonsblaze.chatbot.plugin.Command;

public class CommandHandler
{
	protected static final String PREFIX = "!";
	protected Map<String, Command> commands = new HashMap<String, Command>();
	protected Map<String, Authorization> commandAuth = new HashMap<String, Authorization>();
	protected Command notFound;

	protected Adapter adapter;
	
	public CommandHandler(Adapter adapter)
	{
		this.adapter = adapter;
	}
	
	public void addPlugin(String commandName, Command command, Authorization auth)
	{
		this.commands.put(commandName, command);
		this.commandAuth.put(commandName, auth);
	}
	
	public void setNotFoundPlugin(Command command)
	{
		this.notFound = command;
	}
	
	protected void notFound(String sender, String commandName, String args)
	{
		if(notFound == null)
		{
			adapter.send("Wait, what?");
			System.err.println("Not Found: " + commandName);
			return;
		}
		else
		{
			notFound.process(sender, commandName, args, adapter);
		}
	}
	
	public Map<String, Command> getCommands()
	{
		return commands;
	}
	
	public Map<String, Authorization> getCommandAuth()
	{
		return commandAuth;
	}
}