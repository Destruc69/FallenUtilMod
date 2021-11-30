/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import io.netty.util.internal.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.*;

import java.util.ArrayList;

public final class Scaffold extends Hack {

	private final CheckboxSetting rotation =
			new CheckboxSetting("Bypass-Strict",
					true);

	public Scaffold()
	{
		super("Scaffold", "Scaffold for you with blocks.");
		setCategory(Category.MOVEMENT);
		addSetting(rotation);
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
		BlockPos playerBlock;
		if (mc.world == null) {
			return;
		}
		if (BlockUtils.isScaffoldPos((playerBlock = getPlayerPosWithEntity()).add(0, -1, 0))) {
			if (BlockUtils.isValidBlock(playerBlock.add(0, -2, 0))) {
				this.place(playerBlock.add(0, -1, 0), EnumFacing.UP);
			} else if (BlockUtils.isValidBlock(playerBlock.add(-1, -1, 0))) {
				this.place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
			} else if (BlockUtils.isValidBlock(playerBlock.add(1, -1, 0))) {
				this.place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
			} else if (BlockUtils.isValidBlock(playerBlock.add(0, -1, -1))) {
				this.place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
			} else if (BlockUtils.isValidBlock(playerBlock.add(0, -1, 1))) {
				this.place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
			} else if (BlockUtils.isValidBlock(playerBlock.add(1, -1, 1))) {
				if (BlockUtils.isValidBlock(playerBlock.add(0, -1, 1))) {
					this.place(playerBlock.add(0, -1, 1), EnumFacing.NORTH);
				}
				this.place(playerBlock.add(1, -1, 1), EnumFacing.EAST);
			} else if (BlockUtils.isValidBlock(playerBlock.add(-1, -1, 1))) {
				if (BlockUtils.isValidBlock(playerBlock.add(-1, -1, 0))) {
					this.place(playerBlock.add(0, -1, 1), EnumFacing.WEST);
				}
				this.place(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
			} else if (BlockUtils.isValidBlock(playerBlock.add(1, -1, 1))) {
				if (BlockUtils.isValidBlock(playerBlock.add(0, -1, 1))) {
					this.place(playerBlock.add(0, -1, 1), EnumFacing.SOUTH);
				}
				this.place(playerBlock.add(1, -1, 1), EnumFacing.WEST);
			} else if (BlockUtils.isValidBlock(playerBlock.add(1, -1, 1))) {
				if (BlockUtils.isValidBlock(playerBlock.add(0, -1, 1))) {
					this.place(playerBlock.add(0, -1, 1), EnumFacing.EAST);
				}
				this.place(playerBlock.add(1, -1, 1), EnumFacing.NORTH);
			}
		}
	}

	public void place(BlockPos posI, EnumFacing face) {
		BlockPos pos = posI;
		if (face == EnumFacing.UP) {
			pos = pos.add(0, -1, 0);
		} else if (face == EnumFacing.NORTH) {
			pos = pos.add(0, 0, 1);
		} else if (face == EnumFacing.SOUTH) {
			pos = pos.add(0, 0, -1);
		} else if (face == EnumFacing.EAST) {
			pos = pos.add(-1, 0, 0);
		} else if (face == EnumFacing.WEST) {
			pos = pos.add(1, 0, 0);
		}
		int oldSlot = mc.player.inventory.currentItem;
		int newSlot = -1;
		for (int i = 0; i < 9; ++i) {
			ItemStack stack = mc.player.inventory.getStackInSlot(i);
			if (InventoryUtil.isItemStackNull(stack) || !(stack.getItem() instanceof ItemBlock)
					|| !Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock())
				continue;
			newSlot = i;
			break;
		}
		if (newSlot == -1) {
			return;
		}
		boolean crouched = false;
		if (!mc.player.isSneaking()
				&& WorldUtil.RIGHTCLICKABLE_BLOCKS.contains(mc.world.getBlockState(pos).getBlock())) {
			mc.player.connection
					.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
			crouched = true;
		}
		if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
			mc.player.connection.sendPacket(new CPacketHeldItemChange(newSlot));
			mc.player.inventory.currentItem = newSlot;
			mc.playerController.updateController();
		}
		if (this.rotation.isChecked()) {
			mc.player.setSprinting(false);
			RotationUtils.faceVectorPacket(new Vec3d(0,-5, 0));
		}
		mc.playerController.processRightClickBlock(mc.player, mc.world, pos, face, new Vec3d(0.5, 0.5, 0.5),
				EnumHand.MAIN_HAND);
		mc.player.swingArm(EnumHand.MAIN_HAND);
		mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
		mc.player.inventory.currentItem = oldSlot;
		mc.playerController.updateController();
		if (crouched) {
			mc.player.connection
					.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
	}

	public static BlockPos getPlayerPosWithEntity() {
		return new BlockPos(
				(mc.player.getRidingEntity() != null) ? mc.player.getRidingEntity().posX
						: mc.player.posX,
				(mc.player.getRidingEntity() != null) ? mc.player.getRidingEntity().posY
						: mc.player.posY,
				(mc.player.getRidingEntity() != null) ? mc.player.getRidingEntity().posZ
						: mc.player.posZ);
	}
}