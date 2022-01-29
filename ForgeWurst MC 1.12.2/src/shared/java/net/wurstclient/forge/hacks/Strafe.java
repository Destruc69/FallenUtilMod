/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;

public final class Strafe extends Hack {

	private final CheckboxSetting stable =
			new CheckboxSetting("Stability",
					true);

	public Strafe() {
		super("Strafe", "Move in the air freely.");
		setCategory(Category.MOVEMENT);
		addSetting(stable);
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
		if (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) {

			mc.player.setSprinting(true);

			if (mc.player.onGround) {

				mc.player.motionY = 0.405f;

				final float yaw = GetRotationYawForCalc();
				mc.player.motionX -= MathHelper.sin(yaw) * 0.2f;
				mc.player.motionZ += MathHelper.cos(yaw) * 0.2f;
			}
		}

		if (stable.isChecked()) {
			if (mc.player.onGround && mc.player.moveForward == 0f && mc.player.moveStrafing == 0) {
				mc.player.setVelocity(0, 0, 0);
			}
		}
	}

	private float GetRotationYawForCalc() {
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
}