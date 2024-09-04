package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.BiomeCategoryGUI;
import com.iridium.iridiumskyblock.gui.BiomeOverviewGUI;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BiomeCommand extends Command<Island, User> {

    public BiomeCommand() {
        super(Collections.singletonList("biomes"), "Refresh your Island Biome", "%prefix% &7/is biomes <biome>", "", 10);
    }

    @Override
    public boolean execute(User user, Island island, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        Player player = user.getPlayer();
        if (args.length == 0) {
            player.openInventory(new BiomeOverviewGUI(player).getInventory());
            return false;
        } else {
            Optional<String> categoryName = getCategoryName(String.join(" ", args));

            if (!categoryName.isPresent()) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noBiomeCategory
                        .replace("%prefix%", iridiumTeams.getConfiguration().prefix)
                ));
                return false;
            }

            player.openInventory(new BiomeCategoryGUI(categoryName.get(), player).getInventory());
            return false;
        }
    }

    private Optional<String> getCategoryName(String name) {
        for (String category : IridiumSkyblock.getInstance().getBiomes().categories.keySet()) {
            if (category.equalsIgnoreCase(name)) {
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        return new ArrayList<>(IridiumSkyblock.getInstance().getBiomes().categories.keySet());
    }
}
