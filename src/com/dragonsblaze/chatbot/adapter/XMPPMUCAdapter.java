package com.dragonsblaze.chatbot.adapter;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class XMPPMUCAdapter extends Adapter
{
	private static XMPPConnection connection;

	private static final String RESOURCE = "bot";

	private MultiUserChat chat;

	// private Set<String> ignoreJids = new HashSet<String>();

	public XMPPMUCAdapter(String room, String nickname)
	{
		super(room, nickname);
	}

	public void connect(String server, String username, String password, int port)
	{
		if (connection != null && connection.isConnected())
			return;

		ConnectionConfiguration config = new ConnectionConfiguration(server, port);
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);

		try
		{
			connection = new XMPPConnection(config);

			connection.connect();
			System.out.println("Connected");

			connection.login(username, password, RESOURCE);
			System.out.println("Logged in");
		}
		catch (XMPPException e)
		{
			e.printStackTrace();
		}
	}

	public void join()
	{
		chat = new MultiUserChat(connection, room);
		try
		{
			DiscussionHistory history = new DiscussionHistory();
			history.setSeconds(0);
			chat.join(nickname, null, history, 10000);
			System.out.println("Joined: " + room);
		}
		catch (XMPPException e)
		{
			System.err.println("join(): " + e.getMessage());
		}
	}
	
	public void addMessageListener(String room, Object listener)
	{
		chat.addMessageListener((PacketListener) listener);
	}
	
	public void send(String message)
	{
		try
		{
			chat.sendMessage(message);
		}
		catch (XMPPException e)
		{
			e.printStackTrace();
		}
	}
	
	public void kickParticipant(String nickname, String reason)
	{
		try
		{
			chat.kickParticipant(nickname, reason);
		}
		catch (XMPPException e)
		{
			switch (e.getXMPPError().getCode())
			{
				case 406:
					System.err.println("Kick who?");
					break;
				case 403:
					System.err.println("I'm not allowed to kick anyone.");
					break;
				case 405:
					System.err.println("They've got too much power.");
					break;
				default:
					e.printStackTrace();
			}
		}
	}

	public void disconnect()
	{
		System.out.println("Disconnecting...");
		chat.leave();
		System.out.println("Parted: " + chat.getRoom());

		if (connection != null)
		{
			connection.disconnect();
			System.out.println("Disconnected");
		}
	}
}