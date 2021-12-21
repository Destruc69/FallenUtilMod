package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.KeyBindingUtils;

public final class SpiderHack extends Hack {

	private final CheckboxSetting ncp =
			new CheckboxSetting("Bypass",
					false);

	public SpiderHack() {
		super("Spider", "Allows you to climb up walls like a spider.");
		setCategory(Category.MOVEMENT);
		addSetting(ncp);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		KeyBindingUtils.setPressed(mc.gameSettings.keyBindForward, false);
		KeyBindingUtils.setPressed(mc.gameSettings.keyBindJump, false);
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		if (!ncp.isChecked()) {
			EntityPlayerSP player = event.getPlayer();
			if (!WEntity.isCollidedHorizontally(player))
				return;

			if (player.motionY < 0.2)
				player.motionY = 0.2;
		} else if (ncp.isChecked()) {
			EntityPlayerSP player = event.getPlayer();
			if (WEntity.isCollidedHorizontally(player)) {
				mc.player.jump();
				mc.player.connection.sendPacket(new CPacketPlayer(true));
			}
		}
	}
}