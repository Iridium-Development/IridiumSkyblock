package com.iridium.iridiumskyblock;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Represents a rank of a {@link com.iridium.iridiumskyblock.database.User} on an {@link com.iridium.iridiumskyblock.database.Island}.
 */
@Getter
public enum IslandRank {

    OWNER(4),
    CO_OWNER(3),
    MODERATOR(2),
    MEMBER(1),
    VISITOR(0);

    /**
     * The level of the rank, used to see which ranks are above and below others
     */
    private final int level;

    /**
     * The default constructor.
     * The higher the level, the more permissions this rank has.
     *
     * @param level The index of this rank
     */
    IslandRank(int level) {
        this.level = level;
    }

    @NotNull
    public String getDisplayName() {
        return switch (this) {
            case OWNER -> IridiumSkyblock.getInstance().getMessages().ownerRankDisplayName;
            case CO_OWNER -> IridiumSkyblock.getInstance().getMessages().coOwnerRankDisplayName;
            case MODERATOR -> IridiumSkyblock.getInstance().getMessages().moderatorRankDisplayName;
            case MEMBER -> IridiumSkyblock.getInstance().getMessages().memberRankDisplayName;
            default -> IridiumSkyblock.getInstance().getMessages().visitorRankDisplayName; // Visitor is default
        };
    }

    /**
     * Gets an IslandRank by its level
     *
     * @param level The level of the Island Rank
     * @return The Island Rank
     */
    public static IslandRank getByLevel(int level) {
        return Arrays.stream(values())
                .filter(rankLevel -> rankLevel.level == level)
                .findAny()
                .orElse(null);
    }

}
