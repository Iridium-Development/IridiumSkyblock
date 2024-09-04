package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumteams.Permission;

import java.util.Arrays;

public class Permissions extends com.iridium.iridiumteams.configs.Permissions {
    public Permission border = new Permission(new Item(XMaterial.BEACON, 33, 1, "&9Island Border", Arrays.asList("&7Grant the ability to Change the Island border.", "", "&9&lPermission", "%permission%")), 1, 1);
    public Permission regen = new Permission(new Item(XMaterial.TNT, 34, 1, "&9Regenerate Island", Arrays.asList("&7Grant the ability to regenerate your Island.", "", "&9&lPermission", "%permission%")), 1, 3);

    public Permissions() {
        super("Island", "&9");
    }
}
