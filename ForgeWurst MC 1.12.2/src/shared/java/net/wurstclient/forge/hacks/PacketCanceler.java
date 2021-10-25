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
			new CheckboxSetting("CPacketPlayerAbilities",
					false);

	private final CheckboxSetting Rotation =
			new CheckboxSetting("S16PacketEntityLook",
					false);

	private final CheckboxSetting Animation =
			new CheckboxSetting("CPacketAnimation",
					false);

	private final CheckboxSetting useEntity =
			new CheckboxSetting("CPacketUseEntity",
					false);

	private final CheckboxSetting teleport =
			new CheckboxSetting("CPacketConfirmTeleport",
					false);

	private final CheckboxSetting digging =
			new CheckboxSetting("CPacketPlayerDigging",
					false);

	public PacketCanceler() {
		super("PacketCanceler", "Cancel packets that you desire!.");
		setCategory(Category.MISC);
		addSetting(Abilities);
		addSetting(Animation);
		addSetting(Rotation);
		addSetting(useEntity);
		addSetting(teleport);
		addSetting(digging);
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
		if (Animation.isChecked())
			if (event.getPacket() instanceof CPacketAnimation) {
				event.setCanceled(true);
			}
		if (Rotation.isChecked()) {
			if (event.getPacket() instanceof SPacketEntity.S16PacketEntityLook)
				event.setCanceled(true);
		}
		if (Abilities.isChecked()) {
			if (event.getPacket() instanceof CPacketPlayerAbilities)
				event.setCanceled(true);
		}
		if (useEntity.isChecked()) {
			if (event.getPacket() instanceof CPacketUseEntity)
				event.setCanceled(true);
		}
		if (teleport.isChecked()) {
			if (event.getPacket() instanceof CPacketConfirmTeleport)
				event.setCanceled(true);
		}
		if (digging.isChecked()) {
			if (event.getPacket() instanceof CPacketPlayerDigging)
				event.setCanceled(true);
		}
	}
}
