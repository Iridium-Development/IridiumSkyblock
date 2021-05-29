package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.placeholders.Placeholders;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

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
    }

}
