package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.schematics.FastAsyncWorldEdit;
import com.iridium.iridiumskyblock.schematics.SchematicAsync;
import com.iridium.iridiumskyblock.schematics.SchematicPaster;
import com.iridium.iridiumskyblock.schematics.WorldEdit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public class SchematicManager {

    public SchematicPaster schematicPaster;
    public final Map<String, File> schematicFiles;
    public final TreeMap<String, SchematicPaster> availablePasters;

    private final boolean worldEdit = Bukkit.getPluginManager().isPluginEnabled("WorldEdit");
    private final boolean fawe = Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit")
            || Bukkit.getPluginManager().isPluginEnabled("AsyncWorldEdit");

    public SchematicManager() {

        availablePasters = new TreeMap<>();
        availablePasters.put("internalAsync", new SchematicAsync());
        if ((worldEdit) && WorldEdit.isWorking())
            availablePasters.put("worldedit", new WorldEdit());
        if ((fawe) && FastAsyncWorldEdit.isWorking())
            availablePasters.put("fawe", new FastAsyncWorldEdit());

        if ((worldEdit) && !WorldEdit.isWorking()) {
            IridiumSkyblock.getInstance().getLogger()
                    .warning("WorldEdit version doesn't support minecraft version, disabling WorldEdit integration");
        }
        if ((fawe) && !FastAsyncWorldEdit.isWorking()) {
            IridiumSkyblock.getInstance().getLogger().warning(
                    "FAWE version does not implement API correctly, did you miss an update? Disabling FAWE integration");
        }

        setPasterFromConfig();

        this.schematicFiles = new HashMap<>();
        File parent = new File(IridiumSkyblock.getInstance().getDataFolder(), "schematics");
        for (File file : parent.listFiles()) {
            schematicFiles.put(file.getName(), file);
        }
    }

    public void reload() {
        loadCache();
        setPasterFromConfig();
        schematicPaster.clearCache();
    }

    private void setPasterFromConfig() {
        String paster = IridiumSkyblock.getInstance().getConfiguration().paster;
        if (availablePasters.containsKey(paster))
            this.schematicPaster = availablePasters.get(paster);
        else {
            IridiumSkyblock.getInstance().getLogger().warning("Configuration error, selected paster [" + paster + "] is not available, available choices are " + availablePasters.keySet());
            this.schematicPaster = new SchematicAsync();
        }
    }

    public void loadCache() {
        schematicFiles.clear();
        File parent = new File(IridiumSkyblock.getInstance().getDataFolder(), "schematics");
        for (File file : parent.listFiles()) {
            schematicFiles.put(file.getName(), file);
        }
    }

    public CompletableFuture<Void> pasteSchematic(Island island, Schematics.SchematicConfig schematic) {
        return CompletableFuture.runAsync(() -> {
            if (IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(World.Environment.NORMAL,
                    true)) {
                pasteSchematic(island, schematic.overworld,
                        IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.NORMAL)).join();
            }
            if (IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(World.Environment.NETHER,
                    true)) {
                pasteSchematic(island, schematic.nether,
                        IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.NETHER)).join();
            }
            if (IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(World.Environment.THE_END,
                    true)) {
                pasteSchematic(island, schematic.end,
                        IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.THE_END)).join();
            }
        });
    }

    private CompletableFuture<Void> pasteSchematic(Island island, Schematics.SchematicWorld schematic, World world) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Location location = island.getCenter(world);
        location.add(0, schematic.islandHeight, 0);
        File file = schematicFiles.getOrDefault(schematic.schematicID,
                schematicFiles.values().stream().findFirst().orElse(null));
        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
            if (file == null) {
                location.getBlock().setType(Material.BEDROCK);
                IridiumSkyblock.getInstance().getLogger().warning("Could not find schematic " + schematic.schematicID);
            } else {
                if (fawe) {
                    Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(),
                            () -> schematicPaster.paste(file, location, schematic.ignoreAirBlocks, completableFuture));
                } else {
                    schematicPaster.paste(file, location, schematic.ignoreAirBlocks, completableFuture);
                }
            }
        });
        return completableFuture;
    }

}
