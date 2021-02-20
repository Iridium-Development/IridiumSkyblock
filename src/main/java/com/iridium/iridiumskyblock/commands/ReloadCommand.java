package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command {

    private final IridiumSkyblock iridiumSkyblock;

    public ReloadCommand(IridiumSkyblock iridiumSkyblock) {
        super(Collections.singletonList("reload"), "Reload your configurations", "iridiumchunkbusters.reload", false);
        this.iridiumSkyblock = iridiumSkyblock;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        iridiumSkyblock.loadConfigs();
        sender.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().reloaded.replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
