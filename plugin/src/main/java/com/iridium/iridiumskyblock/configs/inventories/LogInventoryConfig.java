package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumskyblock.Background;
import com.iridium.iridiumskyblock.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LogInventoryConfig {
    /**
     * The size of the GUI
     */
    public int size;
    /**
     * The title of the GUI
     */
    public String title;
    public Item IslandMembers;
    public Item IslandTrusts;
    public Item IslandInvites;
    public Item IslandBank;
    public Item IslandBoosters;
    public Item IslandUpgrades;
    public Item IslandRewards;
    
    public String USER_JOINED;
    public String USER_KICKED;
    public String USER_LEFT;
    public String USER_INVITED;
    public String USER_UNINVITED;
    public String USER_PROMOTED;
    public String USER_DEMOTED;
    public String USER_TRUSTED;
    public String USER_UNTRUSTED;
    public String BANK_DEPOSIT;
    public String BANK_WITHDRAW;
    public String BOOSTER_PURCHASE;
    public String UPGRADE_PURCHASE;
    public String REWARD_REDEEMED;
    /**
     * The background of the GUI
     */
    public Background background;
}
