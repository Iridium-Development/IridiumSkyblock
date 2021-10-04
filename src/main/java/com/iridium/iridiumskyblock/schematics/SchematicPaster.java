package com.iridium.iridiumskyblock.schematics;

import org.bukkit.Location;

import java.io.File;

public interface SchematicPaster {
    void paste(File file, Location location);
}
