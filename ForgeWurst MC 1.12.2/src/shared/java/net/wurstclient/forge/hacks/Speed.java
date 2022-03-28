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
import net.wurstclient.forge.settings.EnumSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.MathUtils;

public final class Speed extends Hack {

	private final EnumSetting<Mode> mode =
			new EnumSetting<>("Mode", Mode.values(), Mode.STRAFE);

	private final SliderSetting speed =
			new SliderSetting("Speed [NORMAL]", 0.3, 0.3, 1 , 0.1, SliderSetting.ValueDisplay.DECIMAL);

	public Speed() {
		super("Speed", "I Show Speed");
		setCategory(Category.MOVEMENT);
		addSetting(mode);
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
		if (mode.getSelected().normal) {
			double[] dir = MathUtils.directionSpeed(speed.getValueF());
			mc.player.motionX = dir[0];
			mc.player.motionZ = dir[1];
		}

		if (mode.getSelected().strafe) {
			if (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) {

				if (mc.player.onGround) {
					mc.player.motionY = 0.405f;
					final float yaw = GetRotationYawForCalc();
					mc.player.motionX -= MathHelper.sin(yaw) * 0.2f;
					mc.player.motionZ += MathHelper.cos(yaw) * 0.2f;
				}
			}
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

	private enum Mode {
		STRAFE("Strafe", false, true),
		NORMAL("Normal", true, false);

		private final String name;
		private final boolean strafe;
		private final boolean normal;

		private Mode(String name, boolean normal, boolean strafe) {
			this.name = name;
			this.normal = normal;
			this.strafe = strafe;
		}

		public String toString() {
			return name;
		}
	}
}
