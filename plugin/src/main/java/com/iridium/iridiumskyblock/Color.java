package com.iridium.iridiumskyblock;

import java.util.Arrays;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a color of the border.
 */
public enum Color {

    BLUE(org.bukkit.Color.BLUE),
    GREEN(org.bukkit.Color.GREEN),
    RED(org.bukkit.Color.RED),
    OFF(null);

    @Nullable
    private final org.bukkit.Color bukkitColor;

    /**
     * The default constructor.
     *
     * @param bukkitColor The Bukkit representation of this color
     */
    Color(@Nullable org.bukkit.Color bukkitColor) {
        this.bukkitColor = bukkitColor;
    }

    /**
     * Returns the Bukkit representation of this color
     *
     * @return The Bukkit representation of this color
     */
    @Nullable
    public org.bukkit.Color getBukkitColor() {
        return bukkitColor;
    }

    /**
     * Returns the color with the provided name, null if there is none.
     * Case insensitive.
     *
     * @param color The color which should be parsed
     * @return The color, null if there is none
     */
    public static Color getColor(String color) {
        return Arrays.stream(Color.values()).filter(color1 -> color1.name().equalsIgnoreCase(color)).findFirst().orElse(null);
    }

}
