package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;

public class IslandRanksInventoryConfig extends NoItemGUI {
    /**
     * The item for Owner Rank
     */
    public Item owner;
    /**
     * The item for Co-Owner Rank
     */
    public Item coOwner;
    /**
     * The item for Moderator Rank
     */
    public Item moderator;
    /**
     * The item for Member Rank
     */
    public Item member;
    /**
     * The item for Visitor Rank
     */
    public Item visitor;

    public IslandRanksInventoryConfig(int size, String title, Background background, Item owner, Item coOwner, Item moderator, Item member, Item visitor) {
        this.size = size;
        this.title = title;
        this.background = background;
        this.owner = owner;
        this.coOwner = coOwner;
        this.moderator = moderator;
        this.member = member;
        this.visitor = visitor;
    }

}