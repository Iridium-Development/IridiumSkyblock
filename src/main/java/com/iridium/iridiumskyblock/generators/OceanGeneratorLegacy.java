package com.iridium.iridiumskyblock.generators;

import com.cryptomorin.xseries.XMaterial;
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

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class OceanGeneratorLegacy extends IridiumChunkGenerator {

    public OceanGeneratorLegacy(String name, boolean generatesTerrain, boolean lowerHorizon) {
        super(name, generatesTerrain, lowerHorizon);
    }

    SkyblockBiomeProvider biomeProvider = new SkyblockBiomeProvider();

    @Override
    public @NotNull ChunkData generateChunkData(
            @NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {

        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()),
                IridiumSkyblock.getInstance().getGenerators().simplexTerrainOctave);
        generator.setScale(IridiumSkyblock.getInstance().getGenerators().simplexTerrainScale);

        final ChunkData chunkData = createChunkData(world);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int currentFloorHeight = (int) ((generator.noise(
                        chunkX * 16 + x, chunkZ * 16 + z, 1.5D, 0.5D, true) + 1)
                        * (getOceanGenerator(world.getEnvironment()).maxFloorHeight - getOceanGenerator(world.getEnvironment()).minFloorHeight)
                        + getOceanGenerator(world.getEnvironment()).minFloorHeight);

                // Generate layer of bedrock
                chunkData.setBlock(x, LocationUtils.getMinHeight(world), z,
                        Objects.requireNonNull(XMaterial.BEDROCK.get())
                );

                // Generate stone layer
                for (int y = LocationUtils.getMinHeight(world) + 1; y < currentFloorHeight - 5; y++) {
                    chunkData.setBlock(x, y, z,
                            Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).mantle.get())
                    );
                }

                // Generate gravel layer
                for (int y = currentFloorHeight - 5; y < currentFloorHeight; y++) {
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

                if(!IridiumSkyblock.getInstance().getGenerators().biomeGradient) {
                    biomeGrid.setBiome(x, z, Objects.requireNonNull(biomeProvider.biomeList.get(random.nextInt(biomeProvider.biomeList.size()))));
                } else {
                    biomeGrid.setBiome(x, z, Objects.requireNonNull(biomeProvider.getBiome(world, x, 0 ,z)));
                }
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
        if (world.getBlockAt(x, minHeightWorld, z).getType() != XMaterial.BEDROCK.get()) {
            if (world.getBlockAt(x, minHeightWorld, z).getState() instanceof InventoryHolder) {
                ((InventoryHolder) world.getBlockAt(x, minHeightWorld, z).getState()).getInventory().clear();
            }
            world.getBlockAt(x, minHeightWorld, z).setType(Material.BEDROCK, false);
        }

        // Generate stone layer
        for (int y = minHeightWorld + 1; y < currentFloorHeight - 5; y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != getOceanGenerator(world.getEnvironment()).mantle.get()
                    && getOceanGenerator(world.getEnvironment()).mantle.get() != null) {

                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(Objects.requireNonNull(getOceanGenerator(world.getEnvironment()).mantle.get()), false);
            }
        }

        // Generate gravel on top of stone
        for (int y = currentFloorHeight -5; y < currentFloorHeight; y++) {
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

            for(int y = currentFloorHeight; y < currentFloorHeight + 5; y++) {
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
                block.setType(getOceanGenerator(world.getEnvironment()).liquidType.get(), false);
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

        // Generate caves
        if (getOceanGenerator(world.getEnvironment()).spawnCaves) {

        }

        // Generate lakes, trees, grass, mineral deposits, etc.
        if (getOceanGenerator(world.getEnvironment()).decorate) {

        }

        // Spawn mobs
        if (getOceanGenerator(world.getEnvironment()).spawnEntities) {

        }
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(World world) {
        if(IridiumSkyblock.getInstance().getBlockPopulatorList().get(world.getEnvironment()) == null) {
            return super.getDefaultPopulators(world);
        }
        return IridiumSkyblock.getInstance().getBlockPopulatorList().get(world.getEnvironment());
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