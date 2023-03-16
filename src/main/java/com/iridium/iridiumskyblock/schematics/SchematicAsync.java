package com.iridium.iridiumskyblock.schematics;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jnbt.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SchematicAsync implements SchematicPaster {

    private static final Map<File, SchematicData> schematicCache = new HashMap<>();

    @Override
    public void paste(File file, Location location, Boolean ignoreAirBlock, CompletableFuture<Void> completableFuture) {
        SchematicData schematicData;
        try {
            schematicData = schematicCache.getOrDefault(file, null);
            if (schematicData == null)
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

        final SchematicData schematicDataF = schematicData;
        CompletableFuture<Void> cf = new CompletableFuture<>();
        final int delay = IridiumSkyblock.getInstance().getConfiguration().pasterDelayInTick;
        (new BukkitRunnable() {
            int blockdataindex = 0;
            List<String> blockdatas = schematicDataF.palette.keySet().stream().collect(Collectors.toList());

            @Override
            public void run() {
                int remaining = IridiumSkyblock.getInstance().getConfiguration().pasterLimitPerTick;

                while (remaining > 0 && blockdataindex < blockdatas.size()) {
                    String blockData = blockdatas.get(blockdataindex);
                    int i = SchematicData.getChildTag(schematicDataF.palette, blockData, IntTag.class).getValue();
                    for (int x = 0; x < width; ++x) {
                        for (int y = 0; y < height; ++y) {
                            for (int z = 0; z < length; ++z) {
                                int index = y * width * length + z * width + x;
                                if (schematicDataF.blockdata[index] == i) {
                                    {
                                        Block block = new Location(location.getWorld(), x + location.getX(),
                                                y + location.getY(),
                                                z + location.getZ()).getBlock();
                                        block.setBlockData(Bukkit.createBlockData(blockData), false);
                                    }
                                }
                            }
                        }
                    }
                    blockdataindex++;
                    remaining--;
                }
                if (blockdataindex == blockdatas.size()) {
                    cf.complete(null);
                }
            }
        }).runTaskTimer(IridiumSkyblock.getInstance(), delay, delay);

        cf.thenAccept(Void->{
            for (Tag tag : schematicDataF.tileEntities) {
                if (!(tag instanceof CompoundTag))
                    continue;
                CompoundTag t = (CompoundTag) tag;
                Map<String, Tag> tags = t.getValue();
    
                int[] pos = SchematicData.getChildTag(tags, "Pos", IntArrayTag.class).getValue();
    
                int x = pos[0];
                int y = pos[1];
                int z = pos[2];
    
                Block block = new Location(location.getWorld(), x + location.getX(), y + location.getY(),
                        z + location.getZ()).getBlock();
                String id = SchematicData.getChildTag(tags, "Id", StringTag.class).getValue().toLowerCase()
                        .replace("minecraft:", "");
                if (id.equalsIgnoreCase("chest")) {
                    List<Tag> items = SchematicData.getChildTag(tags, "Items", ListTag.class).getValue();
                    if (block.getState() instanceof Chest) {
                        Chest chest = (Chest) block.getState();
                        for (Tag item : items) {
                            if (!(item instanceof CompoundTag))
                                continue;
                            Map<String, Tag> itemtag = ((CompoundTag) item).getValue();
                            byte slot = SchematicData.getChildTag(itemtag, "Slot", ByteTag.class).getValue();
                            String name = (SchematicData.getChildTag(itemtag, "id", StringTag.class).getValue())
                                    .toLowerCase().replace("minecraft:", "").replace("reeds", "sugar_cane");
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
        });
    }

    @Override
    public void clearCache() {
        schematicCache.clear();
    }

}
