package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Schematic;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class SchematicManager {
    private final IridiumSkyblock iridiumSkyblock;
    private final HashMap<String, Schematic> schematics = new HashMap<>();

    public SchematicManager(IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
        schematics.put("test", new Schematic(new Location(Bukkit.getWorlds().get(0), -4, 60, -4), new Location(Bukkit.getWorlds().get(0), 10, 70, 10)));
    }

    public CompletableFuture<Void> pasteSchematic(Island island, World world, String schematicID) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Schematic schematic = schematics.get(schematicID);
        AtomicInteger y = new AtomicInteger();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(iridiumSkyblock, () -> {
            if (y.get() == schematic.getHeight()) {
                completableFuture.complete(null);
                return;
            }
            int ylevel = y.getAndIncrement();
            for (int x = 0; x < schematic.getLength(); x++) {
                for (int z = 0; z < schematic.getWidth(); z++) {
                    Block block = island.getCenter(world).subtract(schematic.getLength(), 0, schematic.getWidth()).add(x, ylevel, z).getBlock();
                    schematic.getBlockData()[x][ylevel][z].setBlock(block);
                }
            }
        }, 0, iridiumSkyblock.getConfiguration().SchematicPastingDelay);

        return completableFuture;
    }
}
