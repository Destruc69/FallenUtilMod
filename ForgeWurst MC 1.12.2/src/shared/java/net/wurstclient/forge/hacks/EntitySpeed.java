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

import java.util.Objects;

public final class EntitySpeed extends Hack {

	private final SliderSetting speedbase =
			new SliderSetting("BaseSpeed", 1, 0.05, 1.0, 0.005, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting motion =
			new CheckboxSetting("MotionY = 0",
					false);

	public EntitySpeed() {
		super("EntitySpeed", "Speed with Entity's.");
		setCategory(Category.MOVEMENT);
		addSetting(speedbase);
		addSetting(motion);
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
		if (!mc.player.isRiding()) return;

		if (mc.player.moveStrafing != 0 || mc.player.moveForward != 0) {
			double[] speed = MathUtils.directionSpeed(speedbase.getValueF());

			Objects.requireNonNull(mc.player.getRidingEntity()).motionX = speed[0];
			mc.player.getRidingEntity().motionZ = speed[1];
		}

		if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown()) {
			Objects.requireNonNull(mc.player.getRidingEntity()).motionY = 0;
		}
	}
}