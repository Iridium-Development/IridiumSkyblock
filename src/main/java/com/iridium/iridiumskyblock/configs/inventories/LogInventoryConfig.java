package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumskyblock.support.material.Background;
import com.iridium.iridiumskyblock.support.material.Item;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LogInventoryConfig extends NoItemGUI{
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

    public LogInventoryConfig(int size, String title, Item IslandMembers, Item IslandTrusts, Item IslandInvites, Item IslandBank, Item IslandBoosters, Item IslandUpgrades, Item IslandRewards, String USER_JOINED, String USER_KICKED, String USER_LEFT, String USER_INVITED, String USER_UNINVITED, String USER_PROMOTED, String USER_DEMOTED, String USER_TRUSTED, String USER_UNTRUSTED, String BANK_DEPOSIT, String BANK_WITHDRAW, String BOOSTER_PURCHASE, String UPGRADE_PURCHASE, String REWARD_REDEEMED, Background background) {
        this.size = size;
        this.title = title;
        this.IslandMembers = IslandMembers;
        this.IslandTrusts = IslandTrusts;
        this.IslandInvites = IslandInvites;
        this.IslandBank = IslandBank;
        this.IslandBoosters = IslandBoosters;
        this.IslandUpgrades = IslandUpgrades;
        this.IslandRewards = IslandRewards;
        this.USER_JOINED = USER_JOINED;
        this.USER_KICKED = USER_KICKED;
        this.USER_LEFT = USER_LEFT;
        this.USER_INVITED = USER_INVITED;
        this.USER_UNINVITED = USER_UNINVITED;
        this.USER_PROMOTED = USER_PROMOTED;
        this.USER_DEMOTED = USER_DEMOTED;
        this.USER_TRUSTED = USER_TRUSTED;
        this.USER_UNTRUSTED = USER_UNTRUSTED;
        this.BANK_DEPOSIT = BANK_DEPOSIT;
        this.BANK_WITHDRAW = BANK_WITHDRAW;
        this.BOOSTER_PURCHASE = BOOSTER_PURCHASE;
        this.UPGRADE_PURCHASE = UPGRADE_PURCHASE;
        this.REWARD_REDEEMED = REWARD_REDEEMED;
        this.background = background;
    }
}
