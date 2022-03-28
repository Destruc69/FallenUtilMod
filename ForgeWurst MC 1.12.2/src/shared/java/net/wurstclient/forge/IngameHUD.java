/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge;

import java.util.ArrayList;
import java.util.Comparator;

import net.wurstclient.forge.hacks.ClickGuiHack;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.clickgui.ClickGui;
import net.wurstclient.forge.clickgui.ClickGuiScreen;
import net.wurstclient.forge.compatibility.WMinecraft;

public final class IngameHUD {
	private final Minecraft mc = Minecraft.getMinecraft();
	private final HackList hackList;
	private final ClickGui clickGui;

	float textColor;

	boolean a;
	boolean b;

	public IngameHUD(HackList hackList, ClickGui clickGui) {
		this.hackList = hackList;
		this.clickGui = clickGui;
	}

	@SubscribeEvent
	public void onRenderGUI(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.ALL || mc.gameSettings.showDebugInfo)
			return;

		boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);


		// color
		clickGui.updateColors();

		ClickGui gui = ForgeWurst.getForgeWurst().getGui();
		if (gui.getAcColor()[2] > gui.getAcColor()[1]) {
			textColor = 0x000AFF;

			clickGui.updateColors();
		} else if (gui.getAcColor()[1] > gui.getAcColor()[2]) {
			textColor = 0x11FF00;

			clickGui.updateColors();
		} else if (gui.getAcColor()[0] > gui.getAcColor()[1] && gui.getAcColor()[0] > gui.getAcColor()[2]) {
			textColor = 0xFF0000;

			clickGui.updateColors();
		}


		if (!ForgeWurst.getForgeWurst().getHax().clickGuiHack.nogui().isChecked()) {
			// title
			GL11.glPushMatrix();
			GL11.glScaled(1.55555555, 1.55555555, 1);
			WMinecraft.getFontRenderer().drawStringWithShadow("Fallen", 3, 3, (int) textColor);
			GL11.glPopMatrix();

			// hack list
			int y = 19;
			ArrayList<Hack> hacks = new ArrayList<>();
			hacks.addAll(hackList.getValues());
			hacks.sort(Comparator.comparing(Hack::getName));

			for (Hack hack : hacks) {
				if (!hack.isEnabled())
					continue;

				WMinecraft.getFontRenderer()
						.drawString(hack.getRenderName(), 3, y, (int) textColor);
				y += 9;
			}

			// pinned windows
			if (!(mc.currentScreen instanceof ClickGuiScreen))
				clickGui.renderPinnedWindows(event.getPartialTicks());

			if (blend)
				GL11.glEnable(GL11.GL_BLEND);
			else
				GL11.glDisable(GL11.GL_BLEND);
		}
	}
}
