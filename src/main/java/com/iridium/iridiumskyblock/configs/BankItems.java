package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumskyblock.bankitems.CrystalsBankItem;

import java.util.Arrays;

public class BankItems extends com.iridium.iridiumteams.configs.BankItems {

    public BankItems() {
        super("Island", "&9");
    }
    
    public CrystalsBankItem crystalsBankItem = new CrystalsBankItem(100, new Item(XMaterial.NETHER_STAR, 13, 1, "&9&lIsland Crystals", Arrays.asList(
            "&7%amount% Crystals",
            "&9&l[!] &9Left click to withdraw",
            "&9&l[!] &9Right click to deposit")
    ));

}
