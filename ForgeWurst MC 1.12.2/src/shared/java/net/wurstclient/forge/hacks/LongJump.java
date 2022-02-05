/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.compatibility.WPlayer;
import net.wurstclient.forge.utils.BlockUtils;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class LongJump extends Hack
{
	public LongJump()
	{
		super("LongJump", "Made to bypass NCP.");
		setCategory(Category.MOVEMENT);
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
		if (mc.player.onGround && mc.player.moveForward != 0) {
			mc.player.jump();
		}

		if (mc.player.onGround && mc.player.moveStrafing != 0) {
			mc.player.jump();
		}

		EntityPlayerSP player = event.getPlayer();
		World world = WPlayer.getWorld(player);

		if(!player.isAirBorne || player.isInWater() || player.isInLava()
				|| player.isOnLadder() || player.motionY >= 0)
			return;

		float moveSpeed = 5f;
		float minHeight = 0f;
		float fallSpeed = 1.0f;

		if(minHeight > 0)
		{
			AxisAlignedBB box = player.getEntityBoundingBox();
			box = box.union(box.offset(0, -minHeight, 0));
			// Using expand() with negative values doesn't work in 1.10.2.
			if(world.collidesWithAnyBlock(box))
				return;

			BlockPos min =
					new BlockPos(new Vec3d(box.minX, box.minY, box.minZ));
			BlockPos max =
					new BlockPos(new Vec3d(box.maxX, box.maxY, box.maxZ));
			Stream<BlockPos> stream = StreamSupport
					.stream(BlockPos.getAllInBox(min, max).spliterator(), true);

			// manual collision check, since liquids don't have bounding boxes
			if(stream.map(BlockUtils::getBlock)
					.anyMatch(b -> b instanceof BlockLiquid))
				return;
		}

		player.motionY = Math.max(player.motionY, -fallSpeed);
		player.jumpMovementFactor *= moveSpeed;
	}
}
