package com.iridium.iridiumskyblock.schematics;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jnbt.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SchematicAsync implements SchematicPaster {

    private static final Map<File, SchematicData> schematicCache = new HashMap<>();

    @Override
    public void paste(File file, Location location, Boolean ignoreAirBlock, CompletableFuture<Void> completableFuture) {
        SchematicData schematicData = getSchematicData(file);
        ListIterator<Coordinate> coordinates = getCoordinates(schematicData);
        int delay = IridiumSkyblock.getInstance().getConfiguration().pasterDelayInTick;

        short length = schematicData.length;
        short width = schematicData.width;
        short height = schematicData.height;

        int newLength = (int) (length / 2.00);
        int newWidth = (int) (width / 2.00);
        int newHeight = (int) (height / 2.00);

        location.subtract(newWidth, newHeight, newLength); //Center the schematic (for real this time)

        BukkitRunnable runnable = new BukkitRunnable() {

            @Override
            public void run() {
                int remaining = IridiumSkyblock.getInstance().getConfiguration().pasterLimitPerTick;

                while (remaining != 0 && coordinates.hasNext()) {
                    Coordinate coordinate = coordinates.next();
                    int x = coordinate.x;
                    int y = coordinate.y;
                    int z = coordinate.z;

                    int index = y * width * length + z * width + x;
                    Block block = new Location(location.getWorld(), x + location.getX(), y + location.getY(), z + location.getZ()).getBlock();
                    for (String blockData : schematicData.palette.keySet()) {
                        int i = SchematicData.getChildTag(schematicData.palette, blockData, IntTag.class).getValue();
                        if (schematicData.blockdata[index] == i) {
                            BlockData data = Bukkit.createBlockData(blockData);
                            if (data.getMaterial() == Material.AIR && ignoreAirBlock) continue;
                            block.setBlockData(data, false);
                            remaining--;
                        }
                    }
                }
                if (!coordinates.hasNext()) {
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
                    this.cancel();
                }
            }
        };

        runnable.runTaskTimer(IridiumSkyblock.getInstance(), delay, delay);
    }

    private SchematicData getSchematicData(File file) {
        try {
            SchematicData schematicData = schematicCache.getOrDefault(file, null);
            if (schematicData == null) {
                schematicData = SchematicData.loadSchematic(file);
            }
            schematicCache.put(file, schematicData);
            return schematicData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ListIterator<Coordinate> getCoordinates(SchematicData schematicData) {
        short length = schematicData.length;
        short width = schematicData.width;
        short height = schematicData.height;

        List<Coordinate> coordinates = new ArrayList<>();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                for (int z = 0; z < length; ++z) {
                    coordinates.add(new Coordinate(x, y, z));
                }
            }
        }

        return coordinates.listIterator();
    }

    @Override
    public void clearCache() {
        schematicCache.clear();
    }

}
