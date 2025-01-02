package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Generators;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;
import org.bukkit.generator.WorldInfo;

import java.util.*;
import java.util.stream.Collectors;

public class OceanGenerator extends IridiumChunkGenerator {

    public OceanGenerator(String name, boolean generatesTerrain, boolean lowerHorizon) {
        super(name, generatesTerrain, lowerHorizon);
    }

    @Override
    public @NotNull ChunkData generateChunkData(
            @NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {

        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), IridiumSkyblock.getInstance().getGenerators().simplexTerrainOctave);
        generator.setScale(IridiumSkyblock.getInstance().getGenerators().simplexTerrainScale);

        final ChunkData chunkData = createChunkData(world);

        List<Biome> biomeList = getOceanGenerator(world.getEnvironment()).biomeDataConfig.stream()
                .filter(biomeDataConfig -> biomeDataConfig.biome.get() != null)
                .map(biomeDataConfig -> biomeDataConfig.biome.get())
                .collect(Collectors.toList());

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int currentFloorHeight = (int) ((generator.noise(
                        chunkX * 16 + x, chunkZ * 16 + z, 1.5D, 0.5D, true) + 1)
                        * (getOceanGenerator(world.getEnvironment()).maxFloorHeight - getOceanGenerator(world.getEnvironment()).minFloorHeight)
                        + getOceanGenerator(world.getEnvironment()).minFloorHeight);

                //Generate bedrock
                generateBedrock(world, random, x, z, chunkData);

                // Generate vanilla surface terrain
                if(IridiumSkyblock.getInstance().getGenerators().generateSurface) {
                    generateSurface(world, random, x, z, chunkData);

                } else {
                    // Generate stone layer
                    for (int y = LocationUtils.getMinHeight(world) + 1; y < currentFloorHeight - 4; y++) {
                        chunkData.setBlock(x, y, z,
                                Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).mantle.get())
                        );
                    }

                    // Generate gravel layer
                    for (int y = LocationUtils.getMinHeight(world) + 1; y < currentFloorHeight; y++) {
                        chunkData.setBlock(x, y, z,
                                Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).underFloor.get())
                        );
                    }

                    // Generate sand on top of gravel
                    chunkData.setBlock(x, currentFloorHeight, z,
                            Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).floor.get())
                    );

                    // Generate water or lava on top of the floor
                    for (int y = currentFloorHeight + 1; y <= getOceanGenerator(world.getEnvironment()).liquidHeight; y++) {
                        chunkData.setBlock(x, y, z, Objects.requireNonNull(
                                getOceanGenerator(world.getEnvironment()).liquidType.get()));
                    }
                }

                if(!IridiumSkyblock.getInstance().getGenerators().biomeGradient) {
                    biomeGrid.setBiome(x, z, Objects.requireNonNull(biomeList.get(random.nextInt(biomeList.size()))));
                }
            }
        }

        return chunkData;
    }

    public void generateOcean(World world, int x, int z) {

        Random random = new Random((world.getSeed()));

        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(random, IridiumSkyblock.getInstance().getGenerators().simplexTerrainOctave);
        generator.setScale(IridiumSkyblock.getInstance().getGenerators().simplexTerrainScale);

        int currentFloorHeight = (int) ((generator.noise(
                x, z, 1.5D, 0.5D, true) + 1)
                * (getOceanGenerator(world.getEnvironment()).maxFloorHeight - getOceanGenerator(world.getEnvironment()).minFloorHeight)
                + getOceanGenerator(world.getEnvironment()).minFloorHeight);

        int minHeightWorld = LocationUtils.getMinHeight(world);

        if(!IridiumSkyblock.getInstance().getGenerators().generateSurface) {
            // Generate stone layer
            for (int y = minHeightWorld + 1; y < currentFloorHeight - 4; y++) {
                Block block = world.getBlockAt(x, y, z);
                if (block.getType() != getOceanGenerator(world.getEnvironment()).mantle.get()
                        && getOceanGenerator(world.getEnvironment()).mantle.get() != null) {

                    if (block.getState() instanceof InventoryHolder) {
                        ((InventoryHolder) block.getState()).getInventory().clear();
                    }
                    block.setType(Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).mantle.get()), false);
                }
            }

            // Generate gravel layer
            for (int y = minHeightWorld + 1; y < currentFloorHeight; y++) {
                Block block = world.getBlockAt(x, y, z);
                if (block.getType() != getOceanGenerator(world.getEnvironment()).underFloor.get()
                        && getOceanGenerator(world.getEnvironment()).underFloor.get() != null) {

                    if (block.getState() instanceof InventoryHolder) {
                        ((InventoryHolder) block.getState()).getInventory().clear();
                    }
                    block.setType(Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).underFloor.get()), false);
                }
            }

            // Generate sand on top of gravel
            if (world.getBlockAt(x, currentFloorHeight, z).getType() != getOceanGenerator(world.getEnvironment()).floor.get()
                    && getOceanGenerator(world.getEnvironment()).floor.get() != null) {

                if (world.getBlockAt(x, currentFloorHeight, z).getState() instanceof InventoryHolder) {
                    ((InventoryHolder) world.getBlockAt(x, currentFloorHeight, z).getState()).getInventory().clear();
                }

                for(int y = currentFloorHeight; y < currentFloorHeight + 4; y++) {
                    world.getBlockAt(x, currentFloorHeight, z)
                            .setType(Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).floor.get()), false);
                    currentFloorHeight++;
                }
            }

            // Generate water or lava on top of the floor
            for (int y = currentFloorHeight + 1; y <= getOceanGenerator(world.getEnvironment()).liquidHeight; y++) {
                Block block = world.getBlockAt(x, y, z);
                if (block.getType() != getOceanGenerator(world.getEnvironment()).liquidType.get() && getOceanGenerator(world.getEnvironment()).liquidType.parseMaterial() != null) {
                    if (block.getState() instanceof InventoryHolder) {
                        ((InventoryHolder) block.getState()).getInventory().clear();
                    }
                    block.setType(Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).liquidType.get()), true);
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
        }

        if(IridiumSkyblock.getInstance().getGenerators().useLegacyPopulators) {

        } else {
            shouldGenerateCaves(world, random, x, z);
            shouldGenerateDecorations(world, random , x, z);
            shouldGenerateMobs(world, random, x, z);
        }

    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        if(IridiumSkyblock.getInstance().getGenerators().useLegacyPopulators) {
            return IridiumSkyblock.getInstance().getBlockPopulatorList().get(world.getEnvironment());
        }
        return super.getDefaultPopulators(world);
    }

    @Override
    public boolean shouldGenerateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z) {
        return getOceanGenerator(worldInfo.getEnvironment()).spawnCaves;
    }

    @Override
    public boolean shouldGenerateDecorations(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z) {
        return getOceanGenerator(worldInfo.getEnvironment()).decorate;
    }

    @Override
    public boolean shouldGenerateMobs(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z) {
        return getOceanGenerator(worldInfo.getEnvironment()).spawnEntities;
    }

    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        return getOceanGenerator(world.getEnvironment()).spawnEntities;
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