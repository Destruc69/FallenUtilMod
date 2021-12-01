package net.wurstclient.forge.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.ChatUtils;

public final class HighwayNavigator extends Hack {

    private final CheckboxSetting debug =
            new CheckboxSetting("Debug Mode",
                    false);

    public HighwayNavigator() {
        super("HighwayNav", "Navigate through highways.");
        setCategory(Category.NAV);
        addSetting(debug);
    }

    @Override
    protected void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);

        ChatUtils.warning("Enjoy the ride!, I do not take any action for the fatal outcomes, Please be aware of the road, Also this is very new to me so this is definitely in BETA");
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onUpdate(WUpdateEvent event) {
        BlockPos blockPos = Minecraft.getMinecraft().player.getPosition();

        if (Minecraft.getMinecraft().world.getBlockState(blockPos.down(1)).getBlock() == Blocks.OBSIDIAN &&
                Minecraft.getMinecraft().world.getBlockState(blockPos.east(2)).getBlock() == Blocks.OBSIDIAN &&
                Minecraft.getMinecraft().world.getBlockState(blockPos.west(2)).getBlock() == Blocks.OBSIDIAN &&
                Minecraft.getMinecraft().world.getBlockState(blockPos.north(2)).getBlock() == Blocks.OBSIDIAN &&
                Minecraft.getMinecraft().world.getBlockState(blockPos.south(2)).getBlock() == Blocks.OBSIDIAN) {

            ChatUtils.message("Path found! Moving Forward");
            Minecraft.getMinecraft().player.moveForward = 1f;

        } else {
            Minecraft.getMinecraft().player.setVelocity(0, 0, 0);
            ChatUtils.warning("Holding the breaks! Finding new path");
        }
    }
}