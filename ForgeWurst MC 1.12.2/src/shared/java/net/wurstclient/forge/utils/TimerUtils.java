
package net.wurstclient.forge.utils;

public class TimerUtils {
    private static long time = -1L;

    public static boolean passedS(double s) {
        return passedMs((long)s * 1000L);
    }

    public static boolean passedDms(double dms) {
        return passedMs((long)dms * 10L);
    }

    public static boolean passedDs(double ds) {
        return passedMs((long)ds * 100L);
    }

    public static boolean passedMs(long ms) {
        return passedNS(convertToNS(ms));
    }

    public static void setMs(long ms) {
        time = System.nanoTime() - convertToNS(ms);
    }

    public static boolean passedNS(long ns) {
        return System.nanoTime() - time >= ns;
    }

    public static long getPassedTimeMs() {
        return getMs(System.nanoTime() - time);
    }

    public TimerUtils reset() {
        time = System.nanoTime();
        return this;
    }

    public static long getMs(long time) {
        return time / 1000000L;
    }

    public static long convertToNS(long time) {
        return time * 1000000L;
    }
}

