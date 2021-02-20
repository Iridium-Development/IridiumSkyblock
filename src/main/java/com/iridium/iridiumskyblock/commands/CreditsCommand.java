package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CreditsCommand extends Command {

    private final IridiumSkyblock iridiumSkyblock;

    /*
    Please dont add yourself to this list, if you contribute enough I will add you
     */
    private final HashMap<String, Role> contributors = new HashMap<String, Role>() {{
        put("Peaches_MLG", Role.Owner);
        put("Das", Role.Contributor);
        put("SlashRemix", Role.Contributor);
        put("BomBardyGamer", Role.Contributor);
    }};

    public CreditsCommand(IridiumSkyblock iridiumSkyblock) {
        super(Arrays.asList("credits", "contributors"), "A list of players who helped make IridiumSkyblock", "", false);
        this.iridiumSkyblock = iridiumSkyblock;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        iridiumSkyblock.loadConfigs();
        for (String name : contributors.keySet()) {
            Role role = contributors.get(name);
            sender.sendMessage(StringUtils.color("&7 - &b" + name + " (" + role.name() + ")"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }

    public enum Role {
        Owner, Contributor;
    }
}
