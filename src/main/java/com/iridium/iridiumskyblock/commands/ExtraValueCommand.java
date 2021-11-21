package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.commands.extravalue.AddCommand;
import com.iridium.iridiumskyblock.commands.extravalue.RemoveCommand;
import com.iridium.iridiumskyblock.commands.extravalue.SetCommand;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ExtraValueCommand extends Command {

    public AddCommand addCommand;
    public RemoveCommand removeCommand;
    public SetCommand setCommand;

    /**
     * The default constructor.
     */
    public ExtraValueCommand() {
        super(Collections.singletonList("extravalue"), "Set extra values of island", "%prefix% &7/is extravalue <player/add/remove/set>", "iridiumskyblock.extravalue", false, Duration.ZERO);

        this.addCommand = new AddCommand();
        this.removeCommand = new RemoveCommand();
        this.setCommand = new SetCommand();
        addChilds(addCommand, removeCommand, setCommand);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Gives extra values to specific player's island
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        if (!island.isPresent()) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNoIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().extraValueInfo
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%player%", player.getName()))
                .replace("%amount%", String.valueOf(island.get().getExtraValue())));
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
        // Tab-completion is handled by the subcommands
        return Collections.emptyList();
    }
    
}
