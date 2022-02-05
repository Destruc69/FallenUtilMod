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
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
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
import net.wurstclient.forge.utils.RotationUtils;
import net.wurstclient.forge.utils.TimerUtils;

public final class AutoCrystalReWrite extends Hack {

	//Keeping it simple till i learn more about CA


	//Important Settings
	private final SliderSetting range =
			new SliderSetting("Range", "Range for witch we will break or place the crystals", 4, 1.0, 6, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting speed =
			new SliderSetting("Speed", "Higher the value, slower the CA", 2000, 500, 5000, 100, SliderSetting.ValueDisplay.DECIMAL);

	//SafePlacements
	private final CheckboxSetting safe =
			new CheckboxSetting("SafePlacement [ENABLE]", "Enable Safe Placements, [Prevent placing crystals that could kill you]",
					false);

	private final SliderSetting safe1 =
			new SliderSetting("SafePlacement [VALUE]", "Max damage a crystal can hurt you", 4, 1.0, 8, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	//HealthCap
	private final CheckboxSetting health =
			new CheckboxSetting("HealthCap [ENABLE]", "Enable HealthCap, [If your health is lower than the value we will return]",
					false);

	private final SliderSetting health1 =
			new SliderSetting("HealthCap [VALUE]", "If your health is lower than the value we will return", 4, 1.0, 8, 1.0, SliderSetting.ValueDisplay.DECIMAL);


	public AutoCrystalReWrite() {
		super("AutoCrystal", "Killaura but for Crystals.");
		setCategory(Category.COMBAT);
		addSetting(range);
		addSetting(speed);
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
		//EntityPlayer
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer) {
				if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {
					//We dont want to kill ourselves
					if (e != mc.player) {
						//Getting closest player, we can assume there the biggest threat
						if (mc.world.getClosestPlayer(mc.player.posX, mc.player.posY, mc.player.posZ, range.getValue(), false).getDistance(e) < range.getValueF()) {
							for (int i = -1; i <= 1; i++) {
								for (int j = -1; j <= 1; j++) {
									if (mc.world.getBlockState(e.getPosition().add(i, 0, j)).getBlock()
											.equals(Blocks.AIR)
											&& (mc.world.getBlockState(e.getPosition().add(i, -1, j)).getBlock()
											.equals(Blocks.OBSIDIAN)
											|| mc.world.getBlockState(e.getPosition().add(i, -1, j)).getBlock()
											.equals(Blocks.BEDROCK))) {

										double damage = CrystalUtil.calculateDamage(e.getPosition().add(i, -1, j), mc.player);

										if (safe.isChecked() && damage > safe1.getValueF())
											return;

										if (health.isChecked() && mc.player.getHealth() < health1.getValueF())
											return;

										//Placing Logic
										if (TimerUtils.hasPassed(speed.getValueI())) {

											if (safe.isChecked() && damage > safe1.getValueF())
												return;

											if (health.isChecked() && mc.player.getHealth() < health1.getValueF())
												return;

											mc.playerController.processRightClickBlock(mc.player, mc.world,
													e.getPosition().add(i, -1, j), EnumFacing.UP, mc.objectMouseOver.hitVec,
													EnumHand.MAIN_HAND);

											//Rotation Calculations
											double dd = RotationUtils.getEyesPos().squareDistanceTo(i, -1, j);
											double posXX = e.lastTickPosX + (e.lastTickPosX - e.lastTickPosX) * dd
													- mc.player.posX;
											double posYY = e.lastTickPosY + (e.lastTickPosY - e.lastTickPosY) * dd
													+ e.getEyeHeight() * 0.5 - mc.player.posY
													- mc.player.getEyeHeight();
											double posZZ = e.lastTickPosZ + (e.lastTickPosZ - e.lastTickPosZ) * dd
													- mc.player.posZ;

											//Twice the packets to support placing thorough Blocks
											RotationUtils.faceVectorPacket(new Vec3d(posXX, posYY, posZZ));
											RotationUtils.faceVectorPacket(new Vec3d(posXX, posYY, posZZ));

											//Spoof EnumMainHand
											mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
											mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));

											//Main packets for Placing
											mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(e.getPosition().add(i, -1, j), EnumFacing.UP, EnumHand.MAIN_HAND, (float) posXX, (float) posYY, (float) posZZ));
											mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(e.getPosition().add(i, -1, j), EnumFacing.UP, EnumHand.MAIN_HAND, (float) posXX, (float) posYY, (float) posZZ));

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

					//Rotation Calculations
					double dd = RotationUtils.getEyesPos().distanceTo(
							a.getEntityBoundingBox().getCenter());
					double posXX = a.lastTickPosX + (a.lastTickPosX - a.lastTickPosX) * dd
							- mc.player.posX;
					double posYY = a.lastTickPosY + (a.lastTickPosY - a.lastTickPosY) * dd
							+ a.getEyeHeight() * 0.5 - mc.player.posY
							- mc.player.getEyeHeight();
					double posZZ = a.lastTickPosZ + (a.lastTickPosZ - a.lastTickPosZ) * dd
							- mc.player.posZ;

					RotationUtils.faceVectorPacket(new Vec3d(posXX, posYY, posZZ));
					RotationUtils.faceVectorPacket(new Vec3d(posXX, posYY, posZZ));

					//It's not necessary to send two times the packets if we can see the crystal
					if (TimerUtils.hasPassed(speed.getValueI())) {
						mc.playerController.attackEntity(mc.player, a);
						mc.player.connection.sendPacket(new CPacketUseEntity(a));
						mc.player.connection.sendPacket(new CPacketUseEntity(a));
					}
				}
			}
		}
	}
}