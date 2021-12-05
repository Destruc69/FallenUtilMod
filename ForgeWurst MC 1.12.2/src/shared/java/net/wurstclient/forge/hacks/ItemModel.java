/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WRenderBlockModelEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.settings.SliderSetting;

public final class ItemModel extends Hack {

	private final SliderSetting x =
			new SliderSetting("X", 1, 0.05, 10, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting y =
			new SliderSetting("Y", 1, 0.05, 10, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting z =
			new SliderSetting("Z", 1, 0.05, 10, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	public ItemModel() {
		super("ItemModel", "Change the angle of your arm.");
		setCategory(Category.MOVEMENT);
		addSetting(x);
		addSetting(y);
		addSetting(z);
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

		final EnumHandSide enumHandSide = null;

		if (getEnumHandSide() == EnumHandSide.LEFT || getEnumHandSide() == EnumHandSide.RIGHT) {
			GlStateManager.scale(x.getValue(), y.getValue(), z.getValue());
		}
	}

	public EnumHandSide getEnumHandSide(){
		return getEnumHandSide();
	}
}
