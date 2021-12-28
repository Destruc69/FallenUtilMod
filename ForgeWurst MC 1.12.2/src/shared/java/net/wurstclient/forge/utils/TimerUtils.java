package net.wurstclient.forge.utils;

public class TimerUtils {
    private static long ms;

    public TimerUtils() {
        this.ms = 0;
    }

    public static boolean hasPassed(int ms) {
        return System.currentTimeMillis() - ms >= ms;
    }

    public static void reset() {
        ms = System.currentTimeMillis();
    }
}