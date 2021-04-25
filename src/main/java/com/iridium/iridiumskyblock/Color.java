package com.iridium.iridiumskyblock;

import java.util.Arrays;

/**
 * Represents a color of the border.
 */
public enum Color {

    BLUE,
    GREEN,
    RED,
    OFF;

    /**
     * Returns the color with the provided name, null if there is none.
     * Case insensitive.
     *
     * @param color The color which should be parsed
     * @return The color, null if there is none
     */
    public static Color getColor(String color) {
        return valueOf(color.toUpperCase());
    }

}
