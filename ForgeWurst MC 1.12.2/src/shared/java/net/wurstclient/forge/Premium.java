package net.wurstclient.forge;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.utils.ChatUtils;

public class Premium {

    @SubscribeEvent
    public void premium(WUpdateEvent event) {

        //Old before on github, keep it on to help Fallen grow

        if (!ForgeWurst.getForgeWurst().getHax().discord.isEnabled()) {
            ForgeWurst.getForgeWurst().getHax().discord.setEnabled(true);
            ChatUtils.error("DiscordRPC Can only be disabled in the DEV Version, Sorry about this!, We will make sure to hide IP's for your safety");
        }
    }
}
