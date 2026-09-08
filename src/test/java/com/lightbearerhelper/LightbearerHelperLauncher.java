package com.lightbearerhelper;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

// ./gradlew run, or run this from the IDE with -ea
public class LightbearerHelperLauncher
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(LightbearerHelperPlugin.class);
		RuneLite.main(args);
	}
}
