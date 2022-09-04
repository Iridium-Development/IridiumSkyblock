package com.iridium.iridiumskyblock.placeholders;

import com.iridium.iridiumcore.utils.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.List;

public class TeamChatPlaceholderBuilder implements com.iridium.iridiumteams.TeamChatPlaceholderBuilder {

    @Override
    public List<Placeholder> getPlaceholders(AsyncPlayerChatEvent event, Player player) {
        return Arrays.asList(
                new Placeholder("player", event.getPlayer().getName()),
                new Placeholder("message", event.getMessage())
        );
    }
}
