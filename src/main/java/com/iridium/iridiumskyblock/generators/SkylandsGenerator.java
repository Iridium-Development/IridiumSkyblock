package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Generators;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class SkylandsGenerator extends IridiumChunkGenerator {

    public SkylandsGenerator(String name, boolean generatesTerrain, boolean lowerHorizon) {
        super(name, generatesTerrain, lowerHorizon);
    }

    @Override
    public @NotNull ChunkData generateChunkData(
            @NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {

        SimplexOctaveGenerator generatorX = new SimplexOctaveGenerator(new Random(world.getSeed()), IridiumSkyblock.getInstance().getGenerators().simplexTerrainOctave);
        generatorX.setScale(IridiumSkyblock.getInstance().getGenerators().simplexTerrainScale);

        SimplexOctaveGenerator generatorY = new SimplexOctaveGenerator(new Random(world.getSeed()), IridiumSkyblock.getInstance().getGenerators().simplexTerrainOctave);
        generatorY.setScale(IridiumSkyblock.getInstance().getGenerators().simplexTerrainScale);

        final ChunkData chunkData = createChunkData(world);

        List<Biome> biomeList = getSkylandsGenerator(world.getEnvironment()).biomeDataConfig.stream()
                .filter(biomeDataConfig -> biomeDataConfig.biome.get() != null)
                .map(biomeDataConfig -> biomeDataConfig.biome.get())
                .collect(Collectors.toList());


        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int currentFloorHeight = (int) ((generatorX.noise(
                        chunkX * 16 + x, chunkZ * 16 + z, 1.5D, 0.5D, true) + 1)
                        * (getSkylandsGenerator(world.getEnvironment()).maxFloorHeight - getSkylandsGenerator(world.getEnvironment()).minFloorHeight)
                        + getSkylandsGenerator(world.getEnvironment()).minFloorHeight);

                currentFloorHeight = (int) (generatorX.noise(
                        (chunkX * 16 + xl | chunkZ * 8 + chunkX * 8 + X | baseX + X,
                        chunkZ * 16 + xz | chunkZ * 8 + chunkX * 8 + Z | baseZ + Z,
                        frequency, amplitude)
                        * 7D + 150);

                for (int y = currentHeight; y > 150 - (generatorX.noise(
                        chunkX * 16 + xl | chunkZ * 8 + chunkX * 8 + X | baseX + X,
                        chunkZ * 16 + xz | chunkZ * 8 + chunkX * 8 + Z | baseZ + Z,
                        frequency, amplitude)
                        * 7D - 13D); y--) {

                    if (generatorX.noise(
                            chunkX * 16 + xl | chunkZ * 8 + chunkX * 8 + X | baseX + X,
                            chunkZ * 16 + xz | chunkZ * 8 + chunkX * 8 + Z | baseZ + Z,
                            frequency, amplitude) > spawnrate) {

                        if (y == currentHeight) {
                            if (world.getBiome(chunkX * 16 + xl, chunkZ * 16 + xz).equals(Biome.DESERT) || world.getBiome(chunkX * 16 + xl, chunkZ * 16 + xz).equals(Biome.DESERT_HILLS) || world.getBiome(chunkX * 16 + xl, chunkZ * 16 + xz).equals(Biome.DESERT_MOUNTAINS)) {
                                chunk.setBlock(X, y, Z, Material.SAND);
                            } else {
                                chunk.setBlock(X, y, Z, Material.GRASS);

                            }
                        } else {
                            if (y == currentHeight - 1) {
                                chunk.setBlock(X, y, Z, Material.DIRT);


                            } else {
                                chunk.setBlock(X, y, Z, new Random().nextBoolean() ? Material.COBBLESTONE : Material.STONE);

                            }
                        }
                    }

                    if (aplitudeether2 == true) {
                        currentHeight = (int) (generatorY.noise(
                                chunkX * 16 + X,
                                chunkZ * 16 + Z,
                                frequency2, aplitude2) * 7D + p100);

                        for (int y = currentHeight; y > p100 - (generatorY.noise(
                                chunkX * 16 + X,
                                chunkZ * 16 + Z,
                                frequency2, aplitude2) * 7D - 13D); y--) {

                            if (generatorY.noise(
                                    chunkX * 16 + X,
                                    chunkZ * 16 + Z,
                                    frequency2, aplitude2) > spawnrate2) {

                                if (y == currentHeight) {
                                    if (world.getBiome(
                                            chunkX * 16 + X, chunkZ * 16 + Z).equals(Biome.DESERT)
                                            || world.getBiome(chunkX * 16 + X, chunkZ * 16 + Z).equals(Biome.DESERT_HILLS)
                                            || world.getBiome(chunkX * 16 + X, chunkZ * 16 + Z).equals(Biome.DESERT_MOUNTAINS)) {
                                        chunk.setBlock(X, y, Z, Material.SAND);
                                    } else {
                                        chunk.setBlock(X, y, Z, Material.GRASS);
                                    }
                                } else {
                                    if (y == currentHeight - 1) {
                                        chunk.setBlock(X, y, Z, Material.DIRT);

                                    } else {
                                        chunk.setBlock(X, y, Z, new Random().nextBoolean() ? Material.COBBLESTONE : Material.STONE);
                                    }
                                }
                            }
                        }

                        if (aplitudeether2 == true) {
                            currentHeight = (int) (generatorY.noise(chunkX * 16 + X, chunkZ * 16 + Z, frequency2, aplitude2) * 7D + p100);
                            for (int y = currentHeight; y > p100 - (generatorY.noise(chunkX * 16 + X, chunkZ * 16 + Z, frequency2, aplitude2) * 7D - 13D); y--) {
                                if (generatorY.noise(chunkX * 16 + X, chunkZ * 16 + Z, frequency2, aplitude2) > spawnrate2) {
                                    if (y == currentHeight) {
                                        if (world.getBiome(chunkX * 16 + X, chunkZ * 16 + Z).equals(Biome.DESERT) || world.getBiome(chunkX * 16 + X, chunkZ * 16 + Z).equals(Biome.DESERT_HILLS) || world.getBiome(chunkX * 16 + X, chunkZ * 16 + Z).equals(Biome.DESERT_MOUNTAINS)) {
                                            chunk.setBlock(X, y, Z, Material.SAND);
                                        } else {
                                            chunk.setBlock(X, y, Z, Material.GRASS);
                                        }
                                    } else {
                                        if (y == currentHeight - 1) {
                                            chunk.setBlock(X, y, Z, Material.DIRT);

                                        } else {
                                            chunk.setBlock(X, y, Z, new Random().nextBoolean() ? Material.COBBLESTONE : Material.STONE);
                                        }
                                    }
                                }
                            }
                        }
                    }

                        //Generate bedrock
                        generateBedrock(world, random, x, z, chunkData);

                        // Generate vanilla surface terrain
                        if (IridiumSkyblock.getInstance().getGenerators().generateSurface) {
                            generateSurface(world, random, x, z, chunkData);

                        } else {
                            // Generate stone layer
                            for (int y = LocationUtils.getMinHeight(world) + 1; y < currentFloorHeight - 4; y++) {
                                chunkData.setBlock(x, y, z,
                                        Objects.requireNonNull(getSkylandsGenerator(world.getEnvironment()).mantle.get())
                                );
                            }

                            // Generate gravel layer
                            for (int y = LocationUtils.getMinHeight(world) + 1; y < currentFloorHeight; y++) {
                                chunkData.setBlock(x, y, z,
                                        Objects.requireNonNull(getSkylandsGenerator(world.getEnvironment()).underFloor.get())
                                );
                            }

                            // Generate sand on top of gravel
                            chunkData.setBlock(x, currentFloorHeight, z,
                                    Objects.requireNonNull(getSkylandsGenerator(world.getEnvironment()).floor.get())
                            );

                            // Generate water or lava on top of the floor
                            for (int y = currentFloorHeight + 1; y <= getSkylandsGenerator(world.getEnvironment()).liquidHeight; y++) {
                                chunkData.setBlock(x, y, z, Objects.requireNonNull(
                                        getSkylandsGenerator(world.getEnvironment()).liquidType.get()));
                            }
                        }

                        if (!IridiumSkyblock.getInstance().getGenerators().biomeGradient) {
                            biomeGrid.setBiome(x, z, Objects.requireNonNull(biomeList.get(random.nextInt(biomeList.size()))));
                        }
                    }
                }

                return chunkData;
            }
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(World world) {
        if(IridiumSkyblock.getInstance().getGenerators().useLegacyPopulators) {
            return IridiumSkyblock.getInstance().getBlockPopulatorList().get(world.getEnvironment());
        }
        return super.getDefaultPopulators(world);
    }

    @Override
    public boolean shouldGenerateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z) {
        return getSkylandsGenerator(worldInfo.getEnvironment()).spawnCaves;
    }

    @Override
    public boolean shouldGenerateDecorations(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z) {
        return getSkylandsGenerator(worldInfo.getEnvironment()).decorate;
    }

    @Override
    public boolean shouldGenerateMobs(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z) {
        return getSkylandsGenerator(worldInfo.getEnvironment()).spawnEntities;
    }

    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        return getSkylandsGenerator(world.getEnvironment()).spawnEntities;
    }

    private Generators.SkylandsGeneratorWorld getSkylandsGenerator(World.Environment environment) {
        switch (environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().skylandsGenerator.nether;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().skylandsGenerator.end;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().skylandsGenerator.overworld;
            }
        }
    }
}


// Modified version of AetherGen from
// https://github.com/LoganTheOverlord/AetherGen/blob/master/src/me/Logaaan/wg/CustomGen.java