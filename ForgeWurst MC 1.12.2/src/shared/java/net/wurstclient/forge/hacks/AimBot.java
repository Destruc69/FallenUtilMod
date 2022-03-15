package net.wurstclient.forge.hacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.utils.NotiUtils;

public final class AimBot extends Hack
{
	public AimBot()
	{
		super("AimBot", "Look at entitys with packets.");
		setCategory(Category.COMBAT);
	}
	
	@Override
	protected void onEnable()
	{
		MinecraftForge.EVENT_BUS.register(this);
		NotiUtils.onRender(getClass() + " " + "Enabled");
	}
	
	@Override
	protected void onDisable()
	{
		MinecraftForge.EVENT_BUS.unregister(this);
		NotiUtils.onRender(getClass() + " " + "Disabled");
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		for (Entity e : mc.world.loadedEntityList) {
			if (mc.player.getDistance(e) < 5) {
				lookAtPacket(e.getPosition().getX(), e.getPosition().getY(), e.getPosition().getZ(), mc.player);
			}
		}
	}

	//POSTMAN UTILS
	//POSTMAN UTILS
	//POSTMAN UTILS
	//POSTMAN UTILS
	//POSTMAN UTILS

	public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
		double dirx = me.posX - px;
		double diry = me.posY - py;
		double dirz = me.posZ - pz;

		double len = Math.sqrt(dirx*dirx + diry*diry + dirz*dirz);

		dirx /= len;
		diry /= len;
		dirz /= len;

		double pitch = Math.asin(diry);
		double yaw = Math.atan2(dirz, dirx);

		pitch = pitch * 180.0d / Math.PI;
		yaw = yaw * 180.0d / Math.PI;

		yaw += 90f;

		return new double[]{yaw,pitch};
	}

	private static void setYawAndPitch(float yaw1, float pitch1) {
		mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw1, pitch1, mc.player.onGround));
	}

	private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
		double[] v = calculateLookAt(px, py, pz, me);
		setYawAndPitch((float) v[0], (float) v[1]);
	}
}


