/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
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
			new SliderSetting("Speed", 0.05, 0.05, 1, 0.01, SliderSetting.ValueDisplay.DECIMAL);

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

		mc.player.connection.sendPacket(new CPacketConfirmTeleport());
		mc.player.connection.sendPacket(new CPacketConfirmTransaction());

		mc.player.motionX = speed.getValueF();
		mc.player.motionZ = speed.getValueF();

		mc.player.noClip = true;
		mc.player.onGround = false;

		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
		mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, mc.player.rotationPitch, true));

		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY + speed.getValueF(), mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
			mc.player.setPosition(mc.player.posX, mc.player.posY + speed.getValueF(), mc.player.posZ);
		}

		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY - speed.getValueF(), mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
			mc.player.setPosition(mc.player.posX, mc.player.posY - speed.getValueF(), mc.player.posZ);
		}
	}

	@SubscribeEvent
	public void opac(WSetOpaqueCubeEvent event) {
		event.setCanceled(true);
	}

	@SubscribeEvent
	public void opac1(WIsNormalCubeEvent event) {
		event.setCanceled(true);
	}
}