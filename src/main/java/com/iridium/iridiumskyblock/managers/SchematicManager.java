package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.BlockData;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Schematic;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.SchematicData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.concurrent.CompletableFuture;

/**
 * Class which handles schematics and pastes them.
 */
public class SchematicManager {

    // If we cant find a schematic by the id we will use this one instead
    private final SchematicData defaultSchematic;

    public SchematicManager() {
        // The default schematic, if we cant find a schematic by its id the plugin will use this one instead.
        this.defaultSchematic = new SchematicData("test", new Schematic(new Location(Bukkit.getWorlds().get(0), -4, 60, -4), new Location(Bukkit.getWorlds().get(0), 10, 78, 10)));
        if (IridiumSkyblock.getInstance().getDatabaseManager().getSchematicDataList().size() == 0)
            addDefaultSchematics();

        // Saves the new schematics we added to the database.
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getDatabaseManager().saveSchematics());
    }

    /**
     * Adds the default schematics to the list
     */
    private void addDefaultSchematics() {
        IridiumSkyblock.getInstance().getDatabaseManager().getSchematicDataList().add(new SchematicData("test", new Schematic(new Location(Bukkit.getWorlds().get(0), -4, 60, -4), new Location(Bukkit.getWorlds().get(0), 10, 78, 10))));
        IridiumSkyblock.getInstance().getDatabaseManager().getSchematicDataList().add(new SchematicData("test2", new Schematic(new Location(Bukkit.getWorlds().get(0), 20, 60, 20), new Location(Bukkit.getWorlds().get(0), 30, 78, 30))));
    }


    /**
     * Pastes the island schematic at the designated island
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
}
