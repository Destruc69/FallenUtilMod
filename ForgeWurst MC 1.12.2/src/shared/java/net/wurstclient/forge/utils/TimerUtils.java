package net.wurstclient.forge.utils;

import net.minecraft.client.Minecraft;

public class TimerUtils {

    //Created by Paul (FallenUtilityMod)

    private static float tickLength;

    public static boolean passedTick(double tick) {

        if (tickLength == tick) {
            tick = tick + tick;
        }

        tickLength = Minecraft.getMinecraft().getTickLength();
        if (tickLength == tick) {
            return true;
        } else {
            return false;
        }
    }
}