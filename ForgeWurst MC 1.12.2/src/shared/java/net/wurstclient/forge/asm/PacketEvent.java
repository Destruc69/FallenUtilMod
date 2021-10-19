/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.asm;

import net.minecraft.network.Packet;


public final class PacketEvent {

	private Packet packet;

	public PacketEvent(Packet packet) {
		this.packet = packet;

	}

	public Packet getPacket() {
		return packet;

	}
}

