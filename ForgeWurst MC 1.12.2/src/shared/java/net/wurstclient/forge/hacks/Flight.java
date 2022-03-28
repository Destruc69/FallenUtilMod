/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.MathUtils;

public final class Flight extends Hack {

	private final SliderSetting speed =
			new SliderSetting("Speed", 2.5, 2, 10, 0.5, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting vel =
			new CheckboxSetting("Velocity",
					false);

	public Flight() {
		super("Flight", "I believe i can fly.");
		setCategory(Category.MOVEMENT);
		addSetting(speed);
		addSetting(vel);
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
		if (mc.player.moveForward != 0 && mc.player.moveStrafing != 0) {
			double[] dir = MathUtils.directionSpeed(speed.getValueF());
			mc.player.motionX = dir[0];
			mc.player.motionZ = dir[1];
		}

		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.motionY += speed.getValueF();
		}

		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.motionY -= speed.getValueF();
		}

		if (vel.isChecked()) {
			if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.setVelocity(0, 0, 0);
			}
		}
	}
}