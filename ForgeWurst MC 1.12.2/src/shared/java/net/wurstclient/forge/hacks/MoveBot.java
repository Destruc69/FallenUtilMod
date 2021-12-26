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
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.KeyBindingUtils;

public final class MoveBot extends Hack {

	private final CheckboxSetting forward =
			new CheckboxSetting("Forward",
					false);

	private final CheckboxSetting back =
			new CheckboxSetting("Back",
					false);

	private final CheckboxSetting left =
			new CheckboxSetting("Left",
					false);

	private final CheckboxSetting right =
			new CheckboxSetting("Right",
					false);

	private final CheckboxSetting jump =
			new CheckboxSetting("Jump",
					false);

	public MoveBot()
	{
		super("MoveBot", "Justs moves the player in the way you desire.");
		setCategory(Category.MISC);
		addSetting(forward);
		addSetting(back);
		addSetting(left);
		addSetting(right);
		addSetting(jump);
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

		if (forward.isChecked()) {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindForward, true);
		} else {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindForward, false);
		}

		if (back.isChecked()) {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindBack, true);
		} else {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindBack, false);
		}

		if (left.isChecked()) {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindLeft, true);
		} else {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindLeft, false);
		}

		if (right.isChecked()) {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindRight, true);
		} else {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindRight, false);
		}

	}
}
