package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.database.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.WeatherType;

@AllArgsConstructor
@Getter
public enum SettingType {

    MOB_SPAWN("mob_spawn", IridiumSkyblock.getInstance().getIslandSettings().mobSpawn.getDefaultValue(),
            IridiumSkyblock.getInstance().getIslandSettings().mobSpawn.isFeactureEnabled()),
    LEAF_DECAY("leaf_decay", IridiumSkyblock.getInstance().getIslandSettings().leafDecay.getDefaultValue(),
            IridiumSkyblock.getInstance().getIslandSettings().leafDecay.isFeactureEnabled()),
    WEATHER("weather", IridiumSkyblock.getInstance().getIslandSettings().weather.getDefaultValue(),
            IridiumSkyblock.getInstance().getIslandSettings().weather.isFeactureEnabled(),
            IslandWeatherType::getNext, IslandWeatherType::getPrevious, (island, newValue) -> {
        IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(island).stream().map(User::getPlayer).forEach(player -> {
            IslandWeatherType islandWeatherType = IslandWeatherType.valueOf(newValue);
            if (islandWeatherType == IslandWeatherType.DEFAULT) {
                player.resetPlayerWeather();
            } else {
                player.setPlayerWeather(islandWeatherType == IslandWeatherType.CLEAR ? WeatherType.CLEAR : WeatherType.DOWNFALL);
            }
        });
    }),
    TIME("time", IridiumSkyblock.getInstance().getIslandSettings().time.getDefaultValue(),
            IridiumSkyblock.getInstance().getIslandSettings().time.isFeactureEnabled(),
            IslandTime::getNext, IslandTime::getPrevious, (island, newValue) -> {
        IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(island).stream().map(User::getPlayer).forEach(player -> {
            IslandTime islandTime = IslandTime.valueOf(newValue);
            player.setPlayerTime(islandTime.getTime(), islandTime.isRelative());
        });
    }),
    ENDERMAN_GRIEF("enderman_grief", IridiumSkyblock.getInstance().getIslandSettings().endermanGrief.getDefaultValue(),
            IridiumSkyblock.getInstance().getIslandSettings().endermanGrief.isFeactureEnabled()),
    LIQUID_FLOW("liquid_flow", IridiumSkyblock.getInstance().getIslandSettings().liquidFlow.getDefaultValue(),
            IridiumSkyblock.getInstance().getIslandSettings().liquidFlow.isFeactureEnabled()),
    TNT_DAMAGE("tnt_damage", IridiumSkyblock.getInstance().getIslandSettings().tntDamage.getDefaultValue(),
            IridiumSkyblock.getInstance().getIslandSettings().tntDamage.isFeactureEnabled()),
    FIRE_SPREAD("fire_spread", IridiumSkyblock.getInstance().getIslandSettings().fireSpread.getDefaultValue(),
            IridiumSkyblock.getInstance().getIslandSettings().fireSpread.isFeactureEnabled());

    private final String settingName;
    private final String defaultValue;
    private final boolean feactureValue;
    private final NewValue next;
    private final NewValue previous;
    private final SettingValueChange onChange;

    SettingType(String settingName, String defaultValue, boolean feactureEnabled) {
        this.settingName = settingName;
        this.defaultValue = defaultValue;
        this.feactureValue = feactureEnabled;
        this.next = current -> current.equalsIgnoreCase("true") ? "false" : "true";
        this.previous = current -> current.equalsIgnoreCase("true") ? "false" : "true";
        this.onChange = (island, newValue) -> {};

    }

    public static SettingType getByName(String name) {
        for (SettingType settingType : values()) {
            if (settingType.settingName.equalsIgnoreCase(name)) return settingType;
        }
        return SettingType.MOB_SPAWN;
    }

    public interface NewValue {
        String getNew(String current);
    }
}