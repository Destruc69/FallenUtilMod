package net.wurstclient.forge.clickgui;

import net.minecraft.client.Minecraft;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.ForgeWurst;

public class Backround {

    public void backround(WUpdateEvent event) {
        if (ForgeWurst.getForgeWurst().getHax().clickGuiHack.isEnabled()) {
            if (Minecraft.getMinecraft().currentScreen != null) {
                Minecraft.getMinecraft().currentScreen.drawDefaultBackground();
            }
        }
    }
}