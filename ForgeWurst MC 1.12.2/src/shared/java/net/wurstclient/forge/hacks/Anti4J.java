/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.*;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.utils.ChatUtils;

public final class Anti4J extends Hack {
	public Anti4J() {
		super("Anti4J", "Prevents the Log4j Exploit.");
		setCategory(Category.EXPLOIT);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onChat(WChatOutputEvent event) {
		if (event.getMessage().contains("/") || event.getMessage().contains("-") || event.getMessage().contains("jdni:ldap")) {
			mc.world.sendQuittingDisconnectingPacket();
			mc.currentScreen = null;
			mc.player.connection.sendPacket(new SPacketDisconnect());
			mc.player.connection.sendPacket(new net.minecraft.network.login.server.SPacketDisconnect());
		}
	}

	@SubscribeEvent
	public void onChat1(WChatInputEvent event) {
		if (event.getChatLines().contains("/") || event.getChatLines().contains("-") || event.getChatLines().contains("jdni:ldap")) {
			mc.world.sendQuittingDisconnectingPacket();
			mc.currentScreen = null;
			mc.player.connection.sendPacket(new SPacketDisconnect());
		}
	}
}
