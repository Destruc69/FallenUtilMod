/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.CrystalUtil;
import net.wurstclient.forge.utils.PlayerControllerUtils;
import net.wurstclient.forge.utils.RotationUtils;
import net.wurstclient.forge.utils.TimerUtils;

import java.lang.reflect.Field;

public final class AutoCrystalReWrite extends Hack {

	private final SliderSetting range =
			new SliderSetting("Range", "Range for witch we will break or place the crystals", 4, 1.0, 6, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting breakk =
			new CheckboxSetting("AutoPlace", "Auto Place the crystals near an entity",
					false);

	private final SliderSetting health =
			new SliderSetting("FastPlace", "At what health the target should be for us to fast place? \n" +
					"+ , Could be buggy on most servers.", 5, 1, 10, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting enh =
			new CheckboxSetting("EnableFastPlace", "For the setting Above, should this be applied?",
					false);

	private final SliderSetting fastBreak =
			new SliderSetting("FastBreak", "At what health the target should be for us to fast break? \n" +
					"+ , Could be buggy on most servers.", 5, 1, 10, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting enb =
			new CheckboxSetting("EnableFastBreak", "For the setting Above, should this be applied?",
					false);

	private final SliderSetting cap =
			new SliderSetting("AntiSuicide [CAP]", "Whats the max damage a placed crystal can do to you?", 4, 1.0, 10, 1.0, SliderSetting.ValueDisplay.DECIMAL);


	private final CheckboxSetting anti =
			new CheckboxSetting("AntiSuicide", "Will not place a crystal if it will kill you",
					false);

	private final SliderSetting delay =
			new SliderSetting("Delay [TICK]", "Delay for witch we will place and break the crystals \n" +
					", 20 Ticks is 1 Second", 10.0, 10.0, 50.0, 10.0, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting enemy =
			new CheckboxSetting("DamageEnemy", "For the setting below, should this be applied?",
					false);

	private final SliderSetting enemy1 =
			new SliderSetting("DamageEnemy [LIMIT]", "Whats the minimum damage a placed crystal can do to the enemy?", 4, 1.0, 10, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting debug =
			new CheckboxSetting("DebugMode", "Explains what AutoCrystal is thinking",
					false);

	public AutoCrystalReWrite() {
		super("AutoCrystal", "Killaura but for Crystals, The rotations in this bypass crazy.");
		setCategory(Category.COMBAT);
		addSetting(range);
		addSetting(breakk);
		addSetting(health);
		addSetting(enh);
		addSetting(fastBreak);
		addSetting(enb);
		addSetting(cap);
		addSetting(anti);
		addSetting(delay);
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

		if (TimerUtils.passedTick(delay.getValue())) {
			if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal || mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal && breakk.isChecked()) {
				for (Entity e : mc.world.loadedEntityList) {
					if (e instanceof EntityPlayer) {
						if (e != mc.player) {
							if (e != null && e != mc.player && mc.world.getClosestPlayer(mc.player.posX, mc.player.posY, mc.player.posZ, range.getValue(), false).getDistance(e) < range.getValue()) {

								BlockPos[] positions = {e.getPosition().add(1, 0, 0), e.getPosition().add(-1, 0, 0), e.getPosition().add(0, 0, 1), e.getPosition().add(0, 0, -1)};

								if (mc.world.getBlockState(e.getPosition()).getBlock() instanceof BlockObsidian || mc.world.getBlockState(e.getPosition()).getBlock().equals(Blocks.BEDROCK))
									return;

								boolean b = Math.round(e.posX) == Math.round(mc.player.posX);
								boolean c = Math.round(e.posZ) == Math.round(mc.player.posZ);
								if (b && c)
									return;

								for (int i = -1; i <= 1; i++) {
									for (int j = -1; j <= 1; j++) {
										if (mc.world.getBlockState(e.getPosition().add(i, 0, j)).getBlock()
												.equals(Blocks.AIR)
												&& (mc.world.getBlockState(e.getPosition().add(i, -1, j)).getBlock()
												.equals(Blocks.OBSIDIAN)
												|| mc.world.getBlockState(e.getPosition().add(i, -1, j)).getBlock()
												.equals(Blocks.BEDROCK))) {

											float selfDamage = CrystalUtil.calculateDamage(e.getPosition().add(i, -1, j), mc.player);
											float enemyDamage = CrystalUtil.calculateDamage(mc.player.getPosition(), (EntityLivingBase) e);

											if (anti.isChecked() && selfDamage > cap.getValueI())
												return;

											if (enemy.isChecked() && enemyDamage > enemy1.getValueI())
												return;

											RotationUtils.faceVectorPacket(new Vec3d(i, -1, j));

											mc.playerController.processRightClickBlock(mc.player, mc.world,
													e.getPosition().add(i, -1, j), EnumFacing.UP, mc.objectMouseOver.hitVec,
													EnumHand.MAIN_HAND);
										}
									}
								}
							}
						}
					}
				}
			}
		}


		for (Entity a : mc.world.loadedEntityList) {
			if (a instanceof EntityEnderCrystal) {
				if (mc.player.getDistance(a) < range.getValue()) {
					if (TimerUtils.passedTick(delay.getValue())) {
						mc.playerController.attackEntity(mc.player, a);

						double xx = a.getEntityBoundingBox().calculateXOffset(a.getRenderBoundingBox(), mc.player.posX);
						double yy = a.getEntityBoundingBox().calculateYOffset(a.getRenderBoundingBox(), mc.player.posY);
						double zz = a.getEntityBoundingBox().calculateZOffset(a.getRenderBoundingBox(), mc.player.posZ);

						RotationUtils.faceVectorPacket(new Vec3d(xx, yy, zz));

						mc.player.connection.sendPacket(new CPacketUseEntity(a));
					}
				}
			}
		}


		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer) {
				if (mc.player.getDistance(e) < range.getValue()) {
					if (enh.isChecked()) {
						if (((EntityPlayer) e).getHealth() < health.getValue())
							try {
								Field rightClickDelayTimer =
										mc.getClass().getDeclaredField(wurst.isObfuscated()
												? "field_71467_ac" : "rightClickDelayTimer");
								rightClickDelayTimer.setAccessible(true);
								rightClickDelayTimer.setInt(mc, 0);

							} catch (ReflectiveOperationException l) {
								setEnabled(false);
								throw new RuntimeException(l);
							}
					}

					if (enb.isChecked()) {
						if (((EntityPlayer) e).getHealth() < fastBreak.getValue()) {
							try {
								PlayerControllerUtils.setBlockHitDelay(0);
							} catch (ReflectiveOperationException reflectiveOperationException) {
								reflectiveOperationException.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}