/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.ChatUtils;

public final class PacketCanceler extends Hack {
	private final CheckboxSetting Abilities =
			new CheckboxSetting("Abilities",
					false);

	private final CheckboxSetting Rotation =
			new CheckboxSetting("Rotation",
					false);

	private final CheckboxSetting Animation =
			new CheckboxSetting("Animation",
					false);

	private final CheckboxSetting CPacketPlayer =
			new CheckboxSetting("CPacketPlayer",
					false);

	public PacketCanceler() {
		super("PacketCanceler", "Cancel packets that you desire!.");
		setCategory(Category.MISC);
		addSetting(Abilities);
		addSetting(Animation);
		addSetting(CPacketPlayer);
		addSetting(Rotation);
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
		if (CPacketPlayer.isChecked())
			if (event.getPacket() instanceof CPacketPlayer)
				event.setCanceled(true);
		if (Animation.isChecked())
			if (event.getPacket() instanceof CPacketAnimation)
				event.setCanceled(true);
		if (Rotation.isChecked())
			if (event.getPacket() instanceof SPacketEntity.S16PacketEntityLook)
				event.setCanceled(true);
		if (Abilities.isChecked())
			if (event.getPacket() instanceof CPacketPlayerAbilities)
				event.setCanceled(true);
	}
}

