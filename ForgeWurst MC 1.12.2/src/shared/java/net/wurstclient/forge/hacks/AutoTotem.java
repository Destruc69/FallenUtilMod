/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.InventoryUtil;
import net.wurstclient.forge.utils.TimerUtils;

public final class AutoTotem extends Hack {

	private long time = -1L;

	private final CheckboxSetting totem =
			new CheckboxSetting("Totem",
					false);

	private final CheckboxSetting end =
			new CheckboxSetting("EndCrystal",
					false);

	private final CheckboxSetting gap =
			new CheckboxSetting("Gapple",
					false);

	public AutoTotem() {
		super("OffHand", "Puts things in your OffHand.");
		setCategory(Category.COMBAT);
		addSetting(totem);
		addSetting(end);
		addSetting(gap);
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
		if (totem.isChecked()) {
			if (TimerUtils.hasReached(500, true)) {

				Item oldItem = mc.player.getHeldItemOffhand().getItem();
				int slot = InventoryUtil.getSlot(Items.TOTEM_OF_UNDYING);
				InventoryUtil.clickSlot(slot);
				InventoryUtil.clickSlot(45);
				if (oldItem != Items.AIR) {
					InventoryUtil.clickSlot(slot);
				}
			}
		}


		ItemStack engolden = new ItemStack(Items.GOLDEN_APPLE, 1, (short) 1);

		if (gap.isChecked()) {
			if (TimerUtils.hasReached(500, true)) {

				Item oldItem = mc.player.getHeldItemOffhand().getItem();
				int slot = mc.player.inventory.getSlotFor(engolden);
				InventoryUtil.clickSlot(slot);
				InventoryUtil.clickSlot(45);
				if (oldItem != Items.AIR) {
					InventoryUtil.clickSlot(slot);
				}
			}

			if (end.isChecked()) {
				if (TimerUtils.hasReached(500, true)) {

					Item oldItem = mc.player.getHeldItemOffhand().getItem();
					int slot = InventoryUtil.getSlot(Items.END_CRYSTAL);
					InventoryUtil.clickSlot(slot);
					InventoryUtil.clickSlot(45);
					if (oldItem != Items.AIR) {
						InventoryUtil.clickSlot(slot);
					}
				}
			}
		}
	}
}