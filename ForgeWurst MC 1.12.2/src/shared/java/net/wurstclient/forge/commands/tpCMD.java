/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.commands;

import net.minecraft.client.entity.EntityPlayerSP;
import net.wurstclient.forge.Command;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.utils.MathUtils;

public final class tpCMD extends Command
{
    public tpCMD()
    {
        super("tp", "Lets you teleport to a com.",
                "Syntax: .tp <x> <y> <z>");
    }

    @Override
    public void call(String[] args) throws CmdException
    {

        if(args.length != 2)
            throw new CmdSyntaxError();

        mc.player.setPosition(mc.player.posX = Integer.parseInt(args[0]), mc.player.posY = Integer.parseInt(args[1]), mc.player.posZ = Integer.parseInt(args[2]));
    }
}