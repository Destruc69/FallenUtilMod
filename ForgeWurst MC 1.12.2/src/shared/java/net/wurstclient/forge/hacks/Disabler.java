/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.EntityFakePlayer;
import net.wurstclient.forge.utils.TimerUtils;

public final class Disabler extends Hack {

	public Disabler() {
		super("Disabler", "Prevents anti-cheat flags.");
		setCategory(Category.MOVEMENT);
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
	public void onIn(WPacketInputEvent event) {
		if (event.getPacket() instanceof CPacketKeepAlive || event.getPacket() instanceof CPacketConfirmTransaction) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onOut(WPacketOutputEvent event) {
		if (event.getPacket() instanceof CPacketKeepAlive || event.getPacket() instanceof CPacketConfirmTransaction) {
			event.setCanceled(true);
		}
	}
}