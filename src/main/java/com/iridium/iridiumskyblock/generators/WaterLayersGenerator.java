package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;

public class WaterLayersGenerator {

    static void generateGravelLayer(World world, int x, int z, XMaterial bottomMaterial, int currentFloorHeight, int minHeightWorld) {
        for (int y = minHeightWorld + 1; y < currentFloorHeight; y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != bottomMaterial.parseMaterial() && bottomMaterial.parseMaterial() != null) {
                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(bottomMaterial.parseMaterial(), false);
            }
        }
    }

    static void generateBedrockLayer(World world, int x, int z, int minHeightWorld) {
        if (world.getBlockAt(x, minHeightWorld, z).getType() != XMaterial.BEDROCK.parseMaterial()) {
            if (world.getBlockAt(x, minHeightWorld, z).getState() instanceof InventoryHolder) {
                ((InventoryHolder) world.getBlockAt(x, minHeightWorld, z).getState()).getInventory().clear();
            }
            world.getBlockAt(x, minHeightWorld, z).setType(Material.BEDROCK, false);
        }
    }

    private void replaceAllwithAir(World world, int x, int z, int waterHeight) {
        for (int y = waterHeight + 1; y < world.getMaxHeight(); y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != Material.AIR) {
                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(Material.AIR, false);
            }
        }
    }

    static void generateWateronFloor(World world, int x, int z, int waterHeight, int currentFloorHeight) {
        XMaterial oceanMaterial = world.getEnvironment() == Environment.NETHER ? XMaterial.LAVA : XMaterial.WATER;
        for (int y = currentFloorHeight + 1; y <= waterHeight; y++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getType() != oceanMaterial.parseMaterial() && oceanMaterial.parseMaterial() != null) {
                if (block.getState() instanceof InventoryHolder) {
                    ((InventoryHolder) block.getState()).getInventory().clear();
                }
                block.setType(oceanMaterial.parseMaterial(), false);
            }
        }
    }

    static void generateSandonGravel(World world, int x, int z, XMaterial topMaterial, int currentFloorHeight) {
        if (world.getBlockAt(x, currentFloorHeight, z).getType() != topMaterial.parseMaterial() && topMaterial.parseMaterial() != null) {
            if (world.getBlockAt(x, currentFloorHeight, z).getState() instanceof InventoryHolder) {
                ((InventoryHolder) world.getBlockAt(x, currentFloorHeight, z).getState()).getInventory().clear();
            }
            world.getBlockAt(x, currentFloorHeight, z).setType(topMaterial.parseMaterial(), false);
        }
    }
}
