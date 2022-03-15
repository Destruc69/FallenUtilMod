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
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.utils.EntityFakePlayer;

public final class FakePlayer extends Hack {

	private EntityFakePlayer fakePlayer;

	public FakePlayer() {
		super("FakePlayer", "Spawns a FakePlayer.");
		setCategory(Category.MISC);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);

		fakePlayer = new EntityFakePlayer();
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		fakePlayer.despawn();
	}

	@SubscribeEvent
	public void no(WUpdateEvent event) {
		fakePlayer.setCustomNameTag(mc.player.getName());
	}
}
