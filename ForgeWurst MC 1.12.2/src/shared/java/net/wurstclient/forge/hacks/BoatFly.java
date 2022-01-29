/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.PlayerUtils;

import java.util.Objects;

public final class BoatFly extends Hack {
	private final SliderSetting speed =
			new SliderSetting("Speed", 1, 0.05, 5, 0.005, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting velocity =
			new CheckboxSetting("Velocity",
					false);

	public BoatFly() {
		super("EntityFly", "Fly with Entity's.");
		setCategory(Category.MOVEMENT);
		addSetting(speed);
		addSetting(velocity);

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

		if (!mc.player.isRiding()) return;
		EntityPlayerSP player = event.getPlayer();

		player.capabilities.isFlying = false;
		Objects.requireNonNull(mc.player.getRidingEntity()).motionX = 0;
		mc.player.getRidingEntity().motionY = 0;
		mc.player.getRidingEntity().motionZ = 0;
		player.jumpMovementFactor = speed.getValueF();

		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.getRidingEntity().motionY += speed.getValue();
		}
		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.getRidingEntity().motionY -= speed.getValue();
		}

		if (velocity.isChecked())
			if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown())
				mc.player.getRidingEntity().setVelocity(0, 0, 0);
	}
}