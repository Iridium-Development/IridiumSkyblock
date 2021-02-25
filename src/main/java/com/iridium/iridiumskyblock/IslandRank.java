package com.iridium.iridiumskyblock;

import lombok.Getter;

@Getter
public enum IslandRank {
    OWNER(4), CO_OWNER(3), MODERATOR(2), MEMBER(1), VISITOR(0);
    /**
     * The level of the rank, used to see which ranks are above and below others
     */
    private final int level;

    IslandRank(int level) {
        this.level = level;
    }

    /**
     * Gets an IslandRank by its level
     *
     * @param level The level of the Island Rank
     * @return The Island Rank
     */
    public static IslandRank getByLevel(int level) {
        for (IslandRank islandRank : IslandRank.values()) {
            if (islandRank.getLevel() == level) return islandRank;
        }
        return null;
    }
}
