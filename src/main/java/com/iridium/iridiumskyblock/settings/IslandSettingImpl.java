package com.iridium.iridiumskyblock.settings;

import com.iridium.iridiumcore.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a setting in the Island settings system.
 * Serialized in the Configuration files.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class IslandSettingImpl {
    /**
     * The Item used to display the item
     */
    private Item item;
    /**
     * The default value of this Setting
     */
    private String defaultValue;
    /**
     * The setting is enabled or not
     */
    private boolean enabled;
    /**
     * The setting is changeable on gui or not
     */
    private boolean changeable;

    public abstract Enum<?> getNext(String value);

    public abstract Enum<?> getPrevious(String value);

    public abstract Enum<?> getByName(String value);

}
