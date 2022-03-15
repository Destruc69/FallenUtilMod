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
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Timer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.*;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Field;

public final class Scaffold extends Hack {

	public Scaffold() {
		super("Scaffold", "Scaffold for you with blocks.");
		setCategory(Category.MOVEMENT);

		//PHOBOS
		//PHOBOS
		//PHOBOS
		//PHOBOS
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
	public void onUpdateWalkingPlayerPost(WUpdateEvent event) {
		//PHOBOS
		//PHOBOS
		//PHOBOS
		BlockPos playerBlock;
		if (BlockUtils.isScaffoldPos((playerBlock = BlockUtils.getPlayerPosWithEntity()).add(0, -1, 0))) {
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
		Block block;
		BlockPos pos = posI;
		if (face == EnumFacing.UP) {
			pos = pos.add(0, -1, 0);
		} else if (face == EnumFacing.NORTH) {
			pos = pos.add(0, 0, 1);
		} else if (face == EnumFacing.SOUTH) {
			//PHOBOS
			//PHOBOS
			pos = pos.add(0, 0, -1);
		} else if (face == EnumFacing.EAST) {
			pos = pos.add(-1, 0, 0);
		} else if (face == EnumFacing.WEST) {
			pos = pos.add(1, 0, 0);
		}

		//PHOBOS
		int oldSlot = Scaffold.mc.player.inventory.currentItem;
		int newSlot = -1;
		for (int i = 0; i < 9; ++i) {
			ItemStack stack = Scaffold.mc.player.inventory.getStackInSlot(i);
			if (InventoryUtil.isItemStackNull(stack) || !(stack.getItem() instanceof ItemBlock) || !Block.getBlockFromItem((Item) stack.getItem()).getDefaultState().isFullBlock())
				continue;
			newSlot = i;
			break;
		}
		if (newSlot == -1) {
			return;
		}

		BlockPos blockPos = Minecraft.getMinecraft().player.getPosition().down();
		Block blocks = Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock();

		boolean crouched = false;
		if (!Scaffold.mc.player.isSneaking() && !(blocks.getBlockState().getBlock() instanceof BlockAir)) {
			Scaffold.mc.player.connection.sendPacket((Packet) new CPacketEntityAction((Entity) Scaffold.mc.player, CPacketEntityAction.Action.START_SNEAKING));
			crouched = true;
		}
		if (!(Scaffold.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
			Scaffold.mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(newSlot));
			Scaffold.mc.player.inventory.currentItem = newSlot;
			Scaffold.mc.playerController.updateController();
		}


		//PHOBOS		//PHOBOS		//PHOBOS
		Scaffold.mc.playerController.processRightClickBlock(Scaffold.mc.player, Scaffold.mc.world, pos, face, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);

		if (!mc.gameSettings.keyBindJump.isKeyDown()) {
			float[] angle = MathUtils.calcAngle(Scaffold.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double) ((float) pos.getX() + 0.5f), (double) ((float) pos.getY() - 0.5f), (double) ((float) pos.getZ() + 0.5f)));
			Scaffold.mc.player.connection.sendPacket((Packet) new CPacketPlayer.Rotation(angle[0], (float) MathHelper.normalizeAngle((int) ((int) angle[1]), (int) 360), Scaffold.mc.player.onGround));
		}

		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			Scaffold.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, mc.player.onGround));
		}
		//PHOBOS		//PHOBOS		//PHOBOS		//PHOBOS		//PHOBOS
		Scaffold.mc.player.swingArm(EnumHand.MAIN_HAND);
		Scaffold.mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(oldSlot));
		Scaffold.mc.player.inventory.currentItem = oldSlot;
		Scaffold.mc.playerController.updateController();
		if (crouched) {
			Scaffold.mc.player.connection.sendPacket((Packet) new CPacketEntityAction((Entity) Scaffold.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
	}
}