package com.iridium.iridiumskyblock;

import com.cryptomorin.xseries.XSound;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Represents a reward for a {@link Mission}.
 * Serialized in Configuration files.
 */
@NoArgsConstructor
@AllArgsConstructor
public class Reward {

    public Item item;
    public List<String> commands;
    public int islandExperience;
    public int crystals;
    public double money;
    public int experience;
    public XSound sound;

    /**
     * Claims this reward for the provided user and island.
     * Sets the experience, executes the commands and plays the sound.
     *
     * @param player The Player which should receive the reward
     * @param island The Island of the Player
     */
    public void claim(Player player, Island island) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        island.setExperience(island.getExperience() + islandExperience);
        commands.forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));
        sound.play(player);
        IslandLog islandLog = new IslandLog(island, LogAction.REWARD_REDEEMED, user, null, crystals, money, experience, ChatColor.stripColor(item.displayName));
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
    }

}
