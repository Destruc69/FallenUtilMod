package net.wurstclient.forge.hacks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.SliderSetting;

public final class Anchor extends Hack {

	private final SliderSetting speed =
			new SliderSetting("Speed", 4, 3.0, 8, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting fall =
			new SliderSetting("Fall Distance", 1, 3.0, 8, 1.0, SliderSetting.ValueDisplay.DECIMAL);

	public Anchor() {
		super("Anchor", "Fast fall when your 2 blocks over a hole.");
		setCategory(Category.COMBAT);
		addSetting(fall);
		addSetting(speed);
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
	public void onUpdate(WUpdateEvent event) {
		if (event.getPlayer().fallDistance > fall.getValue())
			mc.player.motionY -= speed.getValue();
	}
}