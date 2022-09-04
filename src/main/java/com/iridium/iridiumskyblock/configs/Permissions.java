package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumteams.Permission;

import java.util.Arrays;

public class Permissions extends com.iridium.iridiumteams.configs.Permissions {
    public Permission border = new Permission(new Item(XMaterial.BEACON, 33, 1, "&bIsland Border", Arrays.asList("&7Grant the ability to Change the Island border.", "", "&b&lPermission", "%permission%")), 1, 1);

    public Permissions() {
        super("Island", "&9");
    }
}
