package com.dragonsblaze.chatbot.handler;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import com.dragonsblaze.chatbot.Bot;
import com.dragonsblaze.chatbot.adapter.Adapter;

public class XMPPCommandHandler extends CommandHandler implements PacketListener
{
	public XMPPCommandHandler(Adapter adapter)
	{
		super(adapter);
	}

	@Override
	public void processPacket(Packet packet)
	{
		if (!(packet instanceof Message))
			return;

		Message message = (Message) packet;
		String messageText = message.getBody();

		if (messageText.length() > 512)
			return;

		if (StringUtils.parseResource(message.getFrom()).equals(Bot.getNickname()))
			return;

		int commandStartIndex, commandEndIndex;

		if (messageText.startsWith(PREFIX))
		{
			commandStartIndex = PREFIX.length();
			commandEndIndex = messageText.indexOf(' ');
			if (commandEndIndex == -1)
				commandEndIndex = messageText.length();
		}
		else if (messageText.startsWith(Bot.getNickname()))
		{
			messageText = messageText.substring(Bot.getNickname().length() + 1).trim();
			commandStartIndex = 0;
			commandEndIndex = commandStartIndex
					+ messageText.substring(commandStartIndex).indexOf(' ');

			if (commandEndIndex == -1)
				commandEndIndex = messageText.length();
		}
		else
			return;

		String commandName = messageText.substring(commandStartIndex, commandEndIndex);

		if (!commands.containsKey(commandName.toLowerCase()))
			notFound(StringUtils.parseResource(message.getFrom()), "", commandName + " "+ messageText
					.substring(commandEndIndex).trim());

		else if (commandAuth.get(commandName.toLowerCase()).compareTo(
				adapter.getAuthLevel(message.getFrom())) > 0)
		{
			adapter.send("STFU!!");
			System.err.println("Not authorized: " + commandName);
			System.err.println("\tSender: " + message.getFrom());
			System.err.println("\tHas: " + adapter.getAuthLevel(message.getFrom()));
			System.err.println("\tRequired: " + commandAuth.get(commandName).name());
		}
		else
			commands.get(commandName.toLowerCase()).process(StringUtils.parseResource(message.getFrom()),
					commandName, messageText.substring(commandEndIndex).trim(), adapter);
	}
}