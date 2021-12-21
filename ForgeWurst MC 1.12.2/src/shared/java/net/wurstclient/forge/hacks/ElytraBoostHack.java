/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.KeyBindingUtils;
import net.wurstclient.forge.utils.PlayerUtils;

import java.lang.reflect.Field;

public final class ElytraBoostHack extends Hack {
	private final SliderSetting speed =
			new SliderSetting("BaseSpeed", 0.05, 0.05, 1, 0.25, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting down =
			new SliderSetting("DownSpeed", 0.05, 0.05, 1, 0.25, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting up =
			new SliderSetting("UpSpeed", 0.05, 0.05, 1, 0.25, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting timer1 =
			new CheckboxSetting("Timer TakeOff",
					false);

	private final CheckboxSetting still =
			new CheckboxSetting("Velocity",
					true);

	private final CheckboxSetting auto =
			new CheckboxSetting("AutoPilot",
					false);

	private final SliderSetting height =
			new SliderSetting("AutoPilot-Height", 200, 1.0, 256, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	public ElytraBoostHack() {
		super("ElytraBoost", "Elytra fly without slowing down.");
		setCategory(Category.MOVEMENT);
		addSetting(speed);
		addSetting(up);
		addSetting(down);
		addSetting(still);
		addSetting(auto);
		addSetting(timer1);
		addSetting(height);
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

		if (mc.player.isElytraFlying()) {

			if (timer1.isChecked() && mc.player.isElytraFlying() && mc.player.fallDistance < 5) {
				setTickLength(50f / 0.7f);
			} else
				setTickLength(50f / 1.0f);

			float yaw = Minecraft.getMinecraft().player.rotationYaw;
			float pitch = Minecraft.getMinecraft().player.rotationPitch;
			if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
				Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed.getValue();
				Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed.getValue();
			}
			if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown())
				Minecraft.getMinecraft().player.motionY += up.getValue();
			if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown())
				Minecraft.getMinecraft().player.motionY -= down.getValue();

			if (still.isChecked()) {
				if (!Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown() && !Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && !Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
					mc.player.setVelocity(0, 0, 0);
				}
			}

			if (auto.isChecked()) {
				if (mc.player.posY < height.getValue()) {
					KeyBindingUtils.setPressed(mc.gameSettings.keyBindForward, true);
					KeyBindingUtils.setPressed(mc.gameSettings.keyBindJump, true);
					KeyBindingUtils.setPressed(mc.gameSettings.keyBindSneak, false);
				} else if (mc.player.posY > height.getValue()) {
					KeyBindingUtils.setPressed(mc.gameSettings.keyBindForward, true);
					KeyBindingUtils.setPressed(mc.gameSettings.keyBindJump, false);
					KeyBindingUtils.setPressed(mc.gameSettings.keyBindSneak, true);
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

