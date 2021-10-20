/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.settings.SliderSetting;

public final class Teleport extends Hack {

	private final SliderSetting x =
			new SliderSetting("X Coord", 1, -30000000, 30000000, 0.1, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting y =
			new SliderSetting("Y Coord", 1, 1.0, 64, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting z =
			new SliderSetting("Z Coords", 1, -30000000, 30000000, 0.1, SliderSetting.ValueDisplay.DECIMAL);

	public Teleport()
	{
		super("Teleport", "Teleport to coords.");
		setCategory(Category.MOVEMENT);
		addSetting(x);
		addSetting(y);
		addSetting(z);
	}
	
	@Override
	protected void onEnable()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	protected void onDisable()
	{
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event)
	{
		mc.player.setPosition(x.getValue(), y.getValue(), z.getValue());
	}
}
