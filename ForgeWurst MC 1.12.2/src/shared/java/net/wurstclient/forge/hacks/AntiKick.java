/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.client.Minecraft;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.KeyBindingUtils;
import net.minecraft.client.settings.KeyBinding;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.minecraft.network.play.server.SPacketDisplayObjective;



public final class AntiKick extends Hack {
	public AntiKick() {
		super("AntiExploit", "Avoids crash expoits, Like 2b2ts new SignExploit.");
		setCategory(Category.EXPLOIT);
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
	public void onPacketOutput(WPacketOutputEvent event) {
		if (event.getPacket() instanceof SPacketSignEditorOpen)
			event.setCanceled(true);
		if (event.getPacket() instanceof SPacketParticles)
			event.setCanceled(true);
		if (event.getPacket() instanceof SPacketChangeGameState)
			event.setCanceled(true);
		if (event.getPacket() instanceof SPacketMaps)
			event.setCanceled(true);
		if (event.getPacket() instanceof SPacketResourcePackSend)
			event.setCanceled(true);
		if (event.getPacket() instanceof SPacketCustomSound)
			event.setCanceled(true);
		if (event.getPacket() instanceof SPacketUpdateBossInfo)
			event.setCanceled(true);
		if (event.getPacket() instanceof SPacketSpawnPainting)
			event.setCanceled(true);
		if (event.getPacket() instanceof SPacketUpdateScore)
			event.setCanceled(true);
		if (event.getPacket() instanceof SPacketTabComplete)
			event.setCanceled(true);
		if (event.getPacket() instanceof SPacketDisplayObjective)
			event.setCanceled(true);
	}


	@SubscribeEvent
	public void onPacketInput(WPacketInputEvent event) {
		if (event.getPacket() instanceof CPacketResourcePackStatus)
			event.setCanceled(true);
		if (event.getPacket() instanceof CPacketUpdateSign)
			event.setCanceled(true);
		if (event.getPacket() instanceof CPacketSeenAdvancements)
			event.setCanceled(true);
	}
}