package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.BankGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which opens the Island bank GUI.
 */
public class BankCommand extends Command {

    /**
     * The default constructor.
     */
    public BankCommand() {
        super(Collections.singletonList("bank"), "Open your Island bank", "", false);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Opens the Island bank GUI.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 5) {
            if (args[1].equalsIgnoreCase("give")) {
                Player player = Bukkit.getPlayer(args[2]);
                if (player != null) {
                    User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                    Optional<Island> island = user.getIsland();
                    if (island.isPresent()) {
                        Optional<BankItem> bankItem =
                                IridiumSkyblock.getInstance().getBankItemList().stream().filter(item -> item.getName().equalsIgnoreCase(args[3])).findFirst();
                        if (bankItem.isPresent()) {
                            try {
                                IslandBank islandBank =
                                        IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island.get(), bankItem.get());
                                islandBank.setNumber(islandBank.getNumber() + Double.parseDouble(args[4]));
                                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().gaveBank.replace("%player%", player.getName()).replace("%amount%", args[4]).replace("%item%", bankItem.get().getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            } catch (NumberFormatException exception) {
                                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notANumber.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            }
                        } else {
                            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noSuchBankItem.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        }
                    } else {
                        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNoIsland.replace(
                                "%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                } else {
                    sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAPlayer.replace(
                            "%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            }
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            Optional<Island> island = user.getIsland();

            if (island.isPresent()) {
                player.openInventory(new BankGUI(island.get()).getInventory());
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().mustBeAPlayer
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
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
        // We currently don't want to tab-completion here
        // Return a new List so it isn't a list of online players
        if (args.length == 2) {
            return Collections.singletonList("give");
        }
        if (args.length == 3) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }
        if (args.length==4) {
            return IridiumSkyblock.getInstance().getBankItemList().stream().map(BankItem::getName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
