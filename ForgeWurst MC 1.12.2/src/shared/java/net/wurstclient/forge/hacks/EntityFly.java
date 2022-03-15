package net.wurstclient.forge.hacks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.TimerUtils;

import java.util.Objects;

public final class EntityFly extends Hack {
	private final SliderSetting speed =
			new SliderSetting("Speed", 0.5, 0.1, 1, 0.1, SliderSetting.ValueDisplay.DECIMAL);

	private final SliderSetting down =
			new SliderSetting("DownSpeed", "Every 50MS we go up and down", 0.5, 0.1, 1, 0.1, SliderSetting.ValueDisplay.DECIMAL);

	private final CheckboxSetting velocity =
			new CheckboxSetting("Velocity",
					false);

	public EntityFly() {
		super("EntityFly", "Fly with Entity's.");
		setCategory(Category.MOVEMENT);
		addSetting(speed);
		addSetting(velocity);
		addSetting(down);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);

		TimerUtils.reset();
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		if (!mc.player.isRiding()) return;

		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			Objects.requireNonNull(mc.player.getRidingEntity()).motionY += speed.getValueF();
		}

		if (mc.gameSettings.keyBindBack.isKeyDown()) {
			Objects.requireNonNull(mc.player.getRidingEntity()).motionY -= speed.getValueF();
		}

		if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0) {
			Objects.requireNonNull(mc.player.getRidingEntity()).setVelocity(0, 0, 0);
		}
	}
}