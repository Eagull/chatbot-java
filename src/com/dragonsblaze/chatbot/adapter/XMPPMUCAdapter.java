package com.dragonsblaze.chatbot.adapter;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import com.dragonsblaze.chatbot.Bot.Authorization;

public class XMPPMUCAdapter extends Adapter
{
	private static XMPPConnection connection;

	private static String resource = null;

	private MultiUserChat chat;

	// private Set<String> ignoreJids = new HashSet<String>();

	public XMPPMUCAdapter(String room, String nickname)
	{
		super(room, nickname);
		if (resource == null)
			resource = "bot" + room.hashCode();
	}

	@Override
	public MultiUserChat getChat()
	{
		return chat;
	}

	@Override
	public void take(Action action, String targetNick, String args) throws XMPPException
	{
		switch (action)
		{
			case kick:
				chat.kickParticipant(targetNick, args);
				break;
			case voice:
				if (args.startsWith("0"))
					chat.revokeVoice(targetNick);
				else
					chat.grantVoice(targetNick);
				break;
			case membership:
				if (args.startsWith("0"))
					chat.revokeMembership(getJid(targetNick));
				else
					chat.grantMembership(getJid(targetNick));
				break;
			case message:
				send(targetNick, args);
			default:
				System.err.println("Not Implemented!");
				break;
		}
	}

	public String getJid(String nickname)
	{
		if (nickname.indexOf("@") > 0)
			return nickname;

		Occupant occupant = chat.getOccupant(roomName + "/" + nickname);
		if (occupant == null)
			return null;

		return occupant.getJid();
	}

	@Override
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

			connection.login(username, password, resource);
			System.out.println("Logged in");
		}
		catch (XMPPException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void join()
	{
		chat = new MultiUserChat(connection, roomName);
		try
		{
			DiscussionHistory history = new DiscussionHistory();
			history.setSeconds(0);
			chat.join(nickname, null, history, 10000);
			System.out.println("Joined: " + roomName);
		}
		catch (XMPPException e)
		{
			System.err.println("join(): " + e.getMessage());
		}
	}

	@Override
	public void addMessageListener(Object listener)
	{
		chat.addMessageListener((PacketListener) listener);
	}

	// TODO: @Override
	public void addPresenceListener(Object listener)
	{

	}

	@Override
	public Authorization getAuthLevel(String nick)
	{
		if (nick.indexOf('@') == -1)
			nick = this.roomName + "/" + nick;

		Occupant occupant = chat.getOccupant(nick);

		if (occupant == null)
			return Authorization.NONE;
		else if (occupant.getAffiliation().equals("owner"))
			return Authorization.OWNER;
		else if (occupant.getAffiliation().equals("admin"))
			return Authorization.ADMIN;
		else if (occupant.getRole().equals("moderator"))
			return Authorization.MODERATOR;
		else if (occupant.getAffiliation().equals("member"))
			return Authorization.MEMBER;
		else if (occupant.getAffiliation().equals("participant"))
			return Authorization.PARTICIPANT;
		else if (occupant.getRole().equals("visitor"))
			return Authorization.VISITOR;

		return Authorization.NONE;
	}

	@Override
	public boolean isSuperior(String first, String second, boolean strict)
	{
		int difference = getAuthLevel(first).compareTo(getAuthLevel(second));
		if (strict)
			return (difference > 0);
		else
			return (difference >= 0);
	}

	@Override
	public void send(String message)
	{
		try
		{
			chat.sendMessage(message);
			System.out.println("Sent: " + message);
		}
		catch (XMPPException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void send(String to, String message)
	{
		if (to == null)
			send(message);
		else
			send(to + ": " + message);
	}

	@Override
	public void sendPrivate(String destination, String message)
	{
		try
		{
			System.err.println("NOT IMPLEMENTED: " + getClass().getName() + ".sendPrivate()");
			chat.createPrivateChat(destination, null).sendMessage(message);
			chat.sendMessage(new Message(destination));
		}
		catch (XMPPException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void grantVoice(String nickname)
	{
		try
		{
			chat.grantVoice(nickname);
		}
		catch (XMPPException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void revokeVoice(String nickname)
	{
		try
		{
			chat.revokeVoice(nickname);
		}
		catch (XMPPException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void grantMembership(String nickname)
	{
		try
		{
			chat.grantMembership(getJid(nickname));
		}
		catch (XMPPException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void kickParticipant(String nickname, String reason)
	{
		try
		{
			chat.kickParticipant(nickname, reason);
		}
		catch (XMPPException e)
		{
			System.err.println(e.getMessage());

			switch (e.getXMPPError().getCode())
			{
				case 406:
					// nickname not found
					break;
				case 403:
					// insufficient bot permissions
					break;
				case 405:
					// target unkickable
					break;
				default:
			}
		}
	}

	@Override
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