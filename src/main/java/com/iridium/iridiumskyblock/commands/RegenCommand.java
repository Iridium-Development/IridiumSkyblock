package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
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
        super(Collections.singletonList("regen"), "Regenerate your Island", "%prefix% &7/is regen <schematic>", "", 300);
    }

    @Override
    public boolean execute(User user, Island island, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        Player player = user.getPlayer();
        if (args.length == 0) {
            if (!IridiumSkyblock.getInstance().getIslandManager().getTeamPermission(island, IridiumSkyblock.getInstance().getUserManager().getUser(player), "regen")) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotRegenIsland
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
                return false;
            }

            player.openInventory(new RegenGUI(player.getOpenInventory().getTopInventory(), player).getInventory());
            return false;
        }

        Optional<String> schematic = IridiumSkyblock.getInstance().getSchematics().schematics.keySet().stream()
                .filter(config -> config.equalsIgnoreCase(args[0]))
                .findFirst();
        if (!schematic.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownSchematic
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return false;
        }

        Schematics.SchematicConfig schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.get(schematic.get());

        if(IridiumSkyblock.getInstance().getConfiguration().payPerRegen) {
            boolean purchased = IridiumSkyblock.getInstance().getSchematicManager().buy(player, schematicConfig);
            if(!purchased) {
                return false;
            }
        }

        IridiumSkyblock.getInstance().getIslandManager().getTeamMembers(island).stream()
                .map(User::getPlayer)
                .filter(Objects::nonNull)
                .forEach(member -> member.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().regeneratingIsland
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                )));

        IridiumSkyblock.getInstance().getIslandManager().clearTeamInventory(island);

        IridiumSkyblock.getInstance().getIslandManager().generateIsland(island, schematicConfig).thenRun(() -> Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
          
            if (IridiumSkyblock.getInstance().getTeamManager().teleport(player, island.getHome(), island)) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHome
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
            }
        }));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        return new ArrayList<>(IridiumSkyblock.getInstance().getSchematics().schematics.keySet());
    }
}
