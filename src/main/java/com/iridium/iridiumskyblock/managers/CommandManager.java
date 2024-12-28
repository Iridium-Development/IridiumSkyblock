package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.commands.supersecretcommands.IsMyComputerOnFire;
import com.iridium.iridiumskyblock.commands.supersecretcommands.IsThisWorking;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.gui.InventoryConfigGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandManager extends com.iridium.iridiumteams.managers.CommandManager<Island, User> {
    public CommandManager(String command) {
        super(IridiumSkyblock.getInstance(), "&9", command);
    }

    @Override
    public void registerCommands() {
        super.registerCommands();
        registerCommand(IridiumSkyblock.getInstance().getCommands().visitCommand);
        registerCommand(IridiumSkyblock.getInstance().getCommands().borderCommand);
        registerCommand(IridiumSkyblock.getInstance().getCommands().regenCommand);
        registerCommand(IridiumSkyblock.getInstance().getCommands().biomeCommand);
        registerCommand(IridiumSkyblock.getInstance().getCommands().clearDataCommand);
        registerCommand(new IsThisWorking());
        registerCommand(new IsMyComputerOnFire());
    }

    @Override
    public void noArgsDefault(@NotNull CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            if (IridiumSkyblock.getInstance().getTeamManager().getTeamViaID(user.getTeamID()).isPresent()) {
                player.openInventory(new InventoryConfigGUI(IridiumSkyblock.getInstance().getInventories().islandMenu).getInventory());
                return;
            }
            if (IridiumSkyblock.getInstance().getConfiguration().createRequiresName) {
                Bukkit.getServer().dispatchCommand(commandSender, "is help");
                return;
            }
            Bukkit.getServer().dispatchCommand(commandSender, "is create");
        }
    }
}
