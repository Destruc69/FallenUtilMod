/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.util.Timer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;

import java.lang.reflect.Field;

public final class Strafe extends Hack {

	private final CheckboxSetting ncp =
			new CheckboxSetting("BypassSafe", "Slower version for AC's",
					false);

	private final CheckboxSetting stable =
			new CheckboxSetting("Stability",
					true);

	public Strafe() {
		super("Strafe", "Move in the air freely.");
		setCategory(Category.MOVEMENT);
		addSetting(stable);
		addSetting(ncp);
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
	public void onUpdate(WUpdateEvent event) {
		if (!ncp.isChecked()) {
			if (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) {

				mc.player.setSprinting(true);

				if (mc.player.onGround) {

					setTickLength((float) (50 / 1.7));

					mc.player.motionY = 0.405f;

					final float yaw = GetRotationYawForCalc();
					mc.player.motionX -= MathHelper.sin(yaw) * 0.3f;
					mc.player.motionZ += MathHelper.cos(yaw) * 0.3f;
				} else {
					setTickLength((float) (50 / 1.05));

					if (mc.player.fallDistance == 1) {
						mc.player.motionY = -0.4;
					}
				}
			}

			if (stable.isChecked()) {
				if (mc.player.onGround && mc.player.moveForward == 0f && mc.player.moveStrafing == 0) {
					mc.player.setVelocity(0, 0, 0);
				}
			}
		} else {
			if (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) {

				mc.player.setSprinting(true);

				if (mc.player.onGround) {

					setTickLength((float) (50 / 1.6));

					mc.player.motionY = 0.405f;

					final float yaw = GetRotationYawForCalc();
					mc.player.motionX -= MathHelper.sin(yaw) * 0.2f;
					mc.player.motionZ += MathHelper.cos(yaw) * 0.2f;
				} else {
					setTickLength((float) (50 / 1.1));

					if (mc.player.fallDistance == 1) {
						mc.player.motionY = -0.4;
					}
				}
			}

			if (stable.isChecked()) {
				if (mc.player.onGround && mc.player.moveForward == 0f && mc.player.moveStrafing == 0) {
					mc.player.setVelocity(0, 0, 0);
				}
			}
		}
	}
		private float GetRotationYawForCalc () {
			float rotationYaw = mc.player.rotationYaw;
			if (mc.player.moveForward < 0.0f) {
				rotationYaw += 180.0f;
			}
			float n = 1.0f;
			if (mc.player.moveForward < 0.0f) {
				n = -0.5f;
			} else if (mc.player.moveForward > 0.0f) {
				n = 0.5f;
			}
			if (mc.player.moveStrafing > 0.0f) {
				rotationYaw -= 90.0f * n;
			}
			if (mc.player.moveStrafing < 0.0f) {
				rotationYaw += 90.0f * n;
			}
			return rotationYaw * 0.017453292f;
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
}

