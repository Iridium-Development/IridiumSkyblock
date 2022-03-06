package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.bank.CrystalsBankItem;
import com.iridium.iridiumskyblock.bank.ExperienceBankItem;
import com.iridium.iridiumskyblock.bank.MoneyBankItem;

import java.util.Arrays;

/**
 * The bank item configuration used by IridiumSkyblock (bankitems.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankItems {

    public CrystalsBankItem crystalsBankItem = new CrystalsBankItem(10, new Item(XMaterial.NETHER_STAR, 13, 1, "&b&lIsland Crystals", Arrays.asList("&7%amount% Crystals", "&b&l[!] &bLeft click to withdraw", "&b&l[!] &bRight click to deposit"), null));
    public ExperienceBankItem experienceBankItem = new ExperienceBankItem(10, new Item(XMaterial.EXPERIENCE_BOTTLE, 15, 1, "&b&lIsland Experience", Arrays.asList("&7%amount% Experience", "&b&l[!] &bLeft click to withdraw", "&b&l[!] &bRight click to deposit"), null));
    public MoneyBankItem moneyBankItem = new MoneyBankItem(10, new Item(XMaterial.PAPER, 11, 1, "&b&lIsland Money", Arrays.asList("&7$%amount%", "&b&l[!] &bLeft click to withdraw", "&b&l[!] &bRight click to deposit"), null));

}
