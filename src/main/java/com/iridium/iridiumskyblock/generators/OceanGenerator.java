package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
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
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class OceanGenerator extends ChunkGenerator {

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        final ChunkData chunkData = createChunkData(world);
        generator.setScale(0.005D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int currentFloorHeight = (int) ((generator.noise(chunkX * 16 + x, chunkZ * 16 + z, 1.5D, 0.5D, true) + 1) * (getMaxFloorHeight(world.getEnvironment()) - getMinFloorHeight(world.getEnvironment())) + getMinFloorHeight(world.getEnvironment()));

                // Generate layer of bedrock
                chunkData.setBlock(x, LocationUtils.getMinHeight(world), z,
                        Objects.requireNonNull(XMaterial.BEDROCK.parseMaterial())
                );

                // Generate gravel layer
                for (int y = LocationUtils.getMinHeight(world) + 1; y < currentFloorHeight; y++) {
                    chunkData.setBlock(x, y, z,
                            Objects.requireNonNull(getUnderFloor(world.getEnvironment()).parseMaterial())
                    );
                }

                // Generate sand on top of gravel
                chunkData.setBlock(x, currentFloorHeight, z,
                        Objects.requireNonNull(getFloor(world.getEnvironment()).parseMaterial())
                );

                // Generate water or lava on top of the floor
                for (int y = currentFloorHeight + 1; y <= getliquidHeight(world.getEnvironment()); y++) {
                    chunkData.setBlock(x, y, z, Objects.requireNonNull(getLiquidType(world.getEnvironment()).parseMaterial()));
                }
            }
        }

        return chunkData;
    }

    public void generateWater(World world, int x, int z) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        generator.setScale(0.005D);

        int currentFloorHeight = (int) ((generator.noise(x, z, 1.5D, 0.5D, true) + 1) * (getMaxFloorHeight(world.getEnvironment()) - getMinFloorHeight(world.getEnvironment())) + getMinFloorHeight(world.getEnvironment()));
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
            if (block.getType() != getUnderFloor(world.getEnvironment()).parseMaterial() && getUnderFloor(world.getEnvironment()).parseMaterial() != null) {
                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(getUnderFloor(world.getEnvironment()).parseMaterial(), false);
            }
        }

        // Generate sand on top of gravel
        if (world.getBlockAt(x, currentFloorHeight, z).getType() != getFloor(world.getEnvironment()).parseMaterial() && getFloor(world.getEnvironment()).parseMaterial() != null) {
            if (world.getBlockAt(x, currentFloorHeight, z).getState() instanceof InventoryHolder) {
                ((InventoryHolder) world.getBlockAt(x, currentFloorHeight, z).getState()).getInventory().clear();
            }
            world.getBlockAt(x, currentFloorHeight, z).setType(getFloor(world.getEnvironment()).parseMaterial(), false);
        }

        // Generate water or lava on top of the floor
        XMaterial oceanMaterial = world.getEnvironment() == Environment.NETHER ? XMaterial.LAVA : XMaterial.WATER;
        for (int y = currentFloorHeight + 1; y <= getliquidHeight(world.getEnvironment()); y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != oceanMaterial.parseMaterial() && oceanMaterial.parseMaterial() != null) {
                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(oceanMaterial.parseMaterial(), false);
            }
        }

        // Replace everything else with air
        for (int y = getliquidHeight(world.getEnvironment()) + 1; y < world.getMaxHeight(); y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != Material.AIR) {
                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(Material.AIR, false);
            }
        }
    }

    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        return true;
    }

    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return null;
    }

    public XMaterial getFloor(Environment environment) {
        switch(environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.nether.floor;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.end.floor;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.overworld.floor;
            }
        }
    }

    public XMaterial getUnderFloor(Environment environment) {
        switch(environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.nether.underFloor;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.end.underFloor;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.overworld.underFloor;
            }
        }
    }

    public XMaterial getLiquidType(Environment environment) {
        switch(environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.nether.liquidType;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.end.liquidType;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.overworld.liquidType;
            }
        }
    }

    public int getliquidHeight (Environment environment) {
        switch(environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.nether.liquidHeight;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.end.liquidHeight;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.overworld.liquidHeight;
            }
        }
    }

    public int getMinFloorHeight(Environment environment) {
        switch(environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.nether.minFloorHeight;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.end.minFloorHeight;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.overworld.minFloorHeight;
            }
        }
    }

    public int getMaxFloorHeight(Environment environment) {
        switch(environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.nether.maxFloorHeight;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.end.maxFloorHeight;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.overworld.maxFloorHeight;
            }
        }
    }

    public boolean getCanSpawnEntities(Environment environment) {
        switch(environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.nether.canSpawnEntities;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.end.canSpawnEntities;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.overworld.canSpawnEntities;
            }
        }
    }

}