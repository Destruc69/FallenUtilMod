/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.clickgui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.HackList;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.Setting;
import net.wurstclient.forge.utils.JsonUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public final class ClickGui {
	private final ArrayList<Window> windows = new ArrayList<>();
	private final ArrayList<Popup> popups = new ArrayList<>();
	private final Path windowsFile;

	private float[] bgColor = new float[3];
	private float[] acColor = new float[3];
	private float opacity;
	private int maxHeight;

	private String tooltip;

	public ClickGui(Path windowsFile) {
		this.windowsFile = windowsFile;
	}

	public void init(HackList hax) {
		LinkedHashMap<Category, Window> windowMap = new LinkedHashMap<>();
		for (Category category : Category.values())
			windowMap.put(category, new Window(category.getName()));

		for (Hack hack : hax.getRegistry())
			if (hack.getCategory() != null)
				windowMap.get(hack.getCategory()).add(new HackButton(hack));

		windows.addAll(windowMap.values());

		Window uiSettings = new Window("UI Settings");
		for (Setting setting : hax.clickGuiHack.getSettings().values())
			uiSettings.add(setting.getComponent());
		windows.add(uiSettings);

		for (Window window : windows)
			window.setMinimized(true);

		windows.add(hax.radarHack.getWindow());

		int x = 5;
		int y = 5;
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		for (Window window : windows) {
			window.pack();

			if (x + window.getWidth() + 5 > sr.getScaledWidth()) {
				x = 5;
				y += 18;
			}

			window.setX(x);
			window.setY(y);
			x += window.getWidth() + 5;
		}

		JsonObject json;
		try (BufferedReader reader = Files.newBufferedReader(windowsFile)) {
			json = JsonUtils.jsonParser.parse(reader).getAsJsonObject();

		} catch (NoSuchFileException e) {
			saveWindows();
			return;

		} catch (Exception e) {
			System.out.println("Failed to load " + windowsFile.getFileName());
			e.printStackTrace();

			saveWindows();
			return;
		}

		for (Window window : windows) {
			JsonElement jsonWindow = json.get(window.getTitle());
			if (jsonWindow == null || !jsonWindow.isJsonObject())
				continue;

			JsonElement jsonX = jsonWindow.getAsJsonObject().get("x");
			if (jsonX.isJsonPrimitive() && jsonX.getAsJsonPrimitive().isNumber())
				window.setX(jsonX.getAsInt());

			JsonElement jsonY = jsonWindow.getAsJsonObject().get("y");
			if (jsonY.isJsonPrimitive() && jsonY.getAsJsonPrimitive().isNumber())
				window.setY(jsonY.getAsInt());

			JsonElement jsonMinimized =
					jsonWindow.getAsJsonObject().get("minimized");
			if (jsonMinimized.isJsonPrimitive()
					&& jsonMinimized.getAsJsonPrimitive().isBoolean())
				window.setMinimized(jsonMinimized.getAsBoolean());

			JsonElement jsonPinned = jsonWindow.getAsJsonObject().get("pinned");
			if (jsonPinned.isJsonPrimitive()
					&& jsonPinned.getAsJsonPrimitive().isBoolean())
				window.setPinned(jsonPinned.getAsBoolean());
		}

		saveWindows();
	}

	private void saveWindows() {
		JsonObject json = new JsonObject();

		for (Window window : windows) {
			if (window.isClosable())
				continue;

			JsonObject jsonWindow = new JsonObject();
			jsonWindow.addProperty("x", window.getX());
			jsonWindow.addProperty("y", window.getY());
			jsonWindow.addProperty("minimized", window.isMinimized());
			jsonWindow.addProperty("pinned", window.isPinned());
			json.add(window.getTitle(), jsonWindow);
		}

		try (BufferedWriter writer = Files.newBufferedWriter(windowsFile)) {
			JsonUtils.prettyGson.toJson(json, writer);

		} catch (IOException e) {
			System.out.println("Failed to save " + windowsFile.getFileName());
			e.printStackTrace();
		}
	}

	public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
		boolean popupClicked =
				handlePopupMouseClick(mouseX, mouseY, mouseButton);

		if (!popupClicked)
			handleWindowMouseClick(mouseX, mouseY, mouseButton);

		for (Popup popup : popups)
			if (popup.getOwner().getParent().isClosing())
				popup.close();

		windows.removeIf(w -> w.isClosing());
		popups.removeIf(p -> p.isClosing());
	}

	private boolean handlePopupMouseClick(int mouseX, int mouseY,
										  int mouseButton) {
		for (int i = popups.size() - 1; i >= 0; i--) {
			Popup popup = popups.get(i);
			Component owner = popup.getOwner();
			Window parent = owner.getParent();

			int x0 = parent.getX() + owner.getX();
			int y0 =
					parent.getY() + 13 + parent.getScrollOffset() + owner.getY();

			int x1 = x0 + popup.getX();
			int y1 = y0 + popup.getY();
			int x2 = x1 + popup.getWidth();
			int y2 = y1 + popup.getHeight();

			if (mouseX < x1 || mouseY < y1)
				continue;
			if (mouseX >= x2 || mouseY >= y2)
				continue;

			int cMouseX = mouseX - x0;
			int cMouseY = mouseY - y0;
			popup.handleMouseClick(cMouseX, cMouseY, mouseButton);

			popups.remove(i);
			popups.add(popup);
			return true;
		}

		return false;
	}

	private void handleWindowMouseClick(int mouseX, int mouseY, int mouseButton) {
		for (int i = windows.size() - 1; i >= 0; i--) {
			Window window = windows.get(i);
			if (window.isInvisible())
				continue;

			int x1 = window.getX();
			int y1 = window.getY();
			int x2 = x1 + window.getWidth();
			int y2 = y1 + window.getHeight();
			int y3 = y1 + 13;

			if (mouseX < x1 || mouseY < y1)
				continue;
			if (mouseX >= x2 || mouseY >= y2)
				continue;

			if (mouseY < y3)
				handleTitleBarMouseClick(window, mouseX, mouseY, mouseButton);
			else if (!window.isMinimized()) {
				window.validate();

				int cMouseX = mouseX - x1;
				int cMouseY = mouseY - y3;

				if (window.isScrollingEnabled() && mouseX >= x2 - 3)
					handleScrollbarMouseClick(window, cMouseX, cMouseY,
							mouseButton);
				else {
					if (window.isScrollingEnabled())
						cMouseY -= window.getScrollOffset();

					handleComponentMouseClick(window, cMouseX, cMouseY,
							mouseButton);
				}

			} else
				continue;

			windows.remove(i);
			windows.add(window);
			break;
		}
	}

	private void handleTitleBarMouseClick(Window window, int mouseX, int mouseY,
										  int mouseButton) {
		if (mouseButton != 0)
			return;

		if (mouseY < window.getY() + 2 || mouseY >= window.getY() + 11) {
			window.startDragging(mouseX, mouseY);
			return;
		}

		int x3 = window.getX() + window.getWidth();

		if (window.isClosable()) {
			x3 -= 11;
			if (mouseX >= x3 && mouseX < x3 + 9) {
				window.close();
				return;
			}
		}

		if (window.isPinnable()) {
			x3 -= 11;
			if (mouseX >= x3 && mouseX < x3 + 9) {
				window.setPinned(!window.isPinned());
				saveWindows();
				return;
			}
		}

		if (window.isMinimizable()) {
			x3 -= 11;
			if (mouseX >= x3 && mouseX < x3 + 9) {
				window.setMinimized(!window.isMinimized());
				saveWindows();
				return;
			}
		}

		window.startDragging(mouseX, mouseY);
	}

	private void handleScrollbarMouseClick(Window window, int mouseX,
										   int mouseY, int mouseButton) {
		if (mouseButton != 0)
			return;

		if (mouseX >= window.getWidth() - 1)
			return;

		double outerHeight = window.getHeight() - 13;
		double innerHeight = window.getInnerHeight();
		double maxScrollbarHeight = outerHeight - 2;
		int scrollbarY =
				(int) (outerHeight * (-window.getScrollOffset() / innerHeight) + 1);
		int scrollbarHeight =
				(int) (maxScrollbarHeight * outerHeight / innerHeight);

		if (mouseY < scrollbarY || mouseY >= scrollbarY + scrollbarHeight)
			return;

		window.startDraggingScrollbar(window.getY() + 13 + mouseY);
	}

	private void handleComponentMouseClick(Window window, int mouseX,
										   int mouseY, int mouseButton) {
		for (int i2 = window.countChildren() - 1; i2 >= 0; i2--) {
			Component c = window.getChild(i2);

			if (mouseX < c.getX() || mouseY < c.getY())
				continue;
			if (mouseX >= c.getX() + c.getWidth()
					|| mouseY >= c.getY() + c.getHeight())
				continue;

			c.handleMouseClick(mouseX, mouseY, mouseButton);
			break;
		}
	}

	public void render(int mouseX, int mouseY, float partialTicks) {

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(4);

		// scrolling
		int dWheel = Mouse.getDWheel();
		if (dWheel != 0)
			for (int i = windows.size() - 1; i >= 0; i--) {
				Window window = windows.get(i);

				if (!window.isScrollingEnabled() || window.isMinimized()
						|| window.isInvisible())
					continue;

				if (mouseX < window.getX() || mouseY < window.getY() + 13)
					continue;
				if (mouseX >= window.getX() + window.getWidth()
						|| mouseY >= window.getY() + window.getHeight())
					continue;

				int scroll = window.getScrollOffset() + dWheel / 16;
				scroll = Math.min(scroll, 0);
				scroll = Math.max(scroll,
						-window.getInnerHeight() + window.getHeight() - 13);
				window.setScrollOffset(scroll);
				break;
			}

		tooltip = null;
		for (Window window : windows) {
			if (window.isInvisible())
				continue;

			// dragging
			if (window.isDragging())
				if (Mouse.isButtonDown(0))
					window.dragTo(mouseX, mouseY);
				else {
					window.stopDragging();
					saveWindows();
				}

			// scrollbar dragging
			if (window.isDraggingScrollbar())
				if (Mouse.isButtonDown(0))
					window.dragScrollbarTo(mouseY);
				else
					window.stopDraggingScrollbar();

			renderWindow(window, mouseX, mouseY, partialTicks);
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		for (Popup popup : popups) {
			Component owner = popup.getOwner();
			Window parent = owner.getParent();

			int x1 = parent.getX() + owner.getX();
			int y1 =
					parent.getY() + 13 + parent.getScrollOffset() + owner.getY();

			GL11.glPushMatrix();
			GL11.glTranslated(x1, y1, 0);

			int cMouseX = mouseX - x1;
			int cMouseY = mouseY - y1;
			popup.render(cMouseX, cMouseY);

			GL11.glPopMatrix();
		}

		// tooltip
		if (tooltip != null) {
			String[] lines = tooltip.split("\n");
			Minecraft mc = Minecraft.getMinecraft();
			FontRenderer fr = WMinecraft.getFontRenderer();

			int tw = 0;
			int th = lines.length * fr.FONT_HEIGHT;
			for (String line : lines) {
				int lw = fr.getStringWidth(line);
				if (lw > tw)
					tw = lw;
			}
			int sw = mc.currentScreen.width;
			int sh = mc.currentScreen.height;

			int xt1 = mouseX + tw + 11 <= sw ? mouseX + 8 : mouseX - tw - 8;
			int xt2 = xt1 + tw + 3;
			int yt1 = mouseY + th - 2 <= sh ? mouseY - 4 : mouseY - th - 4;
			int yt2 = yt1 + th + 2;

			// background
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], 0.75F);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(xt1, yt1);
			GL11.glVertex2i(xt1, yt2);
			GL11.glVertex2i(xt2, yt2);
			GL11.glVertex2i(xt2, yt1);
			GL11.glEnd();

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.75f);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2i(xt1, yt1);
			GL11.glVertex2i(xt1, yt2);
			GL11.glVertex2i(xt2, yt2);
			GL11.glVertex2i(xt2, yt1);
			GL11.glEnd();

			// text
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			for (int i = 0; i < lines.length; i++)
				fr.drawStringWithShadow(lines[i], xt1 + 2, yt1 + 2 + i * fr.FONT_HEIGHT,
						0xffffff);
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void renderPinnedWindows(float partialTicks)
	{

	}

	public void updateColors()
	{
		HackList hax = ForgeWurst.getForgeWurst().getHax();

		opacity = hax.clickGuiHack.getOpacity();
		bgColor = hax.clickGuiHack.getBgColor();
		maxHeight = hax.clickGuiHack.getMaxHeight();

		acColor = hax.clickGuiHack.getAcColor();
	}

	private void renderWindow(Window window, int mouseX, int mouseY,
							  float partialTicks)
	{
		int x1 = window.getX();
		int y1 = window.getY();
		int x2 = x1 + window.getWidth();
		int y2 = y1 + window.getHeight();
		int y3 = y1 + 13;

		if(mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2)
			tooltip = null;

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		if(!window.isMinimized())
		{
			window.setMaxHeight(maxHeight);
			window.validate();

			// scrollbar
			if(window.isScrollingEnabled())
			{
				int xs1 = x2 - 3;
				int xs2 = xs1 + 2;
				int xs3 = x2;

				double outerHeight = y2 - y3;
				double innerHeight = window.getInnerHeight();
				double maxScrollbarHeight = outerHeight - 2;
				double scrollbarY =
						outerHeight * (-window.getScrollOffset() / innerHeight) + 1;
				double scrollbarHeight =
						maxScrollbarHeight * outerHeight / innerHeight;

				int ys1 = y3;
				int ys2 = y2;
				int ys3 = ys1 + (int)scrollbarY;
				int ys4 = ys3 + (int)scrollbarHeight;

				// window background
				GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2i(xs2, ys1);
				GL11.glVertex2i(xs2, ys2);
				GL11.glVertex2i(xs3, ys2);
				GL11.glVertex2i(xs3, ys1);
				GL11.glVertex2i(xs1, ys1);
				GL11.glVertex2i(xs1, ys3);
				GL11.glVertex2i(xs2, ys3);
				GL11.glVertex2i(xs2, ys1);
				GL11.glVertex2i(xs1, ys4);
				GL11.glVertex2i(xs1, ys2);
				GL11.glVertex2i(xs2, ys2);
				GL11.glVertex2i(xs2, ys4);
				GL11.glEnd();

				GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.75f);
				GL11.glBegin(GL11.GL_LINE_LOOP);
				GL11.glVertex2i(xs2, ys1);
				GL11.glVertex2i(xs2, ys2);
				GL11.glVertex2i(xs3, ys2);
				GL11.glVertex2i(xs3, ys1);
				GL11.glVertex2i(xs1, ys1);
				GL11.glVertex2i(xs1, ys3);
				GL11.glVertex2i(xs2, ys3);
				GL11.glVertex2i(xs2, ys1);
				GL11.glVertex2i(xs1, ys4);
				GL11.glVertex2i(xs1, ys2);
				GL11.glVertex2i(xs2, ys2);
				GL11.glVertex2i(xs2, ys4);
				GL11.glEnd();

				boolean hovering = mouseX >= xs1 && mouseY >= ys3
						&& mouseX < xs2 && mouseY < ys4;

				// scrollbar
				GL11.glColor4f(acColor[0], acColor[1], acColor[2],
						hovering ? opacity * 1.5F : opacity);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2i(xs1, ys3);
				GL11.glVertex2i(xs1, ys4);
				GL11.glVertex2i(xs2, ys4);
				GL11.glVertex2i(xs2, ys3);
				GL11.glEnd();
			}

			int x3 = x1 + 2;
			int x4 = window.isScrollingEnabled() ? x2 - 3 : x2;
			int x5 = x4 - 2;
			int y4 = y3 + window.getScrollOffset();

			// window background
			// left & right
			GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(x1, y3);
			GL11.glVertex2i(x1, y2);
			GL11.glVertex2i(x3, y2);
			GL11.glVertex2i(x3, y3);
			GL11.glVertex2i(x5, y3);
			GL11.glVertex2i(x5, y2);
			GL11.glVertex2i(x4, y2);
			GL11.glVertex2i(x4, y3);
			GL11.glEnd();

			GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.75f);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2i(x1, y3);
			GL11.glVertex2i(x1, y2);
			GL11.glVertex2i(x3, y2);
			GL11.glVertex2i(x3, y3);
			GL11.glVertex2i(x5, y3);
			GL11.glVertex2i(x5, y2);
			GL11.glVertex2i(x4, y2);
			GL11.glVertex2i(x4, y3);
			GL11.glEnd();

			if(window.isScrollingEnabled())
			{
				ScaledResolution sr =
						new ScaledResolution(Minecraft.getMinecraft());
				int sf = sr.getScaleFactor();
				GL11.glScissor(x1 * sf,
						(int)((sr.getScaledHeight_double() - y2) * sf),
						window.getWidth() * sf, (y2 - y3) * sf);
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
			}
			GL11.glPushMatrix();
			GL11.glTranslated(x1, y4, 0);

			GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
			GL11.glBegin(GL11.GL_QUADS);

			// window background
			// between children
			int xc1 = 2;
			int xc2 = x5 - x1;
			for(int i = 0; i < window.countChildren(); i++)
			{
				int yc1 = window.getChild(i).getY();
				int yc2 = yc1 - 2;
				GL11.glVertex2i(xc1, yc2);
				GL11.glVertex2i(xc1, yc1);
				GL11.glVertex2i(xc2, yc1);
				GL11.glVertex2i(xc2, yc2);
			}

			// window background
			// bottom
			int yc1;
			if(window.countChildren() == 0)
				yc1 = 0;
			else
			{
				Component lastChild =
						window.getChild(window.countChildren() - 1);
				yc1 = lastChild.getY() + lastChild.getHeight();
			}
			int yc2 = yc1 + 2;
			GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(xc1, yc2);
			GL11.glVertex2i(xc1, yc1);
			GL11.glVertex2i(xc2, yc1);
			GL11.glVertex2i(xc2, yc2);
			GL11.glEnd();

			GL11.glColor4f(acColor[0], acColor[1], acColor[2], opacity);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2i(xc1, yc2);
			GL11.glVertex2i(xc1, yc1);
			GL11.glVertex2i(xc2, yc1);
			GL11.glVertex2i(xc2, yc2);
			GL11.glEnd();

			// render children
			int cMouseX = mouseX - x1;
			int cMouseY = mouseY - y4;
			for(int i = 0; i < window.countChildren(); i++)
				window.getChild(i).render(cMouseX, cMouseY, partialTicks);

			GL11.glPopMatrix();
			if(window.isScrollingEnabled())
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}


		// title bar buttons
		int x3 = x2;
		int y4 = y1 + 2;
		int y5 = y3 - 2;
		boolean hoveringY = mouseY >= y4 && mouseY < y5;

		if(window.isClosable())
		{
			x3 -= 11;
			int x4 = x3 + 9;
			boolean hovering = hoveringY && mouseX >= x3 && mouseX < x4;
			renderCloseButton(x3, y4, x4, y5, hovering);
		}

		if(window.isPinnable())
		{

		}


		// title bar background
		// behind title
		GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.75f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y3);
		GL11.glVertex2i(x3, y3);
		GL11.glVertex2i(x3, y1);
		GL11.glEnd();

		GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.75f);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y3);
		GL11.glVertex2i(x3, y3);
		GL11.glVertex2i(x3, y1);
		GL11.glEnd();

		// window title
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1, 1, 1, 1);
		FontRenderer fontRenderer = WMinecraft.getFontRenderer();
		String title =
				fontRenderer.trimStringToWidth(window.getTitle(), x3 - x1);
		fontRenderer.drawString(title, x1 + 2, y1 + 3, 0xffffff);
	}

	private void renderTitleBarButton(int x1, int y1, int x2, int y2,
									  boolean hovering)
	{
		int x3 = x2 + 2;

		// button background
		GL11.glColor4f(1000, 0, 0, opacity);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y2);
		GL11.glVertex2i(x2, y2);
		GL11.glVertex2i(x2, y1);
		GL11.glEnd();

		GL11.glColor4f(1000, 0, 0, opacity);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y2);
		GL11.glVertex2i(x2, y2);
		GL11.glVertex2i(x2, y1);
		GL11.glEnd();
	}

	private void renderMinimizeButton(int x1, int y1, int x2, int y2,
									  boolean hovering, boolean minimized)
	{

	}

	private void renderPinButton(int x1, int y1, int x2, int y2,
								 boolean hovering, boolean pinned) {

	}

	private void renderCloseButton(int x1, int y1, int x2, int y2,
								   boolean hovering)
	{
		renderTitleBarButton(x1, y1, x2, y2, hovering);

		double xc1 = x1 + 2;
		double xc2 = x1 + 3;
		double yc1 = y1 + 3;
		double yc2 = y1 + 2;

		// outline
		GL11.glColor4f(1000F, 0F, 0F, opacity);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(xc1, yc1);
		GL11.glVertex2d(xc2, yc2);
		GL11.glEnd();
	}

	public float[] getBgColor()
	{
		return bgColor;
	}

	public float[] getAcColor()
	{
		return acColor;
	}

	public float getOpacity()
	{
		return opacity;
	}

	public void setTooltip(String tooltip)
	{
		this.tooltip = tooltip;
	}

	public void addWindow(Window window)
	{
		windows.add(window);
	}

	public void addPopup(Popup popup)
	{
		popups.add(popup);
	}
}