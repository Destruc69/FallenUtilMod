package net.wurstclient.forge.hacks;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;

public final class Welcomer extends Hack {

	private final SliderSetting ValueX =
			new SliderSetting("X", 1, 0.05, 800, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting ValueY =
			new SliderSetting("Y", 1, 0.05, 800, 0.05, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting Color =
			new SliderSetting("Colors", 1, 1.0, 4.0, 1.0, SliderSetting.ValueDisplay.DECIMAL);


	public Welcomer() {

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

		GL11.glColor3f(1, 1, 1);

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
				"Welcome" + " " + Name(), ValueX.getValueF(), ValueY.getValueF(), textColor);
		GL11.glPopMatrix();
	}

	public static String Name() {
		return Minecraft.getMinecraft().player.getName();
	}
}