/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.wurstclient.forge.utils.ChatUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;

public final class Coords extends Hack {

	private final SliderSetting ValueX =
			new SliderSetting("X", 1, 0.05, 800, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting ValueY =
			new SliderSetting("Y", 1, 0.05, 800, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting Color =
			new SliderSetting("Colors", 1, 1.0, 4.0, 1.0, SliderSetting.ValueDisplay.DECIMAL);


	public Coords() {

		super("Coords", "Shows your coords.");
		setCategory(Category.HUD);
		addSetting(ValueX);
		addSetting(ValueY);
		addSetting(Color);


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
	public void onRenderGUI(RenderGameOverlayEvent.Post event) {

		GL11.glColor3f(1, 1, 1);

		int textColor;

		textColor = 0xffffff;

		if (Color.getValueI() == 1)
			textColor = 0x52ff52;

		if (Color.getValueI() == 2)
			textColor = 0xff0a0a;

		if (Color.getValueI() == 3)
			textColor = 0x31c0ff;

		if (Color.getValueI() == 4)
			textColor = 0xffffff;

		GL11.glPushMatrix();
		GL11.glScaled(1.33333333, 1.33333333, 1);
		WMinecraft.getFontRenderer().drawStringWithShadow(
				getX() + " " + getY() + " " + getZ(), ValueX.getValueF(), ValueY.getValueF(), textColor);
		GL11.glPopMatrix();
	}

	public static String getX() {
		double x = Minecraft.getMinecraft().player.posX;
		return String.valueOf(Math.round(x));

	}

	public static String getY() {
		double y = Minecraft.getMinecraft().player.posY;
		return String.valueOf(Math.round(y));
	}

	public static String getZ() {
		double z = Minecraft.getMinecraft().player.posZ;
		return String.valueOf(Math.round(z));
	}
}