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
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.ChatUtils;

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

	private final CheckboxSetting song3 =
			new CheckboxSetting("Temple OS Remix",
					false);

	private final CheckboxSetting song4 =
			new CheckboxSetting("Mr.BlueSky",
					false);

	private final CheckboxSetting song5 =
			new CheckboxSetting("The Hitman",
					false);

	private final CheckboxSetting song6 =
			new CheckboxSetting("The Power",
					false);

	private final CheckboxSetting song7 =
			new CheckboxSetting("Never Gonna Give You Up",
					false);

	private final CheckboxSetting song8 =
			new CheckboxSetting("Cant Touch This",
					false);

	public MusicBox() {
		super("MusicBox", "Plays built in music!.");
		setCategory(Category.MISC);
		addSetting(song1);
		addSetting(song2);
		addSetting(song3);
		addSetting(song4);
		addSetting(song5);
		addSetting(song6);
		addSetting(song7);
		addSetting(song8);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);

		ForgeWurst.getForgeWurst().getHax().musicBox.setEnabled(false);

		ChatUtils.error("MusicBox is temporarily disabled, Sorry about this!");
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

		if (song3.isChecked())
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("song3.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				song3.setChecked(false);

			} catch (Exception ex) {
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
			}

		if (song4.isChecked())
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("song4.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				song4.setChecked(false);

			} catch (Exception ex) {
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
			}

		if (song5.isChecked())
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("song5.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				song5.setChecked(false);

			} catch (Exception ex) {
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
			}

		if (song6.isChecked())
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("song6.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				song6.setChecked(false);

			} catch (Exception ex) {
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
			}

		if (song7.isChecked())
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("song7.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				song7.setChecked(false);

			} catch (Exception ex) {
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
			}

		if (song8.isChecked())
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("song8.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				song8.setChecked(false);

			} catch (Exception ex) {
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
			}
	}
}