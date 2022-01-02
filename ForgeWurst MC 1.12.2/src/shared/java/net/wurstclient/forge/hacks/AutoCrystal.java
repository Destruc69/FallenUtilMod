/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketPlayerDigging;
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
import net.wurstclient.forge.utils.BlockUtils;
import net.wurstclient.forge.utils.PlayerControllerUtils;
import net.wurstclient.forge.utils.RotationUtils;

import java.lang.reflect.Field;
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

	private final SliderSetting fast =
			new SliderSetting("FastBreak", "Targets health in order to fast place", 4, 1.0, 10, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting place =
			new CheckboxSetting("FastPlace", "Fast Breaks, might not work on most servers",
					true);

	private EntityEnderCrystal enderCrystal;

	boolean a;

	public AutoCrystal() {
		super("AutoCrystal", "Killaura but for crystals.");
		setCategory(Category.COMBAT);
		addSetting(autoPlace);
		addSetting(playersOnly);
		addSetting(range);
		addSetting(fast);
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

		if (place.isChecked()) {
			try {
				Field rightClickDelayTimer =
						mc.getClass().getDeclaredField(wurst.isObfuscated()
								? "field_71467_ac" : "rightClickDelayTimer");
				rightClickDelayTimer.setAccessible(true);
				rightClickDelayTimer.setInt(mc, 0);

			} catch (ReflectiveOperationException e) {
				setEnabled(false);
				throw new RuntimeException(e);
			}
		}

		try {
			for (Entity a : mc.world.loadedEntityList) {
				if (a instanceof EntityEnderCrystal) {
					if (mc.player.getDistance(a) < range.getValue()) {

						double x = enderCrystal.getEntityBoundingBox().calculateXOffset(enderCrystal.getEntityBoundingBox(), mc.player.lastTickPosX);
						double y = enderCrystal.getEntityBoundingBox().calculateYOffset(enderCrystal.getEntityBoundingBox(), mc.player.lastTickPosY);
						double z = enderCrystal.getEntityBoundingBox().calculateZOffset(enderCrystal.getEntityBoundingBox(), mc.player.lastTickPosY);

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

	@SubscribeEvent
	public void onPlayerDamageBlock(WPlayerDamageBlockEvent event) {
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer) {
				if (mc.player.getDistance(e) < range.getValue()) {
					if (((EntityPlayer) e).getHealth() < fast.getValue()) {
						try {
							PlayerControllerUtils.setBlockHitDelay(0);
							float progress = PlayerControllerUtils.getCurBlockDamageMP()
									+ BlockUtils.getHardness(event.getPos());
							if (progress >= 1)
								return;

						} catch (Exception a) {
							throw new RuntimeException(a);
						}

						WMinecraft.getPlayer().connection.sendPacket(new CPacketPlayerDigging(
								CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(),
								event.getFacing()));
					}
				}
			}
		}
	}
}
