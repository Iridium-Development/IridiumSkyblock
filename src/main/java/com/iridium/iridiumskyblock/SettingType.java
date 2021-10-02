package com.iridium.iridiumskyblock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SettingType {

    MOB_SPAWN("mob_spawn", IridiumSkyblock.getInstance().getIslandSettings().mobSpawn.getDefaultValue()),
    LEAF_DECAY("leaf_decay", IridiumSkyblock.getInstance().getIslandSettings().leafDecay.getDefaultValue()),
    WEATHER("weather", IridiumSkyblock.getInstance().getIslandSettings().weather.getDefaultValue(), IslandWeatherType::getNext, IslandWeatherType::getPrevious),
    TIME("time", IridiumSkyblock.getInstance().getIslandSettings().time.getDefaultValue(), IslandTime::getNext, IslandTime::getPrevious),
    ENDERMAN_GRIEF("enderman_grief", IridiumSkyblock.getInstance().getIslandSettings().endermanGrief.getDefaultValue()),
    LIQUID_FLOW("liquid_flow", IridiumSkyblock.getInstance().getIslandSettings().liquidFlow.getDefaultValue()),
    TNT_DAMAGE("tnt_damage", IridiumSkyblock.getInstance().getIslandSettings().tntDamage.getDefaultValue()),
    FIRE_SPREAD("fire_spread", IridiumSkyblock.getInstance().getIslandSettings().fireSpread.getDefaultValue());

    private final String settingName;
    private final String defaultValue;
    private final NewValue next;
    private final NewValue previous;

    SettingType(String settingName, String defaultValue) {
        this.settingName = settingName;
        this.defaultValue = defaultValue;
        this.next = current -> current.equalsIgnoreCase("true") ? "false" : "true";
        this.previous = current -> current.equalsIgnoreCase("true") ? "false" : "true";
    }

    public static SettingType getByName(String name){
        for(SettingType settingType : values()){
            if(settingType.settingName.equalsIgnoreCase(name))return settingType;
        }
        return SettingType.MOB_SPAWN;
    }

    public interface NewValue {
        String getNew(String current);
    }
}
