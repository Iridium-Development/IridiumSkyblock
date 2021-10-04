package com.iridium.iridiumskyblock.schematics;

import lombok.AllArgsConstructor;
import org.jnbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class SchematicData {
    public final short width;
    public final short length;
    public final short height;
    public List<Tag> tileEntities;
    public byte[] blockdata;
    public Map<String, Tag> palette;
    public Integer version;

    public static SchematicData loadSchematic(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        NBTInputStream nbtStream = new NBTInputStream(stream);

        CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
        stream.close();
        nbtStream.close();
        Map<String, Tag> schematic = schematicTag.getValue();

        short width = getChildTag(schematic, "Width", ShortTag.class).getValue();
        short length = getChildTag(schematic, "Length", ShortTag.class).getValue();
        short height = getChildTag(schematic, "Height", ShortTag.class).getValue();
        int version = getChildTag(schematic, "Version", IntTag.class).getValue();
        Map<String, Tag> palette = getChildTag(schematic, "Palette", CompoundTag.class).getValue();
        byte[] blockdata = getChildTag(schematic, "BlockData", ByteArrayTag.class).getValue();
        if (version == 1) {
            List<Tag> tileEntities = getChildTag(schematic, "TileEntities", ListTag.class).getValue();
            return new SchematicData(width, length, height, tileEntities, blockdata, palette, version);
        } else if (version == 2) {
            List<Tag> BlockEntities = getChildTag(schematic, "BlockEntities", ListTag.class).getValue();
            return new SchematicData(width, length, height, BlockEntities, blockdata, palette, version);
        } else {
            return new SchematicData(width, length, height, Collections.emptyList(), blockdata, palette, version);
        }
    }

    public static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws
            IllegalArgumentException {
        if (!items.containsKey(key)) {
            throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }
}
