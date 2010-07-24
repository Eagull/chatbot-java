package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.adapter.Adapter;
import com.dragonsblaze.chatbot.adapter.XMPPMUCAdapter;

public class JidCommand extends Command
{
	@Override
	public void process(String sender, String args, Adapter adapter)
	{
//		if(!(adapter instanceof XMPPMUCAdapter))
			adapter.send(((XMPPMUCAdapter)adapter).getJid(args));
//		else
//			System.out.println("* Not an XMPP MUC");
	}
}