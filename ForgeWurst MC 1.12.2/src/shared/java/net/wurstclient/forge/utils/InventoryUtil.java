/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

import java.util.ArrayList;

public class InventoryUtil {


	public static int getSlot(Item item) {
		try {
			for (ItemStackUtil itemStack : getAllItems()) {
				if (itemStack.itemStack.getItem().equals(item)) {
					return itemStack.slotId;
				}
			}
		} catch (Exception ignored) {

		}

		return -1;
	}

	public static void clickSlot(int id) {
		if (id != -1) {
			try {
				Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.openContainer.windowId, getClickSlot(id), 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
			} catch (Exception ignored) {

			}
		}
	}
	public static int getClickSlot(int id) {
		if (id == -1) {
			return id;
		}

		if (id < 9) {
			id += 36;
			return id;
		}

		if (id == 39) {
			id = 5;
		} else if (id == 38) {
			id = 6;
		} else if (id == 37) {
			id = 7;
		} else if (id == 36) {
			id = 8;
		} else if (id == 40) {
			id = 45;
		}

		return id;
	}
	public static ItemStack getItemStack(int id) {
		try {
			return Minecraft.getMinecraft().player.inventory.getStackInSlot(id);
		} catch (NullPointerException e) {
			return null;
		}
	}
	public static ArrayList<ItemStackUtil> getAllItems() {
		ArrayList<ItemStackUtil> items = new ArrayList<ItemStackUtil>();

		for (int i = 0; i < 36; i++) {
			items.add(new ItemStackUtil(getItemStack(i), i));
		}

		return items;
	}

	public static class ItemStackUtil {
		public ItemStack itemStack;
		public int slotId;

		public ItemStackUtil(ItemStack itemStack, int slotId) {
			this.itemStack = itemStack;
			this.slotId = slotId;
		}
	}

	public static void updateFirstEmptySlot(ItemStack stack) {
		int slot = 0;
		boolean slotFound = false;
		for (int i = 0; i < 36; i++) {
			if (Minecraft.getMinecraft().player.inventory.getStackInSlot(i).isEmpty()) {
				slot = i;
				slotFound = true;
				break;
			}
		}
		if (!slotFound) {
			ChatUtils.message("Could not find empty slot. Operation has been aborted.");
			return;
		}

		int convertedSlot = slot;
		if (slot < 9)
			convertedSlot += 36;

		if (stack.getCount() > 64) {
			ItemStack passStack = stack.copy();
			stack.setCount(64);
			passStack.setCount(passStack.getCount() - 64);
			Minecraft.getMinecraft().player.inventory.setInventorySlotContents(slot, stack);
			Minecraft.getMinecraft().getConnection()
					.sendPacket(new CPacketCreativeInventoryAction(convertedSlot, stack));
			updateFirstEmptySlot(passStack);
			return;
		}

		Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(convertedSlot, stack));
	}

	public static void updateSlot(int slot, ItemStack stack) {
		Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(slot, stack));
	}
}