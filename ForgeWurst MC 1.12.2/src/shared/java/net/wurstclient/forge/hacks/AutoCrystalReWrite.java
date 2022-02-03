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
import net.minecraft.util.Timer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.RotationUtils;
import net.wurstclient.forge.utils.TimerUtils;

import java.lang.reflect.Field;

public final class AutoCrystalReWrite extends Hack {

	//Keeping it simple till i learn more about CA

	private final SliderSetting range =
			new SliderSetting("Range", "Range for witch we will break or place the crystals", 4, 1.0, 6, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting speed =
			new SliderSetting("Speed", "Higher the value, slower the CA", 20000, 5000, 50000, 1000, SliderSetting.ValueDisplay.DECIMAL);

	public AutoCrystalReWrite() {
		super("AutoCrystal", "Killaura but for Crystals.");
		setCategory(Category.COMBAT);
		addSetting(range);
		addSetting(speed);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		setTickLength(50);
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {

		if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0) {
			setTickLength(50 / 0.9f);
		}

		//EntityPlayer
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer) {
				if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {
					if (e != mc.player) {
						if (mc.world.getClosestPlayer(mc.player.posX, mc.player.posY, mc.player.posZ, range.getValue(), false).getDistance(e) < range.getValueF()) {
							for (int i = -1; i <= 1; i++) {
								for (int j = -1; j <= 1; j++) {
									if (TimerUtils.hasPassed(speed.getValueI())) {
										if (mc.world.getBlockState(e.getPosition().add(i, 0, j)).getBlock()
												.equals(Blocks.AIR)
												&& (mc.world.getBlockState(e.getPosition().add(i, -1, j)).getBlock()
												.equals(Blocks.OBSIDIAN)
												|| mc.world.getBlockState(e.getPosition().add(i, -1, j)).getBlock()
												.equals(Blocks.BEDROCK))) {
											mc.playerController.processRightClickBlock(mc.player, mc.world,
													e.getPosition().add(i, -1, j), EnumFacing.UP, mc.objectMouseOver.hitVec,
													EnumHand.MAIN_HAND);

											RotationUtils.faceVectorPacket(new Vec3d(i, -1, j));

											mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
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

					double x = a.getEntityBoundingBox().calculateXOffset(a.getEntityBoundingBox(), mc.player.posX);
					double y = a.getEntityBoundingBox().calculateYOffset(a.getEntityBoundingBox(), mc.player.posY);
					double z = a.getEntityBoundingBox().calculateZOffset(a.getEntityBoundingBox(), mc.player.posZ);

					RotationUtils.faceVectorPacket(new Vec3d(x, y, z));

					if (TimerUtils.hasPassed(speed.getValueI())) {
						mc.playerController.attackEntity(mc.player, a);
						mc.player.connection.sendPacket(new CPacketUseEntity(a));
					}
				}
			}
		}
	}

	private void setTickLength(float tickLength)
	{
		try
		{
			Field fTimer = mc.getClass().getDeclaredField(
					wurst.isObfuscated() ? "field_71428_T" : "timer");
			fTimer.setAccessible(true);

			if(WMinecraft.VERSION.equals("1.10.2"))
			{
				Field fTimerSpeed = Timer.class.getDeclaredField(
						wurst.isObfuscated() ? "field_74278_d" : "timerSpeed");
				fTimerSpeed.setAccessible(true);
				fTimerSpeed.setFloat(fTimer.get(mc), 50 / tickLength);

			}else
			{
				Field fTickLength = Timer.class.getDeclaredField(
						wurst.isObfuscated() ? "field_194149_e" : "tickLength");
				fTickLength.setAccessible(true);
				fTickLength.setFloat(fTimer.get(mc), tickLength);
			}

		}catch(ReflectiveOperationException e)
		{
			throw new RuntimeException(e);
		}
	}
}


