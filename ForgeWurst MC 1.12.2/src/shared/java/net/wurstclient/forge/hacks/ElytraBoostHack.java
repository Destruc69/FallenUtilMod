/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.MathUtils;

import java.lang.reflect.Field;

public final class ElytraBoostHack extends Hack {

	private final SliderSetting upSpeed =
			new SliderSetting("UpSpeed", "Speed for going Up", 0.5, 0.1, 2, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting baseSpeed =
			new SliderSetting("BaseSpeed", "Base speed for going forward, ect", 0.5, 0.1, 8, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting downSpeed =
			new SliderSetting("DownSpeed", "Speed for going Down", 0.5, 0.1, 2, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting velocity =
			new CheckboxSetting("Velocity", "Be still in air if keys are idle",
					false);

	public ElytraBoostHack() {
		super("ElytraFly", "Elytra fly without slowing down.");
		setCategory(Category.MOVEMENT);
		addSetting(upSpeed);
		addSetting(baseSpeed);
		addSetting(downSpeed);
		addSetting(velocity);
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
		if (mc.player.isElytraFlying()) {

			EntityPlayerSP player = event.getPlayer();

			try
			{
				Field inWater = Entity.class.getDeclaredField(wurst.isObfuscated() ? "field_70171_ac" : "inWater");
				inWater.setAccessible(true);
				inWater.setBoolean(player, true);

			}catch(ReflectiveOperationException e)
			{
				setEnabled(false);
				throw new RuntimeException(e);
			}

			mc.player.limbSwing = 0;
			mc.player.limbSwingAmount = 0;
			mc.player.prevLimbSwingAmount = 0;

			if(mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.player.motionY = +upSpeed.getValueF();
			}

			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.motionY = -downSpeed.getValueF();
			}

			if (mc.player.moveStrafing != 0 || mc.player.moveForward != 0) {

				if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
					if (velocity.isChecked()) {
						mc.player.motionY = 0;
					}
				}

				double[] speed = MathUtils.directionSpeed(baseSpeed.getValueF());

				mc.player.motionX = speed[0];
				mc.player.motionZ = speed[1];
			}

			if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0 && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown() && velocity.isChecked()) {
				mc.player.motionX = 0.0;
				mc.player.motionY = 0.0;
				mc.player.motionZ = 0.0;
				mc.player.setVelocity(0.0, 0.0, 0.0);
			}
		}
	}
}