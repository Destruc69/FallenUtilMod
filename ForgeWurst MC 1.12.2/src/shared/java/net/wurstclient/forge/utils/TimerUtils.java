package net.wurstclient.forge.utils;

public final class TimerUtils
{
    private static long time;

    public TimerUtils()
    {
        time = -1;
    }

    public static boolean passed(double ms)
    {
        return System.currentTimeMillis() - time >= ms;
    }

    public static void reset()
    {
        time = System.currentTimeMillis();
    }

    public static void resetTimeSkipTo(long p_MS)
    {
        time = System.currentTimeMillis() + p_MS;
    }

    public static long getTime()
    {
        return time;
    }

    public static void setTime(long time)
    {
        time = time;
    }
}