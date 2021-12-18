package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.Booster;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandBoostersGUI;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BoosterCommand extends Command {

    /**
     * The default constructor.
     */
    public BoosterCommand() {
        super(Arrays.asList("booster", "boosters"), "Open the Island Boosters Menu", "%prefix% &7/is booster <name>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Shows an overview over the members of the Island and allows quick rank management.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (args.length != 2) {
            player.openInventory(new IslandBoostersGUI(island.get(), player.getOpenInventory().getTopInventory()).getInventory());
            return true;
        }

        String boosterName = args[1];
        Booster booster = IridiumSkyblock.getInstance().getBoosterList().get(boosterName);
        if (booster == null) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownBooster.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), boosterName);
        if (islandBooster.isActive() && !booster.stackable) {
            return false;
        }

        if (!PlayerUtils.pay(player, island.get(), booster.crystalsCost, booster.vaultCost)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotAfford.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }
        islandBooster.setTime(LocalDateTime.now().plusSeconds(booster.time + (islandBooster.isActive() && booster.stackable ? islandBooster.getRemainingTime() : 0)));
        IslandLog islandLog = new IslandLog(island.get(), LogAction.BOOSTER_PURCHASE, user, null, 0, boosterName);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().successfullyBoughtBooster
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%booster%", IridiumSkyblock.getInstance().getBoosterList().get(islandBooster.getBooster()).name)
                .replace("%vault_cost%", IridiumSkyblock.getInstance().getNumberFormatter().format(booster.vaultCost))
                .replace("%crystal_cost%", IridiumSkyblock.getInstance().getNumberFormatter().format(booster.crystalsCost))
        ));
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
        return Collections.emptyList();
    }

}
