/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.TimerUtils;

public final class PacketSender extends Hack {

	private final SliderSetting delay =
			new SliderSetting("Delay [MS]", 1000, 0, 10000, 1000, SliderSetting.ValueDisplay.DECIMAL);


	private final CheckboxSetting onGround =
			new CheckboxSetting("CPacketPlayer [ON-GROUND]",
					false);

	private final CheckboxSetting notOnGround =
			new CheckboxSetting("CPacketPlayer [ [NOT] ON-GROUND]",
					false);

	public PacketSender() {
		super("PacketSender", "Send packets that you desire!.");
		setCategory(Category.MISC);
		addSetting(onGround);
		addSetting(notOnGround);
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
	public void onPacketInput(WPacketInputEvent event) {
		if (onGround.isChecked()) {
			if (TimerUtils.hasReached(delay.getValueI(), true)) {
				mc.player.connection.sendPacket(new CPacketPlayer(true));
			}
		}
		if (notOnGround.isChecked()) {
			if (TimerUtils.hasReached(delay.getValueI(), true)) {
				mc.player.connection.sendPacket(new CPacketPlayer(false));
			}
		}
	}
}