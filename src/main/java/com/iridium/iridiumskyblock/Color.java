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

    public static Color getColor(String color) {
        return Arrays.stream(Color.values()).filter(color1 -> color1.name().equalsIgnoreCase(color)).findFirst().orElse(null);
    }
}
