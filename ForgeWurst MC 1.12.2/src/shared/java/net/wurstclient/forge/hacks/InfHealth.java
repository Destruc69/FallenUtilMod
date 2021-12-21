/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.GameType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

public final class InfHealth extends Hack
{
	public InfHealth()
	{
		super("InfHealth", "Make you invincible.");
		setCategory(Category.WORLD);
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
	public void onUpdate(WUpdateEvent event)
	{
		mc.player.setHealth(10f);
		mc.player.setPlayerSPHealth(10f);
		mc.player.setGameType(GameType.CREATIVE);
		mc.player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 100, 2));
		mc.player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 100, 2));
		mc.player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 100, 2));
		mc.player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 2));
	}
}
