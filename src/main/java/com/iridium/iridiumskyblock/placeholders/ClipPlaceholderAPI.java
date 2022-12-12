package com.iridium.iridiumskyblock.placeholders;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class ClipPlaceholderAPI extends PlaceholderExpansion {
    
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "iridiumskyblock";
    }

    @Override
    public String getAuthor() {
        return "Peaches_MLG";
    }

    @Override
    public String getVersion() {
        return IridiumSkyblock.getInstance().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String placeholder) {
      
        if (Placeholders.placeholders.containsKey(placeholder)) {
            if (player == null) {
                return IridiumSkyblock.getInstance().getPlaceholders().unknownPlayer;
            }
            return Placeholders.placeholders.get(placeholder).placeholderProcess(player);
        }
        if (Placeholders.placeholders_np.containsKey(placeholder)) {
            return Placeholders.placeholders_np.get(placeholder).placeholderProcess(player);
        }

        return null;
    }
}
