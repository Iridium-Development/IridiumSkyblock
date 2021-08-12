package com.iridium.iridiumskyblock.utils;

public class CheckEnvironment {

    private static boolean isPaper = false;

    static {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            isPaper = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    public static boolean isPaper() {
        return isPaper;
    }
}
