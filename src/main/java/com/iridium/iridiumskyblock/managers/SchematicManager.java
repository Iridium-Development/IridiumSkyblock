package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.schematics.Schematic;
import com.iridium.iridiumskyblock.schematics.SchematicPaster;
import com.iridium.iridiumskyblock.schematics.WorldEdit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Class which handles schematics and pastes them.
 */
public class SchematicManager {

    private final SchematicPaster schematicPaster;
    public final Map<String, File> schematicFiles;

    private final boolean worldEdit = Bukkit.getPluginManager().isPluginEnabled("WorldEdit");
    private final boolean fawe = Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit") || Bukkit.getPluginManager().isPluginEnabled("AsyncWorldEdit");

    public SchematicManager() {
        File parent = new File(IridiumSkyblock.getInstance().getDataFolder(), "schematics");
        this.schematicPaster = worldEdit || fawe ? new WorldEdit() : new Schematic();
        this.schematicFiles = new HashMap<>();
        for (File file : parent.listFiles()) {
            schematicFiles.put(file.getName(), file);
        }
    }

    /**
     * Pastes the island schematic at the designated island.
     *
     * @param island     The island you want the schematic to be pasted at
     * @param schematics A map of worlds to schematics we need to paste
     * @return A completable future of when its finished pasting
     */
    public CompletableFuture<Void> pasteSchematic(final Island island, Map<World, Schematics.SchematicWorld> schematics) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        for (Map.Entry<World, Schematics.SchematicWorld> schematic : schematics.entrySet()) {
            Location location = island.getCenter(schematic.getKey());
            location.add(0, schematic.getValue().islandHeight, 0);
            File file = schematicFiles.getOrDefault(schematic.getValue().schematicID, schematicFiles.values().stream().findFirst().get());
            if (fawe) {
                Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> schematicPaster.paste(file, location, schematic.getValue().ignoreAirBlocks, completableFuture));
            } else {
                schematicPaster.paste(file, location, schematic.getValue().ignoreAirBlocks, completableFuture);
            }
        }
        return completableFuture;
    }

}
