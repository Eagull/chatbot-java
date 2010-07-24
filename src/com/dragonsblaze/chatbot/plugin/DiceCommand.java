package com.dragonsblaze.chatbot.plugin;

import com.dragonsblaze.chatbot.adapter.Adapter;

public class DiceCommand extends Command
{
	private static final int MAX_COUNT = 20;
	
	@Override
	public void process(String sender, String args, Adapter adapter)
	{
		String[] params = args.trim().split(" |d");
		String rolls = "";

		int count = 1, sides = 6, result = 0;

		try
		{
			count = Integer.parseInt(params[0]);
			sides = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e)
		{
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		}

		if (count < 1 || sides < 1 || count > MAX_COUNT)
		{
			adapter.send(sender + ": " + "Inifinity!");
			return;
		}

		for (int i = 0; i < count; i++)
		{
			int roll = (int) (sides * Math.random() + 1);
			rolls += ", " + roll;
			result += roll;
		}

		if (count == 1)
			adapter.send(sender + ": " + "Rolled a " + sides + "-sided die: " + result);
		else
			adapter.send(sender + ": " + "Rolled " + count + " counts of " + sides + "-sided dice: {"
					+ rolls.substring(2) + "} : " + result);
	}
	
	@Override
	public String getHelpText()
	{
		return "dice c n - throws c counts of n-sided dice and reports the outcome";
	}
}