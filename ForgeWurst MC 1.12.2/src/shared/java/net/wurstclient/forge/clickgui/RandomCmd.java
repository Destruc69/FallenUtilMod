/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.clickgui;

import net.wurstclient.forge.Command;
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.utils.ChatUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.nio.file.Path;
import java.util.Random;

public final class RandomCmd extends Command {
    public RandomCmd() {
        super("random", "Random number roller.",
                "Syntax: .random <min> <max>");
    }

    @Override
    public void call(String[] args) throws CmdException {

        Random r = new Random();

        int randomNumber = r.nextInt(Integer.parseInt(args[1]) + 1 - Integer.parseInt(args[0]) + Integer.parseInt(args[0]));

        int answer = Integer.parseInt(String.valueOf(randomNumber));

        int min = Integer.parseInt(args[0]);

        int max = Integer.parseInt(args[1]);

        ChatUtils.message(String.valueOf(answer));
    }
}