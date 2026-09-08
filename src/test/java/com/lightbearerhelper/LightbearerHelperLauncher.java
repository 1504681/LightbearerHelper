package com.lightbearerhelper;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

/**
 * Local development entry point: starts a full RuneLite client with this plugin registered.
 * Run with {@code ./gradlew run} (or from the IDE with the {@code -ea} VM flag).
 */
public class LightbearerHelperLauncher
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(LightbearerHelperPlugin.class);
		RuneLite.main(args);
	}
}
