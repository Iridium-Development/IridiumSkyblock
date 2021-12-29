package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.gui.ConfirmationGUI;
import com.iridium.iridiumskyblock.managers.DatabaseManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Command which allows admins to bypass Island restrictions.
 */
public class ClearDataCommand extends Command {

    /**
     * The default constructor.
     */
    public ClearDataCommand() {
        super(Collections.singletonList("clearalldata"), "Resets all IridiumSkyblock Data", "iridiumskyblock.clearalldata", false, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Allows admins to bypass Island restrictions.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            execute(sender);
            return true;
        }

        Player player = (Player) sender;
        player.openInventory(new ConfirmationGUI(() -> execute(player), getCooldownProvider()).getInventory());
        // Return false because the cooldown is set by the ConfirmationGUI
        return false;
    }

    private void execute(CommandSender commandSender) {
        DatabaseManager databaseManager = IridiumSkyblock.getInstance().getDatabaseManager();
        databaseManager.getUserTableManager().clear();
        databaseManager.getIslandTableManager().clear();
        databaseManager.getIslandInviteTableManager().clear();
        databaseManager.getIslandPermissionTableManager().clear();
        databaseManager.getIslandBlocksTableManager().clear();
        databaseManager.getIslandSpawnersTableManager().clear();
        databaseManager.getIslandBankTableManager().clear();
        databaseManager.getIslandMissionTableManager().clear();
        databaseManager.getIslandRewardTableManager().clear();
        databaseManager.getIslandUpgradeTableManager().clear();
        databaseManager.getIslandTrustedTableManager().clear();
        databaseManager.getIslandBoosterTableManager().clear();
        databaseManager.getIslandWarpTableManager().clear();
        databaseManager.getIslandLogTableManager().clear();

        commandSender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dataReset.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        // Return a new List, so it isn't a list of online players
        return Collections.emptyList();
    }

}
