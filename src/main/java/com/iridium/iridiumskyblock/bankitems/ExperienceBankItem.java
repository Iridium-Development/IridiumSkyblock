package com.iridium.iridiumskyblock.bankitems;

import com.iridium.iridiumskyblock.BankItem;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@NoArgsConstructor
public class ExperienceBankItem extends BankItem {

    public ExperienceBankItem(double defaultAmount, Item item) {
        super("experience", defaultAmount, true, item);
    }

    @Override
    public void withdraw(Player player, Number amount) {
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Island island = user.getIsland().orElse(null);
        if (island != null) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, this);
            int experience = Math.min(amount.intValue(), (int) islandBank.getNumber());
            if (experience > 0) {
                islandBank.setNumber(islandBank.getNumber() - experience);
                PlayerUtils.setTotalExperience(player, PlayerUtils.getTotalExperience(player) + experience);
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
            int experience = Math.min(amount.intValue(), PlayerUtils.getTotalExperience(player));
            if (experience > 0) {
                islandBank.setNumber(islandBank.getNumber() + experience);
                PlayerUtils.setTotalExperience(player, PlayerUtils.getTotalExperience(player) - experience);
                //Success Message
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    @Override
    public String toString(Number number) {
        return String.valueOf(number.intValue());
    }
}
