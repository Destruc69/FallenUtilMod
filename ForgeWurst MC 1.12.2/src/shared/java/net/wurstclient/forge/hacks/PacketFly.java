/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
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
	public void onPacketInput(WPacketInputEvent event) {
		if (event.getPacket() instanceof SPacketPlayerPosLook && mc.currentScreen == null) {
			SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
			mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
			mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());

		}
	}


	@SubscribeEvent
	public void WUpdateEvent(WUpdateEvent event) {
		mc.player.setVelocity(0, 0, 0);

		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.motionY += speed.getValue();
		}
		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.motionY -= speed.getValue();
		}

		double yawRad = Math.toRadians(mc.player.rotationYaw);
		new Vec3d(-mc.player.moveStrafing, 0.0, mc.player.moveForward);
		if (mc.gameSettings.keyBindForward.isKeyDown()) {
			mc.player.motionX = -Math.sin(yawRad) * speed.getValue();
			mc.player.motionZ = Math.cos(yawRad) * speed.getValue();
		}

		double y = mc.player.posY + mc.player.motionY;
		mc.player.connection.sendPacket(new CPacketConfirmTeleport());
		mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, y, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));
		mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY + mc.player.motionY, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));
	}
}