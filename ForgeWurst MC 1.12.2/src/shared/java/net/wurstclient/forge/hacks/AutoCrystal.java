/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WPlayerDamageBlockEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.*;

import java.util.ArrayList;

public final class AutoCrystal extends Hack {

	private final SliderSetting range =
			new SliderSetting("Range", "Range for witch we will break and place the crystals", 4, 1.0, 5, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting autoPlace =
			new CheckboxSetting("AutoPlace", "Shall we place crystals automatically?",
					true);

	private final CheckboxSetting playersOnly =
			new CheckboxSetting("PlayersOnly", "Only place near EntityPlayers",
					true);

	private EntityEnderCrystal enderCrystal;

	boolean a;

	public AutoCrystal() {
		super("AutoCrystal", "Killaura but for crystals.");
		setCategory(Category.COMBAT);
		addSetting(autoPlace);
		addSetting(playersOnly);
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
		try {
			for (Entity a : mc.world.loadedEntityList) {
				if (a instanceof EntityEnderCrystal) {
					if (mc.player.getDistance(a) < range.getValue()) {

						double x = enderCrystal.getRenderBoundingBox().calculateXOffset(enderCrystal.getRenderBoundingBox(), mc.player.lastTickPosX);
						double y = enderCrystal.getRenderBoundingBox().calculateYOffset(enderCrystal.getRenderBoundingBox(), mc.player.lastTickPosY);
						double z = enderCrystal.getRenderBoundingBox().calculateZOffset(enderCrystal.getEntityBoundingBox(), mc.player.lastTickPosY);

						RotationUtils.faceVectorPacketInstant(new Vec3d(x, y, z));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Entity e : mc.world.loadedEntityList) {
			if (mc.player.getDistance(e) < range.getValue()) {
				if (e instanceof EntityEnderCrystal) {
					Minecraft.getMinecraft().playerController.attackEntity(mc.player, e);
					mc.player.swingArm(EnumHand.MAIN_HAND);
					return;
				}
			}
		}

		if (autoPlace.isChecked() && mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal || mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal) {

			ArrayList<Entity> attackEntityList = new ArrayList<Entity>();

			for (Entity e : mc.world.loadedEntityList) {

				if (e instanceof EntityPlayer && e != mc.player && playersOnly.isChecked()) {
					attackEntityList.add(e);
				}
				if (e != mc.player && !playersOnly.isChecked()) {

					attackEntityList.add(e);
				}
			}

			Entity minEntity = null;
			Float minDistance = 100f;

			for (Entity e : attackEntityList) {
				assert mc.player != null;
				if (mc.player.getDistance(e) < minDistance) {
					minEntity = e;
					minDistance = mc.player.getDistance(e);
				}
			}
			if (minEntity != null && mc.player.getDistance(minEntity) < range.getValue() && a == false) {
				for (int i = -5; i <= 5; i++) {
					for (int j = -5; j <= 5; j++) {
						if (mc.world.getBlockState(minEntity.getPosition().add(i, 0, j)).getBlock()
								.equals(Blocks.AIR)
								&& (mc.world.getBlockState(minEntity.getPosition().add(i, -1, j)).getBlock()
								.equals(Blocks.OBSIDIAN)
								|| mc.world.getBlockState(minEntity.getPosition().add(i, -1, j)).getBlock()
								.equals(Blocks.BEDROCK))) {
							mc.playerController.processRightClickBlock(mc.player, mc.world,
									minEntity.getPosition().add(i, -1, j), EnumFacing.UP, mc.objectMouseOver.hitVec,
									EnumHand.MAIN_HAND);
						}
					}
				}
			}
		}
	}
}