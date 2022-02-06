package com.iridium.iridiumskyblock;

import com.iridium.iridiumcore.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a permission in the Island permissions system.
 * Serialized in the Configuration files.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    private boolean modifiable = true;
    /**
     * The Item used to display the item
     */
    private Item item;
    /**
     * The page the item will be on
     */
    private int page;
    /**
     * The default rank of the permission.
     * All Ranks lower than this are denied this permission and all Ranks higher or equal are allowed
     */
    private IslandRank defaultRank;

    public Permission(Item item, IslandRank defaultRank) {
        this.item = item;
        this.defaultRank = defaultRank;
        this.page = 1;
    }

}
