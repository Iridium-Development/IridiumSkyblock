package com.iridium.iridiumskyblock.schematics;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extent.MaskingExtent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.SideEffectSet;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class WorldEdit implements SchematicPaster {

    private static final HashMap<File, ClipboardFormat> cachedClipboardFormat = new HashMap<>();

    public static boolean isWorking()
    {
        try{
            final Platform platform = com.sk89q.worldedit.WorldEdit.getInstance().getPlatformManager().queryCapability(Capability.WORLD_EDITING);
            int liveDataVersion = platform.getDataVersion();
            return liveDataVersion != -1;
        }
        catch(Throwable t)
        {
            IridiumSkyblock.getInstance().getLogger().warning("WorldEdit threw an error during initializing, make sure it's updated and API compatible(FAWE isn't API compatible) ::"+t.getMessage());
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

            clipboard.setOrigin(clipboard.getRegion().getMinimumPoint()); // Change the //copy point to the minimum corner
            try (EditSession editSession = com.sk89q.worldedit.WorldEdit.getInstance().newEditSession(new BukkitWorld(location.getWorld()))) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                        .copyEntities(true)
                        .ignoreAirBlocks(ignoreAirBlock)
                        .build();
                Operations.complete(operation);
                Operations.complete(editSession.commit());
                cachedClipboardFormat.putIfAbsent(file, format);
                completableFuture.complete(null);
            }
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearCache() {
        cachedClipboardFormat.clear();
    }

    public void deleteIsland(World world, Location pos1, Location pos2, int minHeight, int maxHeight, CompletableFuture<Void> completableFuture, int delay) {

        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);

        try (EditSession editSession = com.sk89q.worldedit.WorldEdit.getInstance().newEditSession(weWorld)) {
            BlockVector3 min = BlockVector3.at(pos1.getBlockX(), minHeight, pos1.getBlockZ());
            BlockVector3 max = BlockVector3.at(pos2.getBlockX(), maxHeight, pos2.getBlockZ());
            CuboidRegion selection = new CuboidRegion(min, max);

            editSession.getChangeSet().setRecordChanges(false);
            editSession.setReorderMode(EditSession.ReorderMode.FAST);
            editSession.setSideEffectApplier(SideEffectSet.none());
            ExistingBlockMask nonAirMask = new ExistingBlockMask(editSession);
            editSession.setMask(nonAirMask);

            // grab all points and remove air blocks from the selection
            List<BlockVector3> points = new ArrayList<>();
            for (BlockVector3 point : selection) {
                if (nonAirMask.test(point)) {
                    points.add(point);
                }
            }

            points = points.reversed(); // style points

            // group points into chunks
            Map<BlockVector2, List<BlockVector3>> chunkBatches = new LinkedHashMap<>();
            for (BlockVector3 pt : points) {
                BlockVector2 chunkPos = BlockVector2.at(pt.getX() >> 4, pt.getZ() >> 4);
                chunkBatches.computeIfAbsent(chunkPos, k -> new ArrayList<>()).add(pt);
            }

            // chunk list
            List<Map.Entry<BlockVector2, List<BlockVector3>>> batches = new ArrayList<>(chunkBatches.entrySet());

            new BukkitRunnable() {
                int chunkIndex = 0;
                int blockInsideChunkIndex = 0;
                final int blocksPerTick = IridiumSkyblock.getInstance().getConfiguration().pasterLimitPerTick;
                private final com.sk89q.worldedit.world.block.BlockState air =
                        com.sk89q.worldedit.world.block.BlockTypes.AIR.getDefaultState();

                @Override
                public void run() {
                    int processedThisTick = 0;

                    while (processedThisTick < blocksPerTick) {

                        if (chunkIndex >= batches.size()) {
                            finish();
                            return;
                        }

                        List<BlockVector3> currentChunkBlocks = batches.get(chunkIndex).getValue();

                        while (blockInsideChunkIndex < currentChunkBlocks.size() && processedThisTick < blocksPerTick) {
                            BlockVector3 point = currentChunkBlocks.get(blockInsideChunkIndex);

                            try {
                                editSession.setBlock(point, air);
                            } catch (MaxChangedBlocksException e) {
                                finish();
                                return;
                            }

                            blockInsideChunkIndex++;
                            processedThisTick++;
                        }

                        if (blockInsideChunkIndex >= currentChunkBlocks.size()) {
                            chunkIndex++;
                            blockInsideChunkIndex = 0;
                            editSession.flushSession(); // deprecated, but this pushes changes to the server, so it's critical for performance
                        }
                    }
                }

                private void finish() {
                    this.cancel();

                    completableFuture.complete(null);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!IridiumSkyblock.getInstance().getConfiguration().generatorType.isTerrainGenerator()) {
                                weWorld.fixLighting(selection.getChunks());
                            }
                        }
                    }.runTask(IridiumSkyblock.getInstance());
                }
            }.runTaskTimer(IridiumSkyblock.getInstance(), delay, 1L);
        }
    }

    public void regenIsland(World world, World regenWorld, Location pos1, Location pos2, Location pos3, Location pos4, int minHeight, int maxHeight, CompletableFuture<Void> completableFuture, int delay) {

        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
        com.sk89q.worldedit.world.World weRegenWorld = BukkitAdapter.adapt(regenWorld);

        // there's gotta be a more succinct method to do this
        BlockVector3 min = BlockVector3.at(pos1.getBlockX(), minHeight, pos1.getBlockZ());
        BlockVector3 max = BlockVector3.at(pos2.getBlockX(), maxHeight, pos2.getBlockZ());
        CuboidRegion selection = new CuboidRegion(min, max);

        BlockVector3 min2 = BlockVector3.at(pos3.getBlockX(), minHeight, pos3.getBlockZ());
        BlockVector3 max2 = BlockVector3.at(pos4.getBlockX(), maxHeight, pos4.getBlockZ());
        CuboidRegion selection2 = new CuboidRegion(min2, max2);

        BlockArrayClipboard clipboard = new BlockArrayClipboard(selection);

        // copy
        try (EditSession editSession = com.sk89q.worldedit.WorldEdit.getInstance().newEditSession(weRegenWorld)) {
            editSession.getChangeSet().setRecordChanges(false);
            editSession.setReorderMode(EditSession.ReorderMode.FAST);
            editSession.setSideEffectApplier(SideEffectSet.none());
            ExistingBlockMask nonAirMask = new ExistingBlockMask(editSession);
            editSession.setMask(nonAirMask);

            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    weRegenWorld,
                    selection,
                    clipboard,
                    selection.getMinimumPoint()
            );

            Operations.complete(forwardExtentCopy);

        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

        // paste
        EditSession editSession = com.sk89q.worldedit.WorldEdit.getInstance().newEditSession(weWorld);
        editSession.getChangeSet().setRecordChanges(false);
        editSession.setReorderMode(EditSession.ReorderMode.FAST);
        editSession.setSideEffectApplier(SideEffectSet.none());

        BlockVector3 offset = selection2.getMinimumPoint().subtract(clipboard.getMinimumPoint());

        Mask clipboardMask = new ExistingBlockMask(clipboard);
        MaskingExtent maskedClipboard = new MaskingExtent(clipboard, clipboardMask);

        // grab all points and remove air blocks from the selection
        List<BlockVector3> points = new ArrayList<>();
        for (BlockVector3 point : clipboard.getRegion()) {
            if (maskedClipboard.getMask().test(point)) {
                points.add(point);
            }
        }

        // group points into chunks
        Map<BlockVector2, List<BlockVector3>> chunkBatches = new LinkedHashMap<>();
        for (BlockVector3 pt : points) {
            BlockVector2 chunkPos = BlockVector2.at(pt.getX() >> 4, pt.getZ() >> 4);
            chunkBatches.computeIfAbsent(chunkPos, k -> new ArrayList<>()).add(pt);
        }

        // chunk list
        List<Map.Entry<BlockVector2, List<BlockVector3>>> batches = new ArrayList<>(chunkBatches.entrySet());

        new BukkitRunnable() {
            int chunkIndex = 0;
            int blockInsideChunkIndex = 0;
            final int blocksPerTick = IridiumSkyblock.getInstance().getConfiguration().pasterLimitPerTick;
            private final com.sk89q.worldedit.world.block.BlockState air =
                    com.sk89q.worldedit.world.block.BlockTypes.AIR.getDefaultState();

            @Override
            public void run() {
                int processedThisTick = 0;

                while (processedThisTick < blocksPerTick) {

                    if (chunkIndex >= batches.size()) {
                        finish();
                        return;
                    }

                    List<BlockVector3> currentChunkBlocks = batches.get(chunkIndex).getValue();

                    while (blockInsideChunkIndex < currentChunkBlocks.size() && processedThisTick < blocksPerTick) {

                        BlockVector3 point = currentChunkBlocks.get(blockInsideChunkIndex);
                        com.sk89q.worldedit.world.block.BlockState block = clipboard.getBlock(point);

                        try {
                            editSession.setBlock(point.add(offset), block);
                        } catch (MaxChangedBlocksException e) {
                            finish();
                            return;
                        }

                        blockInsideChunkIndex++;
                        processedThisTick++;
                    }

                    if (blockInsideChunkIndex >= currentChunkBlocks.size()) {
                        chunkIndex++;
                        blockInsideChunkIndex = 0;
                        editSession.flushSession(); // style points
                    }
                }
            }

            private void finish() {
                this.cancel();

                completableFuture.complete(null);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!IridiumSkyblock.getInstance().getConfiguration().generatorType.isTerrainGenerator()) {
                            weWorld.fixLighting(selection.getChunks());
                        }
                    }
                }.runTask(IridiumSkyblock.getInstance());
            }
        }.runTaskTimer(IridiumSkyblock.getInstance(), delay, 1L);
    }
}
