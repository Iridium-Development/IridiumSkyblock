package com.iridium.iridiumskyblock.bankitems;

import com.iridium.iridiumskyblock.BankItem;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@NoArgsConstructor
public class MoneyBankItem extends BankItem {

    public MoneyBankItem(double defaultAmount, Item item) {
        super("money", defaultAmount, true, item);
    }

    @Override
    public void withdraw(Player player, Number amount) {
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Island island = user.getIsland().orElse(null);
        if (island != null) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, this);
            double money = Math.min(amount.doubleValue(), islandBank.getNumber());
            if (money > 0) {
                islandBank.setNumber(islandBank.getNumber() - money);
                IridiumSkyblock.getInstance().getEconomy().depositPlayer(player, money);
                //Success Message
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    @Override
    public void deposit(Player player, Number amount) {
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Island island = user.getIsland().orElse(null);
        if (island != null) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, this);
            double money = Math.min(amount.doubleValue(), IridiumSkyblock.getInstance().getEconomy().getBalance(player));
            if (money > 0) {
                islandBank.setNumber(islandBank.getNumber() + money);
                IridiumSkyblock.getInstance().getEconomy().withdrawPlayer(player, money);
                //Success Message
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    @Override
    public String toString(Number number) {
        return String.valueOf(number.doubleValue());
    }
}
