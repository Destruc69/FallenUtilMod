package net.wurstclient.forge.hacks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.ChatUtils;

public final class Stash extends Hack {

	private final CheckboxSetting chest =
			new CheckboxSetting("Chests",
					false);

	private final CheckboxSetting portal =
			new CheckboxSetting("Portals",
					false);

	private final CheckboxSetting anti =
			new CheckboxSetting("AntiSpam",
					false);

	public Stash() {
		super("StashFinder", "Notifies when stashes are found.");
		setCategory(Category.WORLD);
		addSetting(chest);
		addSetting(portal);
		addSetting(anti);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		ForgeWurst.getForgeWurst().getHax().antiSpamHack.setEnabled(false);
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		if (chest.isChecked()) {
			for (TileEntity e : mc.world.loadedTileEntityList) {
				if (e instanceof TileEntityChest) {
					ChatUtils.message("Chest Found at:" + " " + e.getPos());

					if (anti.isChecked()) {
						ForgeWurst.getForgeWurst().getHax().antiSpamHack.setEnabled(true);
					}
				}
			}


			for (TileEntity a : mc.world.loadedTileEntityList) {
				if (portal.isChecked()) {
					if (a instanceof TileEntityEndPortal) {
						ChatUtils.message("EndPortal Found at:" + " " + a.getPos());

						if (anti.isChecked()) {
							ForgeWurst.getForgeWurst().getHax().antiSpamHack.setEnabled(true);
						}
					}
				}
			}
		}
	}
}