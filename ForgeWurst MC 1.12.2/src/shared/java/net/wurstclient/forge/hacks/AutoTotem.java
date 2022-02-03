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
			if (passedMs(500)) {
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
			if (passedMs(500)) {
				Item oldItem = mc.player.getHeldItemOffhand().getItem();
				int slot = mc.player.inventory.getSlotFor(engolden);
				InventoryUtil.clickSlot(slot);
				InventoryUtil.clickSlot(45);
				if (oldItem != Items.AIR) {
					InventoryUtil.clickSlot(slot);
				}
			}

			if (end.isChecked()) {
				if (passedMs(500)) {
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

	public boolean passedS(double s) {
		return this.passedMs((long)s * 1000L);
	}

	public boolean passedDms(double dms) {
		return this.passedMs((long)dms * 10L);
	}

	public boolean passedDs(double ds) {
		return this.passedMs((long)ds * 100L);
	}

	public boolean passedMs(long ms) {
		return this.passedNS(this.convertToNS(ms));
	}

	public void setMs(long ms) {
		this.time = System.nanoTime() - this.convertToNS(ms);
	}

	public boolean passedNS(long ns) {
		return System.nanoTime() - this.time >= ns;
	}

	public long getPassedTimeMs() {
		return this.getMs(System.nanoTime() - this.time);
	}

	public TimerUtils reset() {
		this.time = System.nanoTime();
		return this.reset();
	}

	public long getMs(long time) {
		return this.time / 1000000L;
	}

	public long convertToNS(long time) {
		return this.time * 1000000L;
	}
}


