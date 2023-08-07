package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.RegenGUI;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.commands.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class RegenCommand extends Command<Island, User> {

    public RegenCommand() {
        super(Collections.singletonList("regen"), "Regenerate your Island", "%prefix% &7/is regen <schematic>", "");
    }

    @Override
    public void execute(User user, Island island, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        Player player = user.getPlayer();
        if (args.length == 0) {
            if (!IridiumSkyblock.getInstance().getIslandManager().getTeamPermission(island, IridiumSkyblock.getInstance().getUserManager().getUser(player), "regen")) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotRegenIsland
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
                return;
            }

            player.openInventory(new RegenGUI(player.getOpenInventory().getTopInventory(), player).getInventory());
            return;
        }

        Optional<String> schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.keySet().stream()
                .filter(config -> config.equalsIgnoreCase(args[0]))
                .findFirst();
        if (!schematicConfig.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownSchematic
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return;
        }

        IridiumSkyblock.getInstance().getIslandManager().getTeamMembers(island).stream()
                .map(User::getPlayer)
                .filter(Objects::nonNull)
                .forEach(member -> member.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().regeneratingIsland
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                )));

        if(IridiumSkyblock.getInstance().getConfiguration().clearInventoryOnRegen) {
            player.getInventory().clear();
        }

        if(IridiumSkyblock.getInstance().getConfiguration().clearEnderChestOnRegen) {
            player.getEnderChest().clear();
        }

        IridiumSkyblock.getInstance().getIslandManager().generateIsland(island, schematicConfig.get()).thenRun(() -> Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
            if (IridiumSkyblock.getInstance().getTeamManager().teleport(player, island.getHome(), island)) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHome
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
            }
        }));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        return new ArrayList<>(IridiumSkyblock.getInstance().getSchematics().schematics.keySet());
    }
}
