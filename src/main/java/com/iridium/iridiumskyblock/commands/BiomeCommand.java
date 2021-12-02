package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandBiomeGUI;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BiomeCommand extends Command {

    private final IridiumSkyblock plugin = IridiumSkyblock.getInstance();

    public BiomeCommand() {
        super(Collections.singletonList("biome"), "Change your island biome.", "%prefix% &7/is biome <biome>", "", true, Duration.ZERO);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        final Player player = (Player) sender;

        final User user = this.plugin.getUserManager().getUser(player);
        final Optional<Island> islandOptional = user.getIsland();
        if (!islandOptional.isPresent()) {
            player.sendMessage(StringUtils.color(this.plugin.getMessages().noIsland.replace("%prefix%", plugin.getConfiguration().prefix)));
            return false;
        }

        if (args.length != 2) {
            player.openInventory(new IslandBiomeGUI(1, islandOptional.get(), player.getWorld().getEnvironment(), getCooldownProvider(), player.getOpenInventory().getTopInventory()).getInventory());
            // The BiomeGUI handles the cooldown
            return false;
        }

        final Optional<XBiome> biomeOptional = XBiome.matchXBiome(args[1]);
        // Return if the biome doesn't exist or isn't supported by this version
        if (!biomeOptional.isPresent() || biomeOptional.get().getBiome() == null || biomeOptional.get() == XBiome.THE_VOID) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().invalidBiome.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IridiumSkyblock.getInstance().getIslandManager().setIslandBiome(islandOptional.get(), biomeOptional.get());
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().changedBiome
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%biome%", WordUtils.capitalizeFully(biomeOptional.get().name().toLowerCase().replace("_", " ")))));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            return Collections.emptyList();
        }

        Player player = (Player) commandSender;
        return Arrays.stream(XBiome.VALUES)
                .filter(biome -> biome.getEnvironment() == player.getWorld().getEnvironment())
                .filter(biome -> biome.getBiome() != null)
                .map(XBiome::name)
                .collect(Collectors.toList());
    }

}
