package com.dragonsblaze.chatbot.adapter;

import com.dragonsblaze.chatbot.Bot.Authorization;

public abstract class Adapter
{
	protected String roomName, nickname;

	public enum Action
	{
		message, kick, voice, membership,
	}

	public Adapter(String roomName, String nickname)
	{
		this.roomName = roomName;
		this.nickname = nickname;
	}

	public String getRoomName()
	{
		return roomName;
	}

	public abstract Object getChat();

	public abstract void take(Action action, String target, String args) throws Exception;

	public abstract void connect(String server, String username, String password, int port);

	public abstract void join();

	public abstract void addMessageListener(Object listener);

	public abstract Authorization getAuthLevel(String nickname);

	public abstract boolean isSuperior(String first, String second, boolean strict);

	public abstract void send(String message);

	public abstract void send(String to, String message);

	public abstract void sendPrivate(String destination, String message);

	public abstract void grantVoice(String nickname);

	public abstract void revokeVoice(String nickname);

	public abstract void grantMembership(String nickname);

	public abstract void kickParticipant(String nickname, String reason);

	public abstract void disconnect();
}