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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.ChatUtils;

public final class Strafe extends Hack {
	private final SliderSetting speed =
			new SliderSetting("Height", 1, 0.01, 1, 0.01, SliderSetting.ValueDisplay.DECIMAL);

	public Strafe() {
		super("Strafe", "Move in the air freely.");
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

		float yaw = Minecraft.getMinecraft().player.rotationYaw;
		float pitch = Minecraft.getMinecraft().player.rotationPitch;

		if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown() && mc.player.onGround)
			mc.player.motionY = speed.getValue();
		if (Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown() && mc.player.onGround)
			mc.player.motionY = speed.getValue();
		if (Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown() && mc.player.onGround)
			mc.player.motionY = speed.getValue();
		if (Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown() && mc.player.onGround)
			mc.player.motionY = speed.getValue();

		if (mc.player.isAirBorne && Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
			Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.03;
			Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.03;
		}
		if (mc.player.isAirBorne && Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown()) {
			Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.03;
			Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.03;
		}
		if (mc.player.isAirBorne && Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown()) {
			Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.03;
			Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.03;
		}
		if (mc.player.isAirBorne && Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown()) {
			Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.03;
			Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.03;
		}
	}
}