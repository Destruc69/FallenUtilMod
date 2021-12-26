/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;

import java.util.UUID;

public final class AntiBot extends Hack {
	public AntiBot() {
		super("AntiBot", "Helps the client know the difference between bot and a real player and remove the bot.");
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
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer) {
				if (e != mc.player) {
					double health = ((EntityPlayer) e).getHealth();
					boolean spectator = ((EntityPlayer) e).isSpectator();
					double level = ((EntityPlayer) e).experienceLevel;
					String profile1 = ((EntityPlayer) e).getGameProfile().getName();
					UUID profile2 = ((EntityPlayer) e).getGameProfile().getId();
					PropertyMap profile3 = ((EntityPlayer) e).getGameProfile().getProperties();

					if (health < 0) {
						mc.world.removeEntity(e);
					}

					if (level < 0) {
						mc.world.removeEntity(e);
					}

					if (profile1 == null) {
						mc.world.removeEntity(e);
					}

					if (profile2 == null) {
						mc.world.removeEntity(e);
					}

					if (profile3 == null) {
						mc.world.removeEntity(e);
					}

					if (spectator) {
						mc.world.removeEntity(e);
					}
				}
			}
		}
	}
}