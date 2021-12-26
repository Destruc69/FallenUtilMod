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
import net.minecraft.network.play.client.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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

	private final SliderSetting rangeb =
			new SliderSetting("BreakRange", "Range for witch we will break the crystal", 4, 1.0, 5, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting rangep =
			new SliderSetting("PlaceRange", "Range for witch we will place the crystal", 4, 1.0, 5, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting autoPlace =
			new CheckboxSetting("AutoPlace", "Shall we place crystals automatically?",
					true);

	private final CheckboxSetting playersOnly =
			new CheckboxSetting("PlayersOnly", "Only place near EntityPlayers",
					true);

	private final CheckboxSetting pause =
			new CheckboxSetting("PauseWhileActive", "Pause while breaking/eating ect",
					true);

	private final CheckboxSetting set =
			new CheckboxSetting("AutoSet", "Auto switches to a Crystal in your hand",
					true);

	private final SliderSetting delay =
			new SliderSetting("Delay [MS]", "The delay for witch we will place crystals", 200, 0.0, 800, 10.0, SliderSetting.ValueDisplay.DECIMAL);


	private final CheckboxSetting pack =
			new CheckboxSetting("ExtraPacket", "Sends extra packets, may not work on most servers",
					true);

	private final SliderSetting max =
			new SliderSetting("MaxSelfDamage", "Max damage a crystal will damage you", 5, 1.0, 8, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting rage =
			new SliderSetting("Rage", "At what health the target should be for us to fast break", 5, 1.0, 8, 1.0, SliderSetting.ValueDisplay.DECIMAL);


	private EntityEnderCrystal entity;

	public AutoCrystal() {
		super("AutoCrystal", "Killaura but for crystals.");
		setCategory(Category.COMBAT);
		addSetting(autoPlace);
		addSetting(playersOnly);
		addSetting(rangeb);
		addSetting(rage);
		addSetting(max);
		addSetting(delay);
		addSetting(pause);
		addSetting(set);
		addSetting(rangep);
		addSetting(pack);
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
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityEnderCrystal) {
				if (mc.player.getDistance(e) < rangep.getValue()) {
					double x = entity.getEntityBoundingBox().calculateXOffset(entity.getEntityBoundingBox(), mc.player.posX);
					double y = entity.getEntityBoundingBox().calculateYOffset(entity.getEntityBoundingBox(), mc.player.posY);
					double z = entity.getEntityBoundingBox().calculateZOffset(entity.getEntityBoundingBox(), mc.player.posZ);

					RotationUtils.faceVectorPacket(new Vec3d(x, y, z));
				}
			}

			if (e instanceof EntityEnderCrystal) {
				if (mc.player.getDistance(e) < rangeb.getValue()) {
					double x = entity.getEntityBoundingBox().calculateXOffset(entity.getEntityBoundingBox(), mc.player.posX);
					double y = entity.getEntityBoundingBox().calculateYOffset(entity.getEntityBoundingBox(), mc.player.posY);
					double z = entity.getEntityBoundingBox().calculateZOffset(entity.getEntityBoundingBox(), mc.player.posZ);

					RotationUtils.faceVectorPacket(new Vec3d(x, y, z));
				}
			}
		}

		float selfDamage = CrystalUtil.calculateDamage(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), mc.player);
		for (Entity e : mc.world.loadedEntityList) {
			if (mc.player.getDistance(e) < rangeb.getValue()) {
				if (e instanceof EntityEnderCrystal) {
					Minecraft.getMinecraft().playerController.attackEntity(mc.player, e);
					mc.player.swingArm(EnumHand.MAIN_HAND);
					return;
				}
			}
		}

		if (autoPlace.isChecked() && mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {

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
			if (selfDamage < max.getValue()) {
				if (pause.isChecked() && mc.player.isHandActive())
					return;

				if (minEntity != null && mc.player.getDistance(minEntity) < rangep.getValue()) {
					if (TimerUtils.passed(delay.getValueL())) {
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

									if (set.isChecked()) {
										if (mc.player.getHeldItemMainhand().getItem() instanceof ItemAir) {
											double id = InventoryUtil.getHandSlot();
											double item = InventoryUtil.getSlot(Items.END_CRYSTAL);

											InventoryUtil.clickSlot((int) item);
											TimerUtils.passed(100L);
											InventoryUtil.clickSlot((int) id);
										}
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
	public void onPlayerDamageBlock(WPlayerDamageBlockEvent event) {

		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer) {
				if (e != mc.player) {
					double health = ((EntityPlayer) e).getHealth();
					if (health < rage.getValue()) {
						try {
							float progress = PlayerControllerUtils.getCurBlockDamageMP()
									+ BlockUtils.getHardness(event.getPos());

							if (progress >= 1)
								return;

						} catch (ReflectiveOperationException a) {
							setEnabled(false);
							throw new RuntimeException(a);
						}
					}

					WMinecraft.getPlayer().connection.sendPacket(new CPacketPlayerDigging(
							CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(),
							event.getFacing()));
				}
			}
		}
	}
}