/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.MathUtils;
import net.wurstclient.forge.utils.PlayerUtils;

public final class NoSlowDown extends Hack {
	private final CheckboxSetting ncp =
			new CheckboxSetting("NCP-Strict",
					false);

	public NoSlowDown() {
		super("NoSlowDown", "No time to slow down when eating");
		setCategory(Category.MOVEMENT);
		addSetting(ncp);
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
		if (mc.player.isHandActive() && mc.player.getHeldItemMainhand().getItem() instanceof Item) {
			if (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {
				double[] dir = MathUtils.directionSpeed(0.2);
				mc.player.motionX = dir[0];
				mc.player.motionZ = dir[1];
			}

			if (ncp.isChecked()) {
				mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, PlayerUtils.GetLocalPlayerPosFloored(), EnumFacing.DOWN));
			}
		}
	}
}