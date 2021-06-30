package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.BiomeGUI;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BiomeCommand extends Command {

    private final IridiumSkyblock plugin = IridiumSkyblock.getInstance();

    public BiomeCommand() {
        super(Collections.singletonList("biome"), "Change your island biome.", "", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player) sender;

        final User user = this.plugin.getUserManager().getUser(player);
        final Optional<Island> optionalIsland = user.getIsland();

        if (!optionalIsland.isPresent()) {
            player.sendMessage(StringUtils.color(this.plugin.getMessages().noIsland.replace("%prefix%", plugin.getConfiguration().prefix)));
            return;
        }

        if (args.length != 2) {
            player.openInventory(new BiomeGUI(1, optionalIsland.get(), player.getWorld().getEnvironment()).getInventory());
            return;
        }

        final Optional<XBiome> biomeOptional = XBiome.matchXBiome(args[1]);
        if (!biomeOptional.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().invalidBiome.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }
        IridiumSkyblock.getInstance().getIslandManager().setIslandBiome(optionalIsland.get(), biomeOptional.get());

        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().changedBiome
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%biome%", WordUtils.capitalizeFully(biomeOptional.get().name().toLowerCase().replace("_", " ")))));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            return XBiome.VALUES.stream()
                    .filter(biome -> biome.getEnvironment() == player.getWorld().getEnvironment())
                    .map(Enum::toString)
                    .filter(s -> s.toUpperCase().contains(args[1].toUpperCase()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
