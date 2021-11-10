package com.iridium.iridiumskyblock.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.spawn.EssentialsSpawn;
import com.iridium.iridiumcore.Color;
import com.iridium.iridiumcore.dependencies.paperlib.PaperLib;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Various utils for working with {@link Player}'s.
 */
public class PlayerUtils {

    /**
     * Removes the specified amount of crystals and money from the island bank and from
     * the player's purse if there is not enough in the bank.
     *
     * @param player    The Player
     * @param island    The Player's Island
     * @param crystals  The amount of crystals
     * @param money     The amount of money
     * @return If the purchase was successful. {@link PlayerUtils#canPurchase(Player, Island, int, double)} should be preferred.
     */
    public static boolean pay(@NotNull Player player, @NotNull Island island, int crystals, double money) {
        // Don't withdraw stuff if they can't purchase it.
        if (!canPurchase(player, island, crystals, money)) {
            return false;
        }

        IslandBank islandCrystals = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, IridiumSkyblock.getInstance().getBankItems().crystalsBankItem);
        IslandBank islandMoney = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, IridiumSkyblock.getInstance().getBankItems().moneyBankItem);
        Economy economy = IridiumSkyblock.getInstance().getEconomy();

        islandCrystals.setNumber(islandCrystals.getNumber() - crystals);
        if (islandMoney.getNumber() >= money) {
            islandMoney.setNumber(islandMoney.getNumber() - money);
        } else {
            economy.withdrawPlayer(player, money);
        }

        return true;
    }

    /**
     * Check if the player has enough money and crystals to buy something.
     *
     * @param player   The Player
     * @param island   The Player's Island
     * @param crystals The crystals being spent
     * @param money    The money being spent.
     * @return If they can purchase the item
     */
    public static boolean canPurchase(@NotNull Player player, @NotNull Island island, int crystals, double money) {
        IslandBank islandCrystals = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, IridiumSkyblock.getInstance().getBankItems().crystalsBankItem);
        IslandBank islandMoney = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, IridiumSkyblock.getInstance().getBankItems().moneyBankItem);
        Economy economy = IridiumSkyblock.getInstance().getEconomy();

        return islandCrystals.getNumber() >= crystals && (islandMoney.getNumber() >= money || (economy != null && economy.getBalance(player) >= money));
    }

    /**
     * Sends an island's border to a player.
     *
     * @param player The specified Player
     * @param island The specified Island
     */
    public static void sendBorder(@NotNull Player player, @NotNull Island island) {
        final Location centre = island.getCenter(player.getWorld()).clone();
        Color color = island.getColor().equals(Color.OFF) ? Color.BLUE : island.getColor();
        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getNms().sendWorldBorder(player, color, island.getSize() + (island.getSize() % 2 == 0 ? 1 : 0), centre));
    }

    /**
     * Teleports the specified player to spawn.
     *
     * @param player The player we are teleporting
     */
    public static void teleportSpawn(Player player) {
        World spawnWorld = Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().spawnWorldName);
        if (spawnWorld == null) {
            spawnWorld = Bukkit.getWorlds().get(0);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("EssentialsSpawn")) {
            EssentialsSpawn essentialsSpawn = (EssentialsSpawn) Bukkit.getPluginManager().getPlugin("EssentialsSpawn");
            Essentials essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            PaperLib.teleportAsync(player, essentialsSpawn.getSpawn(essentials.getUser(player).getGroup()), PlayerTeleportEvent.TeleportCause.PLUGIN);
        } else {
            PaperLib.teleportAsync(player, spawnWorld.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
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

    /**
     * Returns a list of the names of all online players.
     *
     * @return The names of all players.
     */
    public static List<String> getOnlinePlayerNames() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

}
