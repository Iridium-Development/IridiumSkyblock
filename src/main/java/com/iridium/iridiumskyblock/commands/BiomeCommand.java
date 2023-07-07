package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.BiomeCategoryGUI;
import com.iridium.iridiumskyblock.gui.BiomeOverviewGUI;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.commands.Command;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Optional;

public class BiomeCommand extends Command<Island, User> {

    public BiomeCommand() {
        super(Collections.singletonList("biomes"), "Refresh your Island Biome", "%prefix% &7/is biomes <biome>", "");
    }

    @Override
    public void execute(User user, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        Player player = user.getPlayer();
        if (args.length == 0) {
            player.openInventory(new BiomeOverviewGUI(player.getOpenInventory().getTopInventory()).getInventory());
        } else {
            Optional<String> categoryName = getCategoryName(String.join(" ", args), IridiumSkyblock.getInstance());

            if (!categoryName.isPresent()) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noBiomeCategory
                        .replace("%prefix%", iridiumTeams.getConfiguration().prefix)
                ));
                return;
            }

            player.openInventory(new BiomeCategoryGUI(categoryName.get(), player.getOpenInventory().getTopInventory()).getInventory());
        }
    }

    private Optional<String> getCategoryName(String name, IridiumSkyblock iridiumSkyblock) {
        for (String category : iridiumSkyblock.getBiomes().categories.keySet()) {
            if (category.equalsIgnoreCase(name)) {
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }
}
