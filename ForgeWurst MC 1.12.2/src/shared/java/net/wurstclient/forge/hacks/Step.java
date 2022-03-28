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
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.MathUtils;

public final class Step extends Hack {

	private final SliderSetting forwardSpeed =
			new SliderSetting("ForwardSpeed", 0.01, 0.005, 0.02, 0.005, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting upSpeed =
			new SliderSetting("UpSpeed", 0.85, 0.5, 1, 0.5, SliderSetting.ValueDisplay.DECIMAL);

	public Step() {
		super("Step", "Step up blocks faster.");
		setCategory(Category.MOVEMENT);
		addSetting(upSpeed);
		addSetting(forwardSpeed);
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
		if (mc.player.collidedHorizontally) {
			mc.player.setPosition(mc.player.posX, mc.player.posY + upSpeed.getValueF(), mc.player.posZ);
			double[] dir = MathUtils.directionSpeed(forwardSpeed.getValueF());
			double x = mc.player.posX + dir[0];
			double z = mc.player.posZ + dir[1];
			mc.player.setPosition(x, mc.player.posY, z);
		}
	}
}