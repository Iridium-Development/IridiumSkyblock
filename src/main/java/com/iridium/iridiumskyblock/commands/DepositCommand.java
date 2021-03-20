package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.BankItem;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DepositCommand extends Command {
    /**
     * The default constructor.
     */
    public DepositCommand() {
        super(Collections.singletonList("deposit"), "Deposit into your island bank", "", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 3) {
            // /is deposit <name> <amount>
            return;
        }
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isPresent()) {
            Optional<BankItem> bankItem = IridiumSkyblock.getInstance().getBankItemList().stream().filter(item -> item.getName().equalsIgnoreCase(args[1])).findFirst();
            if (bankItem.isPresent()) {
                bankItem.get().deposit(player, Double.parseDouble(args[2]));
            } else {
                //Bank item doesnt exist
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 2) {
            return IridiumSkyblock.getInstance().getBankItemList().stream().map(BankItem::getName).collect(Collectors.toList());
        }
        return null;
    }
}
