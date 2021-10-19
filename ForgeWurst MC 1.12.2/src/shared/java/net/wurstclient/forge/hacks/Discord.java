/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.EnumSetting;

public final class Discord extends Hack {

	public static DiscordRichPresence presence = new DiscordRichPresence();
	public static club.minnced.discord.rpc.DiscordRPC rpc = club.minnced.discord.rpc.DiscordRPC.INSTANCE;
	public static Thread thread;
	public static int index = 1;

	public Discord() {
		super("DiscordRPC", "Shows that your using Fallen.");
		setCategory(Category.MISC);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);

		DiscordEventHandlers handlers = new DiscordEventHandlers();
		rpc.Discord_Initialize("837013588396081233", handlers, true, "");
		presence.startTimestamp = System.currentTimeMillis() / 1000L;
		presence.details = presence.state = "Idle";
		presence.largeImageKey = "fallen";
		rpc.Discord_UpdatePresence(presence);

	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {

		presence.details = getUsername() + "|" + getWorld();
		presence.state = getBrand() + "|" + getVersion();
		presence.largeImageText = "Fallen Utility Mod";
		rpc.Discord_UpdatePresence(presence);

	}

	private static String getUsername() {
		return mc.player.getName();

	}

	private static String getWorld() {

		if (mc.isSingleplayer()) {
			return "Singleplayer";
		}

		if (!mc.isSingleplayer()) {
		}
		return "Multiplayer";

	}

	private static String getVersion() {

		return mc.getVersion();

	}

	private static String getBrand() {

		return String.valueOf(mc.getConnection().getNetworkManager().getRemoteAddress());

	}
}