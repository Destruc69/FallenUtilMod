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

public final class YawLock extends Hack {

	private final SliderSetting angle =
			new SliderSetting("Rotation Angle", 1, 1.0, 360.0, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	public YawLock() {
		super("YawLock", "Choose the rotation in settings.");
		setCategory(Category.MOVEMENT);
		addSetting(angle);
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

		int x = 0;
		int y = 60;
		int z = 0;

		double dirx = mc.player.posX - 0;
		double diry = mc.player.posY - 0;
		double dirz = mc.player.posZ - 0;

		double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

		dirx /= len;
		diry /= len;
		dirz /= len;

		double pitch = Math.asin(diry);
		double yaw = Math.atan2(dirz, dirx);

//to degree
		pitch = pitch * angle.getValueF() / Math.PI;
		yaw = yaw * angle.getValueF() / Math.PI;

		yaw += 90f;

		if (yaw > mc.player.rotationYaw) {
			mc.player.rotationYaw++;
		} else if (yaw < mc.player.rotationYaw) {
			mc.player.rotationYaw--;
		}
	}
}