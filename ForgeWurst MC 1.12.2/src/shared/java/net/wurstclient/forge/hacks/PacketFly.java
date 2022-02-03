/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.*;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;

public final class PacketFly extends Hack {

	private final SliderSetting speed =
			new SliderSetting("Speed", 0.5, 0.1, 1, 0.1, SliderSetting.ValueDisplay.DECIMAL);

	public PacketFly() {
		super("PacketFly", "Fly around with packets.");
		setCategory(Category.EXPLOIT);
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
	public void WUpdateEvent(WUpdateEvent event) {
		mc.player.setVelocity(0, 0, 0);

		mc.player.onGround = false;
		mc.player.noClip = true;
		mc.player.fallDistance = 0;

		if (!mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown()) {
			mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
		}

		mc.player.connection.sendPacket(new CPacketPlayer(true));

		float yaw = Minecraft.getMinecraft().player.rotationYaw;
		float pitch = Minecraft.getMinecraft().player.rotationPitch;
		if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
			double x = Minecraft.getMinecraft().player.posX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed.getValue();
			double z = Minecraft.getMinecraft().player.posZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed.getValue();
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(x, mc.player.posY, z, mc.player.rotationYaw, mc.player.rotationPitch, true));
			mc.player.setPosition(x, mc.player.posY, z);
		}

		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.setPosition(mc.player.posX, mc.player.posY + speed.getValueF(), mc.player.posZ);
			mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + speed.getValueF(), mc.player.posZ, true));
		}

		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.setPosition(mc.player.posX, mc.player.posY - speed.getValueF(), mc.player.posZ);
			mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - speed.getValueF(), mc.player.posZ, true));
		}
	}

	@SubscribeEvent
	public void packet(WPacketInputEvent event) {
		try {
			CPacketPlayer player = (CPacketPlayer) event.getPacket();

			player.getX(mc.player.posX);
			player.getY(mc.player.posY);
			player.getZ(mc.player.posZ);

			player.getPitch(mc.player.rotationPitch);
			player.getYaw(mc.player.rotationYaw);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void clip(WSetOpaqueCubeEvent event) {
		event.setCanceled(true);
	}

	@SubscribeEvent
	public void clip1(WIsNormalCubeEvent event) {
		event.setCanceled(true);
	}
}