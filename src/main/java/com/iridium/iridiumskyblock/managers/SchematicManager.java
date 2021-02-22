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

/**
 * Class which handles schematics und pastes them.
 */
public class SchematicManager {

    private final IridiumSkyblock iridiumSkyblock;
    private final HashMap<String, Schematic> schematics = new HashMap<>();

    public SchematicManager(IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
        schematics.put("test", new Schematic(new Location(Bukkit.getWorlds().get(0), -4, 60, -4), new Location(Bukkit.getWorlds().get(0), 10, 70, 10)));
    }


    /**
     * Pastes the island schematic at the designated island
     *
     * @param island      The island you want the schematic to be pasted at
     * @param world       The world you want it to be pasted in
     * @param schematicID The schematic's id
     * @return A completable future of when its finished pasting
     */
    public CompletableFuture<Void> pasteSchematic(final Island island, final World world, final String schematicID) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Schematic schematic = schematics.get(schematicID);

        pasteSchematic(island, world, schematic, completableFuture, 0);

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

    private void pasteSchematic(final Island island, final World world, final Schematic schematic, final CompletableFuture<Void> completableFuture, final int y) {
        //If y is equal to Schematic#getHeight then theres nothing else to paste so we should return and complete the completable future
        if (y == schematic.getHeight()) {
            completableFuture.complete(null);
            return;
        }

        // Loop all blocks in the schematics layer at the current y level
        for (int x = 0; x < schematic.getLength(); x++) {
            for (int z = 0; z < schematic.getWidth(); z++) {
                Block block = island.getCenter(world).subtract(schematic.getLength(), 0, schematic.getWidth()).add(x, y, z).getBlock();
                Schematic.BlockData blockData = schematic.getBlockData()[x][y][z];
                // If the block is air blockData is null to save storage (plus its more efficient)
                if (blockData != null) blockData.setBlock(block);
            }
        }

        // If schematicPastingDelay is 0 then we want it to execute immediately
        if (iridiumSkyblock.getConfiguration().schematicPastingDelay == 0) {
            pasteSchematic(island, world, schematic, completableFuture, y + 1);
        } else {
            Bukkit.getScheduler().runTaskLater(iridiumSkyblock, () -> pasteSchematic(island, world, schematic, completableFuture, y + 1), iridiumSkyblock.getConfiguration().schematicPastingDelay);
        }
    }

}
