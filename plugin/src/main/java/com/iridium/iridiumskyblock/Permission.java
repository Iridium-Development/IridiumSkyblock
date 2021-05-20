package com.iridium.iridiumskyblock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a permission in the Island permissions system.
 * Serialized in the Configuration files.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    /**
     * The Item used to display the item
     */
    private Item item;
    /**
     * The default rank of the permission.
     * All Ranks lower than this are denied this permission and all Ranks higher or equal are allowed
     */
    private IslandRank defaultRank;

}
