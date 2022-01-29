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
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.*;
import net.wurstclient.forge.utils.PlayerUtils;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.minecraft.client.Minecraft;
import net.wurstclient.forge.compatibility.WEntity;
import net.minecraft.util.math.Vec3d;
import net.wurstclient.forge.settings.EnumSetting;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.wurstclient.forge.utils.ChatUtils;

public final class EntitySpeed extends Hack {
	private final SliderSetting speed =
			new SliderSetting("Speed", 1, 0.05, 1.0, 0.005, SliderSetting.ValueDisplay.DECIMAL);

	public EntitySpeed() {
		super("EntitySpeed", "Speed with Entity's.");
		setCategory(Category.MOVEMENT);
		addSetting(speed);
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
		if (!mc.player.isRiding()) return;
		if (!ForgeWurst.getForgeWurst().getHax().entitySpeed.isEnabled()) {
			if (mc.gameSettings.keyBindForward.isKeyDown() && mc.player.getRidingEntity().onGround) {
				float yaw = Minecraft.getMinecraft().player.getRidingEntity().rotationYaw;
				float pitch = Minecraft.getMinecraft().player.getRidingEntity().rotationPitch;
				Minecraft.getMinecraft().player.getRidingEntity().motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed.getValue();
				Minecraft.getMinecraft().player.getRidingEntity().motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed.getValue();
			}
		} else {
			if (mc.gameSettings.keyBindForward.isKeyDown()) {
				float yaw = Minecraft.getMinecraft().player.getRidingEntity().rotationYaw;
				float pitch = Minecraft.getMinecraft().player.getRidingEntity().rotationPitch;
				Minecraft.getMinecraft().player.getRidingEntity().motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed.getValue();
				Minecraft.getMinecraft().player.getRidingEntity().motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed.getValue();
			}
		}
	}
}