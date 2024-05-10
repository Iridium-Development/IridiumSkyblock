package com.iridium.iridiumskyblock.schematics;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class FastAsyncWorldEdit implements SchematicPaster {

    private static final HashMap<File, ClipboardFormat> cachedClipboardFormat = new HashMap<>();
    private static Object mutex = new Object();

    public static boolean isWorking() {
        try {
            final Platform platform = com.sk89q.worldedit.WorldEdit.getInstance().getPlatformManager()
                    .queryCapability(Capability.WORLD_EDITING);
            int liveDataVersion = platform.getDataVersion();
            return liveDataVersion != -1;
        } catch (Throwable t) {
            IridiumSkyblock.getInstance().getLogger().warning(
                    "WorldEdit threw an error during initializing, make sure it's updated and API compatible(FAWE isn't API compatible) ::"
                            + t.getMessage());
        }
        return false;
    }

    @Override
    public void paste(File file, Location location, Boolean ignoreAirBlock, CompletableFuture<Void> completableFuture) {
        try {
            ClipboardFormat format = cachedClipboardFormat.getOrDefault(file, ClipboardFormats.findByFile(file));
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();
            int width = clipboard.getDimensions().getBlockX();
            int height = clipboard.getDimensions().getBlockY();
            int length = clipboard.getDimensions().getBlockZ();

            int newLength = (int) (length / 2.00);
            int newWidth = (int) (width / 2.00);
            int newHeight = (int) (height / 2.00);

            location.subtract(newWidth, newHeight, newLength); //Center the schematic (for real this time)

            clipboard.setOrigin(clipboard.getRegion().getMinimumPoint()); // Change the //copy point to the minimum
                                                                          // corner
            {
                Thread t = new Thread() {
                    public void run() {
                        synchronized (mutex) {
                            EditSession editSession = com.sk89q.worldedit.WorldEdit.getInstance()
                                    .newEditSession(new BukkitWorld(location.getWorld()));
                            Operation operation = new ClipboardHolder(clipboard)
                                    .createPaste(editSession)
                                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                                    .copyEntities(true)
                                    .ignoreAirBlocks(ignoreAirBlock)
                                    .build();
                            try {
                                Operations.complete(operation);
                                Operations.complete(editSession.commit());
                            } catch (WorldEditException e) {
                                e.printStackTrace();
                            }
                            cachedClipboardFormat.putIfAbsent(file, format);
                            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    completableFuture.complete(null);
                                }
                            });
                        }
                    };
                };
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearCache() {
        cachedClipboardFormat.clear();
    }
}
