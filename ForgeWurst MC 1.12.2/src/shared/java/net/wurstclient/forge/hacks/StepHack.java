package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.ChatUtils;

public final class StepHack extends Hack {

	private final SliderSetting Height =
			new SliderSetting("StepHeight", 1, 1.0, 80, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	public StepHack() {
		super("Step", "Makes you go up blocks fast.");
		setCategory(Category.MOVEMENT);
		addSetting(Height);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
		mc.player.stepHeight = 0.75f;
	}

	@SubscribeEvent
    public void WUpdateEvent (WUpdateEvent event) {

	    mc.player.stepHeight = Height.getValueF();
		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY+0.75, mc.player.posZ, mc.player.onGround));

	}
}