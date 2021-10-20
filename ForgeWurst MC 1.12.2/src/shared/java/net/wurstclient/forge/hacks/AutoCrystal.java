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
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.CrystalUtil;
import net.wurstclient.forge.utils.KeyBindingUtils;
import net.wurstclient.forge.utils.PlayerControllerUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public final class AutoCrystal extends Hack {

	private final CheckboxSetting autoPlace =
			new CheckboxSetting("AutoPlace",
					true);

	private final CheckboxSetting playersOnly =
			new CheckboxSetting("PlayersOnly",
					true);

	private final CheckboxSetting highPing =
			new CheckboxSetting("HighPing Optimize",
					false);

	private final SliderSetting range =
			new SliderSetting("Range", 5, 4.0, 8, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting maxDamage =
			new SliderSetting("Max Self Damage", 4, 2.0, 8, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	public AutoCrystal() {
		super("AutoCrystal", "Killaura but for crystals.");
		setCategory(Category.COMBAT);
		addSetting(autoPlace);
		addSetting(playersOnly);
		addSetting(highPing);
		addSetting(range);
		addSetting(maxDamage);
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
			if (mc.player.getDistance(e) < range.getValue()) {
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
				if (mc.player.getDistance(e) < minDistance) {
					minEntity = e;
					minDistance = mc.player.getDistance(e);
				}
			}

			if (minEntity != null && mc.player.getDistance(minEntity) < range.getValue()) {
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

				if (highPing.isChecked()) {
					mc.world.removeAllEntities();
					mc.world.getLoadedEntityList();
				}

				float selfDamage = CrystalUtil.calculateDamage(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), mc.player);
				if (selfDamage > maxDamage.getValue()) {
					mc.player.stopActiveHand();
					KeyBindingUtils.setPressed(mc.gameSettings.keyBindUseItem, false);
					KeyBindingUtils.setPressed(mc.gameSettings.keyBindAttack, false);
				}
			}
		}
	}
}