package com.iridium.iridiumskyblock.placeholders;

import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.PlaceholderBuilder;
import com.iridium.iridiumteams.TemporaryCache;
import com.iridium.iridiumteams.UserRank;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserPlaceholderBuilder implements PlaceholderBuilder<User> {
    private final TemporaryCache<User, List<Placeholder>> cache = new TemporaryCache<>();
    private final List<Placeholder> defaultPlaceholders = Arrays.asList(
            new Placeholder("player_rank", "N/A"),
            new Placeholder("player_name", "N/A"),
            new Placeholder("player_join", "N/A")
    );

    @Override
    public List<Placeholder> getPlaceholders(User user) {
        return cache.get(user, Duration.ofSeconds(1), () -> Arrays.asList(
                new Placeholder("player_rank", IridiumSkyblock.getInstance().getUserRanks().getOrDefault(user.getUserRank(), new UserRank("N/A", null)).name),
                new Placeholder("player_name", user.getName()),
                new Placeholder("player_join", user.getJoinTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat)))
        ));
    }

    @Override
    public List<Placeholder> getPlaceholders(Optional<User> optional) {
        return optional.isPresent() ? getPlaceholders(optional.get()) : defaultPlaceholders;
    }
}
