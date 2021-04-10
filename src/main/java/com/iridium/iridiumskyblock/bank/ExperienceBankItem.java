package com.iridium.iridiumskyblock.bank;

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

import java.util.Optional;

/**
 * Represents the experience in the island bank.
 * Serialized in the Configuration files.
 */
@NoArgsConstructor
public class ExperienceBankItem extends BankItem {

    /**
     * The default constructor.
     *
     * @param defaultAmount The default withdrawal amount of this item
     * @param item          The Item which represents this bank item in the {@link com.iridium.iridiumskyblock.gui.BankGUI}
     */
    public ExperienceBankItem(double defaultAmount, Item item) {
        super("experience", defaultAmount, true, item);
    }

    /**
     * Withdraws the given amount of this item from the Player's bank.
     *
     * @param player The player who wants to withdraw
     * @param amount The amount which should be withdrawn
     */
    @Override
    public void withdraw(Player player, Number amount) {
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island.get(), this);
            int experience = Math.min(amount.intValue(), (int) islandBank.getNumber());

            if (experience > 0) {
                islandBank.setNumber(islandBank.getNumber() - experience);
                PlayerUtils.setTotalExperience(player, PlayerUtils.getTotalExperience(player) + experience);
                // TODO: Success Message
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    /**
     * Deposits the given amount of this item to the Player's bank.
     *
     * @param player The player who wants to deposit
     * @param amount The amount which should be deposited
     */
    @Override
    public void deposit(Player player, Number amount) {
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island.get(), this);
            int experience = Math.min(amount.intValue(), PlayerUtils.getTotalExperience(player));
            if (experience > 0) {
                islandBank.setNumber(islandBank.getNumber() + experience);
                PlayerUtils.setTotalExperience(player, PlayerUtils.getTotalExperience(player) - experience);
                // TODO: Success Message
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    /**
     * Returns the string representation of the value of this item.
     *
     * @param number The number which should be formatted
     * @return The string representation of the provided number for this item
     */
    @Override
    public String toString(Number number) {
        return String.valueOf(number.intValue());
    }

}
