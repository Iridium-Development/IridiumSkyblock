package com.iridium.iridiumskyblock.schematics;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.jnbt.*;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Schematic implements SchematicPaster {

    private static final Map<File, SchematicData> schematicCache = new HashMap<>();

    @Override
    public void paste(File file, Location location, Boolean ignoreAirBlock, CompletableFuture<Void> completableFuture) {
        SchematicData schematicData;
        try {
            schematicData = schematicCache.getOrDefault(file, null);
            if (schematicData==null)
                schematicData = SchematicData.loadSchematic(file);
            schematicCache.put(file, schematicData);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        short length = schematicData.length;
        short width = schematicData.width;
        short height = schematicData.height;
        location.subtract(width / 2.00, height / 2.00, length / 2.00); // Centers the schematic

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    Block block = new Location(location.getWorld(), x + location.getX(), y + location.getY(), z + location.getZ()).getBlock();
                    for (String blockData : schematicData.palette.keySet()) {
                        int i = SchematicData.getChildTag(schematicData.palette, blockData, IntTag.class).getValue();
                        if (schematicData.blockdata[index] == i) {
                            block.setBlockData(Bukkit.createBlockData(blockData), false);
                        }
                    }
                }
            }
        }

        for (Tag tag : schematicData.tileEntities) {
            if (!(tag instanceof CompoundTag))
                continue;
            CompoundTag t = (CompoundTag) tag;
            Map<String, Tag> tags = t.getValue();

            int[] pos = SchematicData.getChildTag(tags, "Pos", IntArrayTag.class).getValue();

            int x = pos[0];
            int y = pos[1];
            int z = pos[2];

            Block block = new Location(location.getWorld(), x + location.getX(), y + location.getY(), z + location.getZ()).getBlock();
            String id = SchematicData.getChildTag(tags, "Id", StringTag.class).getValue().toLowerCase().replace("minecraft:", "");
            if (id.equalsIgnoreCase("chest")) {
                List<Tag> items = SchematicData.getChildTag(tags, "Items", ListTag.class).getValue();
                if (block.getState() instanceof Chest) {
                    Chest chest = (Chest) block.getState();
                    for (Tag item : items) {
                        if (!(item instanceof CompoundTag))
                            continue;
                        Map<String, Tag> itemtag = ((CompoundTag) item).getValue();
                        byte slot = SchematicData.getChildTag(itemtag, "Slot", ByteTag.class).getValue();
                        String name = (SchematicData.getChildTag(itemtag, "id", StringTag.class).getValue()).toLowerCase().replace("minecraft:", "").replace("reeds", "sugar_cane");
                        Byte amount = SchematicData.getChildTag(itemtag, "Count", ByteTag.class).getValue();
                        Optional<XMaterial> optionalXMaterial = XMaterial.matchXMaterial(name.toUpperCase());
                        if (optionalXMaterial.isPresent()) {
                            XMaterial material = optionalXMaterial.get();
                            ItemStack itemStack = material.parseItem();
                            if (itemStack != null) {
                                itemStack.setAmount(amount);
                                chest.getBlockInventory().setItem(slot, itemStack);
                            }
                        }
                    }
                }
            }
        }
        completableFuture.complete(null);
    }

    @Override
    public void clearCache() {
        schematicCache.clear();
    }

}
