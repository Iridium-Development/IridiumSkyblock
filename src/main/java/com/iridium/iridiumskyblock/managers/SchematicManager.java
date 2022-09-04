package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.schematics.Schematic;
import com.iridium.iridiumskyblock.schematics.SchematicPaster;
import com.iridium.iridiumskyblock.schematics.WorldEdit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SchematicManager {

    public final SchematicPaster schematicPaster;
    public final Map<String, File> schematicFiles;

    private final boolean worldEdit = Bukkit.getPluginManager().isPluginEnabled("WorldEdit");

    public SchematicManager() {
        File parent = new File(IridiumSkyblock.getInstance().getDataFolder(), "schematics");
        SchematicPaster schematicPaster = worldEdit ? new WorldEdit() : new Schematic();

        if ((worldEdit) && !WorldEdit.isWorking()) {
            IridiumSkyblock.getInstance().getLogger().warning("WorldEdit version doesn't support minecraft version, falling back to default integration");
            schematicPaster = new Schematic();
        }

        this.schematicPaster = schematicPaster;
        this.schematicFiles = new HashMap<>();
        for (File file : parent.listFiles()) {
            schematicFiles.put(file.getName(), file);
        }
    }

    public CompletableFuture<Void> pasteSchematic(Island island, Schematics.SchematicConfig schematic) {
        return CompletableFuture.runAsync(() -> {
            if (IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(World.Environment.NORMAL, true)) {
                pasteSchematic(island, schematic.overworld, IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.NORMAL)).join();
            }
            if (IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(World.Environment.NETHER, true)) {
                pasteSchematic(island, schematic.nether, IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.NETHER)).join();
            }
            if (IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(World.Environment.THE_END, true)) {
                pasteSchematic(island, schematic.end, IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.THE_END)).join();
            }
        });
    }

    private CompletableFuture<Void> pasteSchematic(Island island, Schematics.SchematicWorld schematic, World world) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Location location = island.getCenter(world);
        location.add(0, schematic.islandHeight, 0);
        File file = schematicFiles.getOrDefault(schematic.schematicID, schematicFiles.values().stream().findFirst().orElse(null));
        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
            if (file == null) {
                location.getBlock().setType(Material.BEDROCK);
                IridiumSkyblock.getInstance().getLogger().warning("Could not find schematic " + schematic.schematicID);
            } else {
                schematicPaster.paste(file, location, schematic.ignoreAirBlocks, completableFuture);
            }
        });
        return completableFuture;
    }


}
