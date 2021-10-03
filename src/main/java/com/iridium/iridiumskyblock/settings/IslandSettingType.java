package com.iridium.iridiumskyblock.settings;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IslandSettingType {

    MOB_SPAWN("mob_spawn", IridiumSkyblock.getInstance().getIslandSettings().mobSpawn.getDefaultValue()),
    LEAF_DECAY("leaf_decay", IridiumSkyblock.getInstance().getIslandSettings().leafDecay.getDefaultValue()),
    WEATHER("weather", IridiumSkyblock.getInstance().getIslandSettings().weather.getDefaultValue()),
    TIME("time", IridiumSkyblock.getInstance().getIslandSettings().time.getDefaultValue()),
    ENDERMAN_GRIEF("enderman_grief", IridiumSkyblock.getInstance().getIslandSettings().endermanGrief.getDefaultValue()),
    LIQUID_FLOW("liquid_flow", IridiumSkyblock.getInstance().getIslandSettings().liquidFlow.getDefaultValue()),
    TNT_DAMAGE("tnt_damage", IridiumSkyblock.getInstance().getIslandSettings().tntDamage.getDefaultValue()),
    FIRE_SPREAD("fire_spread", IridiumSkyblock.getInstance().getIslandSettings().fireSpread.getDefaultValue());

    private final String settingName;
    private final String defaultValue;

    public static IslandSettingType getByName(String name) {
        for (IslandSettingType islandSettingType : values()) {
            if (islandSettingType.settingName.equalsIgnoreCase(name)) return islandSettingType;
        }
        return IslandSettingType.MOB_SPAWN;
    }

}
