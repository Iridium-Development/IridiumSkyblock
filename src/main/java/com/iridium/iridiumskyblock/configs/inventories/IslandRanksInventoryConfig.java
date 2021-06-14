package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class IslandRanksInventoryConfig {

    /**
     * The size of the GUI
     */
    public int size;
    /**
     * The title of the GUI
     */
    public String title;
    /**
     * The background of the GUI
     */
    public Background background;
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

}