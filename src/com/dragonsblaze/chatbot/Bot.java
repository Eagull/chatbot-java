package com.dragonsblaze.chatbot;

import com.dragonsblaze.chatbot.adapter.Adapter;
import com.dragonsblaze.chatbot.adapter.XMPPMUCAdapter;
import com.dragonsblaze.chatbot.plugin.DateCommand;
import com.dragonsblaze.chatbot.plugin.KickCommand;
import com.dragonsblaze.chatbot.plugin.SayCommand;

public class Bot
{
	private static final String SERVER = "example.org";

	private static final int PORT = 5222;
	
	private static final String ROOM = "firemoth@chat.speeqe.com";

	private static final String USERNAME = "";

	private static final String PASSWD = "";

	public static final String NICKNAME = "nickname";

	static Adapter adapter;

	public static void main(String[] args)
	{
		new Bot();

		try
		{
			while (true)
				Thread.sleep(100000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public Bot()
	{
		adapter = new XMPPMUCAdapter(ROOM, NICKNAME);
		adapter.connect(SERVER, USERNAME, PASSWD, PORT);
		adapter.join();

		XMPPCommandHandler commandHandler = new XMPPCommandHandler();
		adapter.addMessageListener(ROOM, commandHandler);
		
		commandHandler.addPlugin("date", new DateCommand(adapter));
		commandHandler.addPlugin("kick", new KickCommand(adapter));
		commandHandler.addPlugin("say", new SayCommand(adapter));
		
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				adapter.disconnect();
			}
		});
	}
}