package com.iridium.iridiumskyblock.utils;

import org.bukkit.entity.Player;

/**
 * Various utils for working with {@link Player}'s.
 */
public class PlayerUtils {

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
     * Calculates the total amount of experience a Player has without the level system.
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
