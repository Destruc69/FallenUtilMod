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
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.*;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.MathUtils;

import java.lang.reflect.Field;

public final class PacketFly extends Hack {

	private final SliderSetting baseSpeed =
			new SliderSetting("BaseSpeed", "How much we teleport for going Forward, left tight and back", 0.5, 0.1, 1, 0.01, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting upSpeed =
			new SliderSetting("UpSpeed", "How much we teleport for going Up", 0.5, 0.1, 1, 0.01, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting downSpeed =
			new SliderSetting("DownSpeed", "How much we teleport for going Down", 0.5, 0.1, 1, 0.01, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting silent =
			new CheckboxSetting("SilentPackets", "Should we move the player with the sent packets? (Like AntiKick)",
					false);

	private final SliderSetting timer =
			new SliderSetting("TimerSpeed", "Timer tick speed, lower = slower", 0.5, 0.1, 1, 0.01, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting antiy =
			new CheckboxSetting("AntiKick Y", "Sends packets that you go up and down to bypass NCP",
					false);

	private final CheckboxSetting extra =
			new CheckboxSetting("ConfirmTeleport", "Sends ConfirmTeleport packets",
					false);

	private final CheckboxSetting antixz =
			new CheckboxSetting("AntiKick XZ", "Sends packets that you go right and left to bypass NCP (maybe)",
					false);

	private final CheckboxSetting sneak =
			new CheckboxSetting("SneakPackets", "Sends Start_Sneaking, and Stop_Sneaking packets to help when in ground \n" +
					"WILL ONLY APPLY WHEN IN GROUND, DOES NOT MAKE SENSE IN THE AIR",
					false);

	private final CheckboxSetting onGround =
			new CheckboxSetting("GroundSpoof", "Every packet we send will be an OnGround packet, \n" +
					" spoofing your position relative to airborne and in-ground",
					false);

	public PacketFly() {
		super("PacketFly", "Fly around with packets.");
		setCategory(Category.EXPLOIT);
		addSetting(baseSpeed);
		addSetting(upSpeed);
		addSetting(downSpeed);
		addSetting(silent);
		addSetting(timer);
		addSetting(antiy);
		addSetting(antixz);
		addSetting(extra);
		addSetting(sneak);
		addSetting(onGround);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
		setTickLength(50);
	}

	@SubscribeEvent
	public void WPlayerMove(WPlayerMoveEvent event) {
		if (!onGround.isChecked()) {
			mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
		} else {
			mc.player.connection.sendPacket(new CPacketPlayer(true));
		}

		setTickLength(50 / timer.getValueF());

		if (!onGround.isChecked()) {
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY + mc.player.motionY, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));
		} else {
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY + mc.player.motionY, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
		}

		mc.player.motionX = 0;
		mc.player.motionY = 0;
		mc.player.motionZ = 0;

		mc.player.noClip = true;

		if (antiy.isChecked()) {
			if (!onGround.isChecked()) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1, mc.player.posZ, mc.player.onGround));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ, mc.player.onGround));

				if (!silent.isChecked()) {
					mc.player.setPosition(mc.player.posX, mc.player.posY + 0.1, mc.player.posZ);
					mc.player.setPosition(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ);
				}

			} else {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ, true));
			}
		}

		if (sneak.isChecked()) {
			if (mc.player.collidedHorizontally && mc.player.collidedVertically) {
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
			}
		}

		if (antixz.isChecked()) {
			if (!onGround.isChecked()) {

				if (!silent.isChecked()) {
					mc.player.setPosition(mc.player.posX +0.1, mc.player.posY, mc.player.posZ -0.1);
					mc.player.setPosition(mc.player.posX -0.1, mc.player.posY, mc.player.posZ +0.1);
				}

				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX +0.1, mc.player.posY, mc.player.posZ -0.1, mc.player.onGround));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX -0.1, mc.player.posY, mc.player.posZ +0.1, mc.player.onGround));
			} else {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX +0.1, mc.player.posY, mc.player.posZ -0.1, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX -0.1, mc.player.posY, mc.player.posZ +0.1, true));
			}
		}

		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.setPosition(mc.player.posX, mc.player.posY + upSpeed.getValueF(), mc.player.posZ);
			mc.player.motionY += upSpeed.getValueF();

			if (!onGround.isChecked()) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + upSpeed.getValueF(), mc.player.posZ, mc.player.onGround));
			} else {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + upSpeed.getValueF(), mc.player.posZ, true));
			}
		}

		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.setPosition(mc.player.posX, mc.player.posY - downSpeed.getValueF(), mc.player.posZ);
			mc.player.motionY -= downSpeed.getValueF();

			if (!onGround.isChecked()) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - downSpeed.getValueF(), mc.player.posZ, mc.player.onGround));
			} else {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - downSpeed.getValueF(), mc.player.posZ, true));
			}
		}

		if (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {
			double[] dir = MathUtils.directionSpeed(baseSpeed.getValueF());

			double x = mc.player.posX + mc.player.motionX + dir[0];
			double z = mc.player.posZ + mc.player.motionZ + dir[1];

			if (!onGround.isChecked()) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(x, mc.player.posY, z, mc.player.onGround));
			} else {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(x, mc.player.posY, z, true));
			}

			mc.player.motionX = dir[0];
			mc.player.motionZ = dir[1];
			mc.player.setPosition(x, mc.player.posY, z);
		}
	}

	@SubscribeEvent
	public void onPacket(WPacketOutputEvent event) {
		if (extra.isChecked()) {
			if (event.getPacket() instanceof SPacketPlayerPosLook) {
				SPacketPlayerPosLook packetPlayer = (SPacketPlayerPosLook) event.getPacket();
				double id = packetPlayer.getTeleportId();
				mc.player.connection.sendPacket(new CPacketConfirmTeleport((int) id));
			}
		}
	}


	private void setTickLength(float tickLength)
	{
		try
		{
			Field fTimer = mc.getClass().getDeclaredField(
					wurst.isObfuscated() ? "field_71428_T" : "timer");
			fTimer.setAccessible(true);

			if(WMinecraft.VERSION.equals("1.10.2"))
			{
				Field fTimerSpeed = Timer.class.getDeclaredField(
						wurst.isObfuscated() ? "field_74278_d" : "timerSpeed");
				fTimerSpeed.setAccessible(true);
				fTimerSpeed.setFloat(fTimer.get(mc), 50 / tickLength);

			}else
			{
				Field fTickLength = Timer.class.getDeclaredField(
						wurst.isObfuscated() ? "field_194149_e" : "tickLength");
				fTickLength.setAccessible(true);
				fTickLength.setFloat(fTimer.get(mc), tickLength);
			}

		}catch(ReflectiveOperationException e)
		{
			throw new RuntimeException(e);
		}
	}


	@SubscribeEvent
	public void a(WIsNormalCubeEvent event) {
		event.setCanceled(true);
	}

	@SubscribeEvent
	public void b(WSetOpaqueCubeEvent event) {
		event.setCanceled(true);
	}
}