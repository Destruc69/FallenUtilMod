package net.wurstclient.forge.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.wurstclient.forge.ForgeWurst;

public class Backround {

    public void backround() {
        if (ForgeWurst.getForgeWurst().getHax().clickGuiHack.isEnabled()) {
            GlStateManager.enableLighting();
            GlStateManager.enableFog();
        }
    }
}
