package com.dragonsblaze.chatbot.adapter;

public abstract class Adapter
{
	protected String room, nickname;

	public Adapter(String room, String nickname)
	{
		this.room = room;
		this.nickname = nickname;
	}

	public abstract void connect(String server, String username, String password, int port);

	public abstract void join();

	public abstract void addMessageListener(String room, Object listener);

	public abstract void send(String message);

	public abstract void kickParticipant(String nickname, String reason);

	public abstract void disconnect();
}