package net.wurstclient.forge.hacks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.MathUtils;
import net.wurstclient.forge.utils.TimerUtils;

public final class StepHack extends Hack {

	private final CheckboxSetting reverse =
			new CheckboxSetting("ReverseStep",
					false);

	public StepHack() {
		super("Step", "Makes you go up blocks fast.");
		setCategory(Category.MOVEMENT);
		addSetting(reverse);
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
	public void WPacket(WPacketInputEvent event) {
		try {
			if (!reverse.isChecked()) {
				if (mc.player.collidedHorizontally) {
					if (event.getPacket() instanceof CPacketPlayer) {
						event.setCanceled(true);
					}
					mc.player.setPosition(mc.player.posX, mc.player.posY + mc.player.stepHeight, mc.player.posZ);
				}
			} else {
				mc.player.motionY = -10;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
