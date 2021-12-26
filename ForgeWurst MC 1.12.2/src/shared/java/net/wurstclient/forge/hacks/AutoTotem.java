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
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.utils.InventoryUtil;
import net.wurstclient.forge.utils.TimerUtils;

public final class AutoTotem extends Hack {
	public AutoTotem() {
		super("AutoTotem", "We will activate the totem for you.");
		setCategory(Category.COMBAT);
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
		if (TimerUtils.passed(500L)) {
			Item oldItem = mc.player.getHeldItemOffhand().getItem();
			int slot = InventoryUtil.getSlot(Items.TOTEM_OF_UNDYING);
			InventoryUtil.clickSlot(slot);
			InventoryUtil.clickSlot(45);
			if (oldItem != Items.AIR) {
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.OPEN_INVENTORY));
				InventoryUtil.clickSlot(slot);
			}
		}
	}
}
