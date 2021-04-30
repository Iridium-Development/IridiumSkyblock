package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.BlockData;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Schematic;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.SchematicData;
import com.iridium.iridiumskyblock.utils.TriFunction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Class which handles schematics and pastes them.
 */
public class SchematicManager {

    // If we cant find a schematic by the id we will use this one instead
    private final SchematicData defaultSchematic;

    /**
     * The default constructor.
     */
    public SchematicManager() {
        // The default schematic, if we cant find a schematic by its id the plugin will use this one instead.
        try {
            if (IridiumSkyblock.getInstance().getDatabaseManager().getSchematicTableManager().getEntries().size() == 0) {
                addDefaultSchematics();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.defaultSchematic = IridiumSkyblock.getInstance().getDatabaseManager().getSchematicTableManager().getEntries().get(0);

        // Saves the new schematics we added to the database.
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getDatabaseManager().getSchematicTableManager().save());
    }

    /**
     * Adds the default schematics to the list.
     */
    private void addDefaultSchematics() throws IOException {
        InputStream inputStream = IridiumSkyblock.getInstance().getResource("island.schem");
        IridiumSkyblock.getInstance().getDatabaseManager().getSchematicTableManager().getEntries().add(new SchematicData("island", new BufferedReader(new InputStreamReader(inputStream)).readLine()));
    }


    /**
     * Pastes the island schematic at the designated island.
     *
     * @param island      The island you want the schematic to be pasted at
     * @param world       The world you want it to be pasted in
     * @param schematicID The schematic's id
     * @return A completable future of when its finished pasting
     */
    public CompletableFuture<Void> pasteSchematic(final Island island, final World world, final String schematicID, final int delay) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Schematic schematic = IridiumSkyblock.getInstance().getDatabaseManager().getSchematicTableManager().getEntries().stream().filter(schematicData -> schematicData.getId().equalsIgnoreCase(schematicID)).findFirst().orElse(defaultSchematic).getSchematic();

        pasteSchematic(island, world, schematic, completableFuture, 0, delay);

        return completableFuture;
    }

    /**
     * Pastes the schematic one layer at a time starting at the specified y level
     *
     * @param island            The island the schematic is being pasted at
     * @param world             The world the schematic is being pasted at
     * @param schematic         The schematic being pasted
     * @param completableFuture The completable future that's being returned
     * @param y                 The starting layer of the schematic that's being pasted
     */
    private void pasteSchematic(final Island island, final World world, final Schematic schematic, final CompletableFuture<Void> completableFuture, final int y, final int delay) {
        // If y is equal to Schematic#getHeight then theres nothing else to paste so we should return and complete the completable future
        if (y == schematic.getHeight()) {
            completableFuture.complete(null);
            return;
        }

        // Loop all blocks in the schematics layer at the current y level
        for (int x = 0; x < schematic.getLength(); x++) {
            for (int z = 0; z < schematic.getWidth(); z++) {
                Block block = island.getCenter(world).subtract(schematic.getLength(), -90, schematic.getWidth()).add(x, y, z).getBlock();
                BlockData blockData = schematic.getBlockData()[x][y][z];
                if (blockData != null) blockData.setBlock(block);
            }
        }

        // If schematicPastingDelay is 0 then we want it to execute immediately
        if (delay == 0) {
            pasteSchematic(island, world, schematic, completableFuture, y + 1, delay);
        } else {
            Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> pasteSchematic(island, world, schematic, completableFuture, y + 1, delay), delay);
        }
    }

    /**
     * Shrinks and saves a schematic.
     *
     * @param name The name which this schematic should have
     * @param pos1 One of the corner positions of this schematic
     * @param pos2 The other corner position of this schematic
     */
    public void addSchematic(String name, Location pos1, Location pos2) {
        Schematic schematic = shrinkSchematic(pos1, pos2);
        saveSchematic(name, schematic);
    }

    /**
     * Shrinks a schematic by removing all the air blocks.
     * Required to automatically center island schematics.
     * It's irrelevant where pos1 and pos2 are.
     * <p>
     * Don't even bother to understand this, but here is a short explanation:
     * Checks the 3 planes (x-y, x-z, y-z / x1-x2, x1-x3, x2-x3) from both directions and returns
     * the first non-empty row of blocks. This is used to build new corner positions.
     * I (das_) take all the blame for implementing it in this horrific way.
     *
     * @param pos1 The original first position
     * @param pos2 The original second position
     * @return The newly created schematic representation of the shrunken area
     */
    private Schematic shrinkSchematic(Location pos1, Location pos2) {
        World world = pos1.getWorld();

        int minX = min(pos1.getBlockX(), pos2.getBlockX());
        int minY = min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = max(pos1.getBlockY(), pos2.getBlockY());
        int maxZ = max(pos1.getBlockZ(), pos2.getBlockZ());

        // Here comes the madness
        int firstNonEmptyX = findFirstNonEmpty(minX, maxX, minY, maxY, minZ, maxZ, world::getBlockAt);
        int firstNonEmptyY = findFirstNonEmpty(minY, maxY, minX, maxX, minZ, maxZ, (y, x, z) -> world.getBlockAt(x, y, z));
        int firstNonEmptyZ = findFirstNonEmpty(minZ, maxZ, minX, maxX, minY, maxY, (z, x, y) -> world.getBlockAt(x, y, z));
        int lastNonEmptyX = findLastNonEmpty(minX, maxX, minY, maxY, minZ, maxZ, world::getBlockAt);
        int lastNonEmptyY = findLastNonEmpty(minY, maxY, minX, maxX, minZ, maxZ, (y, x, z) -> world.getBlockAt(x, y, z));
        int lastNonEmptyZ = findLastNonEmpty(minZ, maxZ, minX, maxX, minY, maxY, (z, x, y) -> world.getBlockAt(x, y, z));

        return new Schematic(new Location(world, firstNonEmptyX, firstNonEmptyY, firstNonEmptyZ), new Location(world, lastNonEmptyX, lastNonEmptyY, lastNonEmptyZ));
    }

    /**
     * Tries to find the first non empty row of blocks.
     * x1 - x6 are just weird names, their meaning changes based on the block mapper.
     * x1 & x2, x3 & x4, x5 & x6 are pairs, the first one should always be the smaller number.
     * <p>
     * The block mapper takes the steadily increasing x1 as the first argument,
     * the steadily increasing x3 as the second argument and the steadily increasing x5 as the third one.
     * You can get a block in a world out of that, but I won't explain this, just check out
     * {@link SchematicManager#shrinkSchematic(Location, Location)}.
     *
     * @param x1          x1, please read the explanation above
     * @param x2          x2, please read the explanation above
     * @param x3          x3, please read the explanation above
     * @param x4          x4, please read the explanation above
     * @param x5          x5, please read the explanation above
     * @param x6          x6, please read the explanation above
     * @param blockMapper Maps coordinates to a block, please read the explanation above
     * @return The first non empty row, x1 if there is none
     */
    private int findFirstNonEmpty(int x1, int x2, int x3, int x4, int x5, int x6, TriFunction<Integer, Integer, Integer, Block> blockMapper) {
        for (int i = x1; i < x2; i++) {
            if (isNotEmpty(x3, x4, x5, x6, i, blockMapper)) return i;
        }

        return x1;
    }

    /**
     * Tries to find the last non empty row of blocks.
     * x1 - x6 are just weird names, their meaning changes based on the block mapper.
     * x1 & x2, x3 & x4, x5 & x6 are pairs, the first one should always be the smaller number.
     * <p>
     * The block mapper takes the steadily decreasing x2 as the first argument,
     * the steadily increasing x3 as the second argument and the steadily increasing x5 as the third one.
     * You can get a block in a world out of that, but I won't explain this, just check out
     * {@link SchematicManager#shrinkSchematic(Location, Location)}.
     *
     * @param x1          x1, please read the explanation above
     * @param x2          x2, please read the explanation above
     * @param x3          x3, please read the explanation above
     * @param x4          x4, please read the explanation above
     * @param x5          x5, please read the explanation above
     * @param x6          x6, please read the explanation above
     * @param blockMapper Maps coordinates to a block, please read the explanation above
     * @return The last non empty row, x1 if there is none
     */
    private int findLastNonEmpty(int x1, int x2, int x3, int x4, int x5, int x6, TriFunction<Integer, Integer, Integer, Block> blockMapper) {
        for (int i = x2; i > x1; i--) {
            if (isNotEmpty(x3, x4, x5, x6, i, blockMapper)) return i;
        }

        return x1;
    }

    /**
     * Checks if there a blocks on this section of the plane.
     * x3 - x6 are just weird names, their meaning changes based on the block mapper.
     * x3 & x4, x5 & x6 are pairs, the first one should always be the smaller number.
     * i is the current planes coordinate.
     * <p>
     * The block mapper takes i as the first argument,
     * the steadily increasing x3 as the second argument and the steadily increasing x5 as the third one.
     * You can get a block in a world out of that, but I won't explain this, just check out
     * {@link SchematicManager#shrinkSchematic(Location, Location)}.
     *
     * @param x3          x3, please read the explanation above
     * @param x4          x4, please read the explanation above
     * @param x5          x5, please read the explanation above
     * @param x6          x6, please read the explanation above
     * @param i           i, representing the current planes coordinate, please read the explanation above
     * @param blockMapper Maps coordinates to a block, please read the explanation above
     * @return Whether or not there a blocks on this section of the plane
     */
    private boolean isNotEmpty(int x3, int x4, int x5, int x6, int i, TriFunction<Integer, Integer, Integer, Block> blockMapper) {
        for (int j = x3; j < x4; j++) {
            for (int k = x5; k < x6; k++) {
                if (blockMapper.apply(i, j, k).getType() != Material.AIR) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Saves a schematic under the specified name to the database.
     *
     * @param name      The name of the schematic
     * @param schematic The schematic which should be saved
     */
    private void saveSchematic(String name, Schematic schematic) {
        SchematicData schematicData = new SchematicData(name, schematic);
        IridiumSkyblock.getInstance().getDatabaseManager().getSchematicTableManager().getEntries().removeIf(s -> s.getId().equals(name));
        IridiumSkyblock.getInstance().getDatabaseManager().getSchematicTableManager().getEntries().add(schematicData);
    }

}
