package com.iridium.iridiumskyblock.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.spawn.EssentialsSpawn;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Various utils for working with {@link Player}'s.
 */
public class PlayerUtils {

    public static boolean pay(@NotNull Player player, @NotNull Island island, int crystals, double money) {
        IslandBank islandCrystals = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island,
                IridiumSkyblock.getInstance().getBankItems().crystalsBankItem);
        IslandBank islandMoney = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island,
                IridiumSkyblock.getInstance().getBankItems().moneyBankItem);
        Economy economy = IridiumSkyblock.getInstance().getEconomy();

        if (islandCrystals.getNumber() >= crystals && (islandMoney.getNumber() >= money || (economy != null && economy.getBalance(player) >= money))) {
            islandCrystals.setNumber(islandCrystals.getNumber() - crystals);
            if (islandMoney.getNumber() >= money) {
                islandMoney.setNumber(islandMoney.getNumber() - money);
            } else {
                economy.withdrawPlayer(player, money);
            }
            return true;
        }
        return false;
    }

    /**
     * Sends an island's border to a player.
     *
     * @param player The specified Player
     * @param island The specified Island
     */
    public static void sendBorder(@NotNull Player player, @NotNull Island island) {
        final Location centre = island.getCenter(player.getWorld()).clone();

        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getNms().sendWorldBorder(player, island.getColor(), island.getSize() + (island.getSize() % 2 == 0 ? 1 : 0), centre));
    }

    /**
     * Teleports the specified player to spawn.
     *
     * @param player The player we are teleporting
     */

    public static void teleportSpawn(Player player) {
        World spawnWorld = Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().spawnWorldName);
        if (spawnWorld == null) spawnWorld = Bukkit.getWorlds().get(0);
        if (Bukkit.getPluginManager().isPluginEnabled("EssentialsSpawn")) {
            EssentialsSpawn essentialsSpawn = (EssentialsSpawn) Bukkit.getPluginManager().getPlugin("EssentialsSpawn");
            Essentials essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            player.teleport(essentialsSpawn.getSpawn(essentials.getUser(player).getGroup()));
        } else {
            player.teleport(spawnWorld.getSpawnLocation());
        }
    }

    /**
     * Returns the experience a Player needs for the specified level.
     * Might be wrong if Minecraft's level system changes.
     *
     * @param level The level of the Player
     * @return The amount of experience the Player needs for the next level
     */
    private static int getExpAtLevel(final int level) {
        if (level <= 15) {
            return (2 * level) + 7;
        } else if (level <= 30) {
            return (5 * level) - 38;
        }
        return (9 * level) - 158;
    }

    /**
     * Calculates the total amount of experience a Player has with levels converted to experience.
     *
     * @param player The Player whose experience should be calculated
     * @return The total experience of the provided Player
     */
    public static int getTotalExperience(final Player player) {
        int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
        int currentLevel = player.getLevel();

        while (currentLevel > 0) {
            currentLevel--;
            exp += getExpAtLevel(currentLevel);
        }

        if (exp < 0) {
            exp = Integer.MAX_VALUE;
        }

        return exp;
    }

    /**
     * Sets the experience of a Player. Overrides the current amount.
     *
     * @param player The Player whose experience should be updated
     * @param exp    The total amount of experience the Player should have
     */
    public static void setTotalExperience(final Player player, final int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Experience is negative!");
        }

        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);

        int amount = exp;
        while (amount > 0) {
            final int expToLevel = getExpAtLevel(player.getLevel());
            amount -= expToLevel;
            if (amount >= 0) {
                // give until next level
                player.giveExp(expToLevel);
            } else {
                // give the rest
                amount += expToLevel;
                player.giveExp(amount);
                amount = 0;
            }
        }
    }

}
