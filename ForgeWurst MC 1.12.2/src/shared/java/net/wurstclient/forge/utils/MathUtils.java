/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class MathUtils
{
	public static boolean isInteger(String s)
	{
		try
		{
			Integer.parseInt(s);
			return true;
			
		}catch(NumberFormatException e)
		{
			return false;
		}
	}
	
	public static boolean isDouble(String s)
	{
		try
		{
			Double.parseDouble(s);
			return true;
			
		}catch(NumberFormatException e)
		{
			return false;
		}
	}
	
	public static int floor(float value)
	{
		int i = (int)value;
		return value < i ? i - 1 : i;
	}
	
	public static int floor(double value)
	{
		int i = (int)value;
		return value < i ? i - 1 : i;
	}
	
	public static int clamp(int num, int min, int max)
	{
		return num < min ? min : num > max ? max : num;
	}
	
	public static float clamp(float num, float min, float max)
	{
		return num < min ? min : num > max ? max : num;
	}
	
	public static double clamp(double num, double min, double max)
	{
		return num < min ? min : num > max ? max : num;
	}

	public static float[] calcAngle(final Vec3d from, final Vec3d to) {
		final double difX = to.x - from.x;
		final double difY = (to.y - from.y) * -1.0;
		final double difZ = to.z - from.z;
		final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
		return new float[] { (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0),
				(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
	}

}
