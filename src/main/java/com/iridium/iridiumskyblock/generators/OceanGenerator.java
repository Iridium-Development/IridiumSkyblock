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
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;
import org.bukkit.generator.WorldInfo;

import java.util.*;

public class OceanGenerator extends ChunkGenerator {

    @Override
    public @NotNull ChunkData generateChunkData(
            @NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {

        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        final ChunkData chunkData = createChunkData(world);
        generator.setScale(0.005D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int currentFloorHeight = (int) ((generator.noise(
                        chunkX * 16 + x, chunkZ * 16 + z, 1.5D, 0.5D, true) + 1)
                        * (getOceanGenerator(world.getEnvironment()).maxFloorHeight - getOceanGenerator(world.getEnvironment()).minFloorHeight)
                        + getOceanGenerator(world.getEnvironment()).minFloorHeight);

                // Generate layer of bedrock
                chunkData.setBlock(x, LocationUtils.getMinHeight(world), z,
                        Objects.requireNonNull(XMaterial.BEDROCK.parseMaterial())
                );

                // Generate gravel layer
                for (int y = LocationUtils.getMinHeight(world) + 1; y < currentFloorHeight; y++) {
                    chunkData.setBlock(x, y, z,
                            Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).underFloor.parseMaterial())
                    );
                }

                // Generate sand on top of gravel
                chunkData.setBlock(x, currentFloorHeight, z,
                        Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).floor.parseMaterial())
                );

                // Generate water or lava on top of the floor
                for (int y = currentFloorHeight + 1; y <= getOceanGenerator(world.getEnvironment()).liquidHeight; y++) {
                    chunkData.setBlock(x, y, z, Objects.requireNonNull(
                            getOceanGenerator(world.getEnvironment()).liquidType.parseMaterial()));
                }

                biomeGrid.setBiome(x, z, Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).biome.getBiome()));
            }
        }

        return chunkData;
    }

    public void generateOcean(World world, int x, int z) {

        Random random = new Random((world.getSeed()));

        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(random, 8);
        generator.setScale(0.005D);

        int currentFloorHeight = (int) ((generator.noise(
                x, z, 1.5D, 0.5D, true) + 1)
                * (getOceanGenerator(world.getEnvironment()).maxFloorHeight - getOceanGenerator(world.getEnvironment()).minFloorHeight)
                + getOceanGenerator(world.getEnvironment()).minFloorHeight);

        int minHeightWorld = LocationUtils.getMinHeight(world);

        // Generate layer of bedrock
        if (world.getBlockAt(x, minHeightWorld, z).getType() != XMaterial.BEDROCK.parseMaterial()) {
            if (world.getBlockAt(x, minHeightWorld, z).getState() instanceof InventoryHolder) {
                ((InventoryHolder) world.getBlockAt(x, minHeightWorld, z).getState()).getInventory().clear();
            }
            world.getBlockAt(x, minHeightWorld, z).setType(Material.BEDROCK, false);
        }

        // Generate gravel layer
        for (int y = minHeightWorld + 1; y < currentFloorHeight; y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != getOceanGenerator(world.getEnvironment()).underFloor.parseMaterial()
                    && getOceanGenerator(world.getEnvironment()).underFloor.parseMaterial() != null) {

                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).underFloor.parseMaterial()), false);
            }
        }

        // Generate sand on top of gravel
        if (world.getBlockAt(x, currentFloorHeight, z).getType() != getOceanGenerator(world.getEnvironment()).floor.parseMaterial()
                && getOceanGenerator(world.getEnvironment()).floor.parseMaterial() != null) {

            if (world.getBlockAt(x, currentFloorHeight, z).getState() instanceof InventoryHolder) {
                ((InventoryHolder) world.getBlockAt(x, currentFloorHeight, z).getState()).getInventory().clear();
            }

            for(int y = currentFloorHeight; y < currentFloorHeight + 5; y++) {
                world.getBlockAt(x, currentFloorHeight, z)
                        .setType(Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).floor.parseMaterial()), false);
                currentFloorHeight++;
            }

        }

        // Generate water or lava on top of the floor
        for (int y = currentFloorHeight + 1; y <= getOceanGenerator(world.getEnvironment()).liquidHeight; y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != getOceanGenerator(world.getEnvironment()).liquidType.parseMaterial() && getOceanGenerator(world.getEnvironment()).liquidType.parseMaterial() != null) {
                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(getOceanGenerator(world.getEnvironment()).liquidType.parseMaterial(), false);
            }
        }

        // Replace everything else with air
        for (int y = getOceanGenerator(world.getEnvironment()).liquidHeight + 1; y < world.getMaxHeight(); y++) {
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
        return getOceanGenerator(worldInfo.getEnvironment()).decorate;
    }

    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        return getOceanGenerator(world.getEnvironment()).canSpawnEntities;
    }

    private Generators.OceanGeneratorWorld getOceanGenerator(Environment environment) {
        switch (environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.nether;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.end;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.overworld;
            }
        }
    }
}