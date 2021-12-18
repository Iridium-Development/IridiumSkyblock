package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.User;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command which deposits a currency into the Island bank.
 */
public class DepositCommand extends Command {

    /**
     * The default constructor.
     */
    public DepositCommand() {
        super(Collections.singletonList("deposit"), "Deposit into your Island bank", "%prefix% &7/is deposit <name> <amount>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Deposits a currency into the Island bank.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Optional<BankItem> bankItem = IridiumSkyblock.getInstance().getBankItemList().stream().filter(item -> item.getName().equalsIgnoreCase(args[1])).findFirst();
        if (bankItem.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noSuchBankItem.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        try {
            double amount = bankItem.get().deposit(player, Double.parseDouble(args[2]));
            if (amount > 0) {
                IslandLog islandLog = new IslandLog(island.get(), LogAction.BANK_DEPOSIT, user, null, amount, bankItem.get().getName());
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
            }
            return true;
        } catch (NumberFormatException exception) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notANumber.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
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
        return IridiumSkyblock.getInstance().getBankItemList().stream()
            .map(BankItem::getName)
            .collect(Collectors.toList());
    }

}
