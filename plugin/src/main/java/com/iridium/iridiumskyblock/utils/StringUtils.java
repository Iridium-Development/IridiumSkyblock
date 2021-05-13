package com.iridium.iridiumskyblock.utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import org.apache.commons.lang.WordUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Various utils for modifying Strings.
 */
public class StringUtils {

    /**
     * Applies colors to the provided string.
     * Supports hex colors in Minecraft 1.16+.
     *
     * @param string The string which should have colors
     * @return The new String with applied colors
     */
    public static String color(String string) {
        return IridiumColorAPI.process(string);
    }

    /**
     * Applies colors to the provided strings.
     * Supports hex colors in Minecraft 1.16+.
     * The order of the elements in the list will be the same.
     *
     * @param strings The strings which should have colors
     * @return A list of the same strings with colors
     */
    public static List<String> color(List<String> strings) {
        return strings.stream()
                .map(StringUtils::color)
                .collect(Collectors.toList());
    }

    /**
     * Capitalizes the provided string.
     *
     * @param string The string which should have capitalize
     * @return The Capitalized string
     */
    public static String capitalize(String string) {
        return WordUtils.capitalize(string);
    }

    /**
     * Applies placeholders to the provided strings.
     * Also adds colors.
     * The order of the Strings will be the same.
     *
     * @param lines        The lines which potentially have placeholders
     * @param placeholders The placeholders which should be replaced
     * @return The same lines with replaced placeholders
     */
    public static List<String> processMultiplePlaceholders(List<String> lines, List<Placeholder> placeholders) {
        return lines.stream()
                .map(s -> processMultiplePlaceholders(s, placeholders))
                .collect(Collectors.toList());
    }

    /**
     * Applies Placeholders to the provided string.
     * Also adds colors.
     *
     * @param line         The line which potentially has placeholders
     * @param placeholders The placeholders which should be replaced
     * @return The same line with replaced placeholders
     */
    public static String processMultiplePlaceholders(String line, List<Placeholder> placeholders) {
        String processedLine = line;
        for (Placeholder placeholder : placeholders) {
            processedLine = placeholder.process(processedLine);
        }
        return color(processedLine);
    }

}
