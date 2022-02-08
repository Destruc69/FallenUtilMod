/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.*;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.CrystalUtil;
import net.wurstclient.forge.utils.RotationUtils;
import net.wurstclient.forge.utils.TimerUtils;

import java.util.ArrayList;

public final class AutoCrystalReWrite extends Hack {

	//Keeping it simple till i learn more about CA


	//Important Settings
	private final SliderSetting range =
			new SliderSetting("Range", "Range for witch we will break or place the crystals", 4, 1.0, 6, 0.5, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting breakk =
			new SliderSetting("BreakDelay", "Higher the value, slower the BreakDelay", 1500, 100, 5000, 100, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting placee =
			new SliderSetting("PlaceDelay", "Higher the value, slower the PlaceDelay", 2000, 100, 5000, 100, SliderSetting.ValueDisplay.DECIMAL);

	//SafePlacements
	private final CheckboxSetting safe =
			new CheckboxSetting("SafePlacement [ENABLE]", "Enable Safe Placements, [Prevent placing crystals that could kill you]",
					false);

	private final SliderSetting safe1 =
			new SliderSetting("SafePlacement [VALUE]", "Max damage a crystal can hurt you", 4, 1.0, 8, 0.5, SliderSetting.ValueDisplay.DECIMAL);

	//HealthCap
	private final CheckboxSetting health =
			new CheckboxSetting("HealthCap [ENABLE]", "Enable HealthCap, [If your health is lower than the value we will return]",
					false);

	private final SliderSetting health1 =
			new SliderSetting("HealthCap [VALUE]", "If your health is lower than the value we will return", 4, 1.0, 8, 0.5, SliderSetting.ValueDisplay.DECIMAL);

	boolean selfpause;

	public AutoCrystalReWrite() {
		super("AutoCrystal", "Killaura but for Crystals.");
		setCategory(Category.COMBAT);
		addSetting(range);
		addSetting(breakk);
		addSetting(placee);
		addSetting(safe);
		addSetting(safe1);
		addSetting(health);
		addSetting(health1);
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
		if (breakk.getValueF() == placee.getValueF()) {
			if (TimerUtils.hasPassed(5000000)) {
				ChatUtils.error("The Break and Place delay can not be the same value, ! change it ASAP !");
			}
		}

		//EntityPlayer
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer) {
				if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {
					//We dont want to kill ourselves
					if (e != mc.player) {

						ArrayList<Entity> attackEntityList = new ArrayList<Entity>();

						for (Entity s : mc.world.loadedEntityList) {
							if (s instanceof EntityPlayer && s != mc.player) {
								attackEntityList.add(s);
							}
						}

						Entity minEntity = null;

						for (Entity f : attackEntityList) {
							assert mc.player != null;
							if (mc.world.getClosestPlayer(mc.player.posX, mc.player.posY, mc.player.posZ, range.getValue(), false).getDistance(e) < range.getValueF()) {
								minEntity = f;
							}
						}


						//Getting closest player, we can assume there the biggest threat
						if (mc.world.getClosestPlayer(mc.player.posX, mc.player.posY, mc.player.posZ, range.getValue(), false).getDistance(e) < range.getValueF()) {
							for (int i = -1; i <= 1; i++) {
								for (int j = -1; j <= 1; j++) {
									//Placing Logic
									if (TimerUtils.hasPassed(breakk.getValueI())) {
										if (mc.world.getBlockState(e.getPosition().add(i, 0, j)).getBlock()
												.equals(Blocks.AIR)
												&& (mc.world.getBlockState(e.getPosition().add(i, -1, j)).getBlock()
												.equals(Blocks.OBSIDIAN)
												|| mc.world.getBlockState(e.getPosition().add(i, -1, j)).getBlock()
												.equals(Blocks.BEDROCK))) {

											double damage = CrystalUtil.calculateDamage(e.getPosition().add(i, -1, j), mc.player);

											//If crystal damage is going to be greater than this value we stop
											if (safe.isChecked() && damage > safe1.getValueF())
												return;

											//If health is below this value we will stop
											if (health.isChecked() && mc.player.getHealth() < health1.getValueF())
												return;

											//Place the Crystal
											mc.playerController.processRightClickBlock(mc.player, mc.world,
													minEntity.getPosition().add(i, -1, j), EnumFacing.UP, mc.objectMouseOver.hitVec,
													EnumHand.MAIN_HAND);

											RotationUtils.faceVectorPacket(new Vec3d(e.getPosition().add(i, -1, j)));
											RotationUtils.faceVectorPacket(new Vec3d(e.getPosition().add(i, -1, j)));
											RotationUtils.faceVectorPacket(new Vec3d(e.getPosition().add(i, -1, j)));

											RotationUtils.updateServerRotation();

											mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
											mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
											mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));

											mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(e.getPosition().add(i, -1, j), EnumFacing.UP, EnumHand.MAIN_HAND, i, -1, j));
											mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(e.getPosition().add(i, -1, j), EnumFacing.UP, EnumHand.MAIN_HAND, i, -1, j));
											mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(e.getPosition().add(i, -1, j), EnumFacing.UP, EnumHand.MAIN_HAND, i, -1, j));
										}
									}
								}
							}
						}
					}
				}
			}
		}

		//EntityEndCrystal
		for (Entity a : mc.world.loadedEntityList) {
			if (a instanceof EntityEnderCrystal) {
				if (mc.player.getDistance(a) < range.getValueF()) {

					ArrayList<Entity> crystal = new ArrayList<Entity>();

					Entity crys = null;

					for (Entity j : mc.world.loadedEntityList) {
						if (j instanceof EntityEnderCrystal) {
							crystal.add(j);
						}
					}

					for (Entity m : crystal) {
						assert mc.player != null;
						if (mc.world.getClosestPlayer(mc.player.posX, mc.player.posY, mc.player.posZ, range.getValue(), false).getDistance(m) < range.getValueF()) {
							crys = m;
						}
					}


					if (TimerUtils.hasPassed(placee.getValueI())) {
						mc.playerController.attackEntity(mc.player, a);

						mc.player.connection.sendPacket(new CPacketUseEntity());
						mc.player.connection.sendPacket(new CPacketUseEntity());
						mc.player.connection.sendPacket(new CPacketUseEntity());

						double x = a.getEntityBoundingBox().calculateXOffset(a.getEntityBoundingBox(), mc.player.posX);
						double y = a.getEntityBoundingBox().calculateYOffset(a.getEntityBoundingBox(), mc.player.posY);
						double z = a.getEntityBoundingBox().calculateZOffset(a.getEntityBoundingBox(), mc.player.posZ);

						RotationUtils.updateServerRotation();

						RotationUtils.faceVectorPacket(new Vec3d(x, y, z));
						RotationUtils.faceVectorPacket(new Vec3d(x, y, z));
						RotationUtils.faceVectorPacket(new Vec3d(x, y, z));
					}
				}
			}
		}
	}
}