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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public final class AutoCrystalReWrite extends Hack {

	//Keeping it simple till i learn more about CA

	private final SliderSetting range =
			new SliderSetting("Range", "Range for witch we will break and place the crystals", 4, 1.0, 6, 0.5, SliderSetting.ValueDisplay.DECIMAL);

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
	public void Place(WUpdateEvent event) {
		if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {
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

										if (!PlayerUtils.CanSeeBlock(player.getPosition().add(i, -1, j))) {
											mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(player.getPosition().add(i, -1, j), EnumFacing.UP, EnumHand.MAIN_HAND, player.getPosition().add(i, -1, j).getX(), player.getPosition().add(i, -1, j).getY(), player.getPosition().add(i, -1, j).getZ()));
										}

										lookAtPacket(player.getPosition().add(i, -1, j).getX(), player.getPosition().add(i, -1, j).getY(), player.getPosition().add(i, -1, j).getZ(), mc.player);

										mc.playerController.processRightClickBlock(mc.player, mc.world,
												player.getPosition().add(i, -1, j), EnumFacing.UP, mc.objectMouseOver.hitVec,
												EnumHand.MAIN_HAND);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void Break(WUpdateEvent event) {
		for (Entity crystal : mc.world.loadedEntityList) {
			if (crystal instanceof EntityEnderCrystal) {
				if (mc.player.getDistance(crystal) < range.getValueF()) {

					if (!PlayerUtils.CanSeeBlock(crystal.getPosition())) {
						mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
					}

					lookAtPacket(crystal.getPosition().getX(), crystal.getPosition().getY(), crystal.getPosition().getZ(), mc.player);

					mc.playerController.attackEntity(mc.player, crystal);
				}
			}
		}
	}

	//POSTMAN UTILS
	//POSTMAN UTILS
	//POSTMAN UTILS
	//POSTMAN UTILS
	//POSTMAN UTILS

	public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
		double dirx = me.posX - px;
		double diry = me.posY - py;
		double dirz = me.posZ - pz;

		double len = Math.sqrt(dirx*dirx + diry*diry + dirz*dirz);

		dirx /= len;
		diry /= len;
		dirz /= len;

		double pitch = Math.asin(diry);
		double yaw = Math.atan2(dirz, dirx);

		pitch = pitch * 180.0d / Math.PI;
		yaw = yaw * 180.0d / Math.PI;

		yaw += 90f;

		return new double[]{yaw,pitch};
	}

	private static void setYawAndPitch(float yaw1, float pitch1) {
		mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw1, pitch1, mc.player.onGround));
	}

	private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
		double[] v = calculateLookAt(px, py, pz, me);
		setYawAndPitch((float) v[0], (float) v[1]);
	}

}
