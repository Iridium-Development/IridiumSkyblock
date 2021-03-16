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
            if (IridiumSkyblock.getInstance().getDatabaseManager().getSchematicDataList().size() == 0) {
                addDefaultSchematics();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.defaultSchematic = IridiumSkyblock.getInstance().getDatabaseManager().getSchematicDataList().get(0);

        // Saves the new schematics we added to the database.
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getDatabaseManager().saveSchematics());
    }

    /**
     * Adds the default schematics to the list.
     */
    private void addDefaultSchematics() throws IOException {
        InputStream inputStream = IridiumSkyblock.getInstance().getResource("island.schem");
        IridiumSkyblock.getInstance().getDatabaseManager().getSchematicDataList().add(new SchematicData("island", new BufferedReader(new InputStreamReader(inputStream)).readLine()));
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
        Schematic schematic = IridiumSkyblock.getInstance().getDatabaseManager().getSchematicDataList().stream().filter(schematicData -> schematicData.getId().equalsIgnoreCase(schematicID)).findFirst().orElse(defaultSchematic).getSchematic();

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

    public void addSchematic(String name, Location pos1, Location pos2) {
        Schematic schematic = shrinkSchematic(pos1, pos2);
        saveSchematic(name, schematic);
    }

    private Schematic shrinkSchematic(Location pos1, Location pos2) {
        World world = pos1.getWorld();

        int minX = min(pos1.getBlockX(), pos2.getBlockX());
        int minY = min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = max(pos1.getBlockY(), pos2.getBlockY());
        int maxZ = max(pos1.getBlockZ(), pos2.getBlockZ());

        int firstNonEmptyX = findFirstNonEmpty(minX, maxX, minY, maxY, minZ, maxZ, world::getBlockAt);
        int firstNonEmptyY = findFirstNonEmpty(minY, maxY, minX, maxX, minZ, maxZ, (y, x, z) -> world.getBlockAt(x, y, z));
        int firstNonEmptyZ = findFirstNonEmpty(minZ, maxZ, minX, maxX, minY, maxY, (z, x, y) -> world.getBlockAt(x, y, z));
        int lastNonEmptyX = findLastNonEmpty(minX, maxX, minY, maxY, minZ, maxZ, world::getBlockAt);
        int lastNonEmptyY = findLastNonEmpty(minY, maxY, minX, maxX, minZ, maxZ, (y, x, z) -> world.getBlockAt(x, y, z));
        int lastNonEmptyZ = findLastNonEmpty(minZ, maxZ, minX, maxX, minY, maxY, (z, x, y) -> world.getBlockAt(x, y, z));

        return new Schematic(new Location(world, firstNonEmptyX, firstNonEmptyY, firstNonEmptyZ), new Location(world, lastNonEmptyX, lastNonEmptyY, lastNonEmptyZ));
    }

    private void saveSchematic(String name, Schematic schematic) {
        SchematicData schematicData = new SchematicData(name, schematic);
        IridiumSkyblock.getInstance().getDatabaseManager().getSchematicDataList().removeIf(s -> s.getId().equals(name));
        IridiumSkyblock.getInstance().getDatabaseManager().getSchematicDataList().add(schematicData);
    }

    private int findFirstNonEmpty(int x1, int x2, int x3, int x4, int x5, int x6, TriFunction<Integer, Integer, Integer, Block> blockMapper) {
        for (int i = x1; i < x2; i++) {
            if (isNotEmpty(x3, x4, x5, x6, i, blockMapper)) return i;
        }

        return x1;
    }

    private int findLastNonEmpty(int x1, int x2, int x3, int x4, int x5, int x6, TriFunction<Integer, Integer, Integer, Block> blockMapper) {
        for (int i = x2; i > x1; i--) {
            if (isNotEmpty(x3, x4, x5, x6, i, blockMapper)) return i;
        }

        return x1;
    }

    private boolean isNotEmpty(int x3, int x4, int x5, int x6, int i, TriFunction<Integer, Integer, Integer, Block> blockMapper) {
        boolean isEmpty = true;

        for (int j = x3; j < x4; j++) {
            for (int k = x5; k < x6; k++) {
                if (blockMapper.apply(i, j, k).getType() != Material.AIR) {
                    isEmpty = false;
                }
            }
        }

        return !isEmpty;
    }

}
