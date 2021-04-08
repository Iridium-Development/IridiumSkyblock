package com.iridium.iridiumskyblock;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a permission in the Island permissions system.
 * Serialized in the Configuration files.
 */
@Getter
@NoArgsConstructor
public class Permission {

    /**
     * The name of the permission used for storage purposes
     */
    private String name;
    /**
     * The Item used to display the item
     */
    private Item item;
    /**
     * The default rank of the permission.
     * All Ranks lower than this are denied this permission and all Ranks higher or equal are allowed
     */
    private IslandRank defaultRank;

    /**
     * The default constructor.
     *
     * @param name        The internal name of this permission
     * @param item        The item which should represents this mission
     * @param defaultRank The lowest Island rank which should have this permission by default
     */
    public Permission(String name, Item item, IslandRank defaultRank) {
        this.name = name;
        this.item = item;
        this.defaultRank = defaultRank;
    }

}
