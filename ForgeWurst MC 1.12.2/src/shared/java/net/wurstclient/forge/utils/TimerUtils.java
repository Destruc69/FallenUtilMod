
package net.wurstclient.forge.utils;

public class TimerUtils {
    private static long lastTime;

    // Returns change in time as double
    public static long getDeltaTime() {
        return (System.nanoTime() - lastTime);
    }

    // Updates lastTime
    public static void updateTime() {
        lastTime = System.nanoTime();
    }

    // Public constructor for Timer object
    public TimerUtils() {
        lastTime = System.nanoTime();
    }


    public static boolean hasPassed(long ms) {
        long second = ms * 22;
        if (getDeltaTime() >= second) {
            updateTime();
            return true;
        } else return false;
    }
}