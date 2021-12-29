package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AdminCommand extends Command {

    public AdminCommand() {
        super(Collections.singletonList("admin"), "Commands for admins", "iridiumskyblock.admin", false, Duration.ZERO);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender.isOp()) {
            if(args.length > 1) {
                if(args[1].equalsIgnoreCase("help")) {
                    sendHelp(sender);
                } else if(args[1].equalsIgnoreCase("size")) {
                    if(args.length > 3) {
                        Player target = Bukkit.getPlayer(args[2]);
                        if(target != null) {
                            int size = Integer.parseInt(args[3]);
                            Optional<Island> island = IridiumSkyblockAPI.getInstance().getUser(target).getIsland();
                            island.ifPresent(value -> {
                                value.setSizeAddon(size);
                                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().prefix + " &b" + target.getName() + "'s &7island size is now set to " + value.getSizeAddon() + " &c(Not including upgrades)"));
                                value.getMembers().stream().map(islandUser -> Bukkit.getPlayer(islandUser.getUuid())).filter(Objects::nonNull)
                                        .forEach(islandPlayer -> islandPlayer.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().prefix + " &7Your island's size has been changed to &b" + value.getSizeAddon() + "&7! &c(Not including upgrades)")));
                            });
                            return true;
                        }
                        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().prefix + " &cInvalid player!"));
                        return true;
                    } else if(args.length > 2) {
                        Player target = Bukkit.getPlayer(args[2]);
                        if(target != null) {
                            Optional<Island> island = IridiumSkyblockAPI.getInstance().getUser(target).getIsland();
                            if (island.isPresent()) {
                                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().prefix + " &b" + target.getName() + "'s &7island size is set to " + island.get().getSizeAddon() + " &c(Not including upgrades)"));
                            } else {
                                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().prefix + " &cNo island found!"));
                            }
                            return true;
                        }
                        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().prefix + " &cInvalid player!"));
                        return true;
                    }
                    sender.sendMessage(StringUtils.color("&8=========== &b&lIsland Admin Help &r&8==========="));
                    sender.sendMessage(StringUtils.color("&b/is admin size <player>&f: &7See the size of a player's island."));
                    sender.sendMessage(StringUtils.color("&b/is admin size <player> <amount>&f: &7Set the size of a player's island. This size is on top of their upgrade"));
                    return true;
                }
            } else {
                sendHelp(sender);
            }
        }

        return true;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(StringUtils.color("&8=========== &b&lIsland Admin Help &r&8==========="));
        sender.sendMessage(StringUtils.color("&b/is admin help&f: &7Display admin commands"));
        sender.sendMessage(StringUtils.color("&b/is admin size&f: &7View/Change the size of an island"));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        // We currently don't want to tab-completion here
        // Return a new List, so it isn't a list of online players
        return Collections.emptyList();
    }

}
