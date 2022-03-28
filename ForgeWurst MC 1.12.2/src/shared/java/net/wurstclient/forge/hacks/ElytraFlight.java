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

public final class ElytraFlight extends Hack {

	private final SliderSetting upSpeed =
			new SliderSetting("UpSpeed", 2.5, 0.5, 10, 0.5, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting baseSpeed =
			new SliderSetting("BaseSpeed", 2.5, 0.5, 10, 0.5, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting downSpeed =
			new SliderSetting("DownSpeed", 2.5, 0.5, 10, 0.5, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting vel =
			new CheckboxSetting("Velocity",
					false);

	public ElytraFlight() {
		super("ElytraFlight", "Fly with elytras.");
		setCategory(Category.MOVEMENT);
		addSetting(upSpeed);
		addSetting(baseSpeed);
		addSetting(downSpeed);
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
		if (mc.player.isElytraFlying()) {
			if (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {
				double[] dir = MathUtils.directionSpeed(baseSpeed.getValueF());
				mc.player.motionX = dir[0];
				mc.player.motionZ = dir[1];
			}

			if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
				if (vel.isChecked()) {
					mc.player.motionY = 0;
				}
			}

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.player.motionY += upSpeed.getValueF();
			}

			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.motionY -= downSpeed.getValueF();
			}

			if (vel.isChecked()) {
				if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0 && !mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
					mc.player.setVelocity(0, 0, 0);
				}
			}
		}
	}
}