package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.bank.CrystalsBankItem;
import com.iridium.iridiumskyblock.bank.ExperienceBankItem;
import com.iridium.iridiumskyblock.bank.MoneyBankItem;

import java.util.Arrays;

/**
 * The bank item configuration used by IridiumSkyblock (bankitems.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
public class BankItems {

    public CrystalsBankItem crystalsBankItem = new CrystalsBankItem(10, new Item(XMaterial.NETHER_STAR, 13, 1, "&9&lIsland Crystals", Arrays.asList("&7%amount% Crystals", "&9&l[!] &9Left click to withdraw", "&9&l[!] &9Right click to deposit")));
    public ExperienceBankItem experienceBankItem = new ExperienceBankItem(10, new Item(XMaterial.EXPERIENCE_BOTTLE, 15, 1, "&9&lIsland Experience", Arrays.asList("&7%amount% Experience", "&9&l[!] &9Left click to withdraw", "&9&l[!] &9Right click to deposit")));
    public MoneyBankItem moneyBankItem = new MoneyBankItem(10, new Item(XMaterial.PAPER, 11, 1, "&9&lIsland Money", Arrays.asList("&7$%amount%", "&9&l[!] &9Left click to withdraw", "&9&l[!] &9Right click to deposit")));

}
