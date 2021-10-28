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

	private final SliderSetting pitch =
			new SliderSetting("Pitch", 1, 0.05, 10, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting yaw =
			new SliderSetting("Yaw", 1, 0.05, 10, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	public ItemModel() {
		super("ItemModel", "Change the angle of your arm.");
		setCategory(Category.MOVEMENT);
		addSetting(pitch);
		addSetting(yaw);

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
		mc.player.renderArmPitch = (Float) pitch.getValueF();
		mc.player.renderArmYaw = (Float) yaw.getValueF();
	}
}
