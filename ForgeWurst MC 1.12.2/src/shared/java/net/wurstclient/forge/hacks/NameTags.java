/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.SliderSetting;

import java.util.Set;

public final class NameTags extends Hack {

	private final SliderSetting size =
			new SliderSetting("Size", 5, 1.0, 20, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	public NameTags()
	{
		super("NameTags", "Displays health of the player and make it bolder.");
		setCategory(Category.RENDER);
		addSetting(size);
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
	public void onUpdate(WUpdateEvent event) {
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer) {
				double health = ((EntityPlayer) e).getHealth();

				Set<String> tag = mc.player.getTags();

				boolean b = tag.size() == size.getValueI();
				b = true;

				e.setAlwaysRenderNameTag(true);

				String name = e.getName();

				((EntityPlayer) e).setCustomNameTag(health + " " + "|" + " " + name);

			}
		}
	}
}
