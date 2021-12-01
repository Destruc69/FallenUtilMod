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
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.ChatUtils;
import org.lwjgl.input.Keyboard;

public final class AirJump extends Hack {

	private final CheckboxSetting stati =
			new CheckboxSetting("Static",
					false);

	public AirJump()
	{
		super("AirJump", "Jump in mid air.");
		setCategory(Category.MOVEMENT);
		addSetting(stati);
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
		if (!stati.isChecked()) {
			if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown())
				Minecraft.getMinecraft().player.jump();
		} else
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 1, mc.player.posZ);
			}
	}
}
