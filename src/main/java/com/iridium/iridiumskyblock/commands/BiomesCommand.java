package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.biomes.BiomeCategory;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.BiomeCategoryGUI;
import com.iridium.iridiumskyblock.gui.BiomeOverviewGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which opens the biome menu.
 */
public class BiomesCommand extends Command {

    /**
     * The default constructor.
     */
    public BiomesCommand() {
        super(Collections.singletonList("biome"), "Opens the Biome Menu", "%prefix% &7/is biomes <category>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments. Not
     * called when the command execution was invalid (no permission, no player or command
     * disabled).
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] arguments) {
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (!user.getIsland().isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (arguments.length == 1) {
            player.openInventory(new BiomeOverviewGUI(player.getOpenInventory().getTopInventory()).getInventory());
        } else {
            String[] commandArguments = Arrays.copyOfRange(arguments, 1, arguments.length);
            String categoryName = String.join(" ", commandArguments);
            Optional<BiomeCategory> category = IridiumSkyblock.getInstance().getBiomesManager().getCategoryByName(categoryName);

            if (!category.isPresent()) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noBiomeCategory.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            player.openInventory(new BiomeCategoryGUI(category.get(), player.getOpenInventory().getTopInventory()).getInventory());
        }

        return true;
    }

    /**
     * Handles tab-completion for this command.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param command       The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return IridiumSkyblock.getInstance().getBiomesManager().getCategories().stream()
                .map(biomeCategory -> biomeCategory.name)
                .collect(Collectors.toList());
    }

}
