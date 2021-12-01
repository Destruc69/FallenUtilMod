package net.wurstclient.forge.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.KeyBindingUtils;

public final class HighwayNavigator extends Hack {

    public HighwayNavigator() {
        super("HighwayNav", "Navigate through highways.");
        setCategory(Category.NAV);
    }

    @Override
    protected void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onUpdate(WUpdateEvent event) {
        BlockPos blockPos = Minecraft.getMinecraft().player.getPosition();

        if (Minecraft.getMinecraft().world.getBlockState(blockPos.down()).getBlock() == Blocks.OBSIDIAN) {
            KeyBindingUtils.setPressed(mc.gameSettings.keyBindForward, true);
        } else
            KeyBindingUtils.setPressed(mc.gameSettings.keyBindForward, false);
    }
}