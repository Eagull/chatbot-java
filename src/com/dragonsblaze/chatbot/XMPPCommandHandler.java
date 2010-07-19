package com.dragonsblaze.chatbot;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import com.dragonsblaze.chatbot.plugin.Command;

public class XMPPCommandHandler implements PacketListener
{
	private static final String PREFIX = "!";
	private Map<String, Command> commands = new HashMap<String, Command>();
	
	@Override
	public void processPacket(Packet packet)
	{
		if (!(packet instanceof Message))
			return;
		
		Message message = (Message) packet;
		String messageText = message.getBody();
		
		if (StringUtils.parseResource(message.getFrom()).equals(Bot.NICKNAME))
			return;
		
		if(!messageText.startsWith(PREFIX))
			return;
		
		int commandEndIndex = messageText.indexOf(' ');
		if(commandEndIndex == -1)
			commandEndIndex = messageText.length();
		
		String commandName = messageText.substring(1, commandEndIndex);
		
		if(commands.containsKey(commandName))
			commands.get(commandName).process(StringUtils.parseResource(message.getFrom()), messageText.substring(commandEndIndex).trim());
		else
			System.err.println("Command not found: " + commandName);
		
	}
	
	public Map<String, Command> getPlugins()
	{
		return commands;
	}
	
	public void addPlugin(String commandName, Command command)
	{
		this.commands.put(commandName, command);
	}
}