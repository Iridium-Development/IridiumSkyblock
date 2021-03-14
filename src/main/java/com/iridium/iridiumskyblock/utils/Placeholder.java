package com.iridium.iridiumskyblock.utils;

import lombok.Getter;

/**
 * Represents a placeholder used in configuration files.
 */
@Getter
public class Placeholder {

    private final String key;
    private final String value;

    /**
     * The default constructor.
     *
     * @param key   The placeholder without curly brackets.
     * @param value The actual value of the placeholder
     */
    public Placeholder(String key, String value) {
        this.key = "{" + key + "}";
        this.value = value;
    }

    /**
     * Replaces this placeholder in the provided line with the value of this placeholder.
     *
     * @param line The line which potentially contains the placeholder
     * @return The processed line with this placeholder replaced
     */
    public String process(String line) {
        if (line == null) return "";
        return line.replace(key, value);
    }

}
