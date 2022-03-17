package net.wurstclient.forge.utils;

import net.minecraft.client.Minecraft;

import java.awt.*;

public class NotiUtils {

    public static void onRender(String message) {
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        Minecraft.getMinecraft().fontRenderer.drawString(message, center.x, center.y, 1, false);
        TimerUtils.sleep(1000);
    }
}