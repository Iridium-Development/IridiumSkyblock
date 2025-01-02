package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Generators;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.bukkit.generator.WorldInfo;

import java.util.*;
import java.util.stream.Collectors;

public class FlatGenerator extends IridiumChunkGenerator {

    public FlatGenerator(String name, boolean generatesTerrain, boolean lowerHorizon) {
        super(name, generatesTerrain, lowerHorizon);
    }

    @Override
    public @NotNull ChunkData generateChunkData(
            @NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {

        final ChunkData chunkData = createChunkData(world);

        int floorHeight = getFlatGenerator(world.getEnvironment()).floorHeight;

        List<Biome> biomeList = getFlatGenerator(world.getEnvironment()).biomeDataConfig.stream()
                .filter(biomeDataConfig -> biomeDataConfig.biome.get() != null)
                .map(biomeDataConfig -> biomeDataConfig.biome.get())
                .collect(Collectors.toList());

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {

                // Generate layer of bedrock
                generateBedrock(world, random, x, z, chunkData);

                // Generate stone layer
                for (int y = LocationUtils.getMinHeight(world) + 1; y < floorHeight - 4; y++) {
                    chunkData.setBlock(x, y, z,
                            Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).mantle.get())
                    );
                }

                // Generate dirt layer
                for (int y = floorHeight - 4; y < floorHeight; y++) {
                    if(chunkData.getType(x, y, z) == Material.BEDROCK) continue;
                    chunkData.setBlock(x, y, z,
                            Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).underFloor.get())
                    );
                }

                // Generate grass on top of dirt
                chunkData.setBlock(x, floorHeight, z,
                        Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).floor.get())
                );

                // Generate biome
                if(!IridiumSkyblock.getInstance().getGenerators().biomeGradient) {
                    biomeGrid.setBiome(x, z, Objects.requireNonNull(biomeList.get(random.nextInt(biomeList.size()))));
                }
            }
        }

        return chunkData;
    }

    public void generateFlatland(World world, int x, int z) {

        Random random = new Random((world.getSeed()));

        int floorHeight = getFlatGenerator(world.getEnvironment()).floorHeight;
        int minFloorHeight = world.getMinHeight();

        // Generate stone layer
        for (int y = minFloorHeight + 1; y < floorHeight - 4; y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != getFlatGenerator(world.getEnvironment()).mantle.get()
                    && getFlatGenerator(world.getEnvironment()).mantle.get() != null) {

                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).mantle.get()), false);
            }
        }

        // Generate dirt layer
        for (int y = minFloorHeight + 1; y < floorHeight; y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != getFlatGenerator(world.getEnvironment()).underFloor.get()
                    && getFlatGenerator(world.getEnvironment()).underFloor.get() != null) {

                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).underFloor.get()), false);
            }
        }

        // Generate grass on top of dirt
        if (world.getBlockAt(x, floorHeight, z).getType() != getFlatGenerator(world.getEnvironment()).floor.get()
                && getFlatGenerator(world.getEnvironment()).floor.get() != null) {

            if (world.getBlockAt(x, floorHeight, z).getState() instanceof InventoryHolder) {
                ((InventoryHolder) world.getBlockAt(x, floorHeight, z).getState()).getInventory().clear();
            }

            world.getBlockAt(x, floorHeight, z)
                    .setType(Objects.requireNonNull(getFlatGenerator(world.getEnvironment()).floor.get()), false);
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

        if(IridiumSkyblock.getInstance().getGenerators().useLegacyPopulators) {

        } else {
            shouldGenerateCaves(world, random, x, z);
            shouldGenerateDecorations(world, random , x, z);
            shouldGenerateMobs(world, random, x, z);
        }
    }

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        if (chunkData.getMinHeight() == worldInfo.getMinHeight()) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunkData.setBlock(x, chunkData.getMinHeight(), z, Material.BEDROCK);
                }
            }
        }
    }

    @Override
    public boolean shouldGenerateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z) {
        return getFlatGenerator(worldInfo.getEnvironment()).spawnCaves;
    }

    @Override
    public boolean shouldGenerateDecorations(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z) {
        return getFlatGenerator(worldInfo.getEnvironment()).decorate;
    }

    @Override
    public boolean shouldGenerateMobs(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z) {
        return getFlatGenerator(worldInfo.getEnvironment()).spawnEntities;
    }

    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        return getFlatGenerator(world.getEnvironment()).spawnEntities;
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