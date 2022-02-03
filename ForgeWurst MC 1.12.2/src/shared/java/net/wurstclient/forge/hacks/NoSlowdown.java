/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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
		super("NoSlowDown", "Don't slow down with items / blocks.");
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
		if (NCP.isChecked() && item.isChecked()) {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.ABORT_DESTROY_BLOCK, PlayerUtils.GetLocalPlayerPosFloored(), EnumFacing.DOWN));
			mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
		}

		if (item.isChecked()) {
			if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFood && mc.player.isHandActive()) {

				float yaw = Minecraft.getMinecraft().player.rotationYaw;
				float pitch = Minecraft.getMinecraft().player.rotationPitch;

				if (!mc.player.isSprinting() && mc.player.onGround) {
					Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.04;
					Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.04;
				} else if (mc.player.isSprinting() && mc.player.onGround) {
					Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.010;
					Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.010;
				}

				if (mc.player.isAirBorne) {
					Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.01;
					Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.01;
				}

				if (mc.player.getHeldItemMainhand().getItem() instanceof Item && mc.player.isHandActive()) {
					if (!mc.player.isSprinting()) {
						Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.04;
						Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.04;
					} else if (mc.player.isSprinting()) {
						Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.016;
						Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.016;
					}
				}

				if (block.isChecked()) {
					if (mc.player.onGround && mc.player.moveForward == 0f && mc.player.moveStrafing == 0) {
						mc.player.setVelocity(0, 0, 0);
					}

					BlockPos blockpos = mc.player.getPosition();

					if (mc.world.getBlockState(blockpos.down()).getBlock() instanceof BlockSoulSand) {
						if (!mc.player.isSprinting()) {
							if (mc.gameSettings.keyBindForward.isKeyDown()) {
								Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.08;
								Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.08;
							} else if (mc.player.isSprinting()) {
								if (mc.gameSettings.keyBindForward.isKeyDown()) {
									Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.16;
									Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.16;
								}
							}
						}
					}
				}
			}
		}
	}
}