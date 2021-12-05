package net.wurstclient.forge;

import net.minecraft.client.Minecraft;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.utils.ChatUtils;

public class Premium {

    public void premium(WUpdateEvent event) {

        if (!ForgeWurst.getForgeWurst().getHax().discord.isEnabled() && !Minecraft.getMinecraft().player.getName().contains("12Master") && !Minecraft.getMinecraft().player.getName().contains("Dockson")) {
            ForgeWurst.getForgeWurst().getHax().discord.setEnabled(true);
            ChatUtils.error("DiscordRPC Can only be disabled in the DEV Version, Sorry about this!, We will make sure to hide IP's for your safety");
        }
    }
}
