package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.*;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.Hack.DontSaveState;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;
import net.wurstclient.forge.utils.EntityFakePlayer;
import net.wurstclient.forge.utils.KeyBindingUtils;

@DontSaveState
public final class FreecamHack extends Hack {
	private final SliderSetting speed =
			new SliderSetting("Speed", 1, 0.05, 10, 0.05, ValueDisplay.DECIMAL);

	EntityOtherPlayerMP clonedPlayer;

	public FreecamHack() {
		super("Freecam", "Allows you to move the camera\n"
				+ "without moving your character.");
		setCategory(Category.RENDER);
		addSetting(speed);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);

		clonedPlayer = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
		clonedPlayer.copyLocationAndAnglesFrom(mc.player);
		clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
		mc.world.addEntityToWorld(-100, clonedPlayer);
		mc.player.capabilities.isFlying = true;
		mc.player.capabilities.setFlySpeed((float) (speed.getValue() / 100f));
		mc.player.noClip = true;
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		mc.player.setPosition(clonedPlayer.posX, clonedPlayer.posY, clonedPlayer.posZ);
		mc.world.removeEntityFromWorld(-100);
		clonedPlayer = null;
		mc.player.capabilities.isFlying = false;
		mc.player.capabilities.setFlySpeed(0.05f);
		mc.player.noClip = false;
		mc.player.motionX = mc.player.motionY = mc.player.motionZ = 0.f;
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		mc.player.capabilities.isFlying = true;
		mc.player.capabilities.setFlySpeed((float) (speed.getValue() / 100f));
		mc.player.noClip = true;
		mc.player.onGround = false;
		mc.player.fallDistance = 0;
	}

	@SubscribeEvent
	public void packet(WPacketInputEvent event) {
		if (event.getPacket() instanceof CPacketInput) {
			event.setCanceled(true);
		}
	}
}