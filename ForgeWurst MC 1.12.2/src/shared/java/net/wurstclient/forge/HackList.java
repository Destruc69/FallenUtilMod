/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.item.Item;
import net.wurstclient.forge.compatibility.WHackList;
import net.wurstclient.forge.hacks.*;
import net.wurstclient.forge.settings.Setting;
import net.wurstclient.forge.utils.JsonUtils;

public final class HackList extends WHackList
{
	public final AntiSpamHack antiSpamHack = register(new AntiSpamHack());
	public final BoatFly boatFly = register(new BoatFly());
	public final EntitySpeed entitySpeed = register(new EntitySpeed());
	public final StatSpoof statSpoof = register(new StatSpoof());
	public final PortalGod portalGod = register(new PortalGod());
	public final MoreInv moreInv = register(new MoreInv());
	public final SlowFly slowFly = register(new SlowFly());
	public final NoCom noCom = register(new NoCom());
	public final ItemModel itemModel = register(new ItemModel());
	public final Welcomer welcomer = register(new Welcomer());
	public final FPS fps = register(new FPS());
	public final CrashTest crashTest = register(new CrashTest());
	public final AntiVoid antiVoid = register(new AntiVoid());
	public final Teleport teleport = register(new Teleport());
	public final Scaffold scaffold = register(new Scaffold());
	public final MusicBox musicBox = register(new MusicBox());
	public final Discord discord = register(new Discord());
	public final Stash stash = register(new Stash());
	public final HighwayNavigator highwayNavigator = register(new HighwayNavigator());
	/**
	 * ClickGUI Backround Effects
	 * Fix AutoTotem
	 * Improve Stash/StashFinder
	 * Make Scaffold bypass NCP
	 * More Render Modules for PvP/CPvP
	 * Make ElytraFlight better than it is
	 * FIX JLAYER/MUSICBOX
	 * More General Modules
	 */
	public final AutoCrystal autoCrystal = register(new AutoCrystal());
	public final AntiAFK antiAFK = register(new AntiAFK());
	public final Anchor anchor = register(new Anchor());
	public final AutoTotem autoTotem = register(new AutoTotem());
	public final YawLock yawLock = register(new YawLock());
	public final PacketFly packetFly = register(new PacketFly());
	public final AirJump airJump = register(new AirJump());
	public final Online online = register(new Online());
	public final Coords coords = register(new Coords());
	public final AntiHunger antiHunger = register(new AntiHunger());
	public final NoSlowdown noSlowdown = register(new NoSlowdown());
	public final AntiKick antiKick = register(new AntiKick());
	public final AntiSwing antiSwing = register(new AntiSwing());
	public final NoSound noSound = register(new NoSound());
	public final Criticals criticals = register(new Criticals());
	public final InfiniteEffects infiniteEffects = register(new InfiniteEffects());
	public final PacketCanceler packetCanceler = register(new PacketCanceler());
	public final NoKnockback noKnockback = register(new NoKnockback());
	public final SpamDropHack spamDropHack = register(new SpamDropHack());
	public final AutoBreakHack autoBreakHack = register(new AutoBreakHack());
	public final AutoPlaceHack autoPlaceHack = register(new AutoPlaceHack());
	public final VannilaCrashHack vannilaCrashHack = register(new VannilaCrashHack());
	public final HighJumpHack highJumpHack = register(new HighJumpHack());
	public final AutoJumpHack autojumpHack = register(new AutoJumpHack());
	public final AutoSneakHack autosneakHack = register(new AutoSneakHack());
	public final AutoArmorHack autoArmorHack = register(new AutoArmorHack());
	public final AutoFarmHack autoFarmHack = register(new AutoFarmHack());
	public final AutoFishHack autoFishHack = register(new AutoFishHack());
	public final AutoSprintHack autoSprintHack = register(new AutoSprintHack());
	public final AutoSwimHack autoSwimHack = register(new AutoSwimHack());
	public final AutoToolHack autoToolHack = register(new AutoToolHack());
	public final AutoWalkHack autoWalkHack = register(new AutoWalkHack());
	public final BlinkHack blinkHack = register(new BlinkHack());
	public final Strafe strafe = register(new Strafe());
	public final ChestEspHack chestEspHack = register(new ChestEspHack());
	public final ClickGuiHack clickGuiHack = register(new ClickGuiHack());
	public final FastBreakHack fastBreakHack = register(new FastBreakHack());
	public final FastLadderHack fastLadderHack = register(new FastLadderHack());
	public final FastPlaceHack fastPlaceHack = register(new FastPlaceHack());
	public final StepHack stepHack = register(new StepHack());
	public final ElytraBoostHack elytraboosthack = register(new ElytraBoostHack());
	public final ParkourHack parkourhack = register(new ParkourHack());
	public final FlightHack flightHack = register(new FlightHack());
	public final FreecamHack freecamHack = register(new FreecamHack());
	public final FullbrightHack fullbrightHack = register(new FullbrightHack());
	public final GlideHack glideHack = register(new GlideHack());
	public final ItemEspHack itemEspHack = register(new ItemEspHack());
	public final JesusHack jesusHack = register(new JesusHack());
	public final KillauraHack killauraHack = register(new KillauraHack());
	public final MobEspHack mobEspHack = register(new MobEspHack());
	public final MobSpawnEspHack mobSpawnEspHack =
			register(new MobSpawnEspHack());
	public final NoFallHack noFallHack = register(new NoFallHack());
	public final NoHurtcamHack noHurtcamHack = register(new NoHurtcamHack());
	public final NoWebHack noWebHack = register(new NoWebHack());
	public final NukerHack nukerHack = register(new NukerHack());
	public final PlayerEspHack playerEspHack = register(new PlayerEspHack());
	public final RadarHack radarHack = register(new RadarHack());
	public final RainbowUiHack rainbowUiHack = register(new RainbowUiHack());
	public final SpiderHack spiderHack = register(new SpiderHack());
	public final TimerHack timerHack = register(new TimerHack());
	public final XRayHack xRayHack = register(new XRayHack());
	
	private final Path enabledHacksFile;
	private final Path settingsFile;
	private boolean disableSaving;
	
	public HackList(Path enabledHacksFile, Path settingsFile)
	{
		this.enabledHacksFile = enabledHacksFile;
		this.settingsFile = settingsFile;
	}
	
	public void loadEnabledHacks()
	{
		JsonArray json;
		try(BufferedReader reader = Files.newBufferedReader(enabledHacksFile))
		{
			json = JsonUtils.jsonParser.parse(reader).getAsJsonArray();
			
		}catch(NoSuchFileException e)
		{
			saveEnabledHacks();
			return;
			
		}catch(Exception e)
		{
			System.out
				.println("Failed to load " + enabledHacksFile.getFileName());
			e.printStackTrace();
			
			saveEnabledHacks();
			return;
		}
		
		disableSaving = true;
		for(JsonElement e : json)
		{
			if(!e.isJsonPrimitive() || !e.getAsJsonPrimitive().isString())
				continue;
			
			Hack hack = get(e.getAsString());
			if(hack == null || !hack.isStateSaved())
				continue;
			
			hack.setEnabled(true);
		}
		disableSaving = false;
		
		saveEnabledHacks();
	}
	
	public void saveEnabledHacks()
	{
		if(disableSaving)
			return;
		
		JsonArray enabledHacks = new JsonArray();
		for(Hack hack : getRegistry())
			if(hack.isEnabled() && hack.isStateSaved())
				enabledHacks.add(new JsonPrimitive(hack.getName()));
			
		try(BufferedWriter writer = Files.newBufferedWriter(enabledHacksFile))
		{
			JsonUtils.prettyGson.toJson(enabledHacks, writer);
			
		}catch(IOException e)
		{
			System.out
				.println("Failed to save " + enabledHacksFile.getFileName());
			e.printStackTrace();
		}
	}
	
	public void loadSettings()
	{
		JsonObject json;
		try(BufferedReader reader = Files.newBufferedReader(settingsFile))
		{
			json = JsonUtils.jsonParser.parse(reader).getAsJsonObject();
			
		}catch(NoSuchFileException e)
		{
			saveSettings();
			return;
			
		}catch(Exception e)
		{
			System.out.println("Failed to load " + settingsFile.getFileName());
			e.printStackTrace();
			
			saveSettings();
			return;
		}
		
		disableSaving = true;
		for(Entry<String, JsonElement> e : json.entrySet())
		{
			if(!e.getValue().isJsonObject())
				continue;
			
			Hack hack = get(e.getKey());
			if(hack == null)
				continue;
			
			Map<String, Setting> settings = hack.getSettings();
			for(Entry<String, JsonElement> e2 : e.getValue().getAsJsonObject()
				.entrySet())
			{
				String key = e2.getKey().toLowerCase();
				if(!settings.containsKey(key))
					continue;
				
				settings.get(key).fromJson(e2.getValue());
			}
		}
		disableSaving = false;
		
		saveSettings();
	}
	
	public void saveSettings()
	{
		if(disableSaving)
			return;
		
		JsonObject json = new JsonObject();
		for(Hack hack : getRegistry())
		{
			if(hack.getSettings().isEmpty())
				continue;
			
			JsonObject settings = new JsonObject();
			for(Setting setting : hack.getSettings().values())
				settings.add(setting.getName(), setting.toJson());
			
			json.add(hack.getName(), settings);
		}
		
		try(BufferedWriter writer = Files.newBufferedWriter(settingsFile))
		{
			JsonUtils.prettyGson.toJson(json, writer);
			
		}catch(IOException e)
		{
			System.out.println("Failed to save " + settingsFile.getFileName());
			e.printStackTrace();
		}
	}
}
