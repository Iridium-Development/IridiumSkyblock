package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.placeholders.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;
import java.util.Optional;

public class PlayerChatListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());
        Optional<Island> island = user.getIsland();
        if (user.isIslandChat() && island.isPresent()) {
            event.setCancelled(true);
            for (User islandUser : island.get().getMembers()) {
                Player recipient = Bukkit.getPlayer(islandUser.getUuid());
                if (recipient != null) {
                    recipient.sendMessage(StringUtils.color(StringUtils.processMultiplePlaceholders(IridiumSkyblock.getInstance().getMessages().islandMemberChat, new PlaceholderBuilder().applyIslandPlaceholders(island.get()).build())
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                            .replace("%player%", event.getPlayer().getName())
                            .replace("%message%", event.getMessage()))
                    );
                }
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                User onlineUser = IridiumSkyblockAPI.getInstance().getUser(player);

                if (onlineUser.isIslandChatSpying()) {
                    player.sendMessage(StringUtils.color(StringUtils.processMultiplePlaceholders(IridiumSkyblock.getInstance().getMessages().islandChatSpyMessage, new PlaceholderBuilder().applyIslandPlaceholders(island.get()).build())
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                            .replace("%island%", island.get().getName())
                            .replace("%player%", event.getPlayer().getName())
                            .replace("%message%", event.getMessage()))
                    );
                }
            }
        } else {
            String format = event.getFormat();
            for (Map.Entry<String, Placeholders.Placeholder> entry : Placeholders.placeholders.entrySet()) {
                if (format.contains(entry.getKey().toUpperCase())) {
                    format = format.replace(entry.getKey().toUpperCase(), entry.getValue().placeholderProcess(event.getPlayer()));
                }
            }
            event.setFormat(format);
        }
    }

}
