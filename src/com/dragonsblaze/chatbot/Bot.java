package com.dragonsblaze.chatbot;

/**
 * @author aaditya
 */
import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.dragonsblaze.chatbot.adapter.Adapter;
import com.dragonsblaze.chatbot.adapter.XMPPMUCAdapter;
import com.dragonsblaze.chatbot.handler.XMPPCommandHandler;
import com.dragonsblaze.chatbot.plugin.DateCommand;
import com.dragonsblaze.chatbot.plugin.DefineCommand;
import com.dragonsblaze.chatbot.plugin.DevoiceCommand;
import com.dragonsblaze.chatbot.plugin.DiceCommand;
import com.dragonsblaze.chatbot.plugin.DictCommand;
import com.dragonsblaze.chatbot.plugin.ExpandCommand;
import com.dragonsblaze.chatbot.plugin.ForgetCommand;
import com.dragonsblaze.chatbot.plugin.GoogleCommand;
import com.dragonsblaze.chatbot.plugin.HelpCommand;
import com.dragonsblaze.chatbot.plugin.JidCommand;
import com.dragonsblaze.chatbot.plugin.KickCommand;
import com.dragonsblaze.chatbot.plugin.ModeCommand;
import com.dragonsblaze.chatbot.plugin.OmegleCommand;
import com.dragonsblaze.chatbot.plugin.QuoteCommand;
import com.dragonsblaze.chatbot.plugin.SayCommand;
import com.dragonsblaze.chatbot.plugin.VoiceCommand;
import com.dragonsblaze.chatbot.plugin.XkcdCommand;

public class Bot
{
	private static String nickname;

	private static Adapter adapter;
	
	public static Dictionary dictionary;

	public enum Authorization
	{
		NONE, VISITOR, PARTICIPANT, MEMBER, MODERATOR, ADMIN, OWNER;
	};

	public static void main(String[] args)
	{
		if(args.length < 2)
		{
			System.err.println("Required: config dictionary");
			System.exit(1);
		}
		
		new Bot(args[0], args[1]);

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

	public Bot(String configFilename, String dictionaryFilename)
	{
		Configuration config = null;
		try
		{
			config = new PropertiesConfiguration(configFilename);
			dictionary = new Dictionary(dictionaryFilename);
		}
		catch (ConfigurationException e)
		{
			String[] messages = e.getMessages();
			for (String message : messages)
			{
				System.err.println(message);
			}
			System.exit(2);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		String server = config.getString("server");
		int port = config.getInt("port");
		String username = config.getString("username");
		String password = config.getString("password");
		String room = config.getString("room");
		nickname = config.getString("nickname");
		adapter = new XMPPMUCAdapter(room, nickname);

		adapter.connect(server, username, password, port);
		adapter.join();

		XMPPCommandHandler commandHandler = new XMPPCommandHandler(adapter);
		adapter.addMessageListener(commandHandler);

//		commandHandler.setNotFoundPlugin(new DictCommand());
		commandHandler.addPlugin("help", new HelpCommand(commandHandler), Authorization.NONE);
		
		commandHandler.addPlugin("mode", new ModeCommand(), Authorization.ADMIN);

		commandHandler.addPlugin("jid", new JidCommand(), Authorization.MODERATOR);

		commandHandler.addPlugin("kick", new KickCommand(), Authorization.MEMBER);
		commandHandler.addPlugin("voice", new VoiceCommand(), Authorization.MEMBER);
		commandHandler.addPlugin("devoice", new DevoiceCommand(), Authorization.MEMBER);
		commandHandler.addPlugin("google", new GoogleCommand(), Authorization.MEMBER);
		commandHandler.addPlugin("define", new DefineCommand("define"), Authorization.MEMBER);
		commandHandler.addPlugin("forget", new ForgetCommand(), Authorization.ADMIN);
		commandHandler.addPlugin("dict", new DictCommand(), Authorization.ADMIN);
		commandHandler.addPlugin("omegle", new OmegleCommand(), Authorization.MEMBER);
		
		commandHandler.addPlugin("say", new SayCommand(), Authorization.VISITOR);
		commandHandler.addPlugin("xkcd", new XkcdCommand(), Authorization.VISITOR);
		
		commandHandler.addPlugin("dice", new DiceCommand(), Authorization.NONE);
		commandHandler.addPlugin("quote", new QuoteCommand(), Authorization.NONE);
		commandHandler.addPlugin("expand", new ExpandCommand(), Authorization.NONE);
		commandHandler.addPlugin("date", new DateCommand(), Authorization.NONE);

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				adapter.disconnect();
			}
		});
	}

	public static String getNickname()
	{
		return nickname;
	}
}