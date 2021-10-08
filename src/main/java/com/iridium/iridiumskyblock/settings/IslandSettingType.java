package com.iridium.iridiumskyblock.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IslandSettingType {

    MOB_SPAWN("mob_spawn"),
    ISLAND_TIME("time"),
    ISLAND_WEATHER("weather"),
    LEAF_DECAY("leaf_decay"),
    ENDERMAN_GRIEF("enderman_grief"),
    LIQUID_FLOW("liquid_flow"),
    TNT_DAMAGE("tnt_damage"),
    FIRE_SPREAD("fire_spread");

    public final String settingKey;

}
