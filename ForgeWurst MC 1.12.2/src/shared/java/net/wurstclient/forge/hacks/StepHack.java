package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.ChatUtils;

public final class StepHack extends Hack {

	public StepHack() {
		super("Step", "Makes you go up blocks fast.");
		setCategory(Category.MOVEMENT);
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
	public void WUpdateEvent(WUpdateEvent event) {
		try {
			if (mc.player.collidedHorizontally) {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 1, mc.player.posZ);
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1, mc.player.posZ, mc.player.onGround));
			} else if (mc.player.onGround && !mc.player.isAirBorne) {
				mc.player.motionY -= 5;
				mc.player.connection.sendPacket(new CPacketPlayer(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
