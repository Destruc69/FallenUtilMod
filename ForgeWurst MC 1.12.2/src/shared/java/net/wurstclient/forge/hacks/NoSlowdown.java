/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.PlayerUtils;


public final class NoSlowdown extends Hack {

	private final CheckboxSetting NCP =
			new CheckboxSetting("NCP Strict",
					true);

	private final CheckboxSetting item =
			new CheckboxSetting("Items",
					true);

	private final CheckboxSetting block =
			new CheckboxSetting("Blocks",
					true);

	public NoSlowdown() {
		super("NoSlowDown", "Don't slow down with items.");
		setCategory(Category.MOVEMENT);
		addSetting(NCP);
		addSetting(item);
		addSetting(block);
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
	public void WUpdateEvent(WUpdateEvent event) {

		if (NCP.isChecked())
			mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.ABORT_DESTROY_BLOCK, PlayerUtils.GetLocalPlayerPosFloored(), EnumFacing.DOWN));

		if (item.isChecked()) {
			if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFood && mc.player.isHandActive()) {
				float yaw = Minecraft.getMinecraft().player.rotationYaw;
				float pitch = Minecraft.getMinecraft().player.rotationPitch;
				Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.08;
				Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.08;
			}

			if (block.isChecked()) {
				if (mc.player.onGround && mc.player.moveForward == 0f && mc.player.moveStrafing == 0) {
					mc.player.setVelocity(0, 0, 0);
				}
			}
		}
	}
}