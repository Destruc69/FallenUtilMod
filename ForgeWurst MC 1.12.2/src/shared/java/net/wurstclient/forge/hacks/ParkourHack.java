/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

public final class ParkourHack extends Hack {
	public ParkourHack() {
		super("Parkour", "Will jump for you on parkour.");
		setCategory(Category.MOVEMENT);
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
		if (Minecraft.getMinecraft().player.onGround && !Minecraft.getMinecraft().player.isSneaking()
				&& !Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()
				&& Minecraft.getMinecraft().world
				.getCollisionBoxes(Minecraft.getMinecraft().player, Minecraft.getMinecraft().player
						.getEntityBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001))
				.isEmpty()) {
			Minecraft.getMinecraft().player.jump();
		}
	}
}
