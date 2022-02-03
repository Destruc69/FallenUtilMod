package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
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

	private EntityFakePlayer fakePlayer;

	public FreecamHack() {
		super("Freecam", "Allows you to move the camera\n"
				+ "without moving your character.");
		setCategory(Category.RENDER);
		addSetting(speed);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
		fakePlayer = new EntityFakePlayer();
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		fakePlayer.despawn();
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		mc.player.jumpMovementFactor = speed.getValueF();
		mc.player.capabilities.isFlying = true;

		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.setPosition(mc.player.posX, mc.player.posY - speed.getValueF(), mc.player.posZ);
		}

		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.setPosition(mc.player.posX, mc.player.posY + speed.getValueF(), mc.player.posZ);
		}
	}

	@SubscribeEvent
	public void onPacketInput(WPacketInputEvent event) {
		Packet<?> get = event.getPacket();

		if (get instanceof CPacketPlayer || get instanceof CPacketPlayer.Position || get instanceof CPacketPlayer.Rotation) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void clip(WSetOpaqueCubeEvent event) {
		event.setCanceled(true);
	}

	@SubscribeEvent
	public void clip1(WIsNormalCubeEvent event) {
		event.setCanceled(true);
	}
}
