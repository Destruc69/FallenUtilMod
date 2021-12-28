/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WChatInputEvent;
import net.wurstclient.fmlevents.WChatOutputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;

import javax.swing.*;

public final class Anti4J extends Hack {

	private final CheckboxSetting strict =
			new CheckboxSetting("Strict",
					true);

	public Anti4J() {
		super("Anti4J", "Prevents the Log4j Exploit.");
		setCategory(Category.EXPLOIT);
		addSetting(strict);
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

			JFrame fr = new JFrame();

			JButton b1 = new JButton("[FALLEN] Characters containing Log4j characters where sent/received, We left the game in-case but please run your AntiVirus scans ASAP");

			b1.setBounds(250,200,100,30);
			fr.add(b1);
		}

		if (strict.isChecked()) {
			if (event.getMessage().contains("http") || event.getMessage().contains(":") || event.getMessage().contains(".") || event.getMessage().contains("{")) {
				mc.world.sendQuittingDisconnectingPacket();
				mc.currentScreen = null;
				mc.player.connection.sendPacket(new SPacketDisconnect());
				mc.player.connection.sendPacket(new net.minecraft.network.login.server.SPacketDisconnect());

				JFrame fr = new JFrame();

				JButton b1 = new JButton("[FALLEN] Characters containing Log4j characters where sent/received, We left the game in-case but please run your AntiVirus scans ASAP");

				b1.setBounds(250,200,100,30);
				fr.add(b1);
			}
		}
	}

	@SubscribeEvent
	public void onChat1(WChatInputEvent event) {
		if (event.getChatLines().contains("/") || event.getChatLines().contains("-") || event.getChatLines().contains("jdni:ldap")) {
			mc.world.sendQuittingDisconnectingPacket();
			mc.currentScreen = null;
			mc.player.connection.sendPacket(new SPacketDisconnect());

			JFrame fr = new JFrame();

			JButton b1 = new JButton("[FALLEN] Characters containing Log4j characters where sent/received, We left the game in-case but please run your AntiVirus scans ASAP");

			b1.setBounds(250,200,100,30);
			fr.add(b1);
		}


		if (strict.isChecked()) {
			if (event.getChatLines().contains("http") || event.getChatLines().contains(":") || event.getChatLines().contains(".") || event.getChatLines().contains("{")) {
				mc.world.sendQuittingDisconnectingPacket();
				mc.currentScreen = null;
				mc.player.connection.sendPacket(new SPacketDisconnect());

				JFrame fr = new JFrame();

				JButton b1 = new JButton("[FALLEN] Characters containing Log4j characters where sent/received, We left the game in-case but please run your AntiVirus scans ASAP");

				b1.setBounds(250,200,100,30);
				fr.add(b1);
			}
		}
	}

	@SubscribeEvent
	public void refresh(WUpdateEvent event) {
		mc.ingameGUI.getChatGUI().refreshChat();
		mc.ingameGUI.getChatGUI().resetScroll();
	}
}
