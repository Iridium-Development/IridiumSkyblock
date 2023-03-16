package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.schematics.Schematic;
import com.iridium.iridiumskyblock.schematics.SchematicAsync;
import com.iridium.iridiumskyblock.schematics.SchematicPaster;
import com.iridium.iridiumskyblock.schematics.WorldEdit;
import com.iridium.iridiumskyblock.schematics.FastAsyncWorldEdit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

/**
 * Class which handles schematics and pastes them.
 */
public class SchematicManager {

    public SchematicPaster schematicPaster;
    public final Map<String, File> schematicFiles;
    public final TreeMap<String, SchematicPaster> availablePasters;

    private final boolean worldEdit = Bukkit.getPluginManager().isPluginEnabled("WorldEdit");
    private final boolean fawe = Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit") || Bukkit.getPluginManager().isPluginEnabled("AsyncWorldEdit");

    public SchematicManager() {
        
        availablePasters = new TreeMap<>();
        availablePasters.put("internal", new Schematic());
        availablePasters.put("internalAsync", new SchematicAsync());
        if ((worldEdit) && WorldEdit.isWorking())
            availablePasters.put("worldedit", new WorldEdit());
        if ((fawe) && FastAsyncWorldEdit.isWorking())
            availablePasters.put("fawe", new FastAsyncWorldEdit());

        if ((worldEdit) && !WorldEdit.isWorking())
            {
                IridiumSkyblock.getInstance().getLogger().warning("WorldEdit version doesn't support minecraft version, disabling WorldEdit integration");
            }

        if ((fawe) && !FastAsyncWorldEdit.isWorking())
        {
            IridiumSkyblock.getInstance().getLogger().warning("FAWE version does not implement API correctly, did you miss an update? Disabling FAWE integration");
        }
    
            schematicPaster = availablePasters.lastEntry().getValue();
            setPasterFromConfig();

        this.schematicFiles = new HashMap<>();
        File parent = new File(IridiumSkyblock.getInstance().getDataFolder(), "schematics");
        for (File file : parent.listFiles()) {
            schematicFiles.put(file.getName(), file);
        }
    }
    public void reload()
    {
        loadCache();
        setPasterFromConfig();
        schematicPaster.clearCache();
    }
    private void setPasterFromConfig()
    {
        String paster = IridiumSkyblock.getInstance().getConfiguration().paster;
        if(availablePasters.containsKey(paster))
            this.schematicPaster = availablePasters.get(paster);
        else
        {
            IridiumSkyblock.getInstance().getLogger().warning("Configuration error, selected paster ["+paster+"] is not available, available choices are "+availablePasters.keySet());
            this.schematicPaster = availablePasters.lastEntry().getValue();
        }
    }
    public void loadCache()
    {
        schematicFiles.clear();
        File parent = new File(IridiumSkyblock.getInstance().getDataFolder(), "schematics");
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
