package com.iridium.iridiumskyblock.schematics;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface SchematicPaster {
    void paste(File file, Location location, Boolean ignoreAirBlock, CompletableFuture<Void> completableFuture);
    void clearCache();
    void deleteIsland(World world, Location pos1, Location pos2, int minHeight, int maxHeight, CompletableFuture<Void> completableFuture, int delay);
    void regenIsland(World world, World regenWorld, Location pos1, Location pos2, Location pos3, Location pos4, int minHeight, int maxHeight, CompletableFuture<Void> completableFuture, int delay);
}
