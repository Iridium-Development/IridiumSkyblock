package com.iridium.iridiumskyblock.schematics;

import org.bukkit.Location;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface SchematicPaster {
    void paste(File file, Location location, Boolean ignoreAirBlock, CompletableFuture<Void> completableFuture);
}
