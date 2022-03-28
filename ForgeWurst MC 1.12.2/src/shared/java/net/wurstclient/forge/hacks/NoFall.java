/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.EnumSetting;

public final class NoFall extends Hack {

	private final EnumSetting<Mode> mode =
			new EnumSetting<>("Mode", Mode.values(), Mode.PACKET);

	private final CheckboxSetting ncp =
			new CheckboxSetting("NCP-Strict",
					false);

	public NoFall() {
		super("NoFall", "Prevents falling damage/falling.");
		setCategory(Category.PLAYER);
		addSetting(mode);
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
		if (mode.getSelected().packet) {
			if (mc.player.fallDistance > 4) {
				mc.player.connection.sendPacket(new CPacketPlayer(true));
			}
		}

		if (mode.getSelected().anti) {
			if (mc.player.fallDistance > 4 && !mc.player.onGround) {
				mc.player.motionY -= 10;
			}
		}
	}

	private enum Mode {
		PACKET("Packet", true, false),
		ANTI("Anti", false, true);

		private final String name;
		private final boolean packet;
		private final boolean anti;

		private Mode(String name, boolean packet, boolean anti) {
			this.name = name;
			this.anti = anti;
			this.packet = packet;
		}

		public String toString() {
			return name;
		}
	}
}