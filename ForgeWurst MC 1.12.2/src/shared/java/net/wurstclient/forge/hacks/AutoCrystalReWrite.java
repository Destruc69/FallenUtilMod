/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.RotationUtils;

public final class AutoCrystalReWrite extends Hack {

	private final SliderSetting range =
			new SliderSetting("Range", "Range for witch we will break and place the crystals", 4, 1.0, 5, 1.0, SliderSetting.ValueDisplay.DECIMAL);


	public AutoCrystalReWrite() {
		super("AutoCrystal2", "ReWrite of AutoCrystal.");
		setCategory(Category.MOVEMENT);
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
	public void onUpdate(WUpdateEvent event) {

		//Written by Paul (FallenUtilityMod)

		if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal || mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal) {
			for (Entity e : mc.world.loadedEntityList) {
				if (e instanceof EntityPlayer) {

					BlockPos blockPos = e.getPosition().add(new Vec3i(-1, -1, +1)).add(new Vec3i(+1, -1, -1)).add(new Vec3i(+1, -1, +1)).add(new Vec3i(-1, -1, -1));
					Block block = e.world.getBlockState(blockPos).getBlock();

					if (block.getBlockState().getBlock() instanceof BlockObsidian || block.getBlockState().getBlock().equals(Blocks.BEDROCK)) {
						if (mc.player.getDistance(e) < range.getValue()) {
							if (e != mc.player) {
								mc.playerController.processRightClickBlock(mc.player,
										mc.world, e.getPosition().add(-1, -1, +1).add(+1, -1, -1).add(+1, -1, +1).add(-1, -1, -1), EnumFacing.UP, mc.objectMouseOver.hitVec, EnumHand.MAIN_HAND);
								mc.player.swingArm(EnumHand.MAIN_HAND);

								BlockPos x = e.getPosition().add(new Vec3i(-1, -1, +1)).add(new Vec3i(+1, -1, -1)).add(new Vec3i(+1, -1, +1)).add(new Vec3i(-1, -1, -1));

								double xx = x.getX();
								double yy = x.getY();
								double zz = x.getZ();

								RotationUtils.faceVectorPacketInstant(new Vec3d(xx, yy, zz));
							}
						}
					}
				}
			}

			for (Entity a : mc.world.loadedEntityList) {
				if (a instanceof EntityEnderCrystal) {
					if (mc.player.getDistance(a) < range.getValue()) {
						mc.playerController.attackEntity(mc.player, a);

						double x = a.getEntityBoundingBox().calculateXOffset(a.getEntityBoundingBox(), mc.player.posX);
						double y = a.getEntityBoundingBox().calculateYOffset(a.getEntityBoundingBox(), mc.player.posY);
						double z = a.getEntityBoundingBox().calculateZOffset(a.getEntityBoundingBox(), mc.player.posZ);

						RotationUtils.faceVectorPacketInstant(new Vec3d(x, y, z));
					}
				}
			}
		}
	}
}