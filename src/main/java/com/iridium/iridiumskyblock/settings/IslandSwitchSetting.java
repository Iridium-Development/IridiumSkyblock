package com.iridium.iridiumskyblock.settings;

import com.iridium.iridiumcore.Item;

public class IslandSwitchSetting extends IslandSettingImpl {

    public IslandSwitchSetting(String defaultValue, boolean enabled, boolean changeable, Item item) {
        super(defaultValue, enabled, changeable, item);
    }

    public enum SwitchTypes {ALLOWED, DISALLOWED}

    @Override
    public SwitchTypes getNext(String type) {
        SwitchTypes mobSpawnType = getByName(type);
        return mobSpawnType.equals(SwitchTypes.ALLOWED) ? SwitchTypes.DISALLOWED : SwitchTypes.ALLOWED;
    }

    @Override
    public SwitchTypes getPrevious(String type) {
        return getNext(type);
    }

    @Override
    public SwitchTypes getByName(String name) {
        try {
            return SwitchTypes.valueOf(name);
        } catch (UnsupportedOperationException ignored) {
            return SwitchTypes.ALLOWED;
        }
    }

}
