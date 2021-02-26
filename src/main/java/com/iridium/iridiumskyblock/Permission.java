package com.iridium.iridiumskyblock;

import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Permission(String name, Item item, IslandRank defaultRank) {
        this.name = name;
        this.item = item;
        this.defaultRank = defaultRank;
    }
}
