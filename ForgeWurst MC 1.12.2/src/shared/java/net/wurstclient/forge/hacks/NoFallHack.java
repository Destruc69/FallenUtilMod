/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;

public final class NoFallHack extends Hack {

	private final CheckboxSetting ncp =
			new CheckboxSetting("NCP New",
					false);

	public NoFallHack() {
		super("NoFall", "Protects you from fall damage.");
		setCategory(Category.MOVEMENT);
		addSetting(ncp);
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
	public void onUpdate(WUpdateEvent event) {
		if (event.getPlayer().fallDistance > 4) {
			mc.getConnection().sendPacket(new CPacketPlayer(true));
		}

		if (ncp.isChecked()) {
			if (mc.player.fallDistance == 4) {
				mc.world.sendQuittingDisconnectingPacket();
				mc.player.connection.sendPacket(new SPacketDisconnect());
				mc.player.connection.sendPacket(new net.minecraft.network.play.server.SPacketDisconnect());
			}
		}
	}
}
