package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.TimerUtils;

public final class PacketFly extends Hack {
	private final SliderSetting baseSpeed = new SliderSetting("BaseSpeed", "How much we teleport for going Forward, left tight and back", 0.5D, 0.1D, 1.0D, 0.01D, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting upSpeed = new SliderSetting("DownSpeed", "How much we teleport for going down", 0.5D, 0.1D, 1.0D, 0.01D, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting downSpeed = new SliderSetting("TeleportAmount", "How much we teleport up", 0.5D, 0.1D, 1.0D, 0.01D, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting antikickms = new SliderSetting("AntiKickMS", "Every MS we move down", 500, 100, 1000, 100, SliderSetting.ValueDisplay.DECIMAL);

	public PacketFly() {
		super("PacketFly", "Fly around with packets.");
		setCategory(Category.MOVEMENT);
		addSetting(baseSpeed);
		addSetting(upSpeed);
		addSetting(downSpeed);
	}

	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void WPlayerMove(WUpdateEvent event) {
		mc.player.setVelocity(0, 0, 0);

		if (mc.player.ticksExisted < 20) {
			return;
		}

		if (!TimerUtils.hasReached(antikickms.getValueI())) {
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.player.motionY = upSpeed.getValueF();
			} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.motionY = -downSpeed.getValueF();
			}
		} else {
			TimerUtils.reset();
		}

		mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY + mc.player.motionY, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));

		double y = mc.player.posY + mc.player.motionY;
		y += downSpeed.getValue();
		mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, y, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));
	}

	@SubscribeEvent
	public void packet(WPacketOutputEvent event) {
		if (event.getPacket() instanceof SPacketPlayerPosLook && mc.currentScreen == null) {
			SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
			mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
			mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
		}
	}
}