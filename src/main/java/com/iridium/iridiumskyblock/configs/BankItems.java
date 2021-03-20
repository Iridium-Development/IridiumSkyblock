package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.bankitems.CrystalsBankItem;
import com.iridium.iridiumskyblock.bankitems.ExperienceBankItem;
import com.iridium.iridiumskyblock.bankitems.MoneyBankItem;

import java.util.Arrays;

public class BankItems {
    public CrystalsBankItem crystalsBankItem = new CrystalsBankItem(10, new Item(XMaterial.NETHER_STAR, 13, 1, "&b&lIsland Crystals", Arrays.asList("&7{amount} Crystals", "&b&l[!] &bLeft click to withdraw", "&b&l[!] &bRight click to deposit")));
    public ExperienceBankItem experienceBankItem = new ExperienceBankItem(10, new Item(XMaterial.EXPERIENCE_BOTTLE, 15, 1, "&b&lIsland Experience", Arrays.asList("&7{amount} Experience", "&b&l[!] &bLeft click to withdraw", "&b&l[!] &bRight click to deposit")));
    public MoneyBankItem moneyBankItem = new MoneyBankItem(10, new Item(XMaterial.PAPER, 11, 1, "&b&lIsland Money", Arrays.asList("&7${amount}", "&b&l[!] &bLeft click to withdraw", "&b&l[!] &bRight click to deposit")));
}
