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
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
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

	private final SliderSetting range =
			new SliderSetting("Range", "Range for witch we will break or place the crystals", 4, 1.0, 6, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting delay =
			new SliderSetting("Delay [MS]", "Delay for witch we will break and place the crystals", 400, 100, 600, 50, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting packet =
			new CheckboxSetting("PacketSupport", "Sends packets that may make placing and breaking faster",
					false);

	private final SliderSetting pause =
			new SliderSetting("HealthPause", "If your health is below this value we wont place", 4, 1.0, 10, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting check =
			new CheckboxSetting("SafeChecks", "If you cant see the placement pos, we wont place or break",
					false);

	private final CheckboxSetting anti =
			new CheckboxSetting("AntiSuicide", "Prevents placing a crystal that could kill you",
					false);

	private final SliderSetting anti1 =
			new SliderSetting("AntiSuicide [CAP]", "If the crystal were going to place deals more damage, we will pause", 4, 1.0, 10, 1.0, SliderSetting.ValueDisplay.DECIMAL);


	public AutoCrystalReWrite() {
		super("AutoCrystal", "Killaura but for Crystals.");
		setCategory(Category.COMBAT);
		addSetting(range);
		addSetting(delay);
		addSetting(packet);
		addSetting(pause);
		addSetting(check);
		addSetting(anti);
		addSetting(anti1);
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
				if (mc.player.getDistance(e) < range.getValueF()) {
					if (mc.player.getHealth() > pause.getValueF()) {
						if (TimerUtils.passedMs(delay.getValueI())) {
							if (e != mc.player) {
								if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {
									for (int i = -1; i <= 1; i++) {
										for (int j = -1; j <= 1; j++) {

											double selfdamage = CrystalUtil.calculateDamage(e.getPosition().add(i, -1, j), mc.player);

											if (check.isChecked()) {
												if (mc.player.canEntityBeSeen(e)) {
													break;
												} else {
													continue;
												}
											}

											if (anti.isChecked()) {
												if (selfdamage > anti1.getValueF()) {
													break;
												} else {
													continue;
												}
											}

											if (mc.world.getBlockState(e.getPosition().add(i, 0, j)).getBlock()
													.equals(Blocks.AIR)
													&& (mc.world.getBlockState(e.getPosition().add(i, -1, j)).getBlock()
													.equals(Blocks.OBSIDIAN)
													|| mc.world.getBlockState(e.getPosition().add(i, -1, j)).getBlock()
													.equals(Blocks.BEDROCK))) {
												mc.playerController.processRightClickBlock(mc.player, mc.world,
														e.getPosition().add(i, -1, j), EnumFacing.UP, mc.objectMouseOver.hitVec,
														EnumHand.MAIN_HAND);

												if (packet.isChecked()) {
													mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(e.getPosition().add(i, -1, j), EnumFacing.UP, EnumHand.MAIN_HAND, i, -1, j));
												}

												RotationUtils.faceVectorPacket(new Vec3d(e.getPosition().add(i, -1, j)));
											}
										}
									}
								}
							}
						}
					}
				}
			}

			//EntityEnderCrystal
			for (Entity a : mc.world.loadedEntityList) {
				if (a instanceof EntityEnderCrystal) {
					if (mc.player.getDistance(a) < range.getValueF()) {

						if (check.isChecked()) {
							if (mc.player.canEntityBeSeen(a)) {
								break;
							} else {
								continue;
							}
						}

						if (TimerUtils.passedMs(delay.getValueI())) {
							mc.playerController.attackEntity(mc.player, a);
						}

						double x = a.getEntityBoundingBox().calculateXOffset(a.getEntityBoundingBox(), mc.player.posX);
						double y = a.getEntityBoundingBox().calculateYOffset(a.getEntityBoundingBox(), mc.player.posY);
						double z = a.getEntityBoundingBox().calculateZOffset(a.getEntityBoundingBox(), mc.player.posZ);

						RotationUtils.faceVectorPacket(new Vec3d(x, y, z));
					}
				}
			}
		}
	}
}