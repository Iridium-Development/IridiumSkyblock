package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.configs.Schematics;

public class TestingHelper {

    public static String getSchematicKey() {
        return IridiumSkyblock.getInstance().getSchematics().schematics.keySet().toArray()[0].toString();
    }

    public static Schematics.SchematicConfig getSchematicConfig() {
        return IridiumSkyblock.getInstance().getSchematics().schematics.get(getSchematicKey());
    }

}
