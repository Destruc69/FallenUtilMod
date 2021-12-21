package net.wurstclient.forge.hacks;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.RotationUtils;
import org.lwjgl.opengl.GL11;

public final class Face extends Hack {

	private final SliderSetting ValueX =
			new SliderSetting("X", 1, 0.05, 800, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting ValueY =
			new SliderSetting("Y", 1, 0.05, 800, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting Color =
			new SliderSetting("Colors", 1, 1.0, 4.0, 1.0, SliderSetting.ValueDisplay.DECIMAL);


	public Face() {

		super("Welcomer", "Shows a welcome message.");
		setCategory(Category.HUD);
		addSetting(ValueX);
		addSetting(ValueY);
		addSetting(Color);


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
	public void onRenderGUI(RenderGameOverlayEvent.Post event) {

		int textColor;

		textColor = 0xffffff;

		if (Color.getValueI() == 1)
			textColor = 0x52ff52;

		if (Color.getValueI() == 2)
			textColor = 0xff0a0a;

		if (Color.getValueI() == 3)
			textColor = 0x31c0ff;

		if (Color.getValueI() == 4)
			textColor = 0xffffff;


		GL11.glPushMatrix();
		GL11.glScaled(1.33333333, 1.33333333, 1);
		WMinecraft.getFontRenderer().drawStringWithShadow(
				"Face Vector:" + " " + face(), ValueX.getValueF(), ValueY.getValueF(), textColor);
		GL11.glPopMatrix();
	}

	public static Vec3d face() {
		return RotationUtils.getClientLookVec();
	}
}