package com.iridium.iridiumskyblock.commands.bank;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.commands.Command;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.iridium.iridiumskyblock.database.User;
import java.time.Duration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SetCommand extends Command {
    /**
     * The default constructor.
     */
    public SetCommand() {
        super(Collections.singletonList("set"), "Set a players bank value", "%prefix% &7/is bank set <player> <type> <amount>","iridiumskyblock.bank.set", false, Duration.ZERO);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 5) {
            Player player = Bukkit.getPlayer(args[2]);
            if (player != null) {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                Optional<Island> island = user.getIsland();
                if (island.isPresent()) {
                    Optional<BankItem> bankItem = IridiumSkyblock.getInstance().getBankItemList().stream().filter(item -> item.getName().equalsIgnoreCase(args[3])).findFirst();
                    if (bankItem.isPresent()) {
                        try {
                            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island.get(), bankItem.get());
                            islandBank.setNumber(Math.max(Double.parseDouble(args[4]), 0));
                            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().setBank.replace("%player%", player.getName()).replace("%amount%", args[4]).replace("%item%", bankItem.get().getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            return true;
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notANumber.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        }
                    } else {
                        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noSuchBankItem.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                } else {
                    sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNoIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAPlayer.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 3) {
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(playerName -> playerName.contains(args[2]))
                .collect(Collectors.toList());
        }

        if (args.length == 4) {
            return IridiumSkyblock.getInstance().getBankItemList().stream()
                .map(BankItem::getName)
                .filter(bankItem -> bankItem.contains(args[3]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
