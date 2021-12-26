/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.KeyBindingUtils;
import net.wurstclient.forge.utils.RotationUtils;
import scala.tools.nsc.backend.icode.Primitives;

public final class Portal extends Hack {
	private final SliderSetting range =
			new SliderSetting("Range", 8, 1.0, 50, 1.0, SliderSetting.ValueDisplay.DECIMAL);
	private final CheckboxSetting dis =
			new CheckboxSetting("DistanceToTarget",
					false);

	private Entity entity;
	boolean a = false;

	public Portal() {
		super("PortalFinder", "Bot that finds Portals.");
		setCategory(Category.PATHFINDING);
		addSetting(dis);
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
		for (TileEntity e : mc.world.loadedTileEntityList) {
			if (e instanceof TileEntityEndPortal) {
				if (e.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) < range.getValue()) {
					double dd = RotationUtils.getEyesPos().distanceTo(
							e.getRenderBoundingBox().getCenter());
					double posXX = e.getPos().getX() + (e.getPos().getX() - e.getPos().getX()) * dd
							- mc.player.posX;
					double posYY = e.getPos().getY() + (e.getPos().getY() - e.getPos().getY()) * dd
							+ e.getRenderBoundingBox().calculateYOffset(e.getRenderBoundingBox(), mc.player.posY) * 0.5 - mc.player.posY
							- mc.player.getEyeHeight();
					double posZZ = e.getPos().getZ() + (e.getPos().getZ() - e.getPos().getZ()) * dd
							- mc.player.posZ;

					mc.player.rotationYaw = (float) Math.toDegrees(Math.atan2(posZZ, posXX)) - 90;

					if (dis.isChecked()) {
						ForgeWurst.getForgeWurst().getHax().portal.dis.setChecked(false);
						double dis = e.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ);
						ChatUtils.message("Distance:" + " " + dis);
					}
				}
			}
		}

		if (mc.player.moveForward < 0 && mc.player.moveStrafing < 0) {
			if (mc.player.onGround) {
				mc.player.jump();
			}
		}

		if (mc.player.collidedHorizontally && !mc.player.isInWater()) {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindAttack, true);
			mc.player.rotationPitch = 50;
		}

		if (mc.player.isInWater()) {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindJump, true);
		} else {
			KeyBindingUtils.setPressed(mc.gameSettings.keyBindJump, false);
		}
	}
}