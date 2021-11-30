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
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
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

		if (mc.player.onGround) {
			mc.player.jump();
		}

		if (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) {
			mc.player.motionY = 0.405f;

			final float yaw = GetRotationYawForCalc();
			mc.player.motionX -= MathHelper.sin(yaw) * 0.2f;
			mc.player.motionZ += MathHelper.cos(yaw) * 0.2f;
		} else if (mc.player.onGround) {
			final float yaw = GetRotationYawForCalc();
			mc.player.motionX -= MathHelper.sin(yaw) * 0.2f;
			mc.player.motionZ += MathHelper.cos(yaw) * 0.2f;
			mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.4, mc.player.posZ, false));

		}
	}

	private float GetRotationYawForCalc()
	{
		float rotationYaw = mc.player.rotationYaw;
		if (mc.player.moveForward < 0.0f)
		{
			rotationYaw += 180.0f;
		}
		float n = 1.0f;
		if (mc.player.moveForward < 0.0f)
		{
			n = -0.5f;
		}
		else if (mc.player.moveForward > 0.0f)
		{
			n = 0.5f;
		}
		if (mc.player.moveStrafing > 0.0f)
		{
			rotationYaw -= 90.0f * n;
		}
		if (mc.player.moveStrafing < 0.0f)
		{
			rotationYaw += 90.0f * n;
		}
		return rotationYaw * 0.017453292f;
	}
}