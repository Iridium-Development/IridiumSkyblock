package com.iridium.iridiumskyblock.settings;

import com.iridium.iridiumcore.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class IslandSwitchSetting extends IslandSettingImpl {

    public IslandSwitchSetting(Item item, String defaultValue, boolean enabled, boolean changeable) {
        super(item, defaultValue, enabled, changeable);
    }

    public enum SwitchTypes {ALLOWED, DISALLOWED}

    @Override
    public SwitchTypes getNext(String type) {
        SwitchTypes switchType = getByName(type);
        return switchType.equals(SwitchTypes.ALLOWED) ? SwitchTypes.DISALLOWED : SwitchTypes.ALLOWED;
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
