package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.ShopCategoryGUI;
import com.iridium.iridiumskyblock.gui.ShopOverviewGUI;
import com.iridium.iridiumskyblock.shop.ShopCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which opens the island shop.
 */
public class ShopCommand extends Command {

    /**
     * The default constructor.
     */
    public ShopCommand() {
        super(Collections.singletonList("shop"), "Opens the Island shop", "", true, Duration.ZERO);
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
            player.openInventory(new ShopOverviewGUI().getInventory());
        } else {
            String[] commandArguments = Arrays.copyOfRange(arguments, 1, arguments.length);
            String categoryName = String.join(" ", commandArguments);
            Optional<ShopCategory> category = IridiumSkyblock.getInstance().getShopManager().getCategoryByName(categoryName);

            if (!category.isPresent()) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noShopCategory.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            player.openInventory(new ShopCategoryGUI(category.get()).getInventory());
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
        return IridiumSkyblock.getInstance().getShopManager().getCategories().stream()
                .map(shopCategory -> shopCategory.name)
                .filter(s -> s.toLowerCase().contains(args[1].toLowerCase()))
                .collect(Collectors.toList());
    }

}
