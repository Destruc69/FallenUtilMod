/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
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

import java.lang.reflect.Field;

public final class Scaffold extends Hack {

	private final CheckboxSetting bot =
			new CheckboxSetting("Auto-Bot",
					false);
	private int xPos;
	private int zPos;

	public Scaffold() {
		super("Scaffold", "Scaffold for you with blocks.");
		setCategory(Category.MOVEMENT);
		addSetting(bot);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
		setTickLength(50);
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
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

		if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock) {
			mc.playerController.processRightClickBlock(mc.player, mc.world, pos, face, new Vec3d(0.5, 0.5, 0.5),
					EnumHand.MAIN_HAND);
			mc.player.connection.sendPacket(new CPacketConfirmTransaction());
			mc.player.swingArm(EnumHand.MAIN_HAND);
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, face.rotateAround(face.getAxis()), EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));

			float[] angle = MathUtils.calcAngle(Scaffold.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)pos.getX() + 0.5f), (double)((float)pos.getY() - 0.5f), (double)((float)pos.getZ() + 0.5f)));
			Scaffold.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angle[0], (float) MathHelper.normalizeAngle((int)((int)angle[1]), (int)360), Scaffold.mc.player.onGround));
		}

		setTickLength(50f / 0.9f);
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

	private void setTickLength(float tickLength)
	{
		try
		{
			Field fTimer = mc.getClass().getDeclaredField(
					wurst.isObfuscated() ? "field_71428_T" : "timer");
			fTimer.setAccessible(true);

			if(WMinecraft.VERSION.equals("1.10.2"))
			{
				Field fTimerSpeed = Timer.class.getDeclaredField(
						wurst.isObfuscated() ? "field_74278_d" : "timerSpeed");
				fTimerSpeed.setAccessible(true);
				fTimerSpeed.setFloat(fTimer.get(mc), 50 / tickLength);

			}else
			{
				Field fTickLength = Timer.class.getDeclaredField(
						wurst.isObfuscated() ? "field_194149_e" : "tickLength");
				fTickLength.setAccessible(true);
				fTickLength.setFloat(fTimer.get(mc), tickLength);
			}

		}catch(ReflectiveOperationException e)
		{
			throw new RuntimeException(e);
		}
	}
}

