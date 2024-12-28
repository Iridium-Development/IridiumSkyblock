package com.iridium.iridiumskyblock.generators;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Generators;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.bukkit.generator.WorldInfo;

import java.util.*;

public class FlatGenerator extends ChunkGenerator {

    @Override
    public @NotNull ChunkData generateChunkData(
            @NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {

        final ChunkData chunkData = createChunkData(world);
        int floorHeight = getFlatGenerator(world.getEnvironment()).floorHeight;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {

                // Generate layer of bedrock
                chunkData.setBlock(x, LocationUtils.getMinHeight(world), z,
                        Objects.requireNonNull(XMaterial.BEDROCK.parseMaterial())
                );

                // Generate dirt layer
                for (int y = LocationUtils.getMinHeight(world) + 1; y < floorHeight; y++) {
                    chunkData.setBlock(x, y, z,
                            Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).underFloor.parseMaterial())
                    );
                }

                // Generate grass on top of dirt
                chunkData.setBlock(x, floorHeight, z,
                        Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).floor.parseMaterial())
                );

                biomeGrid.setBiome(x, z, Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).biome.getBiome()));
            }
        }

        return chunkData;
    }

    public void generateFlatland(World world, int x, int z) {

        Random random = new Random((world.getSeed()));

        int floorHeight = getFlatGenerator(world.getEnvironment()).floorHeight;
        int minFloorHeight = world.getMinHeight();

        // Generate layer of bedrock
        if (world.getBlockAt(x, minFloorHeight, z).getType() != XMaterial.BEDROCK.parseMaterial()) {
            if (world.getBlockAt(x, minFloorHeight, z).getState() instanceof InventoryHolder) {
                ((InventoryHolder) world.getBlockAt(x, minFloorHeight, z).getState()).getInventory().clear();
            }
            world.getBlockAt(x, minFloorHeight, z).setType(Material.BEDROCK, false);
        }

        // Generate dirt layer
        for (int y = minFloorHeight + 1; y < floorHeight; y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != getFlatGenerator(world.getEnvironment()).underFloor.parseMaterial()
                    && getFlatGenerator(world.getEnvironment()).underFloor.parseMaterial() != null) {

                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).underFloor.parseMaterial()), false);
            }
        }

        // Generate grass on top of dirt
        if (world.getBlockAt(x, floorHeight, z).getType() != getFlatGenerator(world.getEnvironment()).floor.parseMaterial()
                && getFlatGenerator(world.getEnvironment()).floor.parseMaterial() != null) {

            if (world.getBlockAt(x, floorHeight, z).getState() instanceof InventoryHolder) {
                ((InventoryHolder) world.getBlockAt(x, floorHeight, z).getState()).getInventory().clear();
            }

            world.getBlockAt(x, floorHeight, z)
                    .setType(Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).floor.parseMaterial()), false);

        }

        // Replace everything else with air
        for (int y = floorHeight + 1; y < world.getMaxHeight(); y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != Material.AIR) {
                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(Material.AIR, false);
            }
        }

        // Generate kelp, ores, and mineral deposits
        // BREAKS BELOW 1.18
        //shouldGenerateDecorations(world, random , x, z);
    }

    @Override
    public boolean shouldGenerateDecorations(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z) {
        return getFlatGenerator(worldInfo.getEnvironment()).decorate;
    }

    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        return getFlatGenerator(world.getEnvironment()).canSpawnEntities;
    }

    private Generators.FlatGeneratorWorld getFlatGenerator(Environment environment) {
        switch (environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().flatGenerator.nether;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().flatGenerator.end;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().flatGenerator.overworld;
            }
        }
    }
}