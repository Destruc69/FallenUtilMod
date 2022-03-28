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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.CrystalUtil;
import net.wurstclient.forge.utils.RotationUtils;

public final class AutoCrystal extends Hack {

	private final SliderSetting range = new SliderSetting("Range", "Range for witch we will break and place the crystals", 4.0D, 1.0D, 5.0D, 1.0D, SliderSetting.ValueDisplay.DECIMAL);

	public AutoCrystal() {
		super("AutoCrystal", "Auto Crystal but for Killaura.");
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
	public void place(WUpdateEvent event) {
		if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {
			for (Entity player : mc.world.loadedEntityList) {
				if (player instanceof EntityPlayer) {
					if (mc.world.getClosestPlayer(mc.player.posX, mc.player.posY, mc.player.posZ, range.getValue(), false).getDistance(player) < range.getValueF()) {
						for (int i = -1; i <= 1; i++) {
							for (int j = -1; j <= 1; j++) {
								if (mc.world.getBlockState(player.getPosition().add(i, -1, j)).getBlock().equals(Blocks.AIR))
									;
								mc.playerController.processRightClickBlock(mc.player, mc.world, mc.player.getPosition().add(i, -1, j), EnumFacing.UP, mc.objectMouseOver.hitVec, EnumHand.MAIN_HAND);

								lookAtPacket(player.posX, player.posY, player.posZ, mc.player);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void breakk(WUpdateEvent event) {
		for (Entity crystal : mc.world.loadedEntityList) {
			if (crystal instanceof EntityEnderCrystal) {
				if (mc.player.getDistance(crystal) < range.getValueF()) {
					mc.playerController.attackEntity(mc.player, crystal);

					lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, mc.player);
				}
			}
		}
	}

	public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
		double dirx = me.posX - px;
		double diry = me.posY - py;
		double dirz = me.posZ - pz;

		double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

		dirx /= len;
		diry /= len;
		dirz /= len;

		double pitch = Math.asin(diry);
		double yaw = Math.atan2(dirz, dirx);

		pitch = pitch * 180.0d / Math.PI;
		yaw = yaw * 180.0d / Math.PI;

		yaw += 90f;

		return new double[]{yaw, pitch};
	}

	private static void setYawAndPitch(float yaw1, float pitch1) {
		RotationUtils.faceVectorPacket(new Vec3d(yaw1, pitch1, 0));
	}

	private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
		double[] v = calculateLookAt(px, py, pz, me);
		setYawAndPitch((float) v[0], (float) v[1]);
	}
}
