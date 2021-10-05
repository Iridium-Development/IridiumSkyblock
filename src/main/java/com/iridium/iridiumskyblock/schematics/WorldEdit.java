package com.iridium.iridiumskyblock.schematics;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class WorldEdit implements SchematicPaster {

    private static final HashMap<File, ClipboardFormat> cachedClipboardFormat = new HashMap<>();

    @Override
    public void paste(File file, Location location) {
        try {
            ClipboardFormat format = (cachedClipboardFormat.get(file) != null) ? cachedClipboardFormat.get(file) : ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();
            int width = clipboard.getDimensions().getBlockX();
            int height = clipboard.getDimensions().getBlockY();
            int length = clipboard.getDimensions().getBlockZ();
            location.subtract(width / 2.00, height / 2.00, length / 2.00); // Centers the schematic
            clipboard.setOrigin(clipboard.getRegion().getMinimumPoint()); // Change the //copy point to the minimum corner
            try (EditSession editSession = com.sk89q.worldedit.WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), -1)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                        .copyEntities(true)
                        .ignoreAirBlocks(true)
                        .build();
                Operations.complete(operation);
                cachedClipboardFormat.putIfAbsent(file, format);
            }
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }

    public static void clearClipBoardCache() {
        cachedClipboardFormat.clear();
    }
}
