package com.iridium.iridiumskyblock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PermissionType {
    REDSTONE("redstone"),
    BLOCK_PLACE("blockPlace"),
    BLOCK_BREAK("blockBreak"),
    BUCKET("bucket"),
    DOORS("doors"),
    KILL_MOBS("killMobs"),
    OPEN_CONTAINERS("openContainers"),
    SPAWNERS("spawners"),
    CHANGE_PERMISSIONS("changePermissions"),
    KICK("kick"),
    INVITE("invite"),
    REGEN("regen"),
    PROMOTE("promote"),
    DEMOTE("demote"),
    PICKUP_ITEMS("pickupItems"),
    DROP_ITEMS("dropItems"),
    INTERACT_ENTITIES("interactEntities"),
    MANAGE_WARPS("manageWarps"),
    WITHDRAW_BANK("withdrawBank"),
    TRUST("trust"),
    BORDER("border"),
    EXPEL("expel"),
    BAN("ban"),
    UNBAN("unban"),
    DESTROY_VEHICLE("destroyVehicle"),
    TRAMPLE_CROPS("trampleCrops");

    private final String permissionKey;
}
