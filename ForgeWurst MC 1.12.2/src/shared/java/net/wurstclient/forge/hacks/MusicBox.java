/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import javazoom.jl.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.FileInputStream;

public final class MusicBox extends Hack {

	private final CheckboxSetting song1 =
			new CheckboxSetting("2B2T Im A Griefer",
					false);

	private final CheckboxSetting song2 =
			new CheckboxSetting("NormalFag Song",
					false);

	public MusicBox() {
		super("MusicBox", "Plays built in music!.");
		setCategory(Category.MISC);
		addSetting(song1);
		addSetting(song2);
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

		if (song1.isChecked())
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("song1.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				song1.setChecked(false);

			} catch (Exception ex) {
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
			}

		if (song2.isChecked())
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("song2.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				song2.setChecked(false);

			} catch (Exception ex) {
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
			}
	}
}