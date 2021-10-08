package com.iridium.iridiumskyblock.settings;

import com.iridium.iridiumcore.Item;

public class IslandMobSpawn extends IslandSettingImpl {

    public IslandMobSpawn(Item item, String defaultValue, boolean enabled, boolean changeable) {
        super(item, defaultValue, enabled, changeable);
    }

    public enum MobSpawnTypes {ALL, ANIMALS, MONSTERS, NOTHING}

    @Override
    public MobSpawnTypes getNext(String type) {
        MobSpawnTypes mobSpawnType = getByName(type);
        return MobSpawnTypes.values().length > mobSpawnType.ordinal() + 1 ? MobSpawnTypes.values()[mobSpawnType.ordinal() + 1] : MobSpawnTypes.values()[0];
    }

    @Override
    public MobSpawnTypes getPrevious(String type) {
        MobSpawnTypes mobSpawnType = getByName(type);
        return mobSpawnType.ordinal() - 1 != -1 ? MobSpawnTypes.values()[mobSpawnType.ordinal() - 1] : MobSpawnTypes.values()[3];
    }

    @Override
    public MobSpawnTypes getByName(String name) {
        try {
            return MobSpawnTypes.valueOf(name);
        } catch (UnsupportedOperationException ignored) {
            return MobSpawnTypes.ALL;
        }
    }
}
