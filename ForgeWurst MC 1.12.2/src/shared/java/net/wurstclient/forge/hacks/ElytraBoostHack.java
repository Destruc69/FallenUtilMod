/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;

import java.lang.reflect.Field;

public final class ElytraBoostHack extends Hack {
	private final SliderSetting speed =
			new SliderSetting("BaseSpeed", "Base Speed for Elytra", 0.05, 0.05, 1, 0.025, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting down =
			new SliderSetting("DownSpeed", "Down Speed for Elytra", 0.05, 0.05, 1, 0.025, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting up =
			new SliderSetting("UpSpeed", "Up Speed for Elytra", 0.05, 0.05, 1, 0.025, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting time =
			new CheckboxSetting("TimerTakeOff", "Makes getting off the ground easier",
					false);

	private final CheckboxSetting hold =
			new CheckboxSetting("Velocity", "If idle we will hold you in the air",
					false);

	private final CheckboxSetting auto =
			new CheckboxSetting("AutoPilot", "Fly automatically",
					false);

	private final SliderSetting height =
			new SliderSetting("AutoPilot [HEIGHT]", "Y Level for what we will maintain", 220, 20., 360, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	public ElytraBoostHack() {
		super("ElytraBoost", "Elytra fly without slowing down.");
		setCategory(Category.MOVEMENT);
		addSetting(speed);
		addSetting(up);
		addSetting(down);
		addSetting(time);
		addSetting(auto);
		addSetting(height);
		addSetting(hold);
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

			if (auto.isChecked()) {
				if (mc.player.posY > height.getValue()) {
					KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
					KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
					KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
				}

				if (mc.player.posY < height.getValue()) {
					KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
					KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
					KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), true);
				}
			}

			if (hold.isChecked() && !mc.gameSettings.keyBindForward.isKeyDown() &&
					!mc.gameSettings.keyBindSneak.isKeyDown() &&
					!mc.gameSettings.keyBindJump.isKeyDown() &&
					!mc.gameSettings.keyBindLeft.isKeyDown() &&
					!mc.gameSettings.keyBindRight.isKeyDown()) {

				mc.player.motionX = 0;
				mc.player.motionY = 0;
				mc.player.motionZ = 0;
				mc.player.setVelocity(0, 0, 0);
			}

			float yaw = Minecraft.getMinecraft().player.rotationYaw;
			float pitch = Minecraft.getMinecraft().player.rotationPitch;
			if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
				Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed.getValue();
				Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed.getValue();
			}

			if (Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown()) {
				mc.player.rotationYaw += 5.5;
			}

			if (Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown()) {
				mc.player.rotationYaw -= 5.5;
			}

			if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown()) {
				mc.player.motionY += up.getValueF();
			}

			if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.motionY -= down.getValueF();
			}

			if (time.isChecked() && mc.player.fallDistance < 5) {
				setTickLength(50 / 0.5f);
			} else {
				setTickLength(50);
			}
		}
	}

	private void setTickLength(float tickLength) {
		try {
			Field fTimer = mc.getClass().getDeclaredField(
					wurst.isObfuscated() ? "field_71428_T" : "timer");
			fTimer.setAccessible(true);

			if (WMinecraft.VERSION.equals("1.10.2")) {
				Field fTimerSpeed = Timer.class.getDeclaredField(
						wurst.isObfuscated() ? "field_74278_d" : "timerSpeed");
				fTimerSpeed.setAccessible(true);
				fTimerSpeed.setFloat(fTimer.get(mc), 50 / tickLength);

			} else {
				Field fTickLength = Timer.class.getDeclaredField(
						wurst.isObfuscated() ? "field_194149_e" : "tickLength");
				fTickLength.setAccessible(true);
				fTickLength.setFloat(fTimer.get(mc), tickLength);
			}

		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
}