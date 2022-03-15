/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import javafx.scene.effect.Effect;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.ChatUtils;

public final class Animations extends Hack {

	private final CheckboxSetting hurt =
			new CheckboxSetting("HurtAnimation",
					false);

	private final CheckboxSetting speed =
			new CheckboxSetting("SpeedAnimation",
					false);

	private final CheckboxSetting setDead =
			new CheckboxSetting("SetDead", "Make the player dead client sided",
					false);

	private final CheckboxSetting dead =
			new CheckboxSetting("DeathCancel", "You will not perform death client sided",
					false);

	private final CheckboxSetting drop =
			new CheckboxSetting("DropCancel", "You will not perform the drop items client sided on death",
					false);

	public Animations()
	{
		super("Animations", "Plays/Shows animations.");
		setCategory(Category.MISC);
		addSetting(hurt);
		addSetting(speed);
		addSetting(setDead);
		addSetting(dead);
		addSetting(drop);
	}
	
	@Override
	protected void onEnable()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	protected void onDisable()
	{
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {

		if (dead.isChecked() && setDead.isChecked()) {
			dead.setChecked(false);
			setDead.setChecked(false);
			ChatUtils.error("You cant set the player dead and cancel the death at the same time!");
		}

		if (hurt.isChecked()) {
			mc.player.performHurtAnimation();
		}

		if (dead.isChecked() && !setDead.isChecked()) {
			mc.player.isDead = false;
		}

		if (setDead.isChecked() && !dead.isChecked()) {
			mc.player.isDead = true;
		}

		if (drop.isChecked()) {
			mc.player.setDropItemsWhenDead(false);
		}

		if (speed.isChecked()) {
			mc.player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 5210, 5210));
		}
	}
}
