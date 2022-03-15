package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.MoverType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
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

	EntityOtherPlayerMP camera;
	EntityFakePlayer player;

	public FreecamHack() {
		super("Freecam", "Allows you to move the camera\n"
				+ "without moving your character.");
		setCategory(Category.RENDER);
		addSetting(speed);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);

		if (mc.player == null || mc.world == null) {
			return;
		}

		mc.renderChunksMany = false;

		camera = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
		player = new EntityFakePlayer();

		camera.copyLocationAndAnglesFrom(mc.player);
		camera.prevRotationYaw = mc.player.rotationYaw;
		camera.rotationYawHead = mc.player.rotationYawHead;
		camera.inventory.copyInventory(mc.player.inventory);
		mc.world.addEntityToWorld(-100, camera);
		mc.setRenderViewEntity(camera);

	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		MinecraftForge.EVENT_BUS.unregister(this);
		mc.renderChunksMany = true;

		player.despawn();
		mc.world.removeEntity(player);

		if (mc.player != null && mc.world != null && mc.getRenderViewEntity() != null) {
			mc.player.moveStrafing = 0;
			mc.player.moveForward = 0;
			mc.world.removeEntity(camera);
			mc.setRenderViewEntity(mc.player);
		}
	}

	@SubscribeEvent
	public void update(LivingEvent.LivingUpdateEvent e) {
		if (!e.getEntity().equals(camera) || mc.currentScreen != null) {
			return;
		}

		if (camera == null) {
			return;
		}

		//Update motion
		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			camera.motionY = speed.getValueF();
		} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			camera.motionY = -speed.getValueF();
		} else {
			camera.motionY = 0;
		}

		if (mc.gameSettings.keyBindForward.isKeyDown()) {
			camera.moveForward = 1;
		} else if (mc.gameSettings.keyBindBack.isKeyDown()) {
			camera.moveForward = -1;
		} else {
			camera.moveForward = 0;
		}

		if (mc.gameSettings.keyBindLeft.isKeyDown()) {
			camera.moveStrafing = -1;
		} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
			camera.moveStrafing = 1;
		} else {
			camera.moveStrafing = 0;
		}

		if (camera.moveStrafing != 0 || camera.moveForward != 0) {
			double yawRad = Math.toRadians(camera.rotationYaw - getRotationFromVec(new Vec3d(camera.moveStrafing, 0.0, camera.moveForward))[0]);

			camera.motionX = -Math.sin(yawRad) * speed.getValueF();
			camera.motionZ = Math.cos(yawRad) * speed.getValueF();

			if (mc.gameSettings.keyBindSprint.isKeyDown()) {
				camera.setSprinting(true);
				camera.motionX *= 1.5;
				camera.motionZ *= 1.5;
			} else {
				camera.setSprinting(false);
			}
		} else {
			camera.motionX = 0;
			camera.motionZ = 0;
		}

		player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);

		camera.inventory.copyInventory(mc.player.inventory);
		camera.noClip = true;
		camera.rotationYawHead = camera.rotationYaw;

		camera.rotationYaw = mc.player.rotationYaw;
		camera.rotationPitch = mc.player.rotationPitch;

		camera.move(MoverType.SELF, camera.motionX, camera.motionY, camera.motionZ);
	}

	public static double[] getRotationFromVec(Vec3d vec) {
		double xz = Math.sqrt(vec.x * vec.x + vec.z * vec.z);
		double yaw = normalizeAngle(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
		double pitch = normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, xz)));
		return new double[]{yaw, pitch};
	}

	public static double normalizeAngle(double angle) {
		angle %= 360.0;

		if (angle >= 180.0) {
			angle -= 360.0;
		}

		if (angle < -180.0) {
			angle += 360.0;
		}

		return angle;
	}

	public void packet(WPacketInputEvent event) {
		try {
			if (event.getPacket() instanceof CPacketUseEntity) {
				CPacketUseEntity entity = (CPacketUseEntity) event.getPacket();

				if (entity.getEntityFromWorld(mc.world).equals(mc.player)) {
					event.setCanceled(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}