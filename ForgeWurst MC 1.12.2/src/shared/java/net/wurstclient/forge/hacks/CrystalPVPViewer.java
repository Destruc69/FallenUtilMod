/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.TimerUtils;

public final class CrystalPVPViewer extends Hack {

	private final CheckboxSetting info =
			new CheckboxSetting("FighterInfo",
					false);

	public CrystalPVPViewer() {
		super("CPVPShow", "Grab some popcorn, and watch people fight with Crytstals in the server!.");
		setCategory(Category.MISC);
		addSetting(info);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);

		try {
			ChatUtils.message("Make sure in the CPVP server you are above the fight, Example on CC you just turn the module on at the floating spawn, Place yourself somewhere above the fight, anywhere above the fight.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		mc.setRenderViewEntity(mc.player);
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer) {
				if (e != mc.player) {
					if (mc.player.posY > e.lastTickPosY) {

						if (info.isChecked()) {
							ChatUtils.message("--------------------------");
							ChatUtils.message("Name:" + " " + e.getName());
							ChatUtils.message("Health:" + " " + ((EntityPlayer) e).getHealth());
							ChatUtils.message("Possible Attacking Entity:" + " " + e.getEntityWorld().getClosestPlayer(e.lastTickPosX, e.lastTickPosY, e.lastTickPosZ, 999, false).getName());
							ChatUtils.message("--------------------------");
							info.setChecked(false);
						}

						if (TimerUtils.hasPassed(1500)) {
							mc.setRenderViewEntity(e);
							if (event.getPlayer() == mc.player) {
								event.getPlayer().inventory.copyInventory(((EntityPlayer) e).inventory);
								event.getPlayer().getHeldItemMainhand().getItem().setContainerItem(((EntityPlayer) e).getHeldItem(EnumHand.MAIN_HAND).getItem());
								event.getPlayer().getHeldItemOffhand().getItem().setContainerItem(((EntityPlayer) e).getHeldItem(EnumHand.OFF_HAND).getItem());
							}
						}
					}
				}
			}
		}
	}
}
