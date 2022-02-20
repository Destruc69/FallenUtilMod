/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.CrystalUtil;
import net.wurstclient.forge.utils.RotationUtils;
import net.wurstclient.forge.utils.TimerUtils;

import java.util.ArrayList;

public final class AutoCrystalReWrite extends Hack {

	//Keeping it simple till i learn more about CA


	//Important Settings
	private final SliderSetting range =
			new SliderSetting("Range", "Range for witch we will break or place the crystals", 4, 1.0, 6, 0.5, SliderSetting.ValueDisplay.DECIMAL);


	public AutoCrystalReWrite() {
		super("AutoCrystal", "Killaura but for Crystals.");
		setCategory(Category.COMBAT);
		addSetting(range);
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
	public void placeLogic(WUpdateEvent event) {
		for (Entity player : mc.world.loadedEntityList) {
			if (player instanceof EntityPlayer) {
				if (player != mc.player) {
					if (mc.world.getClosestPlayer(mc.player.posX, mc.player.posY, mc.player.posZ, range.getValue(), false).getDistance(player) < range.getValueF()) {
						for (int i = -1; i <= 1; i++) {
							for (int j = -1; j <= 1; j++) {
								if (mc.world.getBlockState(player.getPosition().add(i, 0, j)).getBlock()
										.equals(Blocks.AIR)
										&& (mc.world.getBlockState(player.getPosition().add(i, -1, j)).getBlock()
										.equals(Blocks.OBSIDIAN)
										|| mc.world.getBlockState(player.getPosition().add(i, -1, j)).getBlock()
										.equals(Blocks.BEDROCK))) {

									mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
									mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
									mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(player.getPosition().add(i, -1, j), EnumFacing.UP, EnumHand.MAIN_HAND, player.getPosition().add(i, -1, j).getX(), player.getPosition().add(i, -1, j).getY(), player.getPosition().add(i, -1, j).getZ()));

									RotationUtils.faceVectorPacket(new Vec3d(player.getPosition().add(i, -1, j).getX(), player.getPosition().add(i, -1, j).getY(), player.getPosition().add(i, -1, j).getZ()));
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void breakLogin(WUpdateEvent event) {
		for (Entity crystal : mc.world.loadedEntityList) {
			if (crystal instanceof EntityEnderCrystal) {
				if (mc.player.getDistance(crystal) < range.getValueF()) {
					mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
					mc.player.connection.sendPacket(new CPacketUseEntity(crystal));

					RotationUtils.faceVectorPacket(new Vec3d(crystal.getPositionVector().x, crystal.getPositionVector().y, crystal.getPositionVector().z));
				}
			}
		}
	}
}
