package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
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

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String format = event.getFormat();
        for (Map.Entry<String, Placeholders.Placeholder> entry : Placeholders.placeholders.entrySet()) {
            if (format.contains(entry.getKey().toUpperCase())) {
                format = format.replace(entry.getKey().toUpperCase(), entry.getValue().placeholderProcess(event.getPlayer()));
            }
        }
        event.setFormat(format);

        User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());
        Optional<Island> island = user.getIsland();
        if (user.isIslandChat() && island.isPresent()) {
            event.setCancelled(true);
            for (User islandUser : island.get().getMembers()) {
                Player recipient = Bukkit.getPlayer(islandUser.getUuid());
                if (recipient != null) {
                    recipient.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandMemberChat
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                            .replace("%player%", event.getPlayer().getName())
                            .replace("%message%", event.getMessage()))
                    );
                }
            }
        }
    }

}
